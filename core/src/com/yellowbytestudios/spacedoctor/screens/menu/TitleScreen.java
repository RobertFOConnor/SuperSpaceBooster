package com.yellowbytestudios.spacedoctor.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBox360Pad;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.BackgroundManager;
import com.yellowbytestudios.spacedoctor.screens.Screen;
import com.yellowbytestudios.spacedoctor.tween.AnimationManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;
import com.yellowbytestudios.spacedoctor.tween.SpriteText;

/**
 * Created by BobbyBoy on 16-Jan-16.
 */
public class TitleScreen implements Screen {

    private OrthoCamera camera;
    private BackgroundManager bg;
    private SpriteButton character, title;
    private SpriteText continueMessage;
    private boolean advancing = false;

    private final Vector2 charStartPos = new Vector2(2000, -800);

    @Override
    public void create() {

        bg = new BackgroundManager();

        camera = new OrthoCamera();
        camera.resize();

        character = new SpriteButton(Assets.CHARACTER, charStartPos);
        title = new SpriteButton(Assets.TITLE, new Vector2(-1100, 350));
        continueMessage = new SpriteText(MainGame.languageFile.get("TOUCH_TO_CONTINUE").toUpperCase(), Fonts.timerFont);
        continueMessage.setPosition(400, -100);

        AnimationManager.applyAnimation(continueMessage, 400, 90);
        AnimationManager.applyAnimation(character, 1080, -250);
        AnimationManager.applyAnimation(title, 70, 350);
        AnimationManager.startAnimation();
    }

    @Override
    public void update(float step) {

        bg.update();

        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBox360Pad.BUTTON_START)) {
                advanceScreen();
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            advanceScreen();

        } else if (Gdx.input.justTouched()) {
            advanceScreen();
        }
    }

    private void advanceScreen() {
        if (!advancing) {
            AnimationManager.applyAnimation(continueMessage, 400, -100);
            AnimationManager.applyAnimation(character, charStartPos.x, charStartPos.y);
            AnimationManager.applyExitAnimation(title, -1100, 350, new MainMenuScreen());
            AnimationManager.startAnimation();
            advancing = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.render(sb);
        character.draw(sb);
        title.draw(sb);
        continueMessage.draw(sb);
        sb.end();
    }

    @Override
    public void resize(int w, int h) {
        camera.resize();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void goBack() {
        Gdx.app.exit();
    }
}
