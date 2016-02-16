package com.yellowbytestudios.spacedoctor.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.MainGame;

public class Door extends Box2DSprite {

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
        spriter.update();
        MainGame.spriterManager.draw(spriter);
    }
}
