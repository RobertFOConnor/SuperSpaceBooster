package com.yellowbytestudios.spacedoctor.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBoxController;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.BackgroundManager;
import com.yellowbytestudios.spacedoctor.screens.Screen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;
import com.yellowbytestudios.spacedoctor.tween.SpriteText;

/**
 * Created by Robert on 03/30/16.
 */
public class WorldSelectScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;
    private SpriteText title;
    private BackgroundManager bg;
    private Array<WorldButton> levelButtons;
    private SpriteButton backButton;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();
        bg = new BackgroundManager();

        title = new SpriteText(MainGame.languageFile.get("SELECT_WORLD").toUpperCase(), Fonts.timerFont);
        title.centerText();
        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT - 60);

        float levelY = (MainGame.HEIGHT/2)-40;
        int levelCount = 1;

        levelButtons = new Array<WorldButton>();
        for (int i = 0; i < 8; i++) {
            WorldButton lb = new WorldButton(new Vector2(40 + (160 * i), levelY - 600), levelCount);
            levelButtons.add(lb);
            levelCount++;
            AnimationManager.applyAnimation(lb, lb.getX(), levelY);
        }

        backButton = new SpriteButton(Assets.GO_BACK, new Vector2(-150, 900));
        AnimationManager.applyAnimation(backButton, 50, backButton.getY());
        AnimationManager.startAnimation();
    }

    private class WorldButton extends SpriteButton {

        private int levelNum;
        private boolean unlocked = true;
        private NinePatch bg;

        public WorldButton(Vector2 pos, int levelNum) {
            super(Assets.BOX, pos);

            this.bg = new NinePatch(getTexture(), 40, 40, 40, 40);
            String img;
            img = Assets.LEVEL_BUTTON;
            setTexture(Assets.manager.get(img, Texture.class));
            this.levelNum = levelNum;
        }

        @Override
        public void draw(Batch sb) {
            bg.draw(sb, getX(), getY(), 120, 80);
            Fonts.GUIFont.draw(sb, "" + levelNum, getX() + 40, getY() + 40);
        }
    }

    @Override
    public void update(float step) {
        camera.update();
        bg.update();

        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBoxController.BUTTON_A)) {
                advanceScreen(1);
            } else if (MainGame.controller.getButton(XBoxController.BUTTON_BACK)) {
                goBack();
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            advanceScreen(1);
        }

        if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            for (WorldButton lb : levelButtons) {
                if (lb.checkTouch(touch) && lb.unlocked) {
                    advanceScreen(lb.levelNum);
                }
            }

            if (backButton.checkTouch(touch)) {
                goBack();
            }
        }
    }

    private void advanceScreen(int worldNum) {
        ScreenManager.setScreen(new LevelSelectScreen(worldNum));
        SoundManager.play(Assets.BUTTON_CLICK);
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.render(sb);
        backButton.draw(sb);
        title.draw(sb);

        for (WorldButton lb : levelButtons) {
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
        for (WorldButton lb : levelButtons) {
            AnimationManager.applyAnimation(lb, lb.getX(), -300);
        }
        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT + 100);
        AnimationManager.applyExitAnimation(backButton, -150, backButton.getY(), new MainMenuScreen());
        AnimationManager.startAnimation();
        SoundManager.play(Assets.BUTTON_CLICK);
    }
}