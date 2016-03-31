package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.spriter.MySpriterAnimationListener;
import com.yellowbytestudios.spacedoctor.box2d.BodyFactory;
import com.yellowbytestudios.spacedoctor.box2d.Box2DContactListeners;
import com.yellowbytestudios.spacedoctor.cameras.BoundedCamera;
import com.yellowbytestudios.spacedoctor.controllers.AndroidController;
import com.yellowbytestudios.spacedoctor.effects.ParticleManager;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.game.GUIManager;
import com.yellowbytestudios.spacedoctor.game.LevelBackgroundManager;
import com.yellowbytestudios.spacedoctor.game.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.game.TileManager;
import com.yellowbytestudios.spacedoctor.game.objects.*;
import com.yellowbytestudios.spacedoctor.game.objects.Character;
import com.yellowbytestudios.spacedoctor.mapeditor.CustomMap;
import com.yellowbytestudios.spacedoctor.mapeditor.MapManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.CoreLevelSaver;
import com.yellowbytestudios.spacedoctor.screens.editor.MapEditorScreen;
import com.yellowbytestudios.spacedoctor.screens.editor.MapEditorSplashScreen;
import com.yellowbytestudios.spacedoctor.screens.menu.LevelSelectScreen;

public class GameScreen implements Screen {


    //Tiled map properties.
    public static int worldNo = 1;
    public static int levelNo = 1;
    private BoundedCamera cam, b2dCam;
    private World world;
    private TiledMap tileMap;
    private Box2DDebugRenderer b2dr; //DEBUG
    private OrthogonalTiledMapRenderer tmr;
    private float PPM = 100;

    //Map Editor
    public static CustomMap customMap;

    //Player objects.
    private Array<SpacemanPlayer> players;

    //Contact Listener.
    private Box2DContactListeners contactListener;

    //GAME-OBJECT ARRAYS.
    private Array<Bullet> bullets;
    private Array<Box> boxes;
    private Array<PickUp> pickups;
    private Array<Enemy> enemies;
    private Array<Platform> platforms;
    private Exit exit;

    //Background manager.
    private LevelBackgroundManager bgManager;
    public static ParticleManager particleManager;
    private GUIManager gui;


    //ANDROID CONTROLLER
    public static AndroidController androidController;

    public static boolean coreMap = false;

    // Set camera boundries.
    private int mapWidth;
    private int mapHeight;

    public GameScreen(int levelNo) {
        GameScreen.levelNo = levelNo;
        GameScreen.worldNo = (levelNo/10)+1;

        //TEMP!!!!
        CoreLevelSaver levelSaver = new CoreLevelSaver("levels/level_"+levelNo+".json", true);
        CustomMap customMap;
        customMap = levelSaver.loadDataValue("LEVEL", CustomMap.class);
        GameScreen.customMap = customMap;
        coreMap = true;
    }

    public GameScreen(CustomMap customMap) {
        GameScreen.customMap = customMap;
        coreMap = false;
    }

    @Override
    public void create() {
        SoundManager.switchMusic(Assets.LEVEL_THEME);
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, MainGame.WIDTH / PPM, MainGame.HEIGHT / PPM);
        cam = new BoundedCamera();
        cam.setToOrtho(false, MainGame.WIDTH, MainGame.HEIGHT);
        b2dr = new Box2DDebugRenderer();
        particleManager = new ParticleManager();

        if (MainGame.DEVICE.equals("ANDROID")) {
            androidController = new AndroidController();
        }

        //Set tile map using Tiled map path.
        tileMap = MapManager.setupMap(customMap.loadMap());

        //Setup map renderer.
        tmr = new OrthogonalTiledMapRenderer(tileMap);
        setupMap();

        bgManager = new LevelBackgroundManager(cam, mapWidth, mapHeight);
    }


    private void setupMap() {

        //Setup camera.
        world = new World(new Vector2(0, -9.8f), true);

        //Setup contact listeners.
        contactListener = new Box2DContactListeners();
        world.setContactListener(contactListener);

        TileManager tileManager = new TileManager();
        tileManager.createWalls(world, tileMap);

        mapWidth = customMap.getMapWidth();
        mapHeight = customMap.getMapHeight();

        b2dCam.setBounds(0, mapWidth / PPM, 0, mapHeight / PPM);
        cam.setBounds(0, mapWidth, 0, mapHeight);

        //Custom map will specify player spawn point.
        float startX = customMap.getStartPos().x;
        float startY = customMap.getStartPos().y;

        players = new Array<SpacemanPlayer>();
        players.add(BodyFactory.createPlayer(world, 0, MainGame.saveData.getHead()));
        players.get(0).setPos(new Vector2(startX, startY));

        //TEMP PLAYER 2!

        if (Controllers.getControllers().size > 1) {
            players.add(BodyFactory.createPlayer(world, 1, 1));
            players.get(1).setPos(new Vector2(startX, startY));
        }

        bullets = new Array<Bullet>();

        boxes = BodyFactory.createBoxes(world, tileMap);
        pickups = BodyFactory.createPickups(world, tileMap);
        enemies = BodyFactory.createEnemies(world, tileMap);
        platforms = BodyFactory.createPlatforms(world, tileMap);
        exit = BodyFactory.createExits(world, tileMap);

        gui = new GUIManager(players, false);
    }

    @Override
    public void update(float step) {

        if(!gui.isPaused()) {

            world.step(step, 8, 3);

            if (MainGame.DEVICE.equals("ANDROID")) {
                androidController.update();
            }

            if (Gdx.input.isKeyPressed(Input.Keys.M)) {
                ScreenManager.setScreen(new MapEditorSplashScreen(new MapEditorScreen(customMap)));
            }

            updateCameras();
            bgManager.update();

            for (SpacemanPlayer p : players) {
                p.update();

                if (p.isShooting()) {
                    addBullet(p, p.getGun().getBullet());
                    p.setShooting(false);
                }

                if (p.isDead() || gui.timeIsUp()) {
                    killPlayer(p);
                }

                if (p.isFinished()) {
                    world.destroyBody(p.getBody());
                    players.removeValue(p, true);

                    if (players.size == 0) {
                        exitLevel();
                    }
                }
            }
            updateObjects();
            removeObjects();
            removeEnemies();
            exit.update();
            particleManager.update();
        }
        gui.update();
    }

    private void updateObjects() {

        for (Platform p : platforms) {
            p.update();
        }

        for (Bullet b : bullets) {
            b.update();
        }

        if (players.size != 0) {
            for (Enemy e : enemies) {
                e.update(players.get(0));

                if(e.isShooting()) {
                    addBullet(e, 0);
                    e.setShooting(false);
                }
            }
        }
    }

    private void removeObjects() {
        if (contactListener.getBodies().size > 0) {
            for (Fixture f : contactListener.getBodies()) {
                Body b = f.getBody();
                if (f.getUserData().equals("bullet")) {
                    bullets.removeValue((Bullet) b.getUserData(), true);
                } else if (f.getUserData().equals("pickup")) {
                    pickups.removeValue((PickUp) b.getUserData(), true);
                }
                world.destroyBody(b);
            }
        }
    }

    private void removeEnemies() {
        if (contactListener.getEnemy() != null) {

            Body enemyBody = contactListener.getEnemy();
            Enemy e = (Enemy) enemyBody.getUserData();
            e.setHealth(e.getHealth() - 1);

            if (e.getHealth() <= 0) {
                killEnemy(e);
                world.destroyBody(enemyBody);
            }
            contactListener.nullifyEnemy();
        }
    }


    private void killEnemy(final Enemy e) {

        SoundManager.play(Assets.ENEMY_DEATH);

        if (!e.isDead()) {

            Player.PlayerListener myListener = new MySpriterAnimationListener() {
                @Override
                public void animationFinished(Animation animation) {
                    enemies.removeValue(e, true);
                }
            };
            e.startDeath(myListener);
            e.setDead(true);
        }
    }


    public void addBullet(Character player, int bulletId) {

        Bullet bullet;
        int dir = 1;
        if (player.facingLeft()) {
            dir = -1;
        }
        bullet = new Bullet(BodyFactory.createBullet(world), dir, bulletId);
        bullet.getBody().setTransform(player.getPos().x + (dir * 1.2f), player.getPos().y-0.1f, 0);
        bullets.add(bullet);
    }

    private void exitLevel() {
        SoundManager.play(Assets.FINISHED_SOUND);
        SoundManager.stop(Assets.JETPACK_SOUND);
        SoundManager.switchMusic(Assets.MAIN_THEME);

        if (!coreMap) { // RETURN TO MAP EDITOR.
            ScreenManager.setScreen(new MapEditorScreen(customMap));
        } else {
            if (levelNo != 10) {
                MainGame.saveData.unlockHead(levelNo);
                MainGame.saveData.setCurrLevel(MainGame.saveData.getCurrLevel() + 1);
                MainGame.saveManager.saveDataValue("PLAYER", MainGame.saveData);
                ScreenManager.setScreen(new GameScreen(levelNo+1));
            } else {
                ScreenManager.setScreen(new ResultsScreen());
            }
        }
        world.dispose();
    }

    private void killPlayer(final SpacemanPlayer p) {
        if (!p.isDieing()) {
            SoundManager.stop(Assets.JETPACK_SOUND);
            SoundManager.play(Assets.ENEMY_DEATH);

            Player.PlayerListener myListener = new MySpriterAnimationListener() {
                @Override
                public void animationFinished(Animation animation) {
                    setupMap();
                }
            };
            p.startDeath(myListener);
            p.setDieing(true);
        }
    }

    private void updateCameras() {

        float zoom = 1f;
        float targetX;
        float targetY;
        Vector2 playerPos = players.get(0).getPos();

        if (players.size == 1) { // (1P) Center camera on player.
            targetX = playerPos.x * PPM + MainGame.WIDTH / 50;
            targetY = playerPos.y * PPM + MainGame.HEIGHT / 50;

        } else { // (2P) Center camera on mid-point between players.
            targetX = ((playerPos.x * PPM) + (players.get(1).getPos().x * PPM)) / 2;
            targetY = ((playerPos.y * PPM) + (players.get(1).getPos().y * PPM)) / 2;

            float distance = (float) Math.sqrt(Math.pow((players.get(1).getPos().x - players.get(0).getPos().x), 2) + Math.pow((players.get(1).getPos().y - players.get(0).getPos().y), 2));

            if (distance > 10) {
                zoom = 1 + ((distance - 10) / 20);
            }
        }

        float SPEEDX = Gdx.graphics.getDeltaTime() * (Math.abs(targetX - cam.position.x) * 1.8f);
        float SPEEDY = Gdx.graphics.getDeltaTime() * (Math.abs(targetY - cam.position.y) * 3);
        float newX = newCamPos(cam.position.x, targetX, SPEEDX);
        float newY = newCamPos(cam.position.y, targetY, SPEEDY);

        cam.setPosition(newX, newY);
        b2dCam.setPosition(newX / PPM, newY / PPM);
        //cam.zoom = zoom;
        //b2dCam.zoom = zoom;
        b2dCam.update();
        cam.update();
    }

    private float newCamPos(float camPos, float target, float speed) {
        if (camPos < target - speed) {
            return camPos + speed;
        } else if (camPos > target + speed) {
            return camPos - speed;
        } else {
            return target;
        }
    }


    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        bgManager.render(sb);
        sb.end();

        //Render tiles.
        tmr.setView(cam);
        tmr.render();

        sb.begin();
        exit.render(sb);
        renderObjects(sb);
        for (SpacemanPlayer p : players) {
            p.render();
        }
        particleManager.render(sb);
        sb.end();

        //DRAW GUI!
        gui.render(sb);

        if (MainGame.DEVICE.equals("ANDROID")) {
            androidController.render(sb);
        }

        //Render Box2D world.
        if (MainGame.TEST_MODE) {
            b2dr.render(world, b2dCam.combined);
        }

        if(gui.isPaused()) {
            sb.begin();
            sb.draw(Assets.manager.get(Assets.ALPHA, Texture.class), 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
            sb.end();
        }
    }

    private void renderObjects(SpriteBatch sb) {
        for (Platform p : platforms) {
            p.render(sb);
        }

        for (Bullet b : bullets) { //DRAW BULLETS.
            b.render(sb);
        }

        for (Box b : boxes) { //DRAW BOXES.
            b.render(sb);
        }

        for (PickUp p : pickups) { //DRAW PICK-UPS.
            p.render(sb);
        }

        for (Enemy e : enemies) { //DRAW ENEMIES.
            e.render(sb);
        }
    }

    @Override
    public void goBack() {

        SoundManager.switchMusic(Assets.MAIN_THEME);
        SoundManager.stop(Assets.JETPACK_SOUND);
        world.dispose();

        if (coreMap) {
            ScreenManager.setScreen(new LevelSelectScreen((levelNo/10)+1));
        } else {
            ScreenManager.setScreen(new MapEditorScreen(customMap));
        }
        customMap = null;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }
}