package com.yellowbytestudios.spacedoctor.game.gui.pause_menu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.yellowbytestudios.spacedoctor.game.player.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.utils.Metrics;

public class PauseMenu {

    private Stage stage;
    private Array<PauseMenuButton> buttons;
    private int selectedButton = 0;
    private Array<SpacemanPlayer> players;

    public PauseMenu(Array<SpacemanPlayer> players) {
        this.players = players;
        stage = new Stage((new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        buttons = new Array<PauseMenuButton>();
    }

    public void addMenuButton(final PauseMenuButton button) {
        buttons.add(button);
        setButtonSize(button);
        button.setPosition(Gdx.graphics.getWidth() / 2f - 250f, Gdx.graphics.getHeight() - 200 * buttons.size);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                button.onClick();
            }
        });
        stage.addActor(button);
        button.setHighlighted(buttons.size == 1);
    }

    public void disposeStage() {
        stage.dispose();
    }

    public void updateController() {
        if (players.get(0).getController().menuUp()) {
            buttons.get(selectedButton).setHighlighted(false);
            selectedButton--;
            if (selectedButton < 0) {
                selectedButton = buttons.size - 1;
            }
            buttons.get(selectedButton).setHighlighted(true);

        } else if (players.get(0).getController().menuDown()) {
            buttons.get(selectedButton).setHighlighted(false);
            selectedButton++;
            if (selectedButton > buttons.size - 1) {
                selectedButton = 0;
            }
            buttons.get(selectedButton).setHighlighted(true);
        } else if (players.get(0).getController().menuSelect()) {
            buttons.get(selectedButton).onClick();
        }
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(Assets.manager.get(Assets.ALPHA, Texture.class), 0, 0, Metrics.WIDTH, Metrics.HEIGHT);
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