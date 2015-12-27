package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;

public class MainGame extends ApplicationAdapter {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static SpriteBatch sb;

    public static final float STEP = 1 / 60f;
    private float accum;

    @Override
    public void create() {
        sb = new SpriteBatch();
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
}
