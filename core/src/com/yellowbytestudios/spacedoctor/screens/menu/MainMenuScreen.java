package com.yellowbytestudios.spacedoctor.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBox360Pad;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.*;
import com.yellowbytestudios.spacedoctor.screens.editor.NewLoadScreen;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;
import com.yellowbytestudios.spacedoctor.tween.SpriteText;

/**
 * Created by BobbyBoy on 26-Jan-16.
 */
public class MainMenuScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;
    private BackgroundManager bg;
    private SpriteText title;
    private SpriteButton editorButton, playButton, statButton, settings;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();

        bg = new BackgroundManager();
        playButton = new SpriteButton(Assets.START_GAME, new Vector2(660, MainGame.HEIGHT));
        editorButton = new SpriteButton(Assets.LEVEL_BUILDER, new Vector2(MainGame.WIDTH, MainGame.HEIGHT - 850));
        statButton = new SpriteButton(Assets.STATS, new Vector2(-600, MainGame.HEIGHT - 850));
        settings = new SpriteButton(Assets.SETTINGS, new Vector2(MainGame.WIDTH, MainGame.HEIGHT - 130));
        title = new SpriteText(MainGame.languageFile.get("MAIN_MENU").toUpperCase(), Fonts.timerFont);
        title.centerText();

        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT - 60);
        AnimationManager.applyAnimation(playButton, 660, MainGame.HEIGHT - 650);
        AnimationManager.applyAnimation(editorButton, 1160, MainGame.HEIGHT - 850);
        AnimationManager.applyAnimation(statButton, 160, MainGame.HEIGHT - 850);
        AnimationManager.applyAnimation(settings, MainGame.WIDTH - 130, MainGame.HEIGHT - 130);
        AnimationManager.startAnimation();
    }


    @Override
    public void update(float step) {
        camera.update();
        bg.update();

        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBox360Pad.BUTTON_A)) {
                advanceScreen(new WorldSelectScreen());
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            advanceScreen(new SettingsScreen());

        }

        if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (playButton.checkTouch(touch)) {
                advanceScreen(new WorldSelectScreen());
            } else if (editorButton.checkTouch(touch)) {
                advanceScreen(new NewLoadScreen());
            } else if (statButton.checkTouch(touch)) {
                advanceScreen(new HelmetSelectScreen());
            } else if (settings.checkTouch(touch)) {
                advanceScreen(new SettingsScreen());
            }
        }
    }


    private void advanceScreen(final Screen s) {

        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT + 100);
        AnimationManager.applyAnimation(playButton, 660, MainGame.HEIGHT);
        AnimationManager.applyAnimation(editorButton, MainGame.WIDTH, MainGame.HEIGHT - 850);
        AnimationManager.applyAnimation(statButton, -600, MainGame.HEIGHT - 850);
        AnimationManager.applyExitAnimation(settings, MainGame.WIDTH, MainGame.HEIGHT - 130, s);

        AnimationManager.startAnimation();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.render(sb);
        title.draw(sb);
        editorButton.draw(sb);
        statButton.draw(sb);
        playButton.draw(sb);
        settings.draw(sb);
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
        advanceScreen(new TitleScreen());
    }
}
