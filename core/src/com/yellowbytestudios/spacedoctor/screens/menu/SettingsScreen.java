package com.yellowbytestudios.spacedoctor.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
    private SpriteButton backButton;
    private Array<Setting> settings;


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


        //Initialize settings and switches.
        settings = new Array<Setting>();

        settings.add(new Setting(MainGame.languageFile.get("MUSIC").toUpperCase(), 700) {
            @Override
            public void onSwitch() {
                super.onSwitch();
                SoundManager.toggleMusic();
            }
        });
        settings.get(settings.size - 1).button.switched_on = SoundManager.musicEnabled;


        settings.add(new Setting(MainGame.languageFile.get("SOUND_FX").toUpperCase(), 500) {
            @Override
            public void onSwitch() {
                super.onSwitch();
                SoundManager.soundFXEnabled = this.button.switched_on;
            }
        });
        settings.get(settings.size - 1).button.switched_on = SoundManager.soundFXEnabled;


        settings.add(new Setting("BOX2D BOUNDS", 300) {
            @Override
            public void onSwitch() {
                super.onSwitch();
                MainGame.TEST_MODE = !MainGame.TEST_MODE;
            }
        });
        settings.get(settings.size - 1).button.switched_on = MainGame.TEST_MODE;


        settings.add(new Setting("LIGHTS", 100) {
            @Override
            public void onSwitch() {
                super.onSwitch();
                MainGame.BOX2D_LIGHTS = !MainGame.BOX2D_LIGHTS;
            }
        });
        settings.get(settings.size - 1).button.switched_on = MainGame.BOX2D_LIGHTS;

        backButton = new SpriteButton(Assets.GO_BACK, new Vector2(-150, 900));


        //Apply starting animations. (Switches and title glide into screen view.)
        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT - 60);
        AnimationManager.applyAnimation(backButton, 50, backButton.getY());
        for (Setting s : settings) {
            AnimationManager.applyAnimation(s.button, 1200, s.button.getY());
            AnimationManager.applyAnimation(s.name, 450, s.name.getY());
        }
        AnimationManager.startAnimation();
    }


    @Override
    public void update(float step) {
        camera.update();
        bg.update();

        if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            for (Setting s : settings) {
                if (s.button.checkTouch(touch)) {
                    s.onSwitch();
                }
            }

            if (backButton.checkTouch(touch)) {
                goBack();
                SoundManager.play(Assets.BUTTON_CLICK);
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

        for (Setting s : settings) {
            s.name.draw(sb);
            s.button.draw(sb);
        }


        backButton.draw(sb);
        sb.end();
    }

    private class Setting {

        SpriteText name;
        SwitchButton button;

        public Setting(String name, float posY) {
            this.name = new SpriteText(name, Fonts.GUIFont);
            this.name.setPosition(-200, posY + 70);
            button = new SwitchButton(new Vector2(1920, posY));
        }

        public void onSwitch() {
            button.toggle();
            SoundManager.play(Assets.BUTTON_CLICK);
        }
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

        //Save new settings to json.
        MainGame.saveData.setMusicEnabled(SoundManager.musicEnabled);
        MainGame.saveData.setSoundFXEnabled(SoundManager.soundFXEnabled);
        MainGame.saveManager.saveDataValue("PLAYER", MainGame.saveData);

        //Apply exit animation.
        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT + 100);
        AnimationManager.applyExitAnimation(backButton, -150, backButton.getY(), new MainMenuScreen());
        for (Setting s : settings) {
            AnimationManager.applyAnimation(s.button, 1920, s.button.getY());
            AnimationManager.applyAnimation(s.name, -200, s.name.getY());
        }
        AnimationManager.startAnimation();
    }
}