package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.controllers.BasicController;
import com.yellowbytestudios.spacedoctor.controllers.KeyboardController;
import com.yellowbytestudios.spacedoctor.controllers.XBoxController;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;

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

    //Jetpack variables!
    private final float maxGas = 500;
    private float currGas = 500;

    //Gun variables!
    private final int maxAmmo = 10;
    private int currAmmo = 10;

    private boolean canMove = true;
    private boolean isDead = false;


    public SpacemanPlayer(Body body, Box2DContactListeners contactListener) {
        this.body = body;
        this.contactListener = contactListener;
        spriter = MainGame.spriterManager.initPlayer();

        WIDTH = 80;
        HEIGHT = 118;


        //Assign type of controls for player. (XBox or Keyboard controls).
        if(MainGame.DEVICE.equals("ANDROID")) {

            controller = GameScreen.androidController;

        } else if (MainGame.hasControllers) {
            controller = new XBoxController();
        } else {
            controller = new KeyboardController();
        }

        assignVariables();
        spriter.setPosition((int) (posX * Box2DVars.PPM), (int) (posY * Box2DVars.PPM - 22));

        //Start player facing right.
        moveRight();
    }


    public void update() {

        assignVariables();

        if (controller.leftPressed()) { // LEFT | RIGHT MOVEMENT
            moveLeft();
        }

        if (controller.rightPressed()) {
            moveRight();
        }

        if(!controller.rightPressed() && !controller.leftPressed()){
            idle();
        }

        if (controller.upPressed()) { // UP | DOWN MOVEMENT

            if (currGas > 0) {
                moveUp();
            } else {
                movingUp = false;
                SoundManager.stop(Assets.manager.get(Assets.JETPACK_SOUND, Sound.class));
            }

        } else {
            movingUp = false;
            movingDown = false;
            SoundManager.stop(Assets.manager.get(Assets.JETPACK_SOUND, Sound.class));
        }

        if (controller.shootPressed() && currAmmo > 0) {
            shooting = true;
            SoundManager.play(Assets.manager.get(Assets.GUN_SOUND, Sound.class));
            currAmmo--;
        }

        if(controller.pausePressed()) {
            SoundManager.play(Assets.manager.get(Assets.DEATH_SOUND, Sound.class));
            ScreenManager.setScreen(new GameScreen(GameScreen.levelNo));
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
            SoundManager.play(Assets.manager.get(Assets.JETPACK_SOUND, Sound.class));
            movingUp = true;
            movingDown = false;
        }

        currGas--;

        addSmoke();
        spriter.setAnimation("jump");
    }

    private void idle() {
        movingRight = false;
        movingLeft = false;

        if (!contactListener.playerInAir()) {
            spriter.setAnimation("idle");
            if (velX != 0) {
                body.setLinearVelocity(0, velY);
            }
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

    public void setPos(Vector2 pos) {
        body.setTransform(pos, 0);
    }


    private void addSmoke() {
        if (!facingLeft()) {
            GameScreen.particleManager.addEffect((int) (posX * 100 - WIDTH / 2), (int) (posY * 100 - HEIGHT / 2));
        } else {
            GameScreen.particleManager.addEffect((int) (posX * 100 - WIDTH / 2) + WIDTH, (int) (posY * 100 - HEIGHT / 2));
        }
    }

    private void assignVariables() {
        velX = body.getLinearVelocity().x;
        velY = body.getLinearVelocity().y;

        posX = body.getPosition().x;
        posY = body.getPosition().y;
    }


    public boolean isShooting() {
        return shooting;
    }


    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public float getCurrGas() {
        return currGas;
    }

    public void setCurrGas(float currGas) {
        this.currGas = currGas;

        if(this.currGas > maxGas) {
            this.currGas = maxGas;
        }
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public int getCurrAmmo() {
        return currAmmo;
    }

    public void setCurrAmmo(int currAmmo) {
        this.currAmmo = currAmmo;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }
}
