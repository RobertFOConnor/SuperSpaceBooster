package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.objects.Box;
import com.yellowbytestudios.spacedoctor.objects.Door;
import com.yellowbytestudios.spacedoctor.objects.PickUp;

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
            fdef.filter.maskBits = Box2DVars.BIT_WALL | Box2DVars.BIT_BOX | Box2DVars.BIT_PICKUP;
            body.createFixture(fdef).setUserData("player");

            //PLAYER FOOT

            shape = new PolygonShape();
            shape.setAsBox(45 / Box2DVars.PPM, 20 / Box2DVars.PPM, new Vector2(0, -60 / Box2DVars.PPM), 0);

            // create fixturedef for player foot
            fdef.shape = shape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = Box2DVars.BIT_PLAYER;
            fdef.filter.maskBits = Box2DVars.BIT_WALL | Box2DVars.BIT_SPIKE | Box2DVars.BIT_DOOR | Box2DVars.BIT_BOX;

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
            cfdef.filter.categoryBits = Box2DVars.BIT_BULLET;
            cfdef.filter.maskBits = Box2DVars.BIT_WALL | Box2DVars.BIT_BOX;
            body.createFixture(cfdef).setUserData("bullet");
            body.setGravityScale(0f);
            shape.dispose();

            return body;

        } else {
            return null;
        }
    }


    public static void createDoors(World world, TiledMap tm) {

        MapLayer ml = tm.getLayers().get("exits");

        if (ml == null) return;

        float width = 50 / Box2DVars.PPM;
        float height = 100 / Box2DVars.PPM;

        for (MapObject mo : ml.getObjects()) {

            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.StaticBody;
            float x = (mo.getProperties().get("x", Float.class) / Box2DVars.PPM) + (width);
            float y = (mo.getProperties().get("y", Float.class) / Box2DVars.PPM) + (height);
            cdef.position.set(x, y);

            Body body = world.createBody(cdef);

            FixtureDef cfdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width, height);
            cfdef.shape = shape;
            cfdef.isSensor = true;
            cfdef.filter.categoryBits = Box2DVars.BIT_DOOR;
            cfdef.filter.maskBits = Box2DVars.BIT_PLAYER;

            body.createFixture(cfdef).setUserData("door");
            shape.dispose();

            Door d = new Door(body);

            //Custom values.
            d.setDestination(mo.getProperties().get("link", String.class));
            int exitX = Integer.parseInt(mo.getProperties().get("exitX", String.class));
            int exitY = Integer.parseInt(mo.getProperties().get("exitY", String.class));
            d.setPlayerPos(new Vector2(exitX, exitY));
            body.setUserData(d);
        }
    }


    public static Array<Box> createBoxes(World world, TiledMap tm) {

        MapLayer ml = tm.getLayers().get("boxes");
        Array<Box> boxes = new Array<Box>();

        if (ml == null) return new Array<Box>();

        float width = 48 / Box2DVars.PPM;
        float height = 48 / Box2DVars.PPM;

        for (MapObject mo : ml.getObjects()) {

            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.DynamicBody;
            float x = (mo.getProperties().get("x", Float.class) / Box2DVars.PPM) + (width);
            float y = (mo.getProperties().get("y", Float.class) / Box2DVars.PPM) + (height);
            cdef.position.set(x, y);

            Body body = world.createBody(cdef);

            FixtureDef cfdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width, height);
            cfdef.shape = shape;
            cfdef.density = 0.1f;
            cfdef.filter.categoryBits = Box2DVars.BIT_BOX;
            cfdef.filter.maskBits = Box2DVars.BIT_PLAYER | Box2DVars.BIT_BULLET | Box2DVars.BIT_WALL | Box2DVars.BIT_BOX;

            body.createFixture(cfdef).setUserData("box");
            shape.dispose();

            Box b = new Box(body);
            boxes.add(b);
            body.setUserData(b);
        }
        return boxes;
    }


    public static Array<PickUp> createPickups(World world, TiledMap tm) {

        MapLayer ml = tm.getLayers().get("pickups");
        Array<PickUp> pickups = new Array<PickUp>();

        if (ml == null) return new Array<PickUp>();

        float width = 35 / Box2DVars.PPM;
        float height = 35 / Box2DVars.PPM;

        for (MapObject mo : ml.getObjects()) {

            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.StaticBody;
            float x = (mo.getProperties().get("x", Float.class) / Box2DVars.PPM) + (width);
            float y = (mo.getProperties().get("y", Float.class) / Box2DVars.PPM) + (height);
            cdef.position.set(x, y);

            Body body = world.createBody(cdef);

            FixtureDef cfdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width, height);
            cfdef.shape = shape;
            cfdef.isSensor = true;
            cfdef.filter.categoryBits = Box2DVars.BIT_PICKUP;
            cfdef.filter.maskBits = Box2DVars.BIT_PLAYER;

            body.createFixture(cfdef).setUserData("pickup");
            shape.dispose();

            PickUp p = new PickUp(body);
            pickups.add(p);
            body.setUserData(p);
        }
        return pickups;
    }
}
