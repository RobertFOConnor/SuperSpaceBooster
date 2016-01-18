package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;

/**
 * Created by BobbyBoy on 10-Jan-16.
 */
public class GUIManager {

    private OrthoCamera camera;
    private SpacemanPlayer player;

    // TIMER
    private long startTurnTime; // Timer variables.
    public static long duration;
    private long timeElapsed;

    private ShapeRenderer shapeRenderer;
    private Texture gas_bar, round_icon;


    public GUIManager(SpacemanPlayer player) {
        camera = new OrthoCamera();
        camera.resize();
        this.player = player;

        //Initialize Timer
        duration = 30000;
        startTurnTime = System.nanoTime();

        shapeRenderer = new ShapeRenderer();
        gas_bar = new Texture(Gdx.files.internal("gas_bar.png"));
        round_icon = new Texture(Gdx.files.internal("round_icon.png"));
    }

    public void update() {


        timeElapsed = duration - ((System.nanoTime() - startTurnTime) / 1000000);

        // Check if time is up.
        if (timeElapsed < 0) {
            SoundManager.play(Assets.manager.get(Assets.DEATH_SOUND, Sound.class));
            ScreenManager.setScreen(new GameScreen(GameScreen.levelNo));
        }
    }

    public void render(SpriteBatch sb) {

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.identity();
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(50, MainGame.HEIGHT - 130, (player.getCurrGas() / 5) * 3, 100);
        shapeRenderer.end();

        sb.setProjectionMatrix(camera.combined);
        sb.begin();

        sb.draw(gas_bar, 50, MainGame.HEIGHT - 130);
        sb.draw(round_icon, 60, MainGame.HEIGHT - 250);
        Fonts.timerFont.draw(sb, player.getMaxAmmo() + "/" + player.getCurrAmmo(), 190, MainGame.HEIGHT - 200);

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
}
