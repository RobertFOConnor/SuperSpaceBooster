package com.yellowbytestudios.spacedoctor.screens.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBox360Pad;
import com.yellowbytestudios.spacedoctor.mapeditor.MapManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.BackgroundManager;
import com.yellowbytestudios.spacedoctor.screens.MainMenuScreen;
import com.yellowbytestudios.spacedoctor.screens.Screen;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;
import com.yellowbytestudios.spacedoctor.tween.SpriteText;


public class SizeSelectScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;
    private BackgroundManager bg;
    private SpriteText title;
    private SpriteButton smallButton, mediumButton, largeButton;
    private SpriteButton backButton;
    private float buttonY = 180;


    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();

        bg = new BackgroundManager();
        smallButton = new SpriteButton(Assets.SMALL_MAP, new Vector2(180, -500));
        mediumButton = new SpriteButton(Assets.MEDIUM_MAP, new Vector2(760, -500));
        largeButton = new SpriteButton(Assets.LARGE_MAP, new Vector2(1340, -500));
        title = new SpriteText("SELECT A SIZE", Fonts.timerFont);
        title.centerText();

        backButton = new SpriteButton(Assets.GO_BACK, new Vector2(-150, 900));

        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT - 60);
        AnimationManager.applyAnimation(backButton, 50, backButton.getY());
        AnimationManager.applyAnimation(smallButton, 180, buttonY);
        AnimationManager.applyAnimation(mediumButton, 760, buttonY);
        AnimationManager.applyAnimation(largeButton, 1340, buttonY);
        AnimationManager.startAnimation();
    }


    @Override
    public void update(float step) {
        camera.update();
        bg.update();

        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBox360Pad.BUTTON_A)) {
                advanceScreen(new MapEditorScreen());
            }

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            advanceScreen(new MapEditorScreen());

        } else if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (smallButton.checkTouch(touch)) {
                MapManager.customMapWidth = 25;
                MapManager.customMapHeight = 15;

                advanceScreen(new MapEditorScreen());
            } else if(mediumButton.checkTouch(touch)) {
                MapManager.customMapWidth = 40;
                MapManager.customMapHeight = 20;

                advanceScreen(new MapEditorScreen());
            } else if(largeButton.checkTouch(touch)) {
                MapManager.customMapWidth = 70;
                MapManager.customMapHeight = 40;

                advanceScreen(new MapEditorScreen());
            } else if (backButton.checkTouch(touch)) {
                goBack();
            }
        }
    }


    private void advanceScreen(final Screen s) {

        AnimationManager.applyAnimation(title, title.getX(), MainGame.HEIGHT + 100);
        AnimationManager.applyAnimation(smallButton, 180, -500);
        AnimationManager.applyAnimation(mediumButton, 760, -500);
        AnimationManager.applyAnimation(largeButton, 1340, -500);
        AnimationManager.applyExitAnimation(backButton, -150, 900, s);

        AnimationManager.startAnimation();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.render(sb);
        title.draw(sb);
        backButton.draw(sb);
        smallButton.draw(sb);
        mediumButton.draw(sb);
        largeButton.draw(sb);
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
        advanceScreen(new NewLoadScreen());
    }
}
