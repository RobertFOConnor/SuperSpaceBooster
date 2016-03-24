package com.yellowbytestudios.spacedoctor.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.MainGame;

public class Exit extends Box2DSprite {

    private com.brashmonkey.spriter.Player spriter;


    public Exit(Body body) {
        super(body);
        spriter = MainGame.spriterManager.getSpiter("exit", "default", 0.85f);
    }

    public void render(SpriteBatch sb) {
        posX = body.getPosition().x;
        posY = body.getPosition().y;
        spriter.setPosition((posX * Box2DVars.PPM), (posY * Box2DVars.PPM));
        spriter.update();
        MainGame.spriterManager.draw(spriter);
    }
}