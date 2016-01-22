package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.Button;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.mapeditor.MapManager;

/**
 * Created by BobbyBoy on 22-Jan-16.
 */
public class MapEditorScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;

    private MapManager mapManager;
    private TiledMap myMap;

    private Button zoomIn, zoomOut, moveButton, eraseButton, playMap;

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

        zoomIn = new Button(new Texture(Gdx.files.internal("mapeditor/zoom_in.png")), new Texture(Gdx.files.internal("mapeditor/zoom_in.png")), new Vector2(MainGame.WIDTH - 280 - 60, 30));
        zoomOut = new Button(new Texture(Gdx.files.internal("mapeditor/zoom_out.png")), new Texture(Gdx.files.internal("mapeditor/zoom_out.png")), new Vector2(MainGame.WIDTH - 170, 30));
        moveButton = new Button(new Texture(Gdx.files.internal("mapeditor/move_button.png")), new Texture(Gdx.files.internal("mapeditor/move_button_selected.png")), new Vector2(30, 30));
        eraseButton = new Button(new Texture(Gdx.files.internal("mapeditor/erase.png")), new Texture(Gdx.files.internal("mapeditor/erase_selected.png")), new Vector2(30 + 140 + 30, 30));


        playMap = new Button(new Texture(Gdx.files.internal("mapeditor/play_map.png")), new Texture(Gdx.files.internal("mapeditor/play_map.png")), new Vector2(30, MainGame.HEIGHT - 170));
    }

    @Override
    public void update(float step) {

        mapManager.update();

        if (Gdx.input.isTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (zoomIn.checkTouch(touch)) {
                mapManager.zoomIn();
            } else if (zoomOut.checkTouch(touch)) {
                mapManager.zoomOut();
            } else if (playMap.checkTouch(touch)) {
                ScreenManager.setScreen(new GameScreen(mapManager.getMap()));

            } else if (moveButton.checkTouch(touch)) {


            } else { //CHECK MAP FOR INTERACTION.

                if (moveButton.isPressed()) {
                    mapManager.dragMap();
                } else if (eraseButton.isPressed()) {
                    mapManager.eraseTiles();
                } else {
                    mapManager.checkForTilePlacement();
                }
            }
        }

        if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (moveButton.checkTouch(touch)) {
                if (moveButton.isPressed()) {
                    moveButton.setPressed(false);
                } else {
                    moveButton.setPressed(true);
                    eraseButton.setPressed(false);
                }
            }

            if (eraseButton.checkTouch(touch)) {
                if (eraseButton.isPressed()) {
                    eraseButton.setPressed(false);
                } else {
                    eraseButton.setPressed(true);
                    moveButton.setPressed(false);
                }
            }
        }
    }


    @Override
    public void render(SpriteBatch sb) {
        // Clear screen.
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glClearColor(1, 1, 1, 0);


        mapManager.render();

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        zoomIn.render(sb);
        zoomOut.render(sb);
        moveButton.render(sb);
        eraseButton.render(sb);
        playMap.render(sb);
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

    }
}