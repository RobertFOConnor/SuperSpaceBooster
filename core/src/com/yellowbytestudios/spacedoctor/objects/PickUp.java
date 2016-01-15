package com.yellowbytestudios.spacedoctor.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.Box2DVars;
import com.yellowbytestudios.spacedoctor.MainGame;

/**
 * Created by BobbyBoy on 10-Jan-16.
 */
public class PickUp extends Box2DSprite {

    private String type = "";
    private float posX, posY;
    private com.brashmonkey.spriter.Player spriter;

    public PickUp(Body body) {
        super(body);
        spriter = MainGame.spriterManager.initGasPickUp();

        posX = body.getPosition().x;
        posY = body.getPosition().y;
    }

    public void render(SpriteBatch sb) {
        spriter.setPosition((int) (posX * Box2DVars.PPM), (int) (posY * Box2DVars.PPM));
        MainGame.spriterManager.draw(spriter);
    }
}