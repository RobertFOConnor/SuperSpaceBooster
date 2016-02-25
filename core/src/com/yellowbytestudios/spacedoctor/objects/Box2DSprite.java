package com.yellowbytestudios.spacedoctor.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;

public class Box2DSprite {

    protected Body body;
    protected Texture texture;
    protected float width;
    protected float height;
    protected float stateTime;
    protected int associationNumber = 0;
    protected boolean activatable = false;


    public Box2DSprite(Body body) {
        this.body = body;
    }

    public void update(float dt) {
        stateTime += dt;
    }

    public void render(SpriteBatch sb) {
        sb.draw(texture, (int) (body.getPosition().x * Box2DVars.PPM - width / 2), (int) (body.getPosition().y * Box2DVars.PPM - height / 2));
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

    public int getAssociationNumber() {
        return associationNumber;
    }

    public boolean isActivatable() {
        return activatable;
    }
}
