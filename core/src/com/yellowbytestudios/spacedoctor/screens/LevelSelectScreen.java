package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.Assets;
import com.yellowbytestudios.spacedoctor.Button;
import com.yellowbytestudios.spacedoctor.Fonts;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.SoundManager;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBox360Pad;

import javax.xml.soap.Text;

public class LevelSelectScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;
    private String title = "SELECT A LEVEL";
    private Texture bg;
    private Array<LevelButton> levelButtons;
    private Controller controller;
    private boolean keysUp = false;

    private int selLevel = 1;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();
        if (MainGame.hasControllers) {
            controller = Controllers.getControllers().get(0);
        }


        bg = Assets.manager.get(Assets.MENU_BG, Texture.class);

        levelButtons = new Array<LevelButton>();

        float levelY = MainGame.HEIGHT / 2 + 50;
        int levelCount = 1;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                levelButtons.add(new LevelButton(new Vector2((MainGame.WIDTH / 4 - 200) * (j + 1), levelY), levelCount));
                levelCount++;
            }
            levelY -= 300;
        }

        selLevel = MainGame.UNLOCKED_LEVEL;
        levelButtons.get(selLevel - 1).setSelected(true);
        SoundManager.stop(Assets.manager.get(Assets.JETPACK_SOUND, Sound.class));
    }

    private class LevelButton extends Button {

        private int levelNum;
        private boolean unlocked = false;
        private boolean selected = false;
        private Texture border;

        public LevelButton(Vector2 pos, int levelNum) {
            super(Assets.manager.get(Assets.LEVEL_LOCKED, Texture.class), Assets.manager.get(Assets.LEVEL_LOCKED, Texture.class), pos);
            if (MainGame.UNLOCKED_LEVEL > levelNum) {
                texture = Assets.manager.get(Assets.LEVEL_COMPLETE, Texture.class);
            } else if (MainGame.UNLOCKED_LEVEL == levelNum) {
                texture = Assets.manager.get(Assets.LEVEL_BUTTON, Texture.class);
            }

            this.levelNum = levelNum;
            border = Assets.manager.get(Assets.LEVEL_BORDER, Texture.class);
        }

        @Override
        public void render(SpriteBatch sb) {
            sb.draw(texture, pos.x, pos.y);
            if (selected) {
                sb.draw(border, pos.x, pos.y);
            }
        }

        @Override
        public boolean isSelected() {
            return selected;
        }

        @Override
        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    @Override
    public void update(float step) {
        camera.update();

        if (MainGame.hasControllers) {
            if (controller.getButton(XBox360Pad.BUTTON_A)) {
                ScreenManager.setScreen(new GameScreen(selLevel));
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            ScreenManager.setScreen(new GameScreen(selLevel));

        }  else if(/*MainGame.DEVICE.equals("ANDROID") &&*/ Gdx.input.justTouched()) {
            Vector2 touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if(touch.y>MainGame.HEIGHT-200) {
                ScreenManager.setScreen(new MapEditorScreen());
            } else {
                ScreenManager.setScreen(new GameScreen(selLevel));
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

        Fonts.timerFont.draw(sb, title, MainGame.WIDTH / 2 - 250, MainGame.HEIGHT - 80);

        for (LevelButton lb : levelButtons) {
            lb.render(sb);
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
    }
}
