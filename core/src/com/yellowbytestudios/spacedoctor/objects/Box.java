package com.yellowbytestudios.spacedoctor.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.media.Assets;

public class Box extends Box2DSprite {

    TextureRegion tr;

    public Box(Body body) {
        super(body);
        body.setUserData(this);
        texture = Assets.manager.get(Assets.BOX, Texture.class);
        tr = new TextureRegion(texture);
        width = texture.getWidth();
        height = texture.getHeight();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(tr,
                (body.getPosition().x * Box2DVars.PPM) - (width / 2),
                (body.getPosition().y * Box2DVars.PPM) - (height / 2),
                width / 2,
                height / 2,
                width,
                height,
                1,
                1,
                body.getAngle() * MathUtils.radiansToDegrees);
    }
}
