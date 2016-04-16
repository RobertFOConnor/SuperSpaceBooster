package com.yellowbytestudios.spacedoctor.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBox360Pad;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.BackgroundManager;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.Screen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;
import com.yellowbytestudios.spacedoctor.tween.SpriteText;

public class LevelSelectScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;
    private SpriteText title;
    private BackgroundManager bg;
    private Array<LevelButton> levelButtons;
    private SpriteButton backButton;
    private int worldNum = 1;

    public LevelSelectScreen(int worldNum) {
        this.worldNum = worldNum;
    }

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();

        bg = new BackgroundManager();

        title = new SpriteText(MainGame.languageFile.get("SELECT_LEVEL").toUpperCase(), Fonts.timerFont);
        title.centerText();
        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT - 60);

        float levelY = (MainGame.HEIGHT/2)-40;
        int levelCount = 1;

        levelButtons = new Array<LevelButton>();
        for (int j = 0; j < 10; j++) {
            LevelButton lb = new LevelButton(new Vector2(180 + (160 * j), levelY - 600), levelCount);
            levelButtons.add(lb);
            levelCount++;
            AnimationManager.applyAnimation(lb, lb.getX(), levelY);
        }
        SoundManager.stop(Assets.JETPACK_SOUND);

        backButton = new SpriteButton(Assets.GO_BACK, new Vector2(-150, 900));
        AnimationManager.applyAnimation(backButton, 50, backButton.getY());
        AnimationManager.startAnimation();
        Gdx.input.setCursorCatched(false);
    }

    private class LevelButton extends SpriteButton {

        private int levelNum;
        private boolean unlocked = false;
        private NinePatch bg;
        private Color color;

        public LevelButton(Vector2 pos, int levelNum) {
            super(Assets.BOX, pos);

            FileHandle loadFile = Gdx.files.internal("levels/level_" + (((worldNum - 1) * 10) + levelNum) + ".json");

            this.bg = new NinePatch(getTexture(), 40, 40, 40, 40);
            String img;

            if (loadFile.exists()) {
                img = Assets.LEVEL_COMPLETE;
                color = Color.WHITE;
                unlocked = true;
            } else {
                img = Assets.LEVEL_BUTTON;
                color = Color.BLACK;
                unlocked = false;
            }
            setTexture(Assets.manager.get(img, Texture.class));
            this.levelNum = levelNum;
        }

        @Override
        public void draw(Batch sb) {
            bg.draw(sb, getX(), getY(), 120, 80);

            Fonts.GUIFont.setColor(color);
            Fonts.GUIFont.draw(sb, worldNum + "-" + levelNum, getX() + 40, getY() + 40);
            Fonts.GUIFont.setColor(Color.WHITE);
        }
    }

    @Override
    public void update(float step) {
        camera.update();
        bg.update();

        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBox360Pad.BUTTON_A)) {
                advanceScreen(1);
            } else if (MainGame.controller.getButton(XBox360Pad.BUTTON_BACK)) {
                goBack();
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            advanceScreen(1);
        }

        if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            for (LevelButton lb : levelButtons) {
                if (lb.checkTouch(touch) && lb.unlocked) {
                    advanceScreen(lb.levelNum);
                }
            }

            if (backButton.checkTouch(touch)) {
                goBack();
            }
        }
    }

    private void advanceScreen(int levelNum) {
        ScreenManager.setScreen(new GameScreen(((worldNum - 1) * 10) + levelNum));
        SoundManager.play(Assets.BUTTON_CLICK);
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.render(sb);
        backButton.draw(sb);
        title.draw(sb);

        for (LevelButton lb : levelButtons) {
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
        for (LevelButton lb : levelButtons) {
            AnimationManager.applyAnimation(lb, lb.getX(), -300);
        }
        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT + 100);
        AnimationManager.applyExitAnimation(backButton, -150, backButton.getY(), new MainMenuScreen());
        AnimationManager.startAnimation();
        SoundManager.play(Assets.BUTTON_CLICK);
    }
}
