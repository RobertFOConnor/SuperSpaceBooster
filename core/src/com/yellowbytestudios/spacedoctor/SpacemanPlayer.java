package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;

/**
 * Created by BobbyBoy on 26-Dec-15.
 */
public class SpacemanPlayer {

    private Body body;

    private float SPEED = 5f;
    private float ACCELERATION = 30f;

    private float posX, posY;
    private float velX, velY;
    protected boolean movingLeft, movingRight, movingUp, movingDown = false;
    public static float WIDTH, HEIGHT;

    private com.brashmonkey.spriter.Player spriter;
    private Box2DContactListeners contactListener;

    public SpacemanPlayer(Body body, Box2DContactListeners contactListener) {
        this.body = body;
        this.contactListener = contactListener;
        spriter = MainGame.spriterManager.initPlayer();

        WIDTH = 80;
        HEIGHT = 118;
    }

    public void update() {
        velX = body.getLinearVelocity().x;
        velY = body.getLinearVelocity().y;

        posX = body.getPosition().x;
        posY = body.getPosition().y;


        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveRight();
        } else {
            movingRight = false;
            movingLeft = false;

            if (!contactListener.playerInAir()) {
                spriter.setAnimation("idle");
                body.setLinearVelocity(0, velY);
            } else {
                spriter.setAnimation("jump");
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveUp();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            moveDown();
        } else {
            movingUp = false;
            movingDown = false;
        }
    }

    public void render(SpriteBatch sb) {
        spriter.setPosition((int) (posX * Box2DVars.PPM), (int) (posY * Box2DVars.PPM-20));
        MainGame.spriterManager.draw(spriter);
    }

    protected void moveLeft() {
        if (velX > -SPEED) {
            body.applyForce(-ACCELERATION, 0, posX, posY, true);
        } else {
            body.setLinearVelocity(-SPEED, velY);
        }

        if (!movingLeft) {
            movingLeft = true;
            movingRight = false;
        }

        if (!facingLeft()) {
            flipSprite();
        }

        if(!contactListener.playerInAir()) {
            spriter.setAnimation("running");
        } else {
            spriter.setAnimation("jump");
        }
    }

    protected void moveRight() {
        if (velX < SPEED) {
            body.applyForce(ACCELERATION, 0, posX, posY, true);
        } else {
            body.setLinearVelocity(SPEED, velY);
        }

        if (!movingRight) {
            movingRight = true;
            movingLeft = false;
        }

        if (facingLeft()) {
            flipSprite();
        }

        if(!contactListener.playerInAir()) {
            spriter.setAnimation("running");
        } else {
            spriter.setAnimation("jump");
        }
    }

    protected void moveUp() {
        if (velY < SPEED) {
            body.applyForce(0, 20, posX, posY, true);
        } else {
            body.setLinearVelocity(velX, SPEED);
        }

        if (!movingUp) {
            movingUp = true;
            movingDown = false;
        }

        addSmoke();
        spriter.setAnimation("jump");
    }

    protected void moveDown() {
        body.setLinearVelocity(0, -40);

        if (!movingDown) {
            movingUp = false;
            movingDown = true;
        }
    }

    public boolean facingLeft() {
        return spriter.flippedX() == 1;
    }

    public void flipSprite() {
        spriter.flip(true, false);
    }

    public Body getBody() {
        return body;
    }

    private void addSmoke() {
        if (!facingLeft()) {
            GameScreen.particleManager.addEffect((int) (posX * 100 - WIDTH / 2), (int) (posY * 100 - HEIGHT / 2));
        } else {
            GameScreen.particleManager.addEffect((int) (posX * 100 - WIDTH / 2) + WIDTH, (int) (posY * 100 - HEIGHT / 2));
        }
    }
}
