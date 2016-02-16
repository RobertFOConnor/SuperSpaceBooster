package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.objects.Button;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBox360Pad;

/**
 * Created by BobbyBoy on 26-Jan-16.
 */
public class MainMenuScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;
    private Texture bg;
    private Button editorButton, playButton;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();

        bg = Assets.manager.get(Assets.MENU_BG, Texture.class);
        playButton = new Button(Assets.START_GAME, new Vector2(310, 550));
        editorButton = new Button(Assets.LEVEL_BUILDER, new Vector2(310, 100));
    }


    @Override
    public void update(float step) {
        camera.update();

        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBox360Pad.BUTTON_A)) {
                ScreenManager.setScreen(new LevelSelectScreen());
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            ScreenManager.setScreen(new SettingsScreen());

        } else if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (playButton.checkTouch(touch)) {
                ScreenManager.setScreen(new LevelSelectScreen());
            } else if (editorButton.checkTouch(touch)) {
                ScreenManager.setScreen(new MapEditorScreen());
            }

        }
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, 0, 0);
        playButton.render(sb);
        editorButton.render(sb);
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
