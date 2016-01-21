package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBox360Pad;

/**
 * Created by BobbyBoy on 16-Jan-16.
 */
public class TitleScreen implements Screen {

    private OrthoCamera camera;
    private Texture bg;
    private Controller controller;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        bg = new Texture(Gdx.files.internal("titlescreen.png"));

        if (MainGame.hasControllers) {
            controller = Controllers.getControllers().get(0);
        }
    }

    @Override
    public void update(float step) {
        if (MainGame.hasControllers) {
            if (controller.getButton(XBox360Pad.BUTTON_START)) {
                ScreenManager.setScreen(new LevelSelectScreen());
            }
        } else if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            ScreenManager.setScreen(new LevelSelectScreen());

        } else if(MainGame.DEVICE.equals("ANDROID") && Gdx.input.justTouched()) {
            ScreenManager.setScreen(new LevelSelectScreen());
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        // Clear screen.
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glClearColor(0, 0, 0, 0);


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

    }
}
