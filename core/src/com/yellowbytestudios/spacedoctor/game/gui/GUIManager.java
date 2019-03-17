package com.yellowbytestudios.spacedoctor.game.gui;

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
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.game.gui.pause_menu.PauseMenu;
import com.yellowbytestudios.spacedoctor.game.gui.pause_menu.PauseMenuButton;
import com.yellowbytestudios.spacedoctor.game.player.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.Fonts;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.utils.Metrics;

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

    public GUIManager(GameScreen gameScreen, final Array<SpacemanPlayer> players, boolean isTimed) {
        camera = new OrthoCamera();
        camera.resize();
        this.players = players;
        this.isTimed = isTimed;

        //Initialize Timer
        duration = 30000;
        startTurnTime = System.nanoTime();

        shapeRenderer = new ShapeRenderer();
        alpha = Assets.manager.get(Assets.ALPHA, Texture.class);

        setupPauseMenu(gameScreen);

        Gdx.input.setInputProcessor(null);
        Gdx.input.setCursorCatched(true);

        Fonts.GUIFont.setColor(Color.WHITE);

        if (GameScreen.coreMap) {
            worldString = "World: " + gameScreen.getWorldNo() + "-" + (gameScreen.getLevelNo() - ((gameScreen.getWorldNo() - 1) * 10));
        } else {
            worldString = "Now Testing";
        }
    }

    private void setupPauseMenu(final GameScreen gameScreen) {
        Skin skin = Assets.manager.get(Assets.SKIN, Skin.class);
        pauseMenu = new PauseMenu(players);
        pauseMenu.addMenuButton(new PauseMenuButton("Resume Game", skin, "default") {
            public void onClick() {
                SoundManager.play(Assets.BUTTON_CLICK);
                paused = false;
                Gdx.input.setCursorCatched(true);
                Gdx.input.setInputProcessor(null);
            }
        });
        pauseMenu.addMenuButton(new PauseMenuButton("Restart Level", skin, "default") {
            public void onClick() {
                SoundManager.play(Assets.BUTTON_CLICK);
                for (SpacemanPlayer p : players) {
                    p.setDead(true);
                    paused = false;
                    Gdx.input.setCursorCatched(true);
                    Gdx.input.setInputProcessor(null);
                }
            }
        });
        pauseMenu.addMenuButton(new PauseMenuButton("Exit Game", skin, "default") {
            public void onClick() {
                SoundManager.play(Assets.BUTTON_CLICK);
                gameScreen.exit();
                pauseMenu.disposeStage();
                paused = false;
                Gdx.input.setInputProcessor(null);
            }
        });

        paused = false;
    }


    public void update() {

        for (com.yellowbytestudios.spacedoctor.game.player.SpacemanPlayer p : players) {
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
        if (paused) {
            pauseMenu.updateController();
        }
    }

    public void render(SpriteBatch sb) {

        renderPlayerGUI(sb);

        if (paused) {
            pauseMenu.render(sb);
        }
    }

    private void renderPlayerGUI(SpriteBatch sb) {
        renderGasLevel();

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(alpha, 0, Metrics.HEIGHT, Metrics.WIDTH, -120);
        for (int i = 0; i < players.size; i++) {
            String ammo = "x" + String.format("%02d", players.get(i).getGun().getAmmo());
            Fonts.GUIFont.draw(sb, ammo, 100 + (i * 200), Metrics.HEIGHT - 70);
        }

        if (isTimed) {
            drawTimer(sb);
        }

        Fonts.GUIFont.draw(sb, worldString, 1600, Metrics.HEIGHT - 55);
        sb.end();
    }

    private void renderGasLevel() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.identity();

        for (int i = 0; i < players.size; i++) {
            SpacemanPlayer player = players.get(i);

            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(100 + (i * 200), Metrics.HEIGHT - 55, 100 * 1.2f, 40);

            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(100 + (i * 200), Metrics.HEIGHT - 55, (player.getCurrGas() / (player.getMaxGas() / 100)) * 1.2f, 40);
        }
        shapeRenderer.end();
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

        Fonts.GUIFont.draw(sb, time, Metrics.WIDTH / 2 - (Fonts.getWidth(Fonts.GUIFont, time) / 2), Metrics.HEIGHT - 30);
        Fonts.GUIFont.setColor(Color.WHITE);
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
}
