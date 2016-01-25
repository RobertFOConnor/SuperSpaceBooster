package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by BobbyBoy on 25-Jan-16.
 */
public class Entity {

    private Texture texture;
    private Vector2 pos;

    public Entity(Texture texture, Vector2 pos) {
        this.texture = texture;
        this.pos = pos;
    }

    public void render(SpriteBatch sb) {
        sb.draw(texture, pos.x, pos.y);
    }

    public Rectangle getBounds() {
        return new Rectangle(pos.x, pos.y, texture.getWidth(), texture.getHeight());
    }

    public boolean checkTouch(Vector2 touch) {
        if (getBounds().contains(touch)) {
            return true;
        }
        return false;
    }
}
