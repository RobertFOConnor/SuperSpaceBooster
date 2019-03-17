package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yellowbytestudios.spacedoctor.utils.Metrics;

public abstract class Screen {

    protected OrthographicCamera camera;
    protected FitViewport viewport;

    public void create() {
        camera = new OrthographicCamera();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Metrics.WIDTH, Metrics.HEIGHT, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
    }

    public abstract void update(float step);

    public abstract void render(SpriteBatch sb);

    public void resize(int w, int h) {
        viewport.update(w, h);
        camera.update();
    }

    public Vector2 getTouchPos() {
        Vector3 rawtouch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(rawtouch);
        return new Vector2(rawtouch.x, rawtouch.y);
    }

    public void dispose() {
    }

    public void show() {
    }

    public void hide() {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void goBack() {
    }
}
