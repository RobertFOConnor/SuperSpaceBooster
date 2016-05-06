package com.yellowbytestudios.spacedoctor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.brashmonkey.spriter.Player;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.controllers.BasicController;
import com.yellowbytestudios.spacedoctor.controllers.KeyboardController;
import com.yellowbytestudios.spacedoctor.controllers.SecondKeyboardController;
import com.yellowbytestudios.spacedoctor.controllers.XBoxController;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.game.objects.Character;
import com.yellowbytestudios.spacedoctor.mapeditor.IDs;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.menu.HelmetSelectScreen;

public class SpacemanPlayer extends Character {

    private BasicController controller;
    private Body body;

    //Physics variables.
    private float ACCELERATION;
    private float SPEED;
    private float JETPACK_POWER;
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
    private Array<Gun> guns;
    private int gunIndex = 0;
    private Gun currGun;

    //Spriter variables.
    private int headType;
    private float[] gasColor;

    //Coins
    private int coins = 0;


    public SpacemanPlayer(Body body, int playerNum, int headType) {
        super(body);
        this.body = body;
        this.headType = headType;
        gasColor = HelmetSelectScreen.CHAR_COLORS[headType];
        spriter = MainGame.spriterManager.getSpiter("player", "idle", 0.58f);

        WIDTH = 80;
        HEIGHT = 118;


        //Assign type of controls for player. (Android, XBox or Keyboard controls).
        if (MainGame.DEVICE.equals("ANDROID")) {
            controller = GameScreen.androidController;
        } else if (MainGame.hasControllers) {
            controller = new XBoxController(playerNum);
        } else {

            if (playerNum == 1) {
                controller = new SecondKeyboardController();
            } else {
                controller = new KeyboardController();
            }
        }

        assignVariables();
        spriter.setPosition((int) (posX * Box2DVars.PPM), (int) (posY * Box2DVars.PPM - 22));

        //Start player facing right.
        moveRight();
        spriter.setObject("head", 1f, 4, headType);


        guns = new Array<Gun>();
        guns.add(new Gun(Gun.BLASTER));
        guns.add(new Gun(Gun.DRILL_CANNON));

        currGun = guns.get(gunIndex);
    }


    public void update() {

        if (!isDead) {
            updateMovement();
            updateGun();
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

        if (controller.upPressed()) { // UP MOVEMENT

            if (currGas > 0) {
                moveUp();
            } else {
                stopJetpack();
            }

        } else {
            stopJetpack();
        }
    }

    private void updateGun() {
        if (controller.shootPressed()) {
            shooting = currGun.shoot(); //Attempt to fire weapon.
        } else if (controller.switchGunPressed()) {
            switchGuns(); //Switch weapons.
        }
    }

    private void switchGuns() {
        gunIndex++;
        if (gunIndex == guns.size) {
            gunIndex = 0;
        }
        currGun = guns.get(gunIndex);
    }

    private void updateSpriterImages() {
        spriter.setPosition((int) (posX * Box2DVars.PPM), (int) (posY * Box2DVars.PPM - 25));
        spriter.update();
        spriter.setObject("head", 1f, IDs.HEAD_FOLDER, headType);
        spriter.setObject("r_arm", 1f, IDs.GUN_FOLDER, currGun.getId());
    }


    public void render() {
        MainGame.spriterManager.draw(spriter);
    }

    private void stopJetpack() {
        if (movingUp) {
            movingUp = false;
            SoundManager.stop(Assets.JETPACK_SOUND);
        }
    }

    private void moveLeft() {
        walk(-1);

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
        walk(1);

        if (!movingRight) {
            movingRight = true;
            movingLeft = false;
        }

        if (facingLeft()) {
            flipSprite();
        }
        setMovingHorImage();
    }

    private void walk(int right) {
        if (Math.abs(velX) < SPEED) {
            body.applyForce(ACCELERATION * right, 0, posX, posY, true);
        } else {
            body.setLinearVelocity(SPEED * right, velY);
        }
    }


    private void moveUp() {
        if (velY < JETPACK_POWER) {
            body.applyForce(0, ACCELERATION * 0.56f, posX, posY, true);
        } else {
            body.setLinearVelocity(velX, JETPACK_POWER);
        }

        if (!movingUp) {
            SoundManager.loop(Assets.JETPACK_SOUND);
            movingUp = true;
        }

        //currGas--;

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

    public void setPos(Vector2 pos) {
        body.setTransform(pos, 0);
    }


    private void addSmoke() {
        int smokeX = (int) (posX * 100 - WIDTH / 2);
        int smokeY = (int) (posY * 100 - HEIGHT / 2);

        if (facingLeft()) {
            smokeX += WIDTH;
        }
        GameScreen.particleManager.addEffect(smokeX, smokeY, gasColor);
    }

    private void assignVariables() {

        ACCELERATION = Gdx.graphics.getDeltaTime() * 2000f;
        SPEED = Gdx.graphics.getDeltaTime() * 500f;
        JETPACK_POWER = Gdx.graphics.getDeltaTime() * 600f;

        velX = body.getLinearVelocity().x;
        velY = body.getLinearVelocity().y;

        posX = body.getPosition().x;
        posY = body.getPosition().y;
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
        this.coins = coins;
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

    public BasicController getController() {
        return controller;
    }

    public Body getBody() {
        return body;
    }

    public Gun getGun() {
        return currGun;
    }

}
