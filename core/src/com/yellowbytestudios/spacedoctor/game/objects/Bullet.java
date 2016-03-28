package com.yellowbytestudios.spacedoctor.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.media.Assets;

/**
 * Created by BobbyBoy on 08-Jan-16.
 */
public class Bullet extends Box2DSprite {

    private Vector2 dir;

    public Bullet(Body body, Vector2 dir, int id) {
        super(body);
        body.setUserData(this);

        if(id == 1) {
            texture = Assets.manager.get(Assets.BULLET_ENEMY, Texture.class);
        } else {
            texture = Assets.manager.get(Assets.BULLET, Texture.class);
        }

        width = texture.getWidth();
        height = texture.getHeight();
        this.dir = dir;
    }

    public void update() {
        body.setLinearVelocity(dir.x * Gdx.graphics.getDeltaTime(), dir.y * Gdx.graphics.getDeltaTime());
    }

    public void render(SpriteBatch sb) {
        sb.draw(texture, (int) (body.getPosition().x * Box2DVars.PPM - width / 2), (int) (body.getPosition().y * Box2DVars.PPM - height / 2));
    }

}
