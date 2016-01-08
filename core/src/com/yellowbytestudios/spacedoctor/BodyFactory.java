package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by BobbyBoy on 08-Jan-16.
 */
public class BodyFactory {

    public static Body createBody(World world, String bodyType) {

        if (bodyType.equals("PLAYER")) {
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.DynamicBody;

            bdef.fixedRotation = true;
            bdef.linearVelocity.set(0f, 0f);
            bdef.position.set(2, 5);

            // create body from bodydef
            Body body = world.createBody(bdef);

            // create box shape for player collision box
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(51 / Box2DVars.PPM, 75 / Box2DVars.PPM);

            // create fixturedef for player collision box
            FixtureDef fdef = new FixtureDef();
            fdef.shape = shape;
            fdef.restitution = 0.03f;
            fdef.filter.categoryBits = Box2DVars.BIT_PLAYER;
            fdef.filter.maskBits = Box2DVars.BIT_WALL;
            body.createFixture(fdef).setUserData("player");

            //PLAYER FOOT

            shape = new PolygonShape();
            shape.setAsBox(45 / Box2DVars.PPM, 20 / Box2DVars.PPM, new Vector2(0, -60 / Box2DVars.PPM), 0);

            // create fixturedef for player foot
            fdef.shape = shape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = Box2DVars.BIT_PLAYER;
            fdef.filter.maskBits = Box2DVars.BIT_WALL | Box2DVars.BIT_SPIKE;

            // create player foot fixture
            body.createFixture(fdef).setUserData("foot");
            shape.dispose();

            return body;


        } else if (bodyType.equals("BULLET")) {

            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.DynamicBody;

            bdef.fixedRotation = true;

            // create body from bodydef
            Body body = world.createBody(bdef);

            // create box shape for bullet.
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(50 / Box2DVars.PPM, 1 / Box2DVars.PPM);

            // create fixturedef for bullet.
            FixtureDef cfdef = new FixtureDef();
            cfdef.shape = shape;
            cfdef.isSensor = true;
            cfdef.filter.categoryBits = Box2DVars.BIT_BULLET;
            cfdef.filter.maskBits = Box2DVars.BIT_WALL;
            body.createFixture(cfdef).setUserData("bullet");
            body.setGravityScale(0f);
            shape.dispose();

            return body;

        } else {
            return null;
        }
    }
}
