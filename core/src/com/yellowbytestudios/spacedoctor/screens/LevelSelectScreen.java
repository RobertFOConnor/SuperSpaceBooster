package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBox360Pad;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.objects.Button;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;
import com.yellowbytestudios.spacedoctor.tween.SpriteText;

public class LevelSelectScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;
    private SpriteText title;
    private Texture bg;
    private Array<LevelButton> levelButtons;
    private LevelButton selectedLevel = null;
    private SpriteButton backButton;

    private int selLevel = 1;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();

        bg = Assets.manager.get(Assets.MENU_BG, Texture.class);

        title = new SpriteText("SELECT A LEVEL", Fonts.timerFont);
        title.centerText();
        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT - 60);

        float levelY = MainGame.HEIGHT / 2 + 50;
        int levelCount = 1;

        levelButtons = new Array<LevelButton>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                LevelButton lb = new LevelButton(new Vector2((MainGame.WIDTH / 4 - 200) * (j + 1), levelY-600), levelCount);
                levelButtons.add(lb);
                levelCount++;

                if (levelCount == MainGame.UNLOCKED_LEVEL) {
                    selectedLevel = levelButtons.get(levelButtons.size - 1);
                }
                AnimationManager.applyAnimation(lb, lb.getX(), levelY);
            }
            levelY -= 300;
        }

        selectedLevel = levelButtons.get(MainGame.UNLOCKED_LEVEL - 1);

        selLevel = MainGame.UNLOCKED_LEVEL;
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

        public LevelButton(Vector2 pos, int levelNum) {
            super(Assets.LEVEL_LOCKED, pos);
            if (MainGame.UNLOCKED_LEVEL > levelNum) {
                setTexture(Assets.manager.get(Assets.LEVEL_COMPLETE, Texture.class));
                unlocked = true;
            } else if (MainGame.UNLOCKED_LEVEL == levelNum) {
                setTexture(Assets.manager.get(Assets.LEVEL_BUTTON, Texture.class));
                unlocked = true;
            }

            this.levelNum = levelNum;
            border = Assets.manager.get(Assets.LEVEL_BORDER, Texture.class);
        }
    }

    @Override
    public void update(float step) {
        camera.update();

        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBox360Pad.BUTTON_A)) {
                advanceScreen(selLevel);
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            advanceScreen(selLevel);

        } else if (Gdx.input.justTouched()) {
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
        sb.draw(bg, 0, 0);
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
