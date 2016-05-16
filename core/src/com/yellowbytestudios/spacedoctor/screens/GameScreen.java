package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.BoundedCamera;
import com.yellowbytestudios.spacedoctor.controllers.AndroidController;
import com.yellowbytestudios.spacedoctor.effects.LightManager;
import com.yellowbytestudios.spacedoctor.effects.ParticleManager;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.game.DungeonGenerator;
import com.yellowbytestudios.spacedoctor.game.GUIManager;
import com.yellowbytestudios.spacedoctor.game.LevelBackgroundManager;
import com.yellowbytestudios.spacedoctor.game.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.game.WorldManager;
import com.yellowbytestudios.spacedoctor.mapeditor.CustomMap;
import com.yellowbytestudios.spacedoctor.mapeditor.MapManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.CoreLevelSaver;
import com.yellowbytestudios.spacedoctor.screens.editor.MapEditorScreen;
import com.yellowbytestudios.spacedoctor.screens.editor.MapEditorSplashScreen;
import com.yellowbytestudios.spacedoctor.screens.menu.LevelSelectScreen;
import com.yellowbytestudios.spacedoctor.screens.menu.MainMenuScreen;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;

public class GameScreen implements Screen {


    //Tiled map properties.
    public static int worldNo = 1;
    public static int levelNo = 1;
    private BoundedCamera cam, b2dCam;
    private TiledMap tileMap;
    private Box2DDebugRenderer b2dr; //DEBUG
    private OrthogonalTiledMapRenderer tmr;
    private static final float PPM = 100;

    private Sprite transition;

    //Map Editor
    public static CustomMap customMap;

    private WorldManager worldManager;
    private LevelBackgroundManager bgManager;
    public static ParticleManager particleManager;
    public static LightManager lightManager;
    private GUIManager gui;


    //ANDROID CONTROLLER
    public static AndroidController androidController;

    public static boolean coreMap = false;

    // Set camera boundries.
    private int mapWidth;
    private int mapHeight;

    public GameScreen(int levelNo) {
        GameScreen.levelNo = levelNo;
        GameScreen.worldNo = (levelNo / 10) + 1;

        //TEMP!!!!
        CoreLevelSaver levelSaver = new CoreLevelSaver("levels/level_" + levelNo + ".json", true);
        CustomMap customMap;
        if (MainGame.DUNGEON_MODE) {
            customMap = DungeonGenerator.getGeneratedMap();
        } else {
            customMap = levelSaver.loadDataValue("LEVEL", CustomMap.class);
        }

        GameScreen.customMap = customMap;
        coreMap = true;
    }

    public GameScreen(CustomMap customMap) {
        GameScreen.customMap = customMap;
        coreMap = false;
    }

    @Override
    public void create() {
        //SoundManager.switchMusic(Assets.LEVEL_THEME);
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

        transition = new Sprite(new Texture(Gdx.files.internal("black.png")));
        transition.setPosition(0, 0);
        AnimationManager.applyLevelStartAnimation(transition, MainGame.WIDTH, 0);
        AnimationManager.startAnimation();
    }


    public void setupMap() {

        worldManager = new WorldManager(this, tileMap);

        mapWidth = customMap.getMapWidth();
        mapHeight = customMap.getMapHeight();

        b2dCam.setBounds(0, mapWidth / PPM, 0, mapHeight / PPM);
        cam.setBounds(0, mapWidth, 0, mapHeight);

        Array<SpacemanPlayer> players = worldManager.getPlayers();
        cam.setPosition(players.get(0).getPos().x * PPM + MainGame.WIDTH / 50, players.get(0).getPos().y * PPM + MainGame.WIDTH / 50);
        b2dCam.setPosition(players.get(0).getPos().x * PPM + MainGame.WIDTH / 50 / PPM, players.get(0).getPos().y * PPM + MainGame.WIDTH / 50 / PPM);

        gui = new GUIManager(this, players, true);

        //lightManager = new LightManager(world, players, exit, b2dCam);


        /*for (PickUp p : pickups) {
            p.setLight(lightManager.createLight(p.getBody().getPosition()));
        }*/
    }

    @Override
    public void update(float step) {

        if (!gui.isPaused()) {

            if (MainGame.DEVICE.equals("ANDROID")) {
                androidController.update();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.M)) {
                ScreenManager.setScreen(new MapEditorSplashScreen(new MapEditorScreen(customMap)));
            }
            updateCameras();
            bgManager.update();
            worldManager.update(step);
            particleManager.update();

            if (MainGame.BOX2D_LIGHTS) {
                lightManager.update();
            }
            gui.updateTimer();
        }
        gui.update();
    }


    @Override
    public void render(SpriteBatch sb) {

        //DRAW BG.
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        bgManager.render(sb);
        sb.end();

        //DRAW TILES.
        tmr.setView(cam);
        tmr.render();

        //DRAW WORLD.
        worldManager.render(sb);

        if (MainGame.BOX2D_LIGHTS) {
            lightManager.render();
        }

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        worldManager.renderBullets(sb);
        particleManager.render(sb);
        sb.end();


        //DRAW GUI!
        gui.render(sb);

        if (MainGame.DEVICE.equals("ANDROID")) {
            androidController.render(sb);
        }

        //Render Box2D world.
        if (MainGame.TEST_MODE) {
            b2dr.render(worldManager.getWorld(), b2dCam.combined);
        }

        sb.begin();
        sb.draw(transition, transition.getX(), transition.getY(), MainGame.WIDTH, MainGame.HEIGHT + 100);
        sb.end();
    }

    public void exitLevel() {
        SoundManager.play(Assets.FINISHED_SOUND);
        SoundManager.stop(Assets.JETPACK_SOUND);

        if (!coreMap) { // RETURN TO MAP EDITOR.
            ScreenManager.setScreen(new MapEditorScreen(customMap));
        } else {
            if (levelNo < 10) {
                MainGame.saveData.setCurrLevel(MainGame.saveData.getCurrLevel() + 1);
                MainGame.saveManager.saveDataValue("PLAYER", MainGame.saveData);

                transition.setPosition(0, MainGame.HEIGHT);
                AnimationManager.applyLevelEndAnimation(transition, 0, 0, new GameScreen(levelNo + 1), worldManager.getWorld());
                AnimationManager.startAnimation();
            } else {
                ScreenManager.setScreen(new MainMenuScreen());
            }
        }
    }


    private void updateCameras() {

        float zoom = 1f;
        float targetX;
        float targetY;

        Array<SpacemanPlayer> players = worldManager.getPlayers();

        if (players.size > 0) {
            Vector2 playerPos = players.get(0).getPos();

            if (players.size == 1) { // (1P) Center camera on player.
                if (players.get(0).facingLeft()) {
                    targetX = (playerPos.x - 5) * PPM + MainGame.WIDTH / 50;
                    targetY = playerPos.y * PPM + MainGame.HEIGHT / 50;
                } else {
                    targetX = (playerPos.x + 5) * PPM + MainGame.WIDTH / 50;
                    targetY = playerPos.y * PPM + MainGame.HEIGHT / 50;
                }

            } else { // (2P) Center camera on mid-point between players.
                targetX = ((playerPos.x * PPM) + (players.get(1).getPos().x * PPM)) / 2;
                targetY = ((playerPos.y * PPM) + (players.get(1).getPos().y * PPM)) / 2;

                float distance = (float) Math.sqrt(Math.pow((players.get(1).getPos().x - players.get(0).getPos().x), 2) + Math.pow((players.get(1).getPos().y - players.get(0).getPos().y), 2));

                if (distance > 10) {
                    zoom = 1 + ((distance - 10) / 20) * (distance / 10);
                }
            }

            float SPEEDX = Gdx.graphics.getDeltaTime() * (Math.abs(targetX - cam.position.x) * 1.8f);
            float SPEEDY = Gdx.graphics.getDeltaTime() * (Math.abs(targetY - cam.position.y) * 3);
            float newX = newCamPos(cam.position.x, targetX, SPEEDX);
            float newY = newCamPos(cam.position.y, targetY, SPEEDY);

            cam.setPosition(newX, newY);
            b2dCam.setPosition(newX / PPM, newY / PPM);
            cam.zoom = zoom;
            b2dCam.zoom = zoom;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
            cam.zoom = 20f;
            b2dCam.zoom = 20f;
        }

        b2dCam.update();
        cam.update();
    }

    public GUIManager getGUI() {
        return gui;
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

    public void exit() {
        SoundManager.stop(Assets.JETPACK_SOUND);
        worldManager.dispose();
        //lightManager.dispose();

        if (coreMap) {
            ScreenManager.setScreen(new LevelSelectScreen((levelNo / 10) + 1));
        } else {
            ScreenManager.setScreen(new MapEditorScreen(customMap));
        }
        customMap = null;
    }

    @Override
    public void goBack() {

        if (!gui.isPaused()) {
            gui.setPaused(true);
        }
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