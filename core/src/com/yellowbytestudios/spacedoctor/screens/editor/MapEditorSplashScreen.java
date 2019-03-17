package com.yellowbytestudios.spacedoctor.screens.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.media.MapEditorAssets;
import com.yellowbytestudios.spacedoctor.screens.Screen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;
import com.yellowbytestudios.spacedoctor.utils.Metrics;

public class MapEditorSplashScreen extends Screen {

    private Sprite loadScreen;
    private Sprite loadWheel;
    private String percentage = "";
    private Screen mapScreen;


    public MapEditorSplashScreen(Screen mapScreen) {
        this.mapScreen = mapScreen;
        MapEditorAssets.load();
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void create() {
        super.create();
        Texture texture = Assets.manager.get(Assets.LOADSCREEN, Texture.class);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        loadScreen = new Sprite(texture);
        loadWheel = new Sprite(Assets.manager.get(Assets.LOADWHEEL, Texture.class));
        loadWheel.setPosition(Metrics.WIDTH - 180, 100);
    }

    @Override
    public void update(float dt) {
        percentage = ((int) (MapEditorAssets.manager.getProgress() * 100) + "%");
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        loadScreen.draw(sb);
        loadWheel.rotate(10f);
        loadWheel.draw(sb);
        Fonts.GUIFont.setColor(Color.WHITE);
        Fonts.GUIFont.draw(sb, percentage, Metrics.WIDTH - Fonts.getWidth(Fonts.GUIFont, percentage) - 250, 160);
        sb.end();

        if (MapEditorAssets.update()) { // DONE LOADING. SHOW EDITOR SCREEN.
            ScreenManager.setScreen(mapScreen);
        }
    }
}
