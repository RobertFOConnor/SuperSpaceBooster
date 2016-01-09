package com.yellowbytestudios.spacedoctor.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by BobbyBoy on 09-Jan-16.
 */
public class Door extends Box2DSprite {

    private String destination = "";
    private Vector2 playerPos;


    public Door(Body body) {
        super(body);
        texture = new Texture(Gdx.files.internal("bullet.png"));
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Vector2 getPlayerPos() {
        return playerPos;
    }

    public void setPlayerPos(Vector2 playerPos) {
        this.playerPos = playerPos;
    }
}
