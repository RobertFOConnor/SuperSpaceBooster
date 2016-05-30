package com.yellowbytestudios.spacedoctor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;

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
    private String worldString;
    private String time = "";

    private GameScreen gameScreen;

    public GUIManager(GameScreen gameScreen, Array<SpacemanPlayer> players, boolean isTimed) {
        this.gameScreen = gameScreen;
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
        Gdx.input.setInputProcessor(null);
        Gdx.input.setCursorCatched(true);

        Fonts.GUIFont.setColor(Color.WHITE);

        if (GameScreen.coreMap) {
            worldString = "World: " + GameScreen.worldNo + "-" + (GameScreen.levelNo - ((GameScreen.worldNo - 1) * 10));
        } else {
            worldString = "Now Testing";
        }
    }

    public void update() {

        for (SpacemanPlayer p : players) {
            if (p.getController().pausePressed()) {
                paused = !paused;
                SoundManager.stop(Assets.JETPACK_SOUND);

                if (paused) {
                    Gdx.input.setCursorCatched(false);
                    pauseMenu.setMenuListener();
                } else {
                    Gdx.input.setCursorCatched(true);
                    Gdx.input.setInputProcessor(null);
                    duration = timeElapsed;
                    startTurnTime = System.nanoTime();
                }
            }
        }
    }

    public void render(SpriteBatch sb) {

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.identity();

        for (int i = 0; i < players.size; i++) {
            SpacemanPlayer player = players.get(i);

            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(100 + (i * 200), MainGame.HEIGHT - 55, 100 * 1.2f, 40);

            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(100 + (i * 200), MainGame.HEIGHT - 55, (player.getCurrGas() / (player.getMaxGas() / 100)) * 1.2f, 40);
        }
        shapeRenderer.end();

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(alpha, 0, MainGame.HEIGHT, MainGame.WIDTH, -120);
        for (int i = 0; i < players.size; i++) {

            Fonts.GUIFont.draw(sb, "x" + String.format("%02d", players.get(i).getGun().getAmmo()), 100 + (i * 200), MainGame.HEIGHT - 70);

            //Fonts.GUIFont.draw(sb, "COINS", 220 + (i * 200), MainGame.HEIGHT - 30);
            //Fonts.GUIFont.draw(sb, String.format("%02d", players.get(i).getCoins()), 220 + (i * 200), MainGame.HEIGHT - 70);
        }

        if (isTimed) {
            drawTimer(sb);
        }

        Fonts.GUIFont.draw(sb, worldString, 1600, MainGame.HEIGHT - 55);
        sb.end();

        if (paused) {
            pauseMenu.render(sb);
        }
    }

    public void updateTimer() {
        timeElapsed = duration - ((System.nanoTime() - startTurnTime) / 1000000);

        String seconds;
        if ((timeElapsed / 1000) < 10) {
            Fonts.timerFont.setColor(Color.RED);
            seconds = "0" + (timeElapsed / 1000);
        } else {
            seconds = (timeElapsed / 1000) + "";
        }

        time = seconds + "." + ((timeElapsed % 1000) / 10);
    }

    private void drawTimer(SpriteBatch sb) {

        Fonts.timerFont.draw(sb, time, MainGame.WIDTH / 2 - (Fonts.getWidth(Fonts.timerFont, time) / 2), MainGame.HEIGHT - 30);
        Fonts.timerFont.setColor(Color.WHITE);
    }

    public boolean timeIsUp() {
        return isTimed && timeElapsed < 0;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        GUIManager.paused = paused;

        if (paused) {
            pauseMenu.setMenuListener();
        }
    }

    private class PauseMenu {

        private Skin skin;
        private Stage stage;

        public PauseMenu() {
            skin = Assets.manager.get(Assets.SKIN, Skin.class);
            stage = new Stage((new StretchViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight())));

            final TextButton resumeButton = new TextButton("Resume Game", skin, "default");
            final TextButton restartButton = new TextButton("Restart Level", skin, "default");
            final TextButton exitButton = new TextButton("Exit Game", skin, "default");

            float buttonX = Gdx.graphics.getWidth() / 2 - 250f;

            setButtonSize(resumeButton);
            resumeButton.setPosition(buttonX, Gdx.graphics.getHeight() - 200);

            setButtonSize(restartButton);
            restartButton.setPosition(buttonX, Gdx.graphics.getHeight() / 2 - 50);

            setButtonSize(exitButton);
            exitButton.setPosition(buttonX, 100);

            resumeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    SoundManager.play(Assets.BUTTON_CLICK);
                    paused = false;
                    Gdx.input.setCursorCatched(true);
                    Gdx.input.setInputProcessor(null);
                }
            });

            restartButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    SoundManager.play(Assets.BUTTON_CLICK);
                    for (SpacemanPlayer p : players) {
                        p.setDead(true);
                        paused = false;
                        Gdx.input.setCursorCatched(true);
                        Gdx.input.setInputProcessor(null);
                    }
                }
            });

            exitButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    SoundManager.play(Assets.BUTTON_CLICK);
                    gameScreen.exit();
                    stage.dispose();
                    paused = false;
                    Gdx.input.setInputProcessor(null);
                }
            });

            stage.addActor(resumeButton);
            stage.addActor(restartButton);
            stage.addActor(exitButton);
        }

        public void render(SpriteBatch sb) {
            sb.begin();
            sb.draw(Assets.manager.get(Assets.ALPHA, Texture.class), 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
            sb.end();

            sb.begin();
            stage.draw();
            sb.end();
        }

        private void setButtonSize(TextButton button) {
            button.setWidth(500);
            button.setHeight(100);
        }

        public void setMenuListener() {
            Gdx.input.setInputProcessor(stage);
        }
    }
}
