package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.Button;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.mapeditor.EditorGUI;
import com.yellowbytestudios.spacedoctor.mapeditor.MapManager;
import com.yellowbytestudios.spacedoctor.mapeditor.TileIDs;

/**
 * Created by BobbyBoy on 22-Jan-16.
 */
public class MapEditorScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;

    private MapManager mapManager;
    private TiledMap myMap;

    private EditorGUI gui;

    public MapEditorScreen(TiledMap myMap) {
        this.myMap = myMap;
    }

    public MapEditorScreen() {
    }


    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();

        mapManager = new MapManager();

        if (myMap != null) {
            mapManager.setMap(myMap);
        }

        gui = new EditorGUI(mapManager);
    }

    @Override
    public void update(float step) {

        mapManager.update();
        gui.update();
    }


    @Override
    public void render(SpriteBatch sb) {
        // Clear screen.
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glClearColor(1, 1, 1, 0);


        mapManager.render(sb);

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        gui.render(sb);
        sb.end();
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
        ScreenManager.setScreen(new MainMenuScreen());
    }
}