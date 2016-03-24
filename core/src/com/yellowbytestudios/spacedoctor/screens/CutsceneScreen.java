package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Mainline;
import com.brashmonkey.spriter.Player;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.screens.menu.MainMenuScreen;

/**
 * Created by Robert on 03/23/16.
 */
public class CutsceneScreen implements Screen {

    private OrthoCamera camera;
    private Player cutscenePlayer;

    public CutsceneScreen(int sceneNum) {
        cutscenePlayer = MainGame.spriterManager.getCutscene(sceneNum);
        cutscenePlayer.setPosition(0, 0);


        Player.PlayerListener endListener = new Player.PlayerListener() {
            @Override
            public void animationFinished(Animation animation) {
                ScreenManager.setScreen(new MainMenuScreen());
            }

            @Override
            public void animationChanged(Animation oldAnim, Animation newAnim) {

            }

            @Override
            public void preProcess(Player player) {

            }

            @Override
            public void postProcess(Player player) {

            }

            @Override
            public void mainlineKeyChanged(Mainline.Key prevKey, Mainline.Key newKey) {

            }
        };
        cutscenePlayer.addListener(endListener);
    }


    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
    }

    @Override
    public void update(float step) {

    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        MainGame.spriterManager.showScene(cutscenePlayer);
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
    }
}
