package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;

/**
 * Created by BobbyBoy on 16-Jan-16.
 */
public class ResultsScreen implements Screen {

    private OrthoCamera camera;
    private Texture bg;

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
        bg = Assets.manager.get(Assets.VICTORY, Texture.class);
        SoundManager.stop(Assets.JETPACK_SOUND);
    }

    @Override
    public void update(float step) {

    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, 0, 0);
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
        ScreenManager.setScreen(new MainMenuScreen());
    }
}
