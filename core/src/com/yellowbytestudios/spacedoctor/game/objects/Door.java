package com.yellowbytestudios.spacedoctor.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.media.Assets;

public class Door extends Box2DSprite {

    private NinePatch ninePatch;
    private Vector2 startPos, endPos;
    private float SPEED = 3f;

    public Door(Body body, float width, float height) {
        super(body);
        this.width = width;
        this.height = height;
        ninePatch = new NinePatch(Assets.manager.get(Assets.BOX, Texture.class), 40, 40, 40, 40);
        startPos = new Vector2(posX, posY);
        endPos = new Vector2(posX - (width / 100), posY);

        active = true;
    }

    public void activate() {

        /*if (!active)
            body.setLinearVelocity(Math.signum(SPEED) * (startPos.x - body.getPosition().x) * SPEED,
                    Math.signum(SPEED) * (startPos.y - body.getPosition().y) * SPEED
            );
        else
            body.setLinearVelocity(Math.signum(SPEED) * (endPos.x - body.getPosition().x) * SPEED,
                    Math.signum(SPEED) * (endPos.y - body.getPosition().y) * SPEED
            );*/
    }

    @Override
    public void render(SpriteBatch sb) {
        this.posX = body.getPosition().x;
        this.posY = body.getPosition().y;
        ninePatch.draw(sb, (posX * Box2DVars.PPM) - width / 2, (posY * Box2DVars.PPM) - height / 2, width, height);
    }
}
