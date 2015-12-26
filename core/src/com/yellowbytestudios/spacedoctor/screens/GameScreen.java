package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.BoundedCamera;

/**
 * Created by BobbyBoy on 15-Oct-15.
 */
public class GameScreen implements Screen {

    private BoundedCamera cam, b2dCam;
    private World world;
    private TiledMap tileMap;
    private Box2DDebugRenderer b2dr;
    private int PPM = 100;
    private OrthogonalTiledMapRenderer tmr;

    @Override
    public void create() {

        //Setup camera.
        cam = new BoundedCamera();
        cam.setToOrtho(false, MainGame.WIDTH, MainGame.HEIGHT);
        world = new World(new Vector2(0, -30f), true);

        b2dr = new Box2DDebugRenderer();
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, MainGame.WIDTH / PPM, MainGame.HEIGHT / PPM);

        //Set tile map using Tiled map path.
        tileMap = new TmxMapLoader().load("test.tmx");

        //Setup map renderer.
        tmr = new OrthogonalTiledMapRenderer(tileMap);
    }

    @Override
    public void update(float step) {
        b2dCam.update();
        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        tmr.setView(cam);
        tmr.render();
        b2dr.render(world, b2dCam.combined);
    }

    @Override
    public void resize(int width, int height) {

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