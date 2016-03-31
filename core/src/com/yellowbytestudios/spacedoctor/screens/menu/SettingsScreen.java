package com.yellowbytestudios.spacedoctor.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.*;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;
import com.yellowbytestudios.spacedoctor.tween.SpriteText;


public class SettingsScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;
    private BackgroundManager bg;
    private Controller controller;
    private SpriteText title;
    private SpriteText music;
    private SpriteText soundFX;
    private SwitchButton musicButton, soundFXButton;
    private SpriteButton backButton;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();
        if (MainGame.hasControllers) {
            controller = Controllers.getControllers().get(0);
        }

        title = new SpriteText(MainGame.languageFile.get("SETTINGS").toUpperCase(), Fonts.timerFont);
        title.centerText();

        bg = new BackgroundManager();
        musicButton = new SwitchButton(new Vector2(1920, 650));
        soundFXButton = new SwitchButton(new Vector2(1920, 250));

        music = new SpriteText(MainGame.languageFile.get("MUSIC").toUpperCase(), Fonts.GUIFont);
        music.setPosition(-200, 720);
        soundFX = new SpriteText(MainGame.languageFile.get("SOUND_FX").toUpperCase(), Fonts.GUIFont);
        soundFX.setPosition(-200, 320);

        musicButton.switched_on = SoundManager.musicEnabled;
        soundFXButton.switched_on = SoundManager.soundFXEnabled;

        backButton = new SpriteButton(Assets.GO_BACK, new Vector2(-150, 900));

        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT - 60);
        AnimationManager.applyAnimation(backButton, 50, backButton.getY());
        AnimationManager.applyAnimation(musicButton, 1200, musicButton.getY());
        AnimationManager.applyAnimation(soundFXButton, 1200, soundFXButton.getY());
        AnimationManager.applyAnimation(soundFX, 450, soundFX.getY());
        AnimationManager.applyAnimation(music, 450, music.getY());
        AnimationManager.startAnimation();
    }


    @Override
    public void update(float step) {
        camera.update();
        bg.update();

        if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (musicButton.checkTouch(touch)) {
                musicButton.toggle();
                SoundManager.toggleMusic();
                MainGame.saveData.setMusicEnabled(SoundManager.musicEnabled);
                MainGame.saveManager.saveDataValue("PLAYER", MainGame.saveData);

            } else if (soundFXButton.checkTouch(touch)) {
                soundFXButton.toggle();
                SoundManager.soundFXEnabled = soundFXButton.switched_on;
                MainGame.saveData.setSoundFXEnabled(SoundManager.soundFXEnabled);
                MainGame.saveManager.saveDataValue("PLAYER", MainGame.saveData);

            } else if (backButton.checkTouch(touch)) {
                goBack();
            }

        }
    }

    @Override
    public void render(SpriteBatch sb) {
        // Clear screen.
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glClearColor(0, 0, 0, 0);


        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.render(sb);
        title.draw(sb);
        music.draw(sb);
        soundFX.draw(sb);
        musicButton.draw(sb);
        soundFXButton.draw(sb);
        backButton.draw(sb);
        sb.end();
    }

    private class SwitchButton extends SpriteButton {

        private boolean switched_on = false;
        private Texture onImg = Assets.manager.get(Assets.SWITCH_ON, Texture.class);
        private Texture offImg = Assets.manager.get(Assets.SWITCH_OFF, Texture.class);

        public SwitchButton(Vector2 pos) {
            super(Assets.SWITCH_ON, pos);
        }

        public void toggle() {
            switched_on = !switched_on;
        }

        @Override
        public void draw(Batch sb) {
            if (switched_on) {
                sb.draw(onImg, getX(), getY());
            } else {
                sb.draw(offImg, getX(), getY());
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
        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT + 100);
        AnimationManager.applyAnimation(backButton, -150, backButton.getY());
        AnimationManager.applyAnimation(musicButton, 1920, musicButton.getY());
        AnimationManager.applyAnimation(soundFXButton, 1920, soundFXButton.getY());
        AnimationManager.applyAnimation(soundFX, -200, soundFX.getY());
        AnimationManager.applyExitAnimation(music, -200, music.getY(), new MainMenuScreen());
        AnimationManager.startAnimation();
    }
}