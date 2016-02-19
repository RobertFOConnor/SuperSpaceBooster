package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.BackgroundManager;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;
import com.yellowbytestudios.spacedoctor.screens.SplashScreen;
import com.yellowbytestudios.spacedoctor.spriter.SpriterManager;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;


public class MainGame extends ApplicationAdapter {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static SpriteBatch sb;

    //Frame-rate variables.
    public static final float STEP = 1 / 60f;

    //Spriter manager. (Smooth Animations)
    public static SpriterManager spriterManager;
    public static AnimationManager animationManager;

    //Controller support variables.
    public static boolean hasControllers = false;
    public static Controller controller;

    public static int UNLOCKED_LEVEL = 1;

    public static String DEVICE;
    private boolean backPressed = false;

    public static final boolean TEST_MODE = false;


    public MainGame(String device) {
        DEVICE = device;
    }


    @Override
    public void create() {
        //Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
        Gdx.input.setCatchBackKey(true);
        sb = new SpriteBatch();
        spriterManager = new SpriterManager(sb);
        animationManager = new AnimationManager();

        checkForController();
        ScreenManager.setScreen(new SplashScreen());
    }

    @Override
    public void render() {

        if (ScreenManager.getCurrentScreen() != null) {
            animationManager.update();

            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Gdx.gl20.glClearColor(0, 0, 0, 0);

            ScreenManager.getCurrentScreen().update(STEP);
            ScreenManager.getCurrentScreen().render(sb);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            if (!backPressed) {
                ScreenManager.getCurrentScreen().goBack();
                backPressed = true;
            }
        } else {
            backPressed = false;
        }
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

        Fonts.dispose();
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
            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Gdx.gl20.glClearColor((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), 0);
        }
        if (ScreenManager.getCurrentScreen() != null)
            ScreenManager.getCurrentScreen().resume();
    }

    private void checkForController() {
        if (Controllers.getControllers().size != 0) {
            hasControllers = true;
            controller = Controllers.getControllers().get(0);
        }
    }
}
