package com.yellowbytestudios.spacedoctor.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.controllers.XBoxController;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.BackgroundManager;
import com.yellowbytestudios.spacedoctor.screens.Screen;
import com.yellowbytestudios.spacedoctor.screens.editor.NewLoadScreen;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;
import com.yellowbytestudios.spacedoctor.tween.SpriteText;
import com.yellowbytestudios.spacedoctor.utils.Metrics;

/**
 * Created by BobbyBoy on 26-Jan-16.
 */
public class MainMenuScreen extends Screen {

    private BackgroundManager bg;
    private SpriteText title;
    private SpriteButton editorButton, playButton, statButton, settings;

    @Override
    public void create() {
        super.create();

        bg = new BackgroundManager();
        playButton = setupButton(Assets.START_GAME, 660, Metrics.HEIGHT, 660, Metrics.HEIGHT - 650);
        editorButton = setupButton(Assets.LEVEL_BUILDER, Metrics.WIDTH, Metrics.HEIGHT - 850, 1160, Metrics.HEIGHT - 850);
        statButton = setupButton(Assets.STATS, -600, Metrics.HEIGHT - 850, 160, Metrics.HEIGHT - 850);
        settings = setupButton(Assets.SETTINGS, Metrics.WIDTH, Metrics.HEIGHT - 130, Metrics.WIDTH - 130, Metrics.HEIGHT - 130);

        title = new SpriteText(MainGame.languageFile.get("MAIN_MENU").toUpperCase(), Fonts.timerFont);
        title.centerText();
        AnimationManager.applyAnimation(title, title.getX(), Metrics.HEIGHT - 60);
        AnimationManager.startAnimation();
        Gdx.input.setCursorCatched(false);
    }

    private SpriteButton setupButton(String ref, float startX, float startY, float endX, float endY) {
        SpriteButton button = new SpriteButton(ref, new Vector2(startX, startY));
        AnimationManager.applyAnimation(button, endX, endY);
        return button;
    }


    @Override
    public void update(float step) {
        bg.update();

        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBoxController.BUTTON_A)) {
                advanceScreen(new LevelSelectScreen(1));
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            advanceScreen(new SettingsScreen());

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();

        }

        if (Gdx.input.justTouched()) {
            Vector2 touch = getTouchPos();

            if (playButton.checkTouch(touch)) {
                advanceScreen(new LevelSelectScreen(1));
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

        AnimationManager.applyAnimation(title, title.getX(), Metrics.HEIGHT + 100);
        AnimationManager.applyAnimation(playButton, 660, Metrics.HEIGHT);
        AnimationManager.applyAnimation(editorButton, Metrics.WIDTH, Metrics.HEIGHT - 850);
        AnimationManager.applyAnimation(statButton, -600, Metrics.HEIGHT - 850);
        AnimationManager.applyExitAnimation(settings, Metrics.WIDTH, Metrics.HEIGHT - 130, s);
        AnimationManager.startAnimation();

        SoundManager.play(Assets.BUTTON_CLICK);
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
    public void goBack() {
        advanceScreen(new TitleScreen());
    }
}
