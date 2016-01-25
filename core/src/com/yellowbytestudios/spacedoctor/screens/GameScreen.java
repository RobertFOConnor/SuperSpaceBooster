package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
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
import com.yellowbytestudios.spacedoctor.Assets;
import com.yellowbytestudios.spacedoctor.BodyFactory;
import com.yellowbytestudios.spacedoctor.Box2DContactListeners;
import com.yellowbytestudios.spacedoctor.GUIManager;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.Platform;
import com.yellowbytestudios.spacedoctor.SoundManager;
import com.yellowbytestudios.spacedoctor.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.TileManager;
import com.yellowbytestudios.spacedoctor.cameras.BoundedCamera;
import com.yellowbytestudios.spacedoctor.controllers.AndroidController;
import com.yellowbytestudios.spacedoctor.effects.LightManager;
import com.yellowbytestudios.spacedoctor.effects.ParticleManager;
import com.yellowbytestudios.spacedoctor.objects.Box;
import com.yellowbytestudios.spacedoctor.objects.Bullet;
import com.yellowbytestudios.spacedoctor.objects.Door;
import com.yellowbytestudios.spacedoctor.objects.Enemy;
import com.yellowbytestudios.spacedoctor.objects.PickUp;

/**
 * Created by BobbyBoy on 15-Oct-15.
 */
public class GameScreen implements Screen {

    private BoundedCamera cam, b2dCam;
    private World world;
    private TiledMap tileMap;
    private Box2DDebugRenderer b2dr;
    private OrthogonalTiledMapRenderer tmr;

    private TileManager tileManager;
    private SpacemanPlayer player;
    private float PPM = 100;

    private Box2DContactListeners contactListener;

    private Array<Bullet> bullets;
    private Array<Box> boxes;
    private Array<PickUp> pickups;
    private Array<Enemy> enemies;
    private Array<Platform> platforms;
    private Door door;
    private Texture bg;

    public static ParticleManager particleManager;
    public static LightManager lightManager;
    private GUIManager gui;

    boolean atDoor = false;
    public static int levelNo = 1;

    //ANDROID CONTROLLER
    public static AndroidController androidController;

    //Map Editor
    public static TiledMap customMap;
    public static boolean isCustomMap = false;


    public GameScreen(int levelNo) {
        this.levelNo = levelNo;
        isCustomMap = false;
    }

    public GameScreen(TiledMap customMap) {
        this.customMap = customMap;
        isCustomMap = true;
    }

    @Override
    public void create() {

        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, MainGame.WIDTH / PPM, MainGame.HEIGHT / PPM);

        b2dr = new Box2DDebugRenderer();
        particleManager = new ParticleManager();
        //lightManager = new LightManager(world, player, cam);

        cam = new BoundedCamera();
        cam.setToOrtho(false, MainGame.WIDTH, MainGame.HEIGHT);

        if (MainGame.DEVICE.equals("ANDROID")) {
            androidController = new AndroidController();
        }

        //Set tile map using Tiled map path.

        if (customMap == null) {
            tileMap = new TmxMapLoader().load("maps/spaceship" + levelNo + ".tmx");
        } else {
            tileMap = customMap;
        }

        //Setup map renderer.
        tmr = new OrthogonalTiledMapRenderer(tileMap);


        setupMap();
    }

    private void setupMap() {
        System.out.println("Setup begins");

        //Setup camera.
        world = new World(new Vector2(0, -9.8f), true);

        //Setup contact listeners.
        contactListener = new Box2DContactListeners();
        world.setContactListener(contactListener);


        tileManager = new TileManager();
        tileManager.createWalls(world, tileMap);

        // Set camera boundries.
        b2dCam.setBounds(0, tileManager.getMapWidth() / PPM, 0, tileManager.getMapHeight() / PPM);
        cam.setBounds(0, tileManager.getMapWidth(), 0, tileManager.getMapHeight());


        int startX;
        int startY;

        if (customMap == null) {
            startX = (Integer.parseInt(tileMap.getProperties().get("startX", String.class)));
            startY = (Integer.parseInt(tileMap.getProperties().get("startY", String.class)));

        } else { // TEMP - Custom map will specify player spawn point.
            startX = 2;
            startY = 5;
        }

        player = new SpacemanPlayer(BodyFactory.createBody(world, "PLAYER"), contactListener);
        player.setPos(new Vector2(startX, startY));

        contactListener.setPlayer(player);

        bullets = new Array<Bullet>();

        bg = new Texture(Gdx.files.internal("bg.png"));

        boxes = BodyFactory.createBoxes(world, tileMap);
        pickups = BodyFactory.createPickups(world, tileMap);
        enemies = BodyFactory.createEnemies(world, tileMap);
        platforms = BodyFactory.createPlatforms(world, tileMap);
        door = BodyFactory.createDoors(world, tileMap);

        gui = new GUIManager(player);
        atDoor = false;
    }

    public void addBullet() {

        Bullet bullet = new Bullet(BodyFactory.createBody(world, "BULLET"));

        float speed = Bullet.SPEED;

        if (player.facingLeft()) {
            bullet.getBody().setLinearVelocity(-speed, 0f);
            bullet.getBody().setTransform(player.getPos().x - 1.2f, player.getPos().y, 0);
        } else {
            bullet.getBody().setLinearVelocity(speed, 0f);
            bullet.getBody().setTransform(player.getPos().x + 1.2f, player.getPos().y, 0);
        }
        bullets.add(bullet);
    }

    @Override
    public void update(float step) {
        world.step(step, 8, 3);

        if (player.isDead() || gui.getTimeElapsed() < 0) {
            SoundManager.play(Assets.manager.get(Assets.DEATH_SOUND, Sound.class));
            setupMap();
        }


        if (MainGame.DEVICE.equals("ANDROID")) {
            androidController.update();
        }

        updateCameras();

        player.update();
        gui.update();

        if (contactListener.getBodies().size > 0) {
            for (Fixture f : contactListener.getBodies()) {
                Body b = f.getBody();
                if (f.getUserData().equals("bullet")) {
                    bullets.removeValue((Bullet) b.getUserData(), true);
                } else if (f.getUserData().equals("pickup")) {
                    PickUp pickUp = (PickUp) b.getUserData();
                    pickUp.activate(player);
                    pickups.removeValue(pickUp, true);
                }
                world.destroyBody(b);
            }
        }

        if (contactListener.getEnemy() != null) {

            Enemy e = (Enemy) contactListener.getEnemy().getUserData();
            e.setHealth(e.getHealth() - 1);

            if (e.getHealth() <= 0) {
                enemies.removeValue(e, true);
                world.destroyBody(contactListener.getEnemy());
            }
            contactListener.nullifyEnemy();
        }

        if (player.isShooting()) {
            addBullet();
            player.setShooting(false);
        }

        for (Platform p : platforms) {
            p.update();
        }

        for (Enemy e : enemies) {
            e.update(player);
        }

        if (contactListener.isAtDoor()) {

            SoundManager.play(Assets.manager.get(Assets.FINISHED_SOUND, Sound.class));
            SoundManager.stop(Assets.manager.get(Assets.JETPACK_SOUND, Sound.class));

            if (GameScreen.isCustomMap) { // RETURN TO MAP EDITOR.
                ScreenManager.setScreen(new MapEditorScreen(customMap));
            } else {

                if (MainGame.UNLOCKED_LEVEL != 10) {
                    MainGame.UNLOCKED_LEVEL += 1;
                    ScreenManager.setScreen(new LevelSelectScreen());
                } else {
                    ScreenManager.setScreen(new ResultsScreen());
                }
            }
        }
    }

    private void updateCameras() {
        float targetX = player.getPos().x * PPM + MainGame.WIDTH / 50;
        float targetY = player.getPos().y * PPM + MainGame.HEIGHT / 50;

        cam.setPosition(targetX, targetY);
        b2dCam.setPosition(player.getPos().x + MainGame.WIDTH / 50 / PPM, player.getPos().y + MainGame.HEIGHT / 50 / PPM);

        b2dCam.update();
        cam.update();
    }


    @Override
    public void render(SpriteBatch sb) {
        // Clear screen.
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glClearColor(0, 0, 0, 0);


        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - bg.getWidth() / 2, cam.position.y - bg.getHeight() / 2);
        sb.end();


        //Render tiles.
        tmr.setView(cam);
        tmr.render();

        sb.begin();

        door.render(sb);
        player.render();

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

        particleManager.render(sb);

        sb.end();


        //DRAW GUI!
        gui.render(sb);

        if (MainGame.DEVICE.equals("ANDROID")) {
            androidController.render(sb);
        }

        //Render Box2D world.
        //b2dr.render(world, b2dCam.combined);
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

    @Override
    public void goBack() {

    }
}