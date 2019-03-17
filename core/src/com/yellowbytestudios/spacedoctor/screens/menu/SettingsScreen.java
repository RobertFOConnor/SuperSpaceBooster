package com.yellowbytestudios.spacedoctor.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.controllers.BasicController;
import com.yellowbytestudios.spacedoctor.controllers.KeyboardController;
import com.yellowbytestudios.spacedoctor.controllers.XBoxController;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.BackgroundManager;
import com.yellowbytestudios.spacedoctor.screens.Screen;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;
import com.yellowbytestudios.spacedoctor.tween.SpriteText;
import com.yellowbytestudios.spacedoctor.utils.Metrics;

public class SettingsScreen extends Screen {

    private BackgroundManager bg;
    private BasicController controller;
    private SpriteText title;
    private SpriteButton backButton;
    private Array<Setting> settings;

    @Override
    public void create() {
        super.create();
        if (MainGame.hasControllers) {
            controller = new XBoxController(0);
        } else {
            controller = new KeyboardController();
        }

        title = new SpriteText(MainGame.languageFile.get("SETTINGS").toUpperCase(), Fonts.timerFont);
        title.centerText();

        bg = new BackgroundManager();

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
        backButton = new SpriteButton(Assets.GO_BACK, new Vector2(-150, 900));


        //Apply starting animations. (Switches and title glide into screen view.)
        AnimationManager.applyAnimation(title, title.getX(), Metrics.HEIGHT - 60);
        AnimationManager.applyAnimation(backButton, 50, backButton.getY());
        for (Setting s : settings) {
            AnimationManager.applyAnimation(s.button, 1200, s.button.getY());
            AnimationManager.applyAnimation(s.name, 450, s.name.getY());
        }
        AnimationManager.startAnimation();
    }


    @Override
    public void update(float step) {
        bg.update();

        if (controller.menuBack()) {
            goBack();
        }

        if (Gdx.input.justTouched()) {
            Vector2 touch = getTouchPos();

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
    public void goBack() {

        //Save new settings to json.
        MainGame.saveData.setMusicEnabled(SoundManager.musicEnabled);
        MainGame.saveData.setSoundFXEnabled(SoundManager.soundFXEnabled);
        MainGame.saveManager.saveDataValue("PLAYER", MainGame.saveData);

        //Apply exit animation.
        AnimationManager.applyAnimation(title, title.getX(), Metrics.HEIGHT + 100);
        AnimationManager.applyExitAnimation(backButton, -150, backButton.getY(), new MainMenuScreen());
        for (Setting s : settings) {
            AnimationManager.applyAnimation(s.button, 1920, s.button.getY());
            AnimationManager.applyAnimation(s.name, -200, s.name.getY());
        }
        AnimationManager.startAnimation();
    }
}