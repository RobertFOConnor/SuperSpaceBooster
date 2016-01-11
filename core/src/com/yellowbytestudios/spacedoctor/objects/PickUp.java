package com.yellowbytestudios.spacedoctor.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by BobbyBoy on 10-Jan-16.
 */
public class PickUp extends Box2DSprite {

    private String type = "";


    public PickUp(Body body) {
        super(body);
        texture = new Texture(Gdx.files.internal("pickup_gas.png"));
        width = texture.getWidth();
        height = texture.getHeight();
    }
}