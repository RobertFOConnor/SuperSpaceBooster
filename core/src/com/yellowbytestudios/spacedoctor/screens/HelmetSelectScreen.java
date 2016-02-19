package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBox360Pad;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;
import com.yellowbytestudios.spacedoctor.tween.SpriteText;

/**
 * Created by BobbyBoy on 19-Feb-16.
 */
public class HelmetSelectScreen implements Screen {

    public static int HELMET_NUM = 0;
    public static Array<Integer> UNLOCKED_HEADS = new Array<Integer>();
    private OrthoCamera camera;
    private Vector2 touch;
    private SpriteText title;
    private BackgroundManager bg;
    private Array<HelmetButton> helmetButtons;
    private SpriteButton backButton;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();

        bg = new BackgroundManager();

        title = new SpriteText("SELECT A HELMET", Fonts.timerFont);
        title.centerText();
        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT - 60);

        float helmetY = MainGame.HEIGHT / 2 + 50;
        int helmetCount = 0;

        UNLOCKED_HEADS.add(0);
        UNLOCKED_HEADS.add(1);
        UNLOCKED_HEADS.add(2);

        helmetButtons = new Array<HelmetButton>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                HelmetButton lb = new HelmetButton(new Vector2(294+((j*183)+(j*200)), helmetY - 600), helmetCount);
                helmetButtons.add(lb);
                helmetCount++;

                AnimationManager.applyAnimation(lb, lb.getX(), helmetY);
            }
            helmetY -= 300;
        }

        backButton = new SpriteButton(Assets.GO_BACK, new Vector2(-150, 900));
        AnimationManager.applyAnimation(backButton, 50, backButton.getY());
        AnimationManager.startAnimation();
    }

    private class HelmetButton extends SpriteButton {

        private int headNum;
        private String headName;
        private boolean unlocked = false;
        private boolean selected = false;
        private Texture border;

        public HelmetButton(Vector2 pos, int headNum) {
            super(Assets.HEAD_1, pos);

            this.headNum = headNum;
            headName = "HEAD "+headNum;

            if(!UNLOCKED_HEADS.contains(headNum, true)) {
                unlocked = false;
                setTexture(Assets.manager.get(Assets.LOCKED_HEAD, Texture.class));
            }
        }

        @Override
        public void draw(Batch sb) {
            sb.draw(getTexture(), getX(), getY());
            Fonts.GUIFont.draw(sb, headName, getX()+Fonts.getWidth(Fonts.GUIFont, headName)/2-20, getY()-50);
        }
    }

    @Override
    public void update(float step) {
        camera.update();
        bg.update();

        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBox360Pad.BUTTON_A)) {
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {

        } else if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            for (HelmetButton lb : helmetButtons) {
                if (lb.checkTouch(touch) && lb.unlocked) {

                }
            }

            if (backButton.checkTouch(touch)) {
                goBack();
            }
        }
    }

    private void advanceScreen(int levelNum) {
        ScreenManager.setScreen(new GameScreen(levelNum));
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.render(sb);
        backButton.draw(sb);
        title.draw(sb);

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
        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT + 100);
        AnimationManager.applyExitAnimation(backButton, -150, backButton.getY(), new MainMenuScreen());
        AnimationManager.startAnimation();
    }
}