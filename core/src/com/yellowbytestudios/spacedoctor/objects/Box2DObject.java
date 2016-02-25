package com.yellowbytestudios.spacedoctor.objects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;


public class Box2DObject {

    public Body body;
    protected BodyDef bodyDef;
    protected FixtureDef fixtureDef;


    public Box2DObject(World world, MapObject rectangleMapObject) {

        Rectangle rectangle = ((RectangleMapObject) rectangleMapObject).getRectangle();

        float width = rectangle.width;
        float height = rectangle.height;


        BodyDef cdef = new BodyDef();
        cdef.type = getBodyType();

        Vector2 pos = new Vector2(0,0);
        cdef.position.set(pos.x+width, pos.y+height);

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

        /*PickUp p = new PickUp(body, type);
        pickups.add(p);
        body.setUserData(p);

        String type = mo.getProperties().get("type", String.class);*/
        /*if (type == null) { //Default to gas pickup if no type is specified.
            type = "gas";
        }*/

    }

    public BodyType getBodyType() {
        return BodyType.StaticBody;
    }
}
