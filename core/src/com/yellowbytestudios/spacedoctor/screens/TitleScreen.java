package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbytestudios.spacedoctor.Assets;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBox360Pad;

/**
 * Created by BobbyBoy on 16-Jan-16.
 */
public class TitleScreen implements Screen {

    private OrthoCamera camera;
    private Texture bg;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        bg = Assets.manager.get(Assets.TITLESCREEN, Texture.class);
    }

    @Override
    public void update(float step) {
        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBox360Pad.BUTTON_START)) {
                advanceScreen();
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            advanceScreen();

        } else if (MainGame.DEVICE.equals("ANDROID") && Gdx.input.justTouched()) {
            advanceScreen();
        }
    }

    private void advanceScreen() {
        ScreenManager.setScreen(new MainMenuScreen());
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, 0, 0);
        sb.end();
    }

    @Override
    public void resize(int w, int h) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void goBack() {
        Gdx.app.exit();
    }
}
