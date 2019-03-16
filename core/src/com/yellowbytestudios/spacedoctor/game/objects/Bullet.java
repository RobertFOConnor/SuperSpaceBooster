package com.yellowbytestudios.spacedoctor.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.media.Assets;

public class Bullet extends Box2DSprite {

    private Vector2 speed;

    public Bullet(Body body, int dir, int id) {
        super(body);
        body.setUserData(this);

        if (id == 1) {
            texture = Assets.manager.get(Assets.BULLET_ENEMY, Texture.class);
            speed = new Vector2(2100*dir, 0);
        } else {
            texture = Assets.manager.get(Assets.BULLET, Texture.class);
            speed = new Vector2(1700*dir, 0);
        }

        width = texture.getWidth();
        height = texture.getHeight();
    }

    public void update(float delta) {
        body.setLinearVelocity(speed.x * Gdx.graphics.getDeltaTime(), speed.y * delta);
    }

    public void render(SpriteBatch sb) {
        sb.draw(texture, (int) (body.getPosition().x * Box2DVars.PPM - width / 2), (int) (body.getPosition().y * Box2DVars.PPM - height / 2));
    }

}
