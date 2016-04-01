package com.yellowbytestudios.spacedoctor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;
import com.yellowbytestudios.spacedoctor.screens.editor.MapEditorScreen;
import com.yellowbytestudios.spacedoctor.screens.menu.LevelSelectScreen;

public class GUIManager {

    private OrthoCamera camera;
    private Array<SpacemanPlayer> players;

    // TIMER
    private long startTurnTime; // Timer variables.
    public static long duration;
    private long timeElapsed;

    private ShapeRenderer shapeRenderer;
    private Texture alpha;

    private boolean isTimed;
    public static boolean paused = false;

    private PauseMenu pauseMenu;


    public GUIManager(Array<SpacemanPlayer> players, boolean isTimed) {
        camera = new OrthoCamera();
        camera.resize();
        this.players = players;
        this.isTimed = isTimed;

        //Initialize Timer
        duration = 30000;
        startTurnTime = System.nanoTime();

        shapeRenderer = new ShapeRenderer();
        alpha = Assets.manager.get(Assets.ALPHA, Texture.class);

        pauseMenu = new PauseMenu();
        paused = false;
    }

    public void update() {

        if (isTimed) {
            timeElapsed = duration - ((System.nanoTime() - startTurnTime) / 1000000);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
            SoundManager.stop(Assets.JETPACK_SOUND);
        }
    }

    public void render(SpriteBatch sb) {

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.identity();

        for (int i = 0; i < players.size; i++) {
            SpacemanPlayer player = players.get(i);

            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(215 + (i * 1425), MainGame.HEIGHT - 55, 100 * 1.2f, 40);

            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(215 + (i * 1425), MainGame.HEIGHT - 55, (player.getCurrGas() / (player.getMaxGas() / 100)) * 1.2f, 40);
        }
        shapeRenderer.end();

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(alpha, 0, MainGame.HEIGHT, MainGame.WIDTH, -120);
        for (int i = 0; i < players.size; i++) {

            Fonts.GUIFont.draw(sb, "x" + String.format("%02d", players.get(i).getCurrAmmo()), 225 + (i * 1425), MainGame.HEIGHT - 70);

            //Fonts.GUIFont.draw(sb, String.format("%07d", players.get(i).getCoins()), (MainGame.WIDTH - 250)+(i*1425), MainGame.HEIGHT - 70);
        }

        if (isTimed) {
            drawTimer(sb);
        }

        Fonts.GUIFont.draw(sb, "World: " + GameScreen.worldNo + "-" + (GameScreen.levelNo - ((GameScreen.worldNo - 1) * 10)), 1500, MainGame.HEIGHT - 55);
        sb.end();

        if (paused) {
            pauseMenu.render(sb);
        }
    }

    private void drawTimer(SpriteBatch sb) {
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
    }

    public boolean timeIsUp() {
        return isTimed && timeElapsed < 0;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    private class PauseMenu {

        private Skin skin;
        private Stage stage;

        public PauseMenu() {
            skin = Assets.manager.get(Assets.SKIN, Skin.class);
            skin.add("menuFont", Fonts.GUIFont, BitmapFont.class);
            stage = new Stage();

            TextButton.TextButtonStyle tbs = skin.get("default", TextButton.TextButtonStyle.class);
            tbs.font = Fonts.GUIFont;

            final TextButton resumeButton = new TextButton("Resume Game", skin, "default");
            final TextButton restartButton = new TextButton("Restart Level", skin, "default");
            final TextButton exitButton = new TextButton("Exit Game", skin, "default");

            resumeButton.setWidth(500);
            resumeButton.setHeight(100);
            resumeButton.setPosition(Gdx.graphics.getWidth() / 2 - 250f, Gdx.graphics.getHeight() - 200);

            restartButton.setWidth(500);
            restartButton.setHeight(100);
            restartButton.setPosition(Gdx.graphics.getWidth() / 2 - 250f, Gdx.graphics.getHeight() / 2 - 50);

            exitButton.setWidth(500);
            exitButton.setHeight(100);
            exitButton.setPosition(Gdx.graphics.getWidth() / 2 - 250f, 100);

            resumeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    paused = false;
                }
            });

            restartButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    for (SpacemanPlayer p : players) {
                        p.setDead(true);
                        paused = false;
                    }
                }
            });

            exitButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    GameScreen.exit();
                }
            });

            stage.addActor(resumeButton);
            stage.addActor(restartButton);
            stage.addActor(exitButton);
            Gdx.input.setInputProcessor(stage);
        }

        public void render(SpriteBatch sb) {
            sb.begin();
            sb.draw(Assets.manager.get(Assets.ALPHA, Texture.class), 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
            sb.end();

            sb.begin();
            stage.draw();
            sb.end();
        }
    }
}
