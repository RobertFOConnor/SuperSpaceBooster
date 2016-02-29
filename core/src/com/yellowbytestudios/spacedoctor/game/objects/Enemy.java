package com.yellowbytestudios.spacedoctor.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.game.SpacemanPlayer;

/**
 * Created by BobbyBoy on 11-Jan-16.
 */
public class Enemy extends Box2DSprite {

    private String type = "";
    private int health = 3;

    private float SPEED;
    private float ACCELERATION;
    private float velX, velY;

    private com.brashmonkey.spriter.Player spriter;
    private boolean followsPlayer = true;
    private boolean movingLeft = true;

    public Enemy(Body body) {
        super(body);
        spriter = MainGame.spriterManager.initDemon();

        assignVariables();
        spriter.setPosition((posX * Box2DVars.PPM), (posY * Box2DVars.PPM) - 75);
    }

    public void render(SpriteBatch sb) {
        spriter.setPosition((posX * Box2DVars.PPM), (posY * Box2DVars.PPM) - 75);
        spriter.update();
        MainGame.spriterManager.draw(spriter);
    }

    private void moveLeft() {
        if (velX > -SPEED) {
            body.applyForce(-ACCELERATION, 0, posX, posY, true);
        } else {
            body.setLinearVelocity(-SPEED, velY);
        }

        if (facingLeft()) {
            flipSprite();
        }
    }

    private void moveRight() {
        if (velX < SPEED) {
            body.applyForce(ACCELERATION, 0, posX, posY, true);
        } else {
            body.setLinearVelocity(SPEED, velY);
        }

        if (!facingLeft()) {
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

        ACCELERATION = Gdx.graphics.getDeltaTime() * 1800f;
        SPEED = Gdx.graphics.getDeltaTime() * 250f;

        velX = body.getLinearVelocity().x;
        velY = body.getLinearVelocity().y;

        posX = body.getPosition().x;
        posY = body.getPosition().y;
    }

    public void update(SpacemanPlayer player) {

        assignVariables();

        if(followsPlayer) {
            followPlayer(player);
        } else {
            moveLeft();
        }
    }

    private void followPlayer(SpacemanPlayer player) {
        if (Math.abs(player.getPos().x - posX) < 8 && Math.abs(player.getPos().y - posY) < 5) {
            if (player.getPos().x < posX) {
                moveLeft();
            } else {
                moveRight();
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

    private boolean facingLeft() {
        return spriter.flippedX() != 1;
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
