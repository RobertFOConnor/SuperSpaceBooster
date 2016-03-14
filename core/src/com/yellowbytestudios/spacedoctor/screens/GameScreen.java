package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Mainline;
import com.brashmonkey.spriter.Player;
import com.yellowbytestudios.spacedoctor.MainGame;
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
import com.yellowbytestudios.spacedoctor.game.objects.Box;
import com.yellowbytestudios.spacedoctor.game.objects.Bullet;
import com.yellowbytestudios.spacedoctor.game.objects.Door;
import com.yellowbytestudios.spacedoctor.game.objects.Enemy;
import com.yellowbytestudios.spacedoctor.game.objects.Exit;
import com.yellowbytestudios.spacedoctor.game.objects.PickUp;
import com.yellowbytestudios.spacedoctor.game.objects.Platform;
import com.yellowbytestudios.spacedoctor.mapeditor.MapManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.screens.editor.MapEditorScreen;

public class GameScreen implements Screen {


    //Tiled map properties.
    public static int levelNo = 1;
    private BoundedCamera cam, b2dCam;
    private World world;
    private TiledMap tileMap;
    private Box2DDebugRenderer b2dr; //DEBUG
    private OrthogonalTiledMapRenderer tmr;
    private float PPM = 100;

    //Map Editor
    public static TiledMap customMap;
    public static boolean isCustomMap = false;

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
    private Array<Door> doors;
    private Exit exit;
    private Texture bg;

    //Background manager.
    private LevelBackgroundManager bgManager;
    public static ParticleManager particleManager;
    private GUIManager gui;


    //ANDROID CONTROLLER
    public static AndroidController androidController;


    public GameScreen(int levelNo) {
        GameScreen.levelNo = levelNo;
        isCustomMap = false;
    }

    public GameScreen(TiledMap customMap) {
        GameScreen.customMap = customMap;
        isCustomMap = true;
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
        if (!isCustomMap) {
            tileMap = new TmxMapLoader().load("maps/spaceship" + levelNo + ".tmx");
        } else {
            tileMap = customMap;
        }

        //Setup map renderer.
        tmr = new OrthogonalTiledMapRenderer(tileMap);
        setupMap();
    }


    private void setupMap() {

        //Setup camera.
        world = new World(new Vector2(0, -9.8f), true);

        //Setup contact listeners.
        contactListener = new Box2DContactListeners();
        world.setContactListener(contactListener);

        TileManager tileManager = new TileManager();
        tileManager.createWalls(world, tileMap);

        // Set camera boundries.
        int mapWidth = tileManager.getMapWidth();
        int mapHeight = tileManager.getMapHeight();
        b2dCam.setBounds(0, mapWidth / PPM, 0, mapHeight / PPM);
        cam.setBounds(0, mapWidth, 0, mapHeight);


        float startX;
        float startY;

        if (!isCustomMap) {
            startX = (Float.parseFloat(tileMap.getProperties().get("startX", String.class)));
            startY = (Float.parseFloat(tileMap.getProperties().get("startY", String.class)));

        } else { // TEMP - Custom map will specify player spawn point.
            startX = MapManager.startX;
            startY = MapManager.startY;
        }

        players = new Array<SpacemanPlayer>();
        players.add(BodyFactory.createPlayer(world, Controllers.getControllers().size-1, MainGame.saveData.getHead()));
        players.get(0).setPos(new Vector2(startX, startY));

        //TEMP PLAYER 2!

        if(Controllers.getControllers().size > 1) {
            players.add(BodyFactory.createPlayer(world, 0, 1));
            players.get(1).setPos(new Vector2(startX, startY));
        }

        bullets = new Array<Bullet>();

        bg = Assets.manager.get(Assets.BG, Texture.class);
        bgManager = new LevelBackgroundManager(mapWidth, mapHeight);

        boxes = BodyFactory.createBoxes(world, tileMap);
        pickups = BodyFactory.createPickups(world, tileMap);
        enemies = BodyFactory.createEnemies(world, tileMap);
        platforms = BodyFactory.createPlatforms(world, tileMap);
        doors = BodyFactory.createDoors(world, tileMap);
        exit = BodyFactory.createExits(world, tileMap);

        gui = new GUIManager(players, false);
    }

    @Override
    public void update(float step) {
        world.step(step, 8, 3);

        if (MainGame.DEVICE.equals("ANDROID")) {
            androidController.update();
        }

        updateCameras();

        for (SpacemanPlayer p : players) {
            p.update();

            if (p.isShooting()) {
                addBullet(p);
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

        gui.update();
        removeObjects();
        removeEnemies();
        updateObjects();
    }

    private void updateObjects() {
        for (Door d : doors) {
            d.activate();
        }

        for (Platform p : platforms) {
            p.update();
        }

        for (Bullet b : bullets) {
            b.update();
        }

        if (players.size != 0) {
            for (Enemy e : enemies) {
                e.update(players.get(0));
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
                    PickUp pickUp = (PickUp) b.getUserData();
                    pickUp.activate(players.get(0));
                    pickups.removeValue(pickUp, true);
                }
                world.destroyBody(b);
            }
        }
    }

    private void removeEnemies() {
        if (contactListener.getEnemy() != null) {

            Enemy e = (Enemy) contactListener.getEnemy().getUserData();
            e.setHealth(e.getHealth() - 1);

            if (e.getHealth() <= 0) {
                enemies.removeValue(e, true);
                world.destroyBody(contactListener.getEnemy());
            }
            contactListener.nullifyEnemy();
        }
    }

    public void addBullet(SpacemanPlayer player) {

        Bullet bullet;
        int dir = 1;
        if (player.facingLeft()) {
            dir = -1;
        }
        bullet = new Bullet(BodyFactory.createBullet(world), new Vector2(dir * 2200, 0));
        bullet.getBody().setTransform(player.getPos().x + (dir * 1.2f), player.getPos().y, 0);
        bullets.add(bullet);
    }

    private void exitLevel() {
        SoundManager.play(Assets.FINISHED_SOUND);
        SoundManager.stop(Assets.JETPACK_SOUND);
        SoundManager.switchMusic(Assets.MAIN_THEME);

        if (GameScreen.isCustomMap) { // RETURN TO MAP EDITOR.
            ScreenManager.setScreen(new MapEditorScreen(customMap));
        } else {

            world.dispose();

            if (levelNo != 10) {
                MainGame.saveData.unlockHead(levelNo);
                MainGame.saveData.setCurrLevel(MainGame.saveData.getCurrLevel() + 1);
                MainGame.saveManager.saveDataValue("PLAYER", MainGame.saveData);
                ScreenManager.setScreen(new LevelSelectScreen());
            } else {
                ScreenManager.setScreen(new ResultsScreen());
            }
        }
        customMap = null;
    }

    private void killPlayer(final SpacemanPlayer p) {
        if (!p.isDieing()) {
            SoundManager.stop(Assets.JETPACK_SOUND);
            SoundManager.play(Assets.DEATH_SOUND);
            System.out.println("YOU DEAD BOI");

            Player.PlayerListener myListener = new Player.PlayerListener() {
                @Override
                public void animationFinished(Animation animation) {
                    setupMap();
                }

                @Override
                public void animationChanged(Animation oldAnim, Animation newAnim) {

                }

                @Override
                public void preProcess(Player player) {

                }

                @Override
                public void postProcess(Player player) {

                }

                @Override
                public void mainlineKeyChanged(Mainline.Key prevKey, Mainline.Key newKey) {

                }
            };
            p.startDeath(myListener);
            p.setDieing(true);
        }
    }

    private void updateCameras() {

        float targetX;
        float targetY;
        Vector2 playerPos = players.get(0).getPos();

        if (players.size == 1) { // (1P) Center camera on player.
            targetX = playerPos.x * PPM + MainGame.WIDTH / 50;
            targetY = playerPos.y * PPM + MainGame.HEIGHT / 50;

        } else { // (2P) Center camera on mid-point between players.
            targetX = ((playerPos.x * PPM) + (players.get(1).getPos().x * PPM)) / 2;
            targetY = ((playerPos.y * PPM) + (players.get(1).getPos().y * PPM)) / 2;
        }

        float SPEEDX = Gdx.graphics.getDeltaTime() * (Math.abs(targetX - cam.position.x) * 1.8f);
        float SPEEDY = Gdx.graphics.getDeltaTime() * (Math.abs(targetY - cam.position.y) * 3);
        float newX = newCamPos(cam.position.x, targetX, SPEEDX);
        float newY = newCamPos(cam.position.y, targetY, SPEEDY);

        cam.setPosition(newX, newY);
        b2dCam.setPosition(newX / PPM, newY / PPM);
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
        sb.draw(bg, cam.position.x - bg.getWidth() / 2, cam.position.y - bg.getHeight() / 2);
        bgManager.render(sb);

        for (Door d : doors) {
            d.render(sb);
        }
        sb.end();

        //Render tiles.
        tmr.setView(cam);
        tmr.render();

        sb.begin();
        exit.render(sb);

        for (SpacemanPlayer p : players) {
            p.render();
        }

        renderObjects(sb);
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

        if (!isCustomMap) {
            ScreenManager.setScreen(new LevelSelectScreen());
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