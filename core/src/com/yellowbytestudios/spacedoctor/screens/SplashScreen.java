package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;

public class SplashScreen implements Screen {

    private OrthoCamera camera;
    private String percentage;
    private Sprite loadScreen;
    private Sprite loadWheel;
    private boolean displayImage = false;

    public SplashScreen() {
        camera = new OrthoCamera();
        camera.resize();
        Fonts.load();
        Assets.load();
    }

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
    }

    @Override
    public void update(float dt) {
        camera.update();
        percentage = ((int) (Assets.manager.getProgress() * 100) + "%");

        if (!displayImage) {
            if (Assets.manager.getProgress() > 0.02) {
                loadScreen = new Sprite(Assets.manager.get(Assets.LOADSCREEN, Texture.class));
                loadWheel = new Sprite(Assets.manager.get(Assets.LOADWHEEL, Texture.class));
                loadWheel.setPosition(MainGame.WIDTH - 180, 100);

                loadScreen.setAlpha(0f);
                loadWheel.setAlpha(0f);

                AnimationManager.applyFadeAnimation(loadScreen);
                AnimationManager.applyFadeAnimation(loadWheel);
                AnimationManager.startAnimation();

                displayImage = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {

        if (displayImage) {
            sb.setProjectionMatrix(camera.combined);
            sb.begin();
            loadScreen.draw(sb);
            loadWheel.rotate(10f);
            loadWheel.draw(sb);
            Fonts.GUIFont.setColor(Color.WHITE);
            Fonts.GUIFont.draw(sb, percentage, MainGame.WIDTH - Fonts.getWidth(Fonts.GUIFont, percentage) - 250, 160);
            Fonts.GUIFont.setColor(Color.WHITE);
            sb.end();
        }

        if (Assets.update()) { // DONE LOADING. SHOW TITLE SCREEN.

            SoundManager.setMusic(Assets.MAIN_THEME);

            if (MainGame.TEST_MODE) {
                ScreenManager.setScreen(new GameScreen(5));
            } else {
                ScreenManager.setScreen(new com.yellowbytestudios.spacedoctor.screens.menu.TitleScreen());
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.resize();
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
