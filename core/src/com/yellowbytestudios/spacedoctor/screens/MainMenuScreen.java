package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.Assets;
import com.yellowbytestudios.spacedoctor.Button;
import com.yellowbytestudios.spacedoctor.Fonts;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBox360Pad;

/**
 * Created by BobbyBoy on 26-Jan-16.
 */
public class MainMenuScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;
    private String title = "MAIN MENU";
    private Texture bg;
    private Controller controller;
    private Button editorButton, playButton, settingsButton;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();
        if (MainGame.hasControllers) {
            controller = Controllers.getControllers().get(0);
        }

        bg = Assets.manager.get(Assets.MENU_BG, Texture.class);
    }


    @Override
    public void update(float step) {
        camera.update();

        if (MainGame.hasControllers) {
            if (controller.getButton(XBox360Pad.BUTTON_A)) {
                //ScreenManager.setScreen(new GameScreen(selLevel));
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            ScreenManager.setScreen(new LevelSelectScreen());

        } else if (MainGame.DEVICE.equals("ANDROID") && Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

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

        Fonts.timerFont.draw(sb, title, MainGame.WIDTH / 2 - 250, MainGame.HEIGHT - 80);

        sb.end();
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
        ScreenManager.setScreen(new TitleScreen());
    }
}
