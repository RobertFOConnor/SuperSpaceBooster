package com.yellowbytestudios.spacedoctor.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.Assets;
import com.yellowbytestudios.spacedoctor.Box2DVars;

/**
 * Created by BobbyBoy on 08-Jan-16.
 */
public class Bullet extends Box2DSprite {

    public static float SPEED = 35f;

    public Bullet(Body body) {
        super(body);
        body.setUserData(this);
        texture = Assets.manager.get(Assets.BULLET, Texture.class);
        width = texture.getWidth();
        height = texture.getHeight();
    }

    public void render(SpriteBatch sb) {
        sb.draw(texture, (int) (body.getPosition().x * Box2DVars.PPM - width / 2), (int) (body.getPosition().y * Box2DVars.PPM - height / 2));
    }

}
