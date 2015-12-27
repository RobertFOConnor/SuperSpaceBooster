package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by BobbyBoy on 26-Dec-15.
 */
public class TileManager {


    public void createWalls(World world, TiledMap tileMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get(0);

        float tileSize = layer.getTileWidth();
        float PPM = 100;

        Vector2 bot_L = new Vector2((-tileSize / 2)/(PPM), (-tileSize / 2)/(PPM));
        Vector2 top_L = new Vector2((-tileSize / 2)/(PPM), ( tileSize / 2)/(PPM));
        Vector2 top_R = new Vector2(( tileSize / 2)/(PPM), ( tileSize / 2)/(PPM));
        Vector2 bot_R = new Vector2(( tileSize / 2)/(PPM), (-tileSize / 2)/(PPM));

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();


        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((col + 0.5f), (row + 0.5f));

                ChainShape chainShape = new ChainShape();

                Vector2[] v;
                v = new Vector2[4];
                v[0] = bot_L;
                v[1] = top_L;
                v[2] = top_R;
                v[3] = bot_R;


                chainShape.createChain(v);
                fdef.density = 1f;
                fdef.shape = chainShape;
                fdef.filter.categoryBits = Box2DVars.BIT_WALL;
                fdef.filter.maskBits = Box2DVars.BIT_PLAYER;


                world.createBody(bdef).createFixture(fdef).setUserData("ground");
                chainShape.dispose();
            }
        }
    }
}
