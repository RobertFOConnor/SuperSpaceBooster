package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.Assets;
import com.yellowbytestudios.spacedoctor.Button;
import com.yellowbytestudios.spacedoctor.Fonts;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.SoundManager;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;

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
        bg = new Texture(Gdx.files.internal("victory.png"));
        SoundManager.stop(Assets.manager.get(Assets.JETPACK_SOUND, Sound.class));
    }

    @Override
    public void update(float step) {

    }

    @Override
    public void render(SpriteBatch sb) {
        // Clear screen.
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glClearColor(0, 0, 0, 0);


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

    }
}
