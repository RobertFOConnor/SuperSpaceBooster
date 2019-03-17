package com.yellowbytestudios.spacedoctor.game.gui.pause_menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PauseMenuButton extends TextButton {

    private boolean highlighted = false;

    public PauseMenuButton(String text, Skin skin, String style) {
        super(text, skin, style);
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
        if (highlighted) {
            this.setColor(Color.MAROON);
            getLabel().setFontScale(1.2f);
        } else {
            this.setColor(Color.WHITE);
            getLabel().setFontScale(1f);
        }
    }

    public void onClick() {

    }
}