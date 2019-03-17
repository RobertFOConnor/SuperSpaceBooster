package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.game.PlayerSaveObject;
import com.yellowbytestudios.spacedoctor.mapeditor.MapManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.media.SaveManager;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;
import com.yellowbytestudios.spacedoctor.screens.SplashScreen;
import com.yellowbytestudios.spacedoctor.spriter.SpriterManager;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;


public class MainGame extends ApplicationAdapter {

    public static SpriteBatch sb;
    public static SaveManager saveManager;
    public static PlayerSaveObject saveData;

    //Frame-rate variables.
    private static final float STEP = 1 / 60f;

    //Spriter manager. (Smooth Animations)
    public static SpriterManager spriterManager;
    private static AnimationManager animationManager;

    //Controller support variables.
    public static boolean hasControllers = false;
    public static Controller controller;

    public static String DEVICE;
    private boolean backPressed = true;
    public static boolean firstTime = false;
    public static final boolean UNLIM_JETPACK = true;
    public static final boolean UNLIM_AMMO = false;
    public static boolean BOX2D_LIGHTS = false;
    public static boolean TEST_MODE = false;
    public static final boolean DUNGEON_MODE = false;
    public static final boolean QUICK_BOOT = false; //Boots straight into first level.
    public static I18NBundle languageFile;
    public static Cursor cursor;
    private FPSLogger fps;
    private boolean fullscreen = false;


    public MainGame(String device) {
        DEVICE = device;
    }


    @Override
    public void create() {

        Gdx.input.setCatchBackKey(true);
        sb = new SpriteBatch();
        spriterManager = new SpriterManager(sb);
        animationManager = new AnimationManager();
        MapManager.initCells();

        //checkForController();
        Fonts.load();
        Assets.load();
        ScreenManager.setScreen(new SplashScreen());

        saveManager = new SaveManager(true);
        saveData = new PlayerSaveObject();

        PlayerSaveObject playerSaveObject = saveManager.loadDataValue("PLAYER", PlayerSaveObject.class);

        if (playerSaveObject != null) {
            saveData = playerSaveObject;
        } else {
            firstTime = true;
        }

        SoundManager.musicEnabled = saveData.isMusicEnabled();
        SoundManager.soundFXEnabled = saveData.isSoundFXEnabled();

        MainGame.saveManager.saveDataValue("PLAYER", MainGame.saveData);

        fps = new FPSLogger();

        setupCursor();

//        Controllers.addListener(new ControllerAdapter() {
//            @Override
//            public void connected(Controller controller) {
//                //checkForController();
//            }
//
//            public void disconnected(Controller controller) {
//                //checkForController();
//                System.out.println("Controller disconnected");
//            }
//        });
    }

    @Override
    public void render() {

        if (ScreenManager.getCurrentScreen() != null) {
            animationManager.update();

            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Gdx.gl20.glClearColor(0, 0, 0, 0);

            ScreenManager.getCurrentScreen().update(STEP);
            ScreenManager.getCurrentScreen().render(sb);

            fps.log();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            if (!backPressed) {
                ScreenManager.getCurrentScreen().goBack();
                backPressed = true;
            }
        } else {
            backPressed = false;
        }

        checkForFullscreen();
    }

    private void checkForFullscreen() {
        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {

                if (!fullscreen) {
                    Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
                    Gdx.graphics.setFullscreenMode(mode);
                } else {
                    Gdx.graphics.setWindowedMode(1280, 720);
                }
                setupCursor();
                fullscreen = !fullscreen;
            }
        }
    }

    private void setupCursor() {
        //Setup cursor.
        Gdx.input.setCursorCatched(false);
        Pixmap pm = new Pixmap(Gdx.files.internal("cursor.png"));
        cursor = Gdx.graphics.newCursor(pm, 0, 0);
        Gdx.graphics.setCursor(cursor);
        pm.dispose();
    }

    @Override
    public void resize(int w, int h) {

        if (ScreenManager.getCurrentScreen() != null)
            ScreenManager.getCurrentScreen().resize(w, h);
    }

    @Override
    public void dispose() {

        if (ScreenManager.getCurrentScreen() != null)
            ScreenManager.getCurrentScreen().dispose();

        sb.dispose();

        Assets.manager.dispose();
        Assets.manager = null;
    }


    @Override
    public void pause() {

        if (ScreenManager.getCurrentScreen() != null)
            ScreenManager.getCurrentScreen().pause();
    }

    @Override
    public void resume() {
        Fonts.load();
        Assets.load();
        while (!Assets.manager.update()) {
        }
        if (ScreenManager.getCurrentScreen() != null)
            ScreenManager.getCurrentScreen().resume();
    }

//    private void checkForController() {
//        if (Controllers.getControllers().size != 0) {
//            hasControllers = true;
//            controller = Controllers.getControllers().get(Controllers.getControllers().size - 1);
//        }
//        System.out.println("Num of controllers: " + Controllers.getControllers().size);
//    }
}
