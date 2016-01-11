package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;
import com.yellowbytestudios.spacedoctor.spriter.SpriterManager;

public class MainGame extends ApplicationAdapter {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static SpriteBatch sb;

    //Frame-rate variables.
    public static final float STEP = 1 / 60f;
    private float accum;

    //Spriter manager. (Smooth Animations)
    public static SpriterManager spriterManager;

    //Controller support variables.
    public static boolean hasControllers = false;

    @Override
    public void create() {
        sb = new SpriteBatch();
        spriterManager = new SpriterManager(sb);
        checkForController();
        Fonts.loadFonts();
        ScreenManager.setScreen(new GameScreen());
    }

    @Override
    public void render() {
        if (ScreenManager.getCurrentScreen() != null) {

            accum += Gdx.graphics.getDeltaTime();
            while (accum >= STEP) {
                accum -= STEP;
                ScreenManager.getCurrentScreen().update(STEP);
                ScreenManager.getCurrentScreen().render(sb);
            }
        }
    }

    private void checkForController() {
        if(Controllers.getControllers().size != 0) {
            hasControllers = true;
            System.out.println("XBox 360 Controller detected!");
        }
    }
}
