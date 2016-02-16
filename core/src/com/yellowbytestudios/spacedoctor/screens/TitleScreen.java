package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.controllers.XBox360Pad;
import com.yellowbytestudios.spacedoctor.tween.SpriteAccessor;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by BobbyBoy on 16-Jan-16.
 */
public class TitleScreen implements Screen {

    private OrthoCamera camera;
    private Texture bg;
    private SpriteButton character, title;

    private final Vector2 charStartPos = new Vector2(2000, -800);

    //Animations
    private TweenManager tweenManager;
    private long startTime, delta;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        bg = Assets.manager.get(Assets.MENU_BG, Texture.class);

        character = new SpriteButton(Assets.CHARACTER, charStartPos);
        title = new SpriteButton(Assets.TITLE, new Vector2(-1100, 350));


        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        tweenManager = new TweenManager();

        applyButtonAnimation(character, 1080, -250);
        applyButtonAnimation(title, 70, 350);

        startTime = TimeUtils.millis();
    }

    private void applyButtonAnimation(SpriteButton b, float x, float y) {
        Tween.to(b, SpriteAccessor.POS_XY, 30f)
                .target(x, y).ease(TweenEquations.easeOutBack)
                .start(tweenManager);
    }

    @Override
    public void update(float step) {
        if (MainGame.hasControllers) {
            if (MainGame.controller.getButton(XBox360Pad.BUTTON_START)) {
                advanceScreen();
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            advanceScreen();

        } else if (MainGame.DEVICE.equals("ANDROID") && Gdx.input.justTouched()) {
            advanceScreen();
        }
        delta = (TimeUtils.millis()-startTime+1000)/1000;
        tweenManager.update(delta);
    }

    private void advanceScreen() {
        applyButtonAnimation(character, charStartPos.x, charStartPos.y);

        TweenCallback myCallBack = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                ScreenManager.setScreen(new MainMenuScreen());
            }
        };

        Tween.to(title, SpriteAccessor.POS_XY, 30f)
                .target(-1100, 350).ease(TweenEquations.easeOutBack).setCallback(myCallBack)
                .setCallbackTriggers(TweenCallback.END).start(tweenManager);

        startTime = TimeUtils.millis();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, 0, 0);
        character.draw(sb);
        title.draw(sb);
        sb.end();
    }

    @Override
    public void resize(int w, int h) {

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
