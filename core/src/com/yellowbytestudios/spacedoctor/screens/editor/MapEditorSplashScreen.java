package com.yellowbytestudios.spacedoctor.screens.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.media.MapEditorAssets;
import com.yellowbytestudios.spacedoctor.screens.Screen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;

public class MapEditorSplashScreen implements Screen {

    private OrthoCamera camera;
    private String percentage="";
    private Screen mapScreen;

    public MapEditorSplashScreen(Screen mapScreen) {
        camera = new OrthoCamera();
        camera.resize();
        this.mapScreen = mapScreen;
        MapEditorAssets.load();
    }

    @Override
    public void create() {
        camera = new OrthoCamera();
        camera.resize();
    }

    @Override
    public void update(float dt) {
        camera.update();
        percentage = ("Loading Map Editor: "+(int) (MapEditorAssets.manager.getProgress() * 100) + "%");
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        Fonts.GUIFont.setColor(Color.BLACK);
        Fonts.GUIFont.draw(sb, percentage, MainGame.WIDTH / 2 - 30, MainGame.HEIGHT / 2);
        Fonts.GUIFont.setColor(Color.WHITE);
        sb.end();

        if (MapEditorAssets.update()) { // DONE LOADING. SHOW EDITOR SCREEN.
            ScreenManager.setScreen(mapScreen);
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.resize();
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
