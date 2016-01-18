package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.objects.Box2DSprite;

public class Platform extends Box2DSprite {

    private float startY;
    private float limit = 3f;
    private float speed = 3f;
    private boolean movingUp = true;
    private boolean horizontal = true;

    public Platform(Body body, String type) {
        super(body);
        body.setUserData(this);


        if(type.equals("horizontal")) {
            horizontal = true;
            startY = body.getPosition().x;
            body.setLinearVelocity(speed, 0f);
            texture = new Texture(Gdx.files.internal("ver_platform_medium.png"));
        } else {
            horizontal = false;
            startY = body.getPosition().y;
            body.setLinearVelocity(0f, speed);
            texture = new Texture(Gdx.files.internal("hor_platform_medium.png"));
        }

        width = texture.getWidth();
        height = texture.getHeight();
    }

    public void update() {

        if(horizontal) {
            if(Math.abs(body.getPosition().x-startY) > limit) {
                changeDirection();
            }
        } else {
            if(Math.abs(body.getPosition().y-startY) > limit) {
                changeDirection();
            }
        }

    }

    private void changeDirection() {
        movingUp = !movingUp;

        if(horizontal) {
            if(movingUp) {
                body.setLinearVelocity(speed, 0f);
            } else {
                body.setLinearVelocity(-speed, 0f);
            }
        } else {
            if(movingUp) {
                body.setLinearVelocity(0f, speed);
            } else {
                body.setLinearVelocity(0f, -speed);
            }
        }

    }

    public void setLimit(float limit) {
        this.limit = limit;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
