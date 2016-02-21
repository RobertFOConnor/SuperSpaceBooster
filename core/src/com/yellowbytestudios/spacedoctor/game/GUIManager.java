package com.yellowbytestudios.spacedoctor.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;

public class GUIManager {

    private OrthoCamera camera;
    private SpacemanPlayer player;

    // TIMER
    private long startTurnTime; // Timer variables.
    public static long duration;
    private long timeElapsed;

    private ShapeRenderer shapeRenderer;
    private Texture gui_display;


    public GUIManager(SpacemanPlayer player) {
        camera = new OrthoCamera();
        camera.resize();
        this.player = player;

        //Initialize Timer
        duration = 30000;
        startTurnTime = System.nanoTime();

        shapeRenderer = new ShapeRenderer();
        gui_display = Assets.manager.get(Assets.GUI_DISPLAY, Texture.class);
    }

    public void update() {
        timeElapsed = duration - ((System.nanoTime() - startTurnTime) / 1000000);

    }

    public void render(SpriteBatch sb) {

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.identity();
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(215, MainGame.HEIGHT - 105, (player.getCurrGas() / 5) * 1.2f, 40);
        shapeRenderer.end();

        sb.setProjectionMatrix(camera.combined);
        sb.begin();

        sb.draw(gui_display, 100, MainGame.HEIGHT - 200);
        Fonts.GUIFont.draw(sb, player.getMaxAmmo() + "/" + player.getCurrAmmo(), 225, MainGame.HEIGHT - 127);

        Fonts.timerFont.draw(sb, "TIME", MainGame.WIDTH / 2 - 60, MainGame.HEIGHT - 30);

        String seconds;
        if ((timeElapsed / 1000) < 10) {
            Fonts.timerFont.setColor(Color.RED);
            seconds = "0" + (timeElapsed / 1000);
        } else {
            seconds = (timeElapsed / 1000) + "";
        }

        Fonts.timerFont.draw(sb, seconds + ":" + ((timeElapsed % 1000) / 10), MainGame.WIDTH / 2 - 80, MainGame.HEIGHT - 100);
        Fonts.timerFont.setColor(Color.WHITE);

        sb.end();
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }
}