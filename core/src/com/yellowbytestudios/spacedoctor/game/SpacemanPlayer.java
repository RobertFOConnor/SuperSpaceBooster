package com.yellowbytestudios.spacedoctor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Player;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.controllers.BasicController;
import com.yellowbytestudios.spacedoctor.controllers.KeyboardController;
import com.yellowbytestudios.spacedoctor.controllers.SecondKeyboardController;
import com.yellowbytestudios.spacedoctor.controllers.XBoxController;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.menu.HelmetSelectScreen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;
import com.yellowbytestudios.spacedoctor.screens.editor.MapEditorScreen;


public class SpacemanPlayer {

    private BasicController controller;
    private Player spriter;
    private Body body;

    //Physics variables.
    private float ACCELERATION;
    private float SPEED;
    private float posX, posY;
    private float velX, velY;
    private boolean movingLeft, movingRight, movingUp = false;
    private boolean dieing = false;
    private boolean isDead = false;
    private boolean finished = false;
    public static float WIDTH, HEIGHT;
    private int numFootContacts = 0;


    //Jetpack variables!
    private float maxGas = 500f;
    private float currGas = maxGas;

    //Gun variables!
    private boolean shooting = false;
    private int currAmmo = 10;

    //Spriter variables.
    private int headType;
    private float[] gasColor;

    //Coins
    private static int coins = 0;


    public SpacemanPlayer(Body body, int playerNum, int headType) {
        this.body = body;
        this.headType = headType;
        gasColor = HelmetSelectScreen.CHAR_COLORS[headType];
        spriter = MainGame.spriterManager.getSpiter("player", "idle", 0.6f);

        WIDTH = 80;
        HEIGHT = 118;


        //Assign type of controls for player. (Android, XBox or Keyboard controls).
        if (MainGame.DEVICE.equals("ANDROID")) {
            controller = GameScreen.androidController;
        } else if (MainGame.hasControllers) {
            controller = new XBoxController(playerNum);
        } else {
            controller = new KeyboardController();
        }

        /*if(playerNum == 1) {
            controller = new SecondKeyboardController();
        }*/

        assignVariables();
        spriter.setPosition((int) (posX * Box2DVars.PPM), (int) (posY * Box2DVars.PPM - 22));

        //Start player facing right.
        moveRight();
        spriter.setObject("head", 1f, 4, headType);
    }


    public void update() {

        if (!isDead) {

            updateMovement();

            updateGun();

            updatePaused();
        } else {
            body.setLinearVelocity(0, 0);
        }

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
                stopJetpack();
            }

        } else {
            stopJetpack();
        }
    }

    private void stopJetpack() {
        if (movingUp) {
            movingUp = false;
            SoundManager.stop(Assets.JETPACK_SOUND);
        }
    }

    private void updateGun() {
        if (controller.shootPressed()) {
            if(currAmmo > 0) {
                shooting = true;
                SoundManager.play(Assets.GUN_SOUND);
                currAmmo--;
            } else {
                SoundManager.play(Assets.GUN_SOUND_EMPTY);
            }
        }
    }

    private void updatePaused() {
        if (controller.pausePressed()) {
            if (!GameScreen.isCustomMap) {
                SoundManager.play(Assets.DEATH_SOUND);
                ScreenManager.setScreen(new GameScreen(GameScreen.levelNo));
            } else {
                ScreenManager.setScreen(new MapEditorScreen(GameScreen.customMap));
            }
        }
    }

    private void updateSpriterImages() {
        spriter.setPosition((int) (posX * Box2DVars.PPM), (int) (posY * Box2DVars.PPM - 22));
        spriter.update();
        spriter.setObject("head", 1f, 4, headType);
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
            body.applyForce(0, ACCELERATION * 0.66f, posX, posY, true);
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

        if (!inAir()) {
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
        if (!inAir()) {
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
            GameScreen.particleManager.addEffect((int) (posX * 100 - WIDTH / 2), (int) (posY * 100 - HEIGHT / 2), gasColor);
        } else {
            GameScreen.particleManager.addEffect((int) (posX * 100 - WIDTH / 2) + WIDTH, (int) (posY * 100 - HEIGHT / 2), gasColor);
        }
    }

    private void assignVariables() {

        ACCELERATION = Gdx.graphics.getDeltaTime() * 1800f;
        SPEED = Gdx.graphics.getDeltaTime() * 450f;

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

    public float getMaxGas() {
        return maxGas;
    }

    public float getCurrGas() {
        return currGas;
    }

    public void setCurrGas(float currGas) {
        this.currGas = currGas;

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

    public void startDeath(Player.PlayerListener listener) {
        spriter.setAnimation("death");
        spriter.addListener(listener);
        isDead = true;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        SpacemanPlayer.coins = coins;
    }

    public void setController(BasicController controller) {
        this.controller = controller;
    }

    public boolean isDieing() {
        return dieing;
    }

    public void setDieing(boolean dieing) {
        this.dieing = dieing;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void addNumFootContacts(int newContact) {
        this.numFootContacts += newContact;
    }

    private boolean inAir() {
        return numFootContacts == 0;
    }

    public Body getBody() {
        return body;
    }
}
