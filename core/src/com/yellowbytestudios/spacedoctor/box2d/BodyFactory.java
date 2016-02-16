package com.yellowbytestudios.spacedoctor.box2d;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.mapeditor.MapManager;
import com.yellowbytestudios.spacedoctor.objects.Box;
import com.yellowbytestudios.spacedoctor.objects.Door;
import com.yellowbytestudios.spacedoctor.objects.Enemy;
import com.yellowbytestudios.spacedoctor.objects.PickUp;
import com.yellowbytestudios.spacedoctor.objects.Platform;

public class BodyFactory {

    public static Body createBody(World world, String bodyType) {

        if (bodyType.equals("PLAYER")) {

            // Create Body Definition object to define settings.
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.DynamicBody;

            bdef.fixedRotation = true;
            bdef.linearVelocity.set(0f, 0f);
            bdef.position.set(2, 5);

            // Create Body object to hold fixtures.
            Body body = world.createBody(bdef);


            // Create circle for players head.
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(51 / Box2DVars.PPM);
            circleShape.setPosition(new Vector2(0, 22 / Box2DVars.PPM));

            // Create Fixture Definition for head collision.
            FixtureDef fdef = new FixtureDef();
            fdef.shape = circleShape;
            fdef.filter.categoryBits = Box2DVars.BIT_PLAYER;
            fdef.filter.maskBits = Box2DVars.BIT_WALL | Box2DVars.BIT_BOX | Box2DVars.BIT_PICKUP | Box2DVars.BIT_ENEMY | Box2DVars.BIT_SPIKE;
            body.createFixture(fdef).setUserData("player");


            // Create box for players torso.
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(32 / Box2DVars.PPM, 44 / Box2DVars.PPM, new Vector2(0, -30 / Box2DVars.PPM), 0);

            // Create Fixture Definition for torso collision box.
            fdef.shape = shape;
            fdef.restitution = 0.03f;
            fdef.filter.categoryBits = Box2DVars.BIT_PLAYER;
            fdef.filter.maskBits = Box2DVars.BIT_WALL | Box2DVars.BIT_BOX | Box2DVars.BIT_PICKUP | Box2DVars.BIT_ENEMY;
            body.createFixture(fdef).setUserData("player");


            // Create box for players foot.
            shape = new PolygonShape();
            shape.setAsBox(30 / Box2DVars.PPM, 20 / Box2DVars.PPM, new Vector2(0, -60 / Box2DVars.PPM), 0);

            // Create Fixture Definition for foot collision box.
            fdef.shape = shape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = Box2DVars.BIT_PLAYER;
            fdef.filter.maskBits = Box2DVars.BIT_WALL | Box2DVars.BIT_DOOR | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY | Box2DVars.BIT_SPIKE;

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
            cfdef.filter.maskBits = Box2DVars.BIT_WALL | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY;
            body.createFixture(cfdef).setUserData("bullet");
            body.setGravityScale(0f);
            shape.dispose();

            return body;

        } else {
            return null;
        }
    }


    public static Door createDoors(World world, TiledMap tm) {

        MapLayer ml = tm.getLayers().get("exits");
        float width = 50 / Box2DVars.PPM;
        float height = 100 / Box2DVars.PPM;
        Door d = null;

        if (ml == null) { //CUSTOM MAP - TEMP
            d = createDoorBody(world, MapManager.exitX, MapManager.exitY, width, height);
        } else {
            for (MapObject mo : ml.getObjects()) {
                d = createDoorBody(world, (mo.getProperties().get("x", Float.class) / Box2DVars.PPM) + (width), (mo.getProperties().get("y", Float.class) / Box2DVars.PPM) + (height), width, height);
            }
        }
        return d;
    }

    private static Door createDoorBody(World world, float x, float y, float width, float height) {
        BodyDef cdef = new BodyDef();
        cdef.type = BodyDef.BodyType.StaticBody;
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
        body.setUserData(d);
        return d;
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
            cfdef.filter.maskBits = Box2DVars.BIT_PLAYER | Box2DVars.BIT_BULLET | Box2DVars.BIT_WALL | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY;

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
            String type = mo.getProperties().get("type", String.class);
            if (type == null) { //Default to gas pickup if no type is specified.
                type = "gas";
            }
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

            PickUp p = new PickUp(body, type);
            pickups.add(p);
            body.setUserData(p);
        }
        return pickups;
    }

    public static Array<Platform> createPlatforms(World world, TiledMap tm) {

        Array<Platform> platforms = new Array<Platform>();

        MapLayer ml = tm.getLayers().get("platforms");
        if (ml == null) return new Array<Platform>();


        for (MapObject mo : ml.getObjects()) {

            String type = mo.getProperties().get("type", String.class);
            float width = (Float.parseFloat(mo.getProperties().get("width", String.class)));
            float height = (Float.parseFloat(mo.getProperties().get("height", String.class)));

            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.KinematicBody;
            float x = mo.getProperties().get("x", Float.class) / Box2DVars.PPM;
            float y = mo.getProperties().get("y", Float.class) / Box2DVars.PPM;
            cdef.position.set(x + (width / 2), y + (height / 2));

            Body body = world.createBody(cdef);

            PolygonShape bodyShape = new PolygonShape();
            bodyShape.setAsBox(width / 2, height / 2);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 1f;
            fixtureDef.shape = bodyShape;
            fixtureDef.filter.categoryBits = Box2DVars.BIT_SPIKE;
            fixtureDef.filter.maskBits = Box2DVars.BIT_PLAYER;

            body.createFixture(fixtureDef).setUserData("wall");
            Platform p = new Platform(body, type);
            p.setLimit(Float.parseFloat(mo.getProperties().get("distance", String.class)));
            body.setUserData(p);
            platforms.add(p);
            bodyShape.dispose();
        }

        return platforms;
    }


    public static Array<Enemy> createEnemies(World world, TiledMap tm) {

        MapLayer ml = tm.getLayers().get("enemies");
        Array<Enemy> enemies = new Array<Enemy>();

        if (ml == null) return new Array<Enemy>();

        float width = 51 / Box2DVars.PPM;
        float height = 75 / Box2DVars.PPM;

        for (MapObject mo : ml.getObjects()) {

            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.DynamicBody;
            float x = (mo.getProperties().get("x", Float.class) / Box2DVars.PPM) + (width);
            float y = (mo.getProperties().get("y", Float.class) / Box2DVars.PPM) + (height);
            cdef.position.set(x, y);
            cdef.fixedRotation = true;

            Body body = world.createBody(cdef);

            FixtureDef cfdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width, height);
            cfdef.shape = shape;
            cfdef.density = 5f;
            cfdef.filter.categoryBits = Box2DVars.BIT_ENEMY;
            cfdef.filter.maskBits = Box2DVars.BIT_PLAYER | Box2DVars.BIT_BULLET | Box2DVars.BIT_WALL | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY;

            body.createFixture(cfdef).setUserData("enemy");
            shape.dispose();

            Enemy e = new Enemy(body);
            enemies.add(e);
            body.setUserData(e);
        }
        return enemies;
    }
}
