package com.yellowbytestudios.spacedoctor.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Player;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.game.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.mapeditor.IDs;


public class Enemy extends Character {

    private String type = "";
    private int health = 3;
    private boolean isDead = false;

    private float SPEED;
    private float ACCELERATION;
    private float velX, velY;

    private float enemySpeed;

    private boolean followsPlayer = true;
    private boolean movingLeft = true;
    private int numFootContacts = 0;
    private float height;

    public Enemy(Body body, int id) {
        super(body);
        name = "enemy";

        if (id == IDs.EYEGUY) {
            spriter = MainGame.spriterManager.getSpiter("eyeball", "walking", 0.8f);
            health = 3;
            enemySpeed = 150f;
            height = 48;
        } else if (id == IDs.PLATTY) {
            spriter = MainGame.spriterManager.getSpiter("platty", "walking", 0.8f);
            health = 2;
            enemySpeed = 300f;
            height = 70;
        }

        assignVariables();
        setSpriterPos();
    }

    private void setSpriterPos() {
        spriter.setPosition((posX * Box2DVars.PPM), (posY * Box2DVars.PPM)-height);
    }

    public void render(SpriteBatch sb) {

        if (!isDead) {
            setSpriterPos();
        }
        spriter.update();
        MainGame.spriterManager.draw(spriter);
    }

    private void moveLeft() {
        if (velX > -SPEED) {
            body.applyForce(-ACCELERATION, 0, posX, posY, true);
        } else {
            body.setLinearVelocity(-SPEED, velY);
        }

        if (!facingLeft()) {
            flipSprite();
        }
    }

    private void moveRight() {
        if (velX < SPEED) {
            body.applyForce(ACCELERATION, 0, posX, posY, true);
        } else {
            body.setLinearVelocity(SPEED, velY);
        }

        if (facingLeft()) {
            flipSprite();
        }
    }

    private void moveUp() {
        if (velY < SPEED) {
            body.applyForce(0, ACCELERATION * 0.066f, posX, posY, true);
        } else {
            body.setLinearVelocity(velX, SPEED);
        }
    }

    private void assignVariables() {

        ACCELERATION = Gdx.graphics.getDeltaTime() * 2800f;
        SPEED = Gdx.graphics.getDeltaTime() * enemySpeed;

        velX = body.getLinearVelocity().x;
        velY = body.getLinearVelocity().y;

        posX = body.getPosition().x;
        posY = body.getPosition().y;
    }

    public void update(SpacemanPlayer player) {

        assignVariables();

        if (!isDead) {

            if (followsPlayer) {
                followPlayer(player);
            } else {
                moveLeft();
            }

            if (inAir()) {
                spriter.setAnimation("air");
            }
        }

        /*if (Math.abs(player.getPos().x - posX) < 8 && Math.abs(player.getPos().y - posY) < 5) {
            int random = (int) (Math.random() * 40);

            if (random == 0) {
                shooting = true;
            }
        }*/
    }

    private void followPlayer(SpacemanPlayer player) {
        if (Math.abs(player.getPos().x - posX) < 8 && Math.abs(player.getPos().y - posY) < 5) {
            if (player.getPos().x < posX) {
                moveLeft();
            } else if (player.getPos().x > posX) {
                moveRight();
            } else {
                body.setLinearVelocity(0f, velY);
            }
        } else {
            body.setLinearVelocity(0f, velY);
        }


        if (velX > 0.5 || velX < -0.5) {
            spriter.setAnimation("walking");
        } else {
            spriter.setAnimation("idle");
        }
    }

    public void startDeath(Player.PlayerListener listener) {
        spriter.setAnimation("death");
        spriter.addListener(listener);
        isDead = true;
    }

    public void addNumFootContacts(int newContact) {
        this.numFootContacts += newContact;
    }

    private boolean inAir() {
        return numFootContacts == 0;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }

    private void flipSprite() {
        spriter.flip(true, false);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
