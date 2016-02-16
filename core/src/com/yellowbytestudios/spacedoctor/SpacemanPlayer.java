package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Player;
import com.yellowbytestudios.spacedoctor.box2d.Box2DContactListeners;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.controllers.BasicController;
import com.yellowbytestudios.spacedoctor.controllers.KeyboardController;
import com.yellowbytestudios.spacedoctor.controllers.XBoxController;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;


public class SpacemanPlayer {

    private Box2DContactListeners contactListener;
    private BasicController controller;
    private Player spriter;
    private Body body;

    //Physics variables.
    private float ACCELERATION = 30f;
    private float SPEED = 7f;
    private float posX, posY;
    private float velX, velY;
    private boolean movingLeft, movingRight, movingUp = false;
    private boolean isDead = false;
    public static float WIDTH, HEIGHT;


    //Jetpack variables!
    private float currGas = 500;

    //Gun variables!
    private boolean shooting = false;
    private int currAmmo = 10;

    //Spriter variables.
    private int headType = 1;
    int angle = 0;


    public SpacemanPlayer(Body body, Box2DContactListeners contactListener) {
        this.body = body;
        this.contactListener = contactListener;
        spriter = MainGame.spriterManager.initPlayer();

        WIDTH = 80;
        HEIGHT = 118;


        //Assign type of controls for player. (Android, XBox or Keyboard controls).
        if (MainGame.DEVICE.equals("ANDROID")) {
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

        updateMovement();

        updateGun();

        updatePaused();

        updateSpriterImages();
    }

    private void updateMovement() {
        assignVariables();

        if (controller.leftPressed()) { // LEFT | RIGHT MOVEMENT
            moveLeft();
        } else if (controller.rightPressed()) {
            moveRight();
        } else {
            idle();
        }

        if (controller.upPressed()) { // UP | DOWN MOVEMENT

            if (currGas > 0) {
                moveUp();
            } else {
                movingUp = false;
                SoundManager.stop(Assets.JETPACK_SOUND);
            }

        } else {
            movingUp = false;
            SoundManager.stop(Assets.JETPACK_SOUND);
        }
    }

    private void updateGun() {
        if (controller.shootPressed() && currAmmo > 0) {
            shooting = true;
            SoundManager.play(Assets.GUN_SOUND);
            currAmmo--;
        }
    }

    private void updatePaused() {
        if (controller.pausePressed()) {
            if (!GameScreen.isCustomMap) {
                SoundManager.play(Assets.DEATH_SOUND);
                ScreenManager.setScreen(new GameScreen(GameScreen.levelNo));
            } else {
                ScreenManager.setScreen(new GameScreen(GameScreen.levelNo));
            }
        }
    }

    private void updateSpriterImages() {
        spriter.setPosition((int) (posX * Box2DVars.PPM), (int) (posY * Box2DVars.PPM - 22));
        spriter.update();
        spriter.setObject("head", 1f, 4, headType);

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            if(angle < 40) {
                angle += 3;
            }
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if(angle > -40) {
                angle -= 3;
            }
        }
    }


    public void render() {
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
            SoundManager.play(Assets.JETPACK_SOUND);
            movingUp = true;
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

    public int getAngle() {
        return angle;
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

        float maxGas = 500;
        if (this.currGas > maxGas) {
            this.currGas = maxGas;
        }
    }

    public int getMaxAmmo() {
        return 10;
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
