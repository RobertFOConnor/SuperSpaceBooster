package com.yellowbytestudios.spacedoctor.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.Box2DVars;

/**
 * Created by BobbyBoy on 09-Jan-16.
 */
public class Box extends Box2DSprite {

    public Box(Body body) {
        super(body);
        body.setUserData(this);
        texture = new Texture(Gdx.files.internal("box.png"));
        width = texture.getWidth();
        height = texture.getHeight();
    }

    public void render(SpriteBatch sb) {
        sb.draw(texture, (int) (body.getPosition().x * Box2DVars.PPM - width / 2), (int) (body.getPosition().y * Box2DVars.PPM - height / 2));
    }
}
