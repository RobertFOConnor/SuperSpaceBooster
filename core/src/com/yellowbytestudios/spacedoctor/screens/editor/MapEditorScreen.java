package com.yellowbytestudios.spacedoctor.screens.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.mapeditor.CustomMap;
import com.yellowbytestudios.spacedoctor.mapeditor.EditorGUI;
import com.yellowbytestudios.spacedoctor.mapeditor.MapManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.game.Button;
import com.yellowbytestudios.spacedoctor.screens.MainMenuScreen;
import com.yellowbytestudios.spacedoctor.screens.Screen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;

import sun.security.provider.SHA;

/**
 * Created by BobbyBoy on 22-Jan-16.
 */
public class MapEditorScreen implements Screen {

    private OrthoCamera camera;
    private Vector2 touch;

    private MapManager mapManager;
    private TiledMap myMap;
    private Button backButton;

    private EditorGUI gui;

    private ShapeRenderer shapeRenderer;

    private CustomMap savedMap;

    public MapEditorScreen(CustomMap savedMap) {
        this.savedMap = savedMap;
    }


    public MapEditorScreen(TiledMap myMap) {
        this.myMap = myMap;
    }

    public MapEditorScreen() {

        MapManager.reset();
    }


    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();

        mapManager = new MapManager();

        if (myMap != null) {
            mapManager.setMap(myMap);
        } else if (savedMap != null) {
            mapManager = new MapManager(savedMap);
        }

        gui = new EditorGUI(mapManager);
        //backButton = new Button(Assets.GO_BACK, new Vector2(20, 900));

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float step) {

        mapManager.update(step);
        gui.update(step);

        if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());
        }
    }


    @Override
    public void render(SpriteBatch sb) {

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.identity();
        shapeRenderer.setColor(Color.SKY);
        shapeRenderer.rect(0,0, MainGame.WIDTH, MainGame.HEIGHT);
        shapeRenderer.end();


        mapManager.render(sb);

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        //backButton.render(sb);
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