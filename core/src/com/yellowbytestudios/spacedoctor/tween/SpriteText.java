package com.yellowbytestudios.spacedoctor.tween;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.media.Fonts;

public class SpriteText extends Sprite {

    private String message;
    private BitmapFont font;

    public SpriteText(String message, BitmapFont font) {
        super();
        this.message = message;
        this.font = font;
    }

    public void draw(SpriteBatch sb) {
        font.draw(sb, message, getX(), getY());
    }

    public String getText() {
        return message;
    }

    //Used for menu titles;
    public void centerText() {
        setPosition(MainGame.WIDTH / 2 - Fonts.getWidth(font, message), MainGame.HEIGHT + 100);
    }
}
