package com.yellowbytestudios.spacedoctor.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Door extends Box2DSprite {

    private Vector2 startPos, endPos;
    private float SPEED = 4f;

    public Door(Body body) {
        super(body);
    }

    public void activate() {

        if (active)
            body.setLinearVelocity(Math.signum(SPEED) * (startPos.x - body.getPosition().x) * SPEED,
                    Math.signum(SPEED) * (startPos.y - body.getPosition().y) * SPEED
            );
        else
            body.setLinearVelocity(Math.signum(SPEED) * (endPos.x - body.getPosition().x) * SPEED,
                    Math.signum(SPEED) * (endPos.y - body.getPosition().y) * SPEED
            );

    }

    public Vector2 getStartPos() {
        return startPos;
    }

    public void setStartPos(Vector2 startPos) {
        this.startPos = startPos;
    }

    public Vector2 getEndPos() {
        return endPos;
    }

    public void setEndPos(Vector2 endPos) {
        this.endPos = endPos;
    }
}
