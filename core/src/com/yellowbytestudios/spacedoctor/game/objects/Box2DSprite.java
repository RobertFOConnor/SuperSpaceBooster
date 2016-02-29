package com.yellowbytestudios.spacedoctor.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;

public class Box2DSprite {

    protected Body body;
    protected Texture texture;
    protected float posX;
    protected float posY;
    protected float width;
    protected float height;
    protected float stateTime;
    protected int[] associationNumbers;
    protected boolean activatable = false;


    public Box2DSprite(Body body) {
        this.body = body;
        this.posX = body.getPosition().x;
        this.posY = body.getPosition().y;
    }

    public void update(float dt) {
        stateTime += dt;
    }

    public void render(SpriteBatch sb) {
        sb.draw(texture, (posX * Box2DVars.PPM - width / 2), (posY * Box2DVars.PPM - height / 2));
    }

    public Body getBody() {
        return body;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int[] getAssociationNumbers() {
        return associationNumbers;
    }

    public boolean isActivatable() {
        return activatable;
    }
}
