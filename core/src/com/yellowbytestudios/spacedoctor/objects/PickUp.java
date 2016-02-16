package com.yellowbytestudios.spacedoctor.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.Assets;
import com.yellowbytestudios.spacedoctor.GUIManager;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;

/**
 * Created by BobbyBoy on 10-Jan-16.
 */
public class PickUp extends Box2DSprite {

    private String type = "";
    private float posX, posY;
    private com.brashmonkey.spriter.Player spriter;

    public PickUp(Body body, String type) {
        super(body);
        this.type = type;

        if (type.equals("gas")) {
            spriter = MainGame.spriterManager.initGasPickUp();
        } else if (type.equals("ammo")) {
            spriter = MainGame.spriterManager.initAmmoPickUp();
        } else if (type.equals("time")) {
            spriter = MainGame.spriterManager.initTimePickUp();
        }


        posX = body.getPosition().x;
        posY = body.getPosition().y;
    }

    public void render(SpriteBatch sb) {
        spriter.setPosition((int) (posX * Box2DVars.PPM), (int) (posY * Box2DVars.PPM));
        spriter.update();
        MainGame.spriterManager.draw(spriter);
    }

    public void activate(SpacemanPlayer player) {
        if (type.equals("ammo")) {
            player.setCurrAmmo(10);
        } else if (type.equals("gas")) {
            player.setCurrGas(player.getCurrGas() + 250);
        } else if (type.equals("time")) {
            GUIManager.duration += 15000;
        }
        SoundManager.play(Assets.PICKUP_SOUND);
    }
}