package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.controllers.BasicController;
import com.yellowbytestudios.spacedoctor.controllers.KeyboardController;
import com.yellowbytestudios.spacedoctor.controllers.XBoxController;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;

/**
 * Created by BobbyBoy on 26-Dec-15.
 */
public class SpacemanPlayer {

    private Body body;
    private float SPEED = 7f;
    private float ACCELERATION = 30f;
    private float posX, posY;
    private float velX, velY;
    private boolean movingLeft, movingRight, movingUp, movingDown = false;
    public static float WIDTH, HEIGHT;
    private com.brashmonkey.spriter.Player spriter;
    private Box2DContactListeners contactListener;
    private boolean shooting = false;

    private BasicController controller;

    public SpacemanPlayer(Body body, Box2DContactListeners contactListener) {
        this.body = body;
        this.contactListener = contactListener;
        spriter = MainGame.spriterManager.initPlayer();

        WIDTH = 80;
        HEIGHT = 118;


        //Assign type of controls for player. (XBox or Keyboard controls).
        if (MainGame.hasControllers) {
            controller = new XBoxController();
        } else {
            controller = new KeyboardController();
        }
    }


    public void update() {
        velX = body.getLinearVelocity().x;
        velY = body.getLinearVelocity().y;

        posX = body.getPosition().x;
        posY = body.getPosition().y;


        if (controller.leftPressed()) { // LEFT | RIGHT MOVEMENT
            moveLeft();
        } else if (controller.rightPressed()) {
            moveRight();
        } else {
            idle();
        }

        if (controller.upPressed()) { // UP | DOWN MOVEMENT
            moveUp();
        } else if (controller.downPressed()) {
            moveDown();
        } else {
            movingUp = false;
            movingDown = false;
        }

        if (controller.shootPressed()) {
            shooting = true;
        }
    }


    public void render(SpriteBatch sb) {
        spriter.setPosition((int) (posX * Box2DVars.PPM), (int) (posY * Box2DVars.PPM - 22));
        MainGame.spriterManager.draw(spriter);
    }


    private void moveLeft() {
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
        setMovingHorImage();
    }


    private void moveRight() {
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
        setMovingHorImage();
    }


    private void moveUp() {
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


    private void moveDown() {
        body.setLinearVelocity(0, -40);

        if (!movingDown) {
            movingUp = false;
            movingDown = true;
        }
    }

    private void idle() {
        movingRight = false;
        movingLeft = false;

        if (!contactListener.playerInAir()) {
            spriter.setAnimation("idle");
            body.setLinearVelocity(0, velY);
        } else {
            spriter.setAnimation("jump");
        }
    }


    public boolean facingLeft() {
        return spriter.flippedX() == 1;
    }


    private void flipSprite() {
        spriter.flip(true, false);
    }


    private void setMovingHorImage() {
        if (!contactListener.playerInAir()) {
            spriter.setAnimation("running");
        } else {
            spriter.setAnimation("jump");
        }
    }


    public Vector2 getPos() {
        return body.getPosition();
    }


    private void addSmoke() {
        if (!facingLeft()) {
            GameScreen.particleManager.addEffect((int) (posX * 100 - WIDTH / 2), (int) (posY * 100 - HEIGHT / 2));
        } else {
            GameScreen.particleManager.addEffect((int) (posX * 100 - WIDTH / 2) + WIDTH, (int) (posY * 100 - HEIGHT / 2));
        }
    }


    public boolean isShooting() {
        return shooting;
    }


    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }
}
