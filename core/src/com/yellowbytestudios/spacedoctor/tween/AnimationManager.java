package com.yellowbytestudios.spacedoctor.tween;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.Screen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by BobbyBoy on 17-Feb-16.
 */
public class AnimationManager {

    //Tween manager.
    public static TweenManager tweenManager;
    private static long startTime, delta;


    public AnimationManager() {
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        tweenManager = new TweenManager();
    }

    public void update() {
        delta = (TimeUtils.millis() - startTime + 1000) / 1000;
        tweenManager.update(delta);
    }

    public static void applyAnimation(Sprite b, float x, float y) {
        Tween.to(b, SpriteAccessor.POS_XY, 17f)
                .target(x, y).ease(TweenEquations.easeOutBack)
                .start(tweenManager);
    }

    public static void applyExitAnimation(Sprite b, float x, float y, final Screen s) {
        TweenCallback myCallBack = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                ScreenManager.setScreen(s);
            }
        };

        Tween.to(b, SpriteAccessor.POS_XY, 17f)
                .target(x, y).ease(TweenEquations.easeOutBack).setCallback(myCallBack)
                .setCallbackTriggers(TweenCallback.END).start(tweenManager);
    }


    public static void applyFadeAnimation(Sprite b) {
        Tween.to(b, SpriteAccessor.OPACITY, 200f)
                .target(1f).ease(TweenEquations.easeOutBack)
                .start(tweenManager);
    }

    public static void applyLevelStartAnimation(Sprite b, float x, float y) {
        Tween.to(b, SpriteAccessor.POS_XY, 35f)
                .target(x, y).ease(TweenEquations.easeOutBounce)
                .start(tweenManager);
    }

    public static void applyLevelEndAnimation(Sprite b, float x, float y, final Screen s, final World world) {
        TweenCallback myCallBack = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                world.dispose();
                ScreenManager.setScreen(s);

            }
        };

        Tween.to(b, SpriteAccessor.POS_XY, 35f)
                .target(x, y).ease(TweenEquations.easeOutBounce).setCallback(myCallBack)
                .setCallbackTriggers(TweenCallback.END).start(tweenManager);
    }

    public static void startAnimation() {
        startTime = TimeUtils.millis();
    }
}
