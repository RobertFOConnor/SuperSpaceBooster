package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.objects.Button;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBox360Pad;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;


public class SettingsScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;
    private Texture bg;
    private Controller controller;

    private String music = "MUSIC";
    private String soundFX = "SOUND FX";
    private SwitchButton musicButton, soundFXButton;
    private Button backButton;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();
        if (MainGame.hasControllers) {
            controller = Controllers.getControllers().get(0);
        }

        bg = Assets.manager.get(Assets.MENU_BG, Texture.class);
        musicButton = new SwitchButton(new Vector2(1200, 650));
        soundFXButton = new SwitchButton(new Vector2(1200, 250));

        if(SoundManager.musicEnabled) {
            musicButton.switched_on = true;
        }

        if(SoundManager.soundFXEnabled) {
            soundFXButton.switched_on = true;
        }

        backButton = new Button(Assets.GO_BACK, new Vector2(50, 900));
    }


    @Override
    public void update(float step) {
        camera.update();

        if (MainGame.hasControllers) {
            if (controller.getButton(XBox360Pad.BUTTON_A)) {
                //ScreenManager.setScreen(new LevelSelectScreen());
            }

        } else if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (musicButton.checkTouch(touch)) {
                musicButton.toggle();
                SoundManager.musicEnabled = musicButton.switched_on;

            } else if (soundFXButton.checkTouch(touch)) {
                soundFXButton.toggle();
                SoundManager.soundFXEnabled = soundFXButton.switched_on;

            } else if(backButton.checkTouch(touch)) {
                ScreenManager.setScreen(new MainMenuScreen());
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
        sb.draw(bg, 0, 0);
        Fonts.GUIFont.draw(sb, music, 450, 720);
        Fonts.GUIFont.draw(sb, soundFX, 450, 320);
        musicButton.render(sb);
        soundFXButton.render(sb);
        backButton.render(sb);
        sb.end();
    }

    private class SwitchButton {

        private boolean switched_on = false;
        private Texture onImg = Assets.manager.get(Assets.SWITCH_ON, Texture.class);
        private Texture offImg = Assets.manager.get(Assets.SWITCH_OFF, Texture.class);
        private Vector2 pos;

        public SwitchButton(Vector2 pos) {
            this.pos = pos;
        }

        public void toggle() {
            switched_on = !switched_on;
        }

        public boolean checkTouch(Vector2 touch) {
            return getBounds().contains(touch);
        }

        public void render(SpriteBatch sb) {
            if (switched_on) {
                sb.draw(onImg, pos.x, pos.y);
            } else {
                sb.draw(offImg, pos.x, pos.y);
            }
        }


        public Rectangle getBounds() {
            return new Rectangle(pos.x, pos.y, onImg.getWidth(), onImg.getHeight());
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
        ScreenManager.setScreen(new TitleScreen());
    }
}