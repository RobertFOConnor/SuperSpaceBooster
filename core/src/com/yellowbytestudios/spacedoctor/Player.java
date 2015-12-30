package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;

/**
 * Created by BobbyBoy on 26-Dec-15.
 */
public class Player {

    private Body body;
    private Sprite sprite;

    private float SPEED = 5f;
    private float ACCELERATION = 30f;

    private float posX, posY;
    private float velX, velY;
    protected boolean movingLeft, movingRight, movingUp, movingDown = false;

    public Player(Body body) {
        this.body = body;
        sprite = new Sprite(new Texture(Gdx.files.internal("player.png")));
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
            movingUp = false;
            movingDown = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveUp();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            moveDown();
        } else {
            movingRight = false;
            movingLeft = false;
            movingUp = false;
            movingDown = false;
        }
    }

    public void render(SpriteBatch sb) {
        sprite.setPosition((int) (posX * 100 - sprite.getWidth() / 2), (int) (posY * 100 - sprite.getHeight() / 2));
        sprite.draw(sb);
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
            movingUp = false;
            movingDown = false;
        }

        if (!facingLeft()) {
            flipSprite(false);
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
            movingUp = false;
            movingDown = false;
        }

        if (facingLeft()) {
            flipSprite(true);
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
            movingLeft = false;
            movingRight = false;
        }

        GameScreen.particleManager.addEffect((int) (posX * 100 - sprite.getWidth() / 2), (int) (posY * 100 - sprite.getHeight() / 2));
    }

    protected void moveDown() {
        body.setLinearVelocity(0, -40);

        if (!movingDown) {
            movingUp = false;
            movingDown = true;
            movingLeft = false;
            movingRight = false;
        }
    }

    public boolean facingLeft() {
        return sprite.isFlipX();
    }

    public void flipSprite(boolean dir) {
        sprite.setFlip(dir, false);
    }

    public Body getBody() {
        return body;
    }
}
