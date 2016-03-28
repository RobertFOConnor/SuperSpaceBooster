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
    private LevelButton selectedLevel = null;
    private SpriteButton backButton;

    private int currLevel = MainGame.saveData.getCurrLevel();
    private int selLevel = 1;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();

        bg = new BackgroundManager();

        title = new SpriteText("SELECT A LEVEL", Fonts.timerFont);
        title.centerText();
        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT - 60);

        float levelY = MainGame.HEIGHT -230;
        int levelCount = 1;

        levelButtons = new Array<LevelButton>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 10; j++) {
                LevelButton lb = new LevelButton(new Vector2(180+(160 * j), levelY-600), levelCount);
                levelButtons.add(lb);
                levelCount++;

                if (levelCount == currLevel) {
                    selectedLevel = levelButtons.get(levelButtons.size - 1);
                }
                AnimationManager.applyAnimation(lb, lb.getX(), levelY);
            }
            levelY -= 110;
        }

        selectedLevel = levelButtons.get(currLevel - 1);

        selLevel = currLevel;
        //levelButtons.get(selLevel - 1).setSelected(true);
        SoundManager.stop(Assets.JETPACK_SOUND);

        backButton = new SpriteButton(Assets.GO_BACK, new Vector2(-150, 900));
        AnimationManager.applyAnimation(backButton, 50, backButton.getY());
        AnimationManager.startAnimation();
    }

    private class LevelButton extends SpriteButton {

        private int levelNum;
        private boolean unlocked = false;
        private boolean selected = false;
        private Texture border;
        private NinePatch bg;

        public LevelButton(Vector2 pos, int levelNum) {
            super(Assets.BOX, pos);

            FileHandle loadFile = Gdx.files.internal("levels/level_"+levelNum+".json");

            this.bg = new NinePatch(getTexture(), 40, 40, 40, 40);

            if (loadFile.exists()) {
                setTexture(Assets.manager.get(Assets.LEVEL_COMPLETE, Texture.class));
                unlocked = true;
            } else {
                setTexture(Assets.manager.get(Assets.LEVEL_BUTTON, Texture.class));
                unlocked = false;
            }

            this.levelNum = levelNum;
            border = Assets.manager.get(Assets.LEVEL_BORDER, Texture.class);
        }

        @Override
        public void draw(Batch sb) {
            bg.draw(sb, getX(), getY(), 120, 80);

            if(unlocked) {
                Fonts.GUIFont.setColor(Color.WHITE);
            } else {
                Fonts.GUIFont.setColor(Color.BLACK);
            }
            Fonts.GUIFont.draw(sb, ""+levelNum, getX() + 40, getY() + 40);
        }
    }

    @Override
    public void update(float step) {
        camera.update();
        bg.update();

        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBox360Pad.BUTTON_A)) {
                advanceScreen(selLevel);
            } else if (MainGame.controller.getButton(XBox360Pad.BUTTON_BACK)) {
                goBack();
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            advanceScreen(selLevel);

        }

        if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            for(LevelButton lb : levelButtons) {
                if(lb.checkTouch(touch) && lb.unlocked) {
                    advanceScreen(lb.levelNum);
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
        for(LevelButton lb : levelButtons) {
            AnimationManager.applyAnimation(lb, lb.getX(), -300);
        }
        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT + 100);
        AnimationManager.applyExitAnimation(backButton, -150, backButton.getY(), new MainMenuScreen());
        AnimationManager.startAnimation();
    }
}
