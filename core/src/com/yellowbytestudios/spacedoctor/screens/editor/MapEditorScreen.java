package com.yellowbytestudios.spacedoctor.screens.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yellowbytestudios.spacedoctor.mapeditor.CustomMap;
import com.yellowbytestudios.spacedoctor.mapeditor.EditorGUI;
import com.yellowbytestudios.spacedoctor.mapeditor.MapManager;
import com.yellowbytestudios.spacedoctor.media.MapEditorAssets;
import com.yellowbytestudios.spacedoctor.screens.Screen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;
import com.yellowbytestudios.spacedoctor.screens.menu.MainMenuScreen;
import com.yellowbytestudios.spacedoctor.utils.Metrics;

public class MapEditorScreen extends Screen {

    private MapManager mapManager;
    private Color menu_bg;
    private EditorGUI gui;
    private ShapeRenderer shapeRenderer;
    private CustomMap savedMap;
    private int width = 0;
    private int height = 0;

    public MapEditorScreen(CustomMap savedMap) {
        this.savedMap = savedMap;
    }

    public MapEditorScreen(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void create() {
        super.create();

        if (savedMap != null) {
            mapManager = new MapManager(savedMap);
        } else {
            mapManager = new MapManager(width, height);
        }
        gui = new EditorGUI(mapManager);
        shapeRenderer = new ShapeRenderer();

        menu_bg = new Color(0.42f, 0.53f, 0.71f, 1);
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void update(float step) {

        mapManager.update();
        gui.update(step);
    }

    public void resize(int w, int h) {
        super.resize(w, h);
        gui.resize(w, h);
    }


    @Override
    public void render(SpriteBatch sb) {

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.identity();
        shapeRenderer.setColor(menu_bg);
        shapeRenderer.rect(0, 0, Metrics.WIDTH, Metrics.HEIGHT);
        shapeRenderer.end();

        mapManager.render(sb);

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        gui.render(sb);
        sb.end();
    }

    @Override
    public void goBack() {
        ScreenManager.setScreen(new MainMenuScreen());
        MapEditorAssets.dispose();
    }
}