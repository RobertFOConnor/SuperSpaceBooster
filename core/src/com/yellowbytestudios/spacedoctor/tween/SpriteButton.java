package com.yellowbytestudios.spacedoctor.tween;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.MapEditorAssets;

public class SpriteButton extends Sprite {

    public SpriteButton(String textureRef, Vector2 pos) {
        super(Assets.manager.get(textureRef, Texture.class));
        setPosition(pos.x, pos.y);
    }

    public SpriteButton(String textureRef, Vector2 pos, boolean mapButton) {
        super(MapEditorAssets.manager.get(textureRef, Texture.class));
        setPosition(pos.x, pos.y);
    }

    public SpriteButton(TextureRegion tr, Vector2 pos) {
        super(tr);
        setPosition(pos.x, pos.y);
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getRegionWidth(), getRegionHeight());
    }

    public boolean checkTouch(Vector2 touch) {
        return getBounds().contains(touch);
    }
}
