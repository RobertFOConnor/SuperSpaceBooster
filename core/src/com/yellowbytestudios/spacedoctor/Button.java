package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by BobbyBoy on 17-Jan-16.
 */
public class Button {

    protected Texture texture, texture_pressed;
    protected Vector2 pos;
    private boolean selected, pressed = false;

    public Button(Texture texture, Texture texture_pressed, Vector2 pos) {
        this.texture = texture;
        this.texture_pressed = texture_pressed;
        this.pos = pos;
    }

    public void render(SpriteBatch sb) {
        if(pressed) {
            sb.draw(texture_pressed, pos.x, pos.y);
        } else {
            sb.draw(texture, pos.x, pos.y);
        }

    }

    public Rectangle getBounds() {
        return new Rectangle(pos.x, pos.y, texture.getWidth(), texture.getHeight());
    }

    public boolean checkTouch(Vector2 touch) {
        if(getBounds().contains(touch)) {
            return true;
        }
        return false;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
