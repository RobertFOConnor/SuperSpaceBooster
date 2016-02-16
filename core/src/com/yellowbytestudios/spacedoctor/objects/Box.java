package com.yellowbytestudios.spacedoctor.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.media.Assets;

/**
 * Created by BobbyBoy on 09-Jan-16.
 */
public class Box extends Box2DSprite {

    public Box(Body body) {
        super(body);
        body.setUserData(this);
        texture = Assets.manager.get(Assets.BOX, Texture.class);
        width = texture.getWidth();
        height = texture.getHeight();
    }
}
