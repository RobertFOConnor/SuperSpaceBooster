package com.yellowbytestudios.spacedoctor.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Player;

/**
 * Created by Robert on 03/26/16.
 */
public class Character extends Box2DSprite {


    protected String name = "";
    protected float posX, posY;
    private float velX, velY;
    private float ACCELERATION;
    private float SPEED, charSpeed;
    public static float WIDTH, HEIGHT;
    protected Player spriter;

    private boolean movingLeft, movingRight, movingUp = false;
    private boolean isDead = false;
    protected boolean shooting = false;

    public Character(Body body) {
        super(body);
    }

    private void assignVariables() {

        ACCELERATION = Gdx.graphics.getDeltaTime() * 2800f;
        SPEED = Gdx.graphics.getDeltaTime() * charSpeed;

        velX = body.getLinearVelocity().x;
        velY = body.getLinearVelocity().y;

        posX = body.getPosition().x;
        posY = body.getPosition().y;
    }

    public Vector2 getPos() {
        return body.getPosition();
    }

    public boolean facingLeft() {
        return spriter.flippedX() == 1;
    }


    public boolean isShooting() {
        return shooting;
    }


    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }
}
