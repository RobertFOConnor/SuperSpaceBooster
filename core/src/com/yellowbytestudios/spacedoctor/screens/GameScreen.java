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
import com.yellowbytestudios.spacedoctor.controllers.AndroidController;
import com.yellowbytestudios.spacedoctor.effects.LightManager;
import com.yellowbytestudios.spacedoctor.effects.ParticleManager;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.game.DungeonGenerator;
import com.yellowbytestudios.spacedoctor.game.GUIManager;
import com.yellowbytestudios.spacedoctor.game.GameCamera;
import com.yellowbytestudios.spacedoctor.game.LevelBackgroundManager;
import com.yellowbytestudios.spacedoctor.game.WorldManager;
import com.yellowbytestudios.spacedoctor.game.player.SpacemanPlayer;
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
    private int worldNo = 1;
    private int levelNo = 1;

    private TiledMap tileMap;
    private Box2DDebugRenderer b2dr; //DEBUG
    private OrthogonalTiledMapRenderer tmr;

    private Sprite transition;

    //Map Editor
    public static CustomMap customMap;

    private WorldManager worldManager;
    private LevelBackgroundManager bgManager;
    public static ParticleManager particleManager;
    public static LightManager lightManager;
    private GUIManager gui;

    private GameCamera gameCamera;

    //ANDROID CONTROLLER
    public static AndroidController androidController;

    public static boolean coreMap = false;

    // Set camera boundries.
    private int mapWidth;
    private int mapHeight;

    public GameScreen(int levelNo) {
        this.levelNo = levelNo;
        worldNo = (levelNo / 10) + 1;

        gameCamera = new GameCamera();

        //TEMP!!!!
        CoreLevelSaver levelSaver = new CoreLevelSaver("levels/level_" + levelNo + ".json", true);
        CustomMap customMap;
        if (MainGame.DUNGEON_MODE) {
            customMap = DungeonGenerator.getGeneratedMap();
        } else {
            customMap = levelSaver.loadDataValue("LEVEL", CustomMap.class);
        }

        this.customMap = customMap;
        coreMap = true;
    }

    public GameScreen(CustomMap customMap) {
        this.customMap = customMap;
        coreMap = false;
    }

    @Override
    public void create() {
        //SoundManager.switchMusic(Assets.LEVEL_THEME);

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

        bgManager = new LevelBackgroundManager(gameCamera.getCam(), mapWidth, mapHeight);

        transition = new Sprite(new Texture(Gdx.files.internal("black.png")));
        transition.setPosition(0, 0);
        AnimationManager.applyLevelStartAnimation(transition, MainGame.WIDTH, 0);
        AnimationManager.startAnimation();
    }


    public void setupMap() {
        Vector2 startPos = customMap.getStartPos();

        worldManager = new WorldManager(this, startPos.x, startPos.y);

        mapWidth = customMap.getMapWidth();
        mapHeight = customMap.getMapHeight();
        Array<SpacemanPlayer> players = worldManager.getPlayers();

        gameCamera.setBounds(mapWidth, mapHeight, startPos.x, startPos.y);

        gui = new GUIManager(this, players, true);
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
            gameCamera.update(worldManager.getPlayers());
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
        sb.setProjectionMatrix(gameCamera.getCam().combined);
        sb.begin();
        bgManager.render(sb);
        sb.end();

        //DRAW TILES.
        tmr.setView(gameCamera.getCam());
        tmr.render();

        //DRAW WORLD.
        worldManager.render(sb);

        if (MainGame.BOX2D_LIGHTS) {
            lightManager.render();
        }

        sb.setProjectionMatrix(gameCamera.getCam().combined);
        sb.begin();
        particleManager.render(sb);
        sb.end();


        //DRAW GUI!
        gui.render(sb);

        if (MainGame.DEVICE.equals("ANDROID")) {
            androidController.render(sb);
        }

        //Render Box2D world.
        if (MainGame.TEST_MODE) {
            b2dr.render(worldManager.getWorld(), gameCamera.getB2dCam().combined);
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


    public GUIManager getGUI() {
        return gui;
    }


    public void exit() {
        SoundManager.stop(Assets.JETPACK_SOUND);
        worldManager.dispose();

        if (coreMap) {
            ScreenManager.setScreen(new LevelSelectScreen((levelNo / 10) + 1));
        } else {
            ScreenManager.setScreen(new MapEditorScreen(customMap));
        }
        customMap = null;
    }

    public int getWorldNo() {
        return worldNo;
    }

    public int getLevelNo() {
        return levelNo;
    }

    @Override
    public void goBack() {

        if (!gui.isPaused()) {
            gui.setPaused(true);
        }
    }

    public TiledMap getTileMap() {
        return tileMap;
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