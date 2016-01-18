package com.yellowbytestudios.spacedoctor.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.Box2DVars;
import com.yellowbytestudios.spacedoctor.MainGame;

/**
 * Created by BobbyBoy on 09-Jan-16.
 */
public class Door extends Box2DSprite {

    private String destination = "";
    private float posX, posY;
    private com.brashmonkey.spriter.Player spriter;


    public Door(Body body) {
        super(body);
        spriter = MainGame.spriterManager.initExit();
    }

    public void render(SpriteBatch sb) {
        posX = body.getPosition().x;
        posY = body.getPosition().y;
        spriter.setPosition((int) (posX * Box2DVars.PPM), (int) (posY * Box2DVars.PPM));
        MainGame.spriterManager.draw(spriter);
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
