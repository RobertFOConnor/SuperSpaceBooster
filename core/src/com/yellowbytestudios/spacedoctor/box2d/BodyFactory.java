package com.yellowbytestudios.spacedoctor.box2d;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.game.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.game.objects.Box;
import com.yellowbytestudios.spacedoctor.game.objects.Door;
import com.yellowbytestudios.spacedoctor.game.objects.Enemy;
import com.yellowbytestudios.spacedoctor.game.objects.Exit;
import com.yellowbytestudios.spacedoctor.game.objects.PickUp;
import com.yellowbytestudios.spacedoctor.game.objects.Platform;
import com.yellowbytestudios.spacedoctor.mapeditor.CustomMapObject;
import com.yellowbytestudios.spacedoctor.mapeditor.IDs;
import com.yellowbytestudios.spacedoctor.mapeditor.MapManager;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;

public class BodyFactory {

    private static float PPM = Box2DVars.PPM;

    public static SpacemanPlayer createPlayer(World world, int playerNum, int headType) {

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
        circleShape.setRadius(49 / PPM);
        circleShape.setPosition(new Vector2(0, 22 / PPM));

        // Create Fixture Definition for head collision.
        FixtureDef fdef = new FixtureDef();
        fdef.shape = circleShape;
        fdef.filter.categoryBits = Box2DVars.BIT_PLAYER;
        fdef.filter.maskBits = Box2DVars.BIT_PLAYER | Box2DVars.BIT_WALL | Box2DVars.BIT_BOX | Box2DVars.BIT_PICKUP | Box2DVars.BIT_ENEMY | Box2DVars.BIT_SPIKE | Box2DVars.BIT_BULLET;
        body.createFixture(fdef).setUserData("player");


        // Create box for players torso.
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(32 / PPM, 44 / PPM, new Vector2(0, -30 / PPM), 0);

        // Create Fixture Definition for torso collision box.
        fdef.shape = shape;
        fdef.restitution = 0.03f;
        fdef.filter.categoryBits = Box2DVars.BIT_PLAYER;
        fdef.filter.maskBits = Box2DVars.BIT_PLAYER | Box2DVars.BIT_WALL | Box2DVars.BIT_BOX | Box2DVars.BIT_PICKUP | Box2DVars.BIT_ENEMY | Box2DVars.BIT_SPIKE | Box2DVars.BIT_BULLET;
        body.createFixture(fdef).setUserData("player");


        // Create box for players foot.
        shape = new PolygonShape();
        shape.setAsBox(30 / PPM, 20 / PPM, new Vector2(0, -60 / PPM), 0);

        // Create Fixture Definition for foot collision box.
        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Box2DVars.BIT_PLAYER;
        fdef.filter.maskBits = Box2DVars.BIT_PLAYER | Box2DVars.BIT_WALL | Box2DVars.BIT_EXIT | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY | Box2DVars.BIT_SPIKE | Box2DVars.BIT_BULLET;

        // create player foot fixture
        body.createFixture(fdef).setUserData("foot");
        shape.dispose();
        SpacemanPlayer p = new SpacemanPlayer(body, playerNum, headType);
        body.setUserData(p);
        return p;
    }

    public static Body createBullet(World world) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;

        bdef.fixedRotation = true;
        bdef.bullet = true;
        // create body from bodydef
        Body body = world.createBody(bdef);

        // create box shape for bullet.
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50 / PPM, 1 / PPM);

        // create fixturedef for bullet.
        FixtureDef cfdef = new FixtureDef();
        cfdef.shape = shape;
        cfdef.filter.categoryBits = Box2DVars.BIT_BULLET;
        cfdef.filter.maskBits = Box2DVars.BIT_WALL | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY | Box2DVars.BIT_PLAYER;
        body.createFixture(cfdef).setUserData("bullet");
        body.setGravityScale(0f);
        shape.dispose();

        return body;
    }

    public static Array<Box> createBoxes(World world, TiledMap tm) {

        MapLayer ml = tm.getLayers().get("boxes");
        Array<Box> boxes = new Array<Box>();

        if (ml == null) return new Array<Box>();

        float width = 48 / PPM;
        float height = 48 / PPM;

        for (MapObject mo : ml.getObjects()) {

            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.DynamicBody;
            Vector2 pos = getMapObjectPos(mo);
            cdef.position.set(pos.x + width, pos.y + height);

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


    public static Exit createExits(World world, TiledMap tm) {

        MapLayer ml = tm.getLayers().get("exits");
        float width = 50 / PPM;
        float height = 100 / PPM;
        Exit exit = null;

        if (ml == null) { //CUSTOM MAP - TEMP
            exit = createExitBody(world, GameScreen.customMap.getExitPos().x, GameScreen.customMap.getExitPos().y, width, height);
        } else {
            for (MapObject mo : ml.getObjects()) {
                Vector2 pos = getMapObjectPos(mo);
                exit = createExitBody(world, pos.x + (width), pos.y + (height), width, height);
            }
        }
        return exit;
    }


    private static Exit createExitBody(World world, float x, float y, float width, float height) {
        BodyDef cdef = new BodyDef();
        cdef.type = BodyDef.BodyType.StaticBody;
        cdef.position.set(x, y);

        Body body = world.createBody(cdef);

        FixtureDef cfdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        cfdef.shape = shape;
        cfdef.isSensor = true;
        cfdef.filter.categoryBits = Box2DVars.BIT_EXIT;
        cfdef.filter.maskBits = Box2DVars.BIT_PLAYER;

        body.createFixture(cfdef).setUserData("door");
        shape.dispose();

        Exit d = new Exit(body);
        body.setUserData(d);
        return d;
    }


    public static Array<PickUp> createPickups(World world, TiledMap tm) {

        MapLayer ml = tm.getLayers().get("pickups");
        Array<PickUp> pickups = new Array<PickUp>();

        if (GameScreen.customMap != null) {
            for (CustomMapObject mapObject : GameScreen.customMap.getItemArray()) {
                Vector2 pos = new Vector2(mapObject.getPos().x / 100, mapObject.getPos().y / 100);
                String type = "coin";
                if(mapObject.getId() == IDs.GAS) {
                    type = "gas";
                } else if(mapObject.getId() == IDs.AMMO) {
                    type = "ammo";
                } else if(mapObject.getId() == IDs.CLOCK) {
                    type = "time";
                }

                pickups.add(createPickUp(world, pos, type));
            }
        } else {
            if (ml != null) {
                for (MapObject mo : ml.getObjects()) {
                    pickups.add(createPickUp(world, getMapObjectPos(mo), mo.getProperties().get("type", String.class)));
                }
            }
        }
        return pickups;
    }

    public static PickUp createPickUp(World world, Vector2 pos, String type) {

        float width = 35 / PPM;
        float height = 35 / PPM;

        BodyDef cdef = new BodyDef();
        cdef.type = BodyDef.BodyType.StaticBody;
        if (type == null) { //Default to gas pickup if no type is specified.
            type = "coin";
        }
        cdef.position.set(pos.x + width, pos.y + height);

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

        PickUp pu = new PickUp(body, type);
        body.setUserData(pu);

        return pu;
    }


    public static Array<Platform> createPlatforms(World world, TiledMap tm) {

        Array<Platform> platforms = new Array<Platform>();

        MapLayer ml = tm.getLayers().get("platforms");

        if (GameScreen.customMap != null) {

            for (CustomMapObject mapObject : GameScreen.customMap.getObstacleArray()) {

                platforms.add(createPlatform(world, new Vector2(mapObject.getPos().x / 100, mapObject.getPos().y / 100)));
            }

        } else {

            if (ml == null) return new Array<Platform>();

        }
        return platforms;
    }

    public static Platform createPlatform(World world, Vector2 pos) {
        float width = 400 / PPM;
        float height = 200 / PPM;

        BodyDef cdef = new BodyDef();
        cdef.type = BodyDef.BodyType.KinematicBody;
        cdef.position.set(pos.x + (width / 2), pos.y + (height / 2));

        Body body = world.createBody(cdef);

        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(width / 2, height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.shape = bodyShape;
        fixtureDef.filter.categoryBits = Box2DVars.BIT_SPIKE;
        fixtureDef.filter.maskBits = Box2DVars.BIT_PLAYER;

        body.createFixture(fixtureDef).setUserData("wall");
        Platform p = new Platform(body, "horizontal");
        p.setLimit(2);
        body.setUserData(p);
        bodyShape.dispose();

        return p;
    }


    public static Array<Door> createDoors(World world, TiledMap tm) {

        Array<Door> doors = new Array<Door>();

        MapLayer ml = tm.getLayers().get("doors");
        if (ml == null) return new Array<Door>();


        for (MapObject mo : ml.getObjects()) {

            Rectangle rectangle = ((RectangleMapObject) mo).getRectangle();

            float width = rectangle.width / PPM;
            float height = rectangle.height / PPM;

            BodyDef cdef = new BodyDef();
            cdef.type = BodyDef.BodyType.KinematicBody;
            Vector2 pos = getMapObjectPos(mo);
            cdef.position.set(pos.x + (width / 2), pos.y + (height / 2));

            Body body = world.createBody(cdef);

            PolygonShape bodyShape = new PolygonShape();
            bodyShape.setAsBox(width / 2, height / 2);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 1f;
            fixtureDef.shape = bodyShape;
            fixtureDef.filter.categoryBits = Box2DVars.BIT_WALL;
            fixtureDef.filter.maskBits = Box2DVars.BIT_PLAYER;

            body.createFixture(fixtureDef).setUserData("wall");
            Door d = new Door(body, width * Box2DVars.PPM, height * Box2DVars.PPM);
            body.setUserData(d);
            doors.add(d);
            bodyShape.dispose();
        }

        return doors;
    }


    public static Array<Enemy> createEnemies(World world, TiledMap tm) {

        MapLayer ml = tm.getLayers().get("enemies");
        Array<Enemy> enemies = new Array<Enemy>();

        if (GameScreen.customMap != null) {
            for (CustomMapObject mapObject : GameScreen.customMap.getEnemyArray()) {
                Vector2 pos = new Vector2(mapObject.getPos().x / 100, mapObject.getPos().y / 100);
                enemies.add(createEnemy(world, pos, mapObject.getId()));
            }
        } else {
            if (ml != null) {
                for (MapObject mo : ml.getObjects()) {
                    enemies.add(createEnemy(world, getMapObjectPos(mo), IDs.EYEGUY));
                }
            }
        }
        return enemies;
    }

    private static Enemy createEnemy(World world, Vector2 pos, int type) {

        float width = 58 / PPM;
        float height = 48 / PPM;

        BodyDef cdef = new BodyDef();
        cdef.type = BodyDef.BodyType.DynamicBody;
        cdef.position.set(pos.x + width, pos.y + height);
        cdef.fixedRotation = true;

        Body body = world.createBody(cdef);

        FixtureDef cfdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        cfdef.shape = shape;
        cfdef.density = 5f;
        cfdef.filter.categoryBits = Box2DVars.BIT_ENEMY;
        cfdef.filter.maskBits = Box2DVars.BIT_PLAYER | Box2DVars.BIT_BULLET | Box2DVars.BIT_WALL | Box2DVars.BIT_SPIKE | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY;


        body.createFixture(cfdef).setUserData("enemy");

        // Create box for enemies foot.
        shape = new PolygonShape();
        shape.setAsBox(30 / PPM, 20 / PPM, new Vector2(0, -60 / PPM), 0);

        // Create Fixture Definition for foot collision box.
        cfdef.shape = shape;
        cfdef.isSensor = true;
        cfdef.filter.categoryBits = Box2DVars.BIT_ENEMY;
        cfdef.filter.maskBits = Box2DVars.BIT_ENEMY | Box2DVars.BIT_WALL | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY | Box2DVars.BIT_SPIKE | Box2DVars.BIT_BULLET;

        // create player foot fixture
        body.createFixture(cfdef).setUserData("enemy_foot");
        shape.dispose();

        Enemy e = new Enemy(body, type);
        body.setUserData(e);
        return e;
    }

    private static Vector2 getMapObjectPos(MapObject mo) {
        return new Vector2((mo.getProperties().get("x", Float.class) / PPM), (mo.getProperties().get("y", Float.class) / PPM));
    }
}
