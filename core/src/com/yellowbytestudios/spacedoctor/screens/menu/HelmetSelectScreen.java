package com.yellowbytestudios.spacedoctor.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brashmonkey.spriter.Player;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
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

public class HelmetSelectScreen implements Screen {

    private static final String[] HELMET_NAMES = {"S-BOOSTER", "LOU-LOU", "G1-BTH", "T-DOGG", "SMITH", "FROGG-E", "NINJA", "ROBO-GOB"};
    public static final float[][] CHAR_COLORS = {{0.7f, 0.9f, 0.9f}, {1f, 0.1f, 0.6f}, {0.97f, 0.89f, 0.13f}, {0.23f, 0.9f, 0.9f}, {0.72f, 0.5f, 0.22f}, {0.3f, 0.78f, 0.17f}, {0.4f, 0.4f, 0.4f}, {0.49f, 0.28f, 0.53f}};

    private OrthoCamera camera;
    private Vector2 touch;
    private SpriteText title;
    private BackgroundManager bg;
    private Array<HelmetButton> helmetButtons;
    private HelmetButton selectedHelmet;
    private SpriteButton backButton;

    private Player spriter;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();

        bg = new BackgroundManager();

        title = new SpriteText(MainGame.languageFile.get("SELECT_HELMET").toUpperCase(), Fonts.timerFont);
        title.centerText();
        AnimationManager.applyAnimation(title, title.getX(), Metrics.HEIGHT - 60);

        float helmetY = Metrics.HEIGHT / 2f + 50f;
        int helmetCount = 0;


        //Unlock all heads.
        for (int i = 0; i < 8; i++) {
            MainGame.saveData.unlockHead(i);
        }
        Fonts.GUIFont.setColor(Color.WHITE);
        helmetButtons = new Array<HelmetButton>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                HelmetButton lb = new HelmetButton(new Vector2(294 + ((j * 183) + (j * 200)), helmetY - 600), helmetCount);
                helmetButtons.add(lb);
                helmetCount++;

                AnimationManager.applyAnimation(lb, lb.getX(), helmetY);
            }
            helmetY -= 350;
        }

        backButton = new SpriteButton(Assets.GO_BACK, new Vector2(-150, 900));
        AnimationManager.applyAnimation(backButton, 50, backButton.getY());
        AnimationManager.startAnimation();

        spriter = MainGame.spriterManager.getSpiter("selector", "default", 1.6f);
        selectedHelmet = helmetButtons.get(MainGame.saveData.getHead());
    }

    private class HelmetButton extends SpriteButton {

        private int headNum;
        private String headName;
        private float labelX;
        private boolean unlocked = false;

        public HelmetButton(Vector2 pos, int headNum) {
            super(Assets.LOCKED_HEAD, pos);

            this.headNum = headNum;
            headName = "LOCKED";

            if (MainGame.saveData.isUnlocked(headNum)) {
                unlocked = true;
                headName = HELMET_NAMES[headNum];

                Texture texture = new Texture(Gdx.files.internal("spaceman/heads/head_" + headNum + ".png"));
                texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                setTexture(texture);
            }
            labelX = Fonts.getWidth(Fonts.GUIFont, headName) - 90;
        }

        @Override
        public void draw(Batch sb) {
            sb.draw(getTexture(), getX(), getY());
            Fonts.GUIFont.draw(sb, headName, getX() - labelX, getY() - 50);
        }
    }

    @Override
    public void update(float step) {
        camera.update();
        bg.update();

        spriter.setPosition(selectedHelmet.getX() + 90, selectedHelmet.getY() + 90);
        spriter.update();


        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBoxController.BUTTON_BACK)) {
                goBack();
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            goBack();
        }

        if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            for (HelmetButton lb : helmetButtons) {
                if (lb.checkTouch(touch) && lb.unlocked) {
                    selectedHelmet = lb;
                    SoundManager.play(Assets.BUTTON_CLICK);
                }
            }

            if (backButton.checkTouch(touch)) {
                goBack();
            }
        }
    }


    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.render(sb);
        backButton.draw(sb);
        title.draw(sb);
        MainGame.spriterManager.draw(spriter);
        for (HelmetButton lb : helmetButtons) {
            lb.draw(sb);
        }

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
        for (HelmetButton lb : helmetButtons) {
            AnimationManager.applyAnimation(lb, lb.getX(), -300);
        }
        AnimationManager.applyAnimation(title, title.getX(), Metrics.HEIGHT + 100);
        AnimationManager.applyExitAnimation(backButton, -150, backButton.getY(), new MainMenuScreen());
        AnimationManager.startAnimation();

        MainGame.saveData.setHead(selectedHelmet.headNum);
        MainGame.saveManager.saveDataValue("PLAYER", MainGame.saveData);

        SoundManager.play(Assets.BUTTON_CLICK);
    }
}