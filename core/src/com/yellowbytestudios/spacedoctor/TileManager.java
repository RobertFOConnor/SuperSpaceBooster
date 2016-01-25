package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.yellowbytestudios.spacedoctor.mapeditor.MapManager;
import com.yellowbytestudios.spacedoctor.mapeditor.TileIDs;

/**
 * Created by BobbyBoy on 26-Dec-15.
 */
public class TileManager {

    private int tileSize, tileMapWidth, tileMapHeight;
    private FixtureDef fdef;

    public void createWalls(World world, TiledMap tileMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get(0);

        if (tileMap.getProperties().get("width", Integer.class) != null) { //Check for Custom Map.
            tileMapWidth = tileMap.getProperties().get("width", Integer.class);
            tileMapHeight = tileMap.getProperties().get("height", Integer.class);
        } else {
            tileMapWidth = MapManager.customMapWidth;
            tileMapHeight = MapManager.customMapHeight;
        }

        tileSize = (int) layer.getTileWidth();

        float PPM = 100;

        //BOTTOM CORNERS
        Vector2 bot_L = new Vector2((-tileSize / 2) / (PPM), (-tileSize / 2) / (PPM));
        Vector2 bot_R = new Vector2((tileSize / 2) / (PPM), (-tileSize / 2) / (PPM));

        //TOP CORNERS
        Vector2 top_L = new Vector2((-tileSize / 2) / (PPM), (tileSize / 2) / (PPM));
        Vector2 top_R = new Vector2((tileSize / 2) / (PPM), (tileSize / 2) / (PPM));

        boolean shouldDrawVector;
        Vector2 start;
        Vector2 finish;

        BodyDef bdef = new BodyDef();
        fdef = new FixtureDef();


        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                TiledMapTileLayer.Cell above_cell = layer.getCell(col, row + 1);

                start = top_L;
                finish = top_R;

                shouldDrawVector = false;

                TiledMapTileLayer.Cell main_cell = cell;

                if (cell != null) {
                    if (above_cell == null) {
                        shouldDrawVector = true;
                    }
                } else {
                    if (above_cell != null) {
                        shouldDrawVector = true;
                        main_cell = above_cell;
                    }
                }

                if (shouldDrawVector) {

                    bdef.type = BodyDef.BodyType.StaticBody;
                    bdef.position.set((col + 0.5f), (row + 0.5f));

                    ChainShape chainShape = new ChainShape();

                    Vector2[] v;
                    v = new Vector2[2];
                    v[0] = start;
                    v[1] = finish;


                    chainShape.createChain(v);
                    fdef.density = 1f;
                    fdef.shape = chainShape;


                    setCollisionVariables(main_cell);
                    world.createBody(bdef).createFixture(fdef).setUserData("ground");
                    chainShape.dispose();
                }
            }
        }

        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                TiledMapTileLayer.Cell left_cell = layer.getCell(col - 1, row);

                start = top_L;
                finish = bot_L;

                shouldDrawVector = false;
                TiledMapTileLayer.Cell main_cell = cell;


                if (cell != null) {
                    if (left_cell == null) {
                        shouldDrawVector = true;
                    }
                } else {
                    if (left_cell != null) {
                        shouldDrawVector = true;
                        main_cell = left_cell;
                    }
                }

                if (shouldDrawVector) {

                    bdef.type = BodyDef.BodyType.StaticBody;
                    bdef.position.set((col + 0.5f), (row + 0.5f));

                    ChainShape chainShape = new ChainShape();

                    Vector2[] v;
                    v = new Vector2[2];
                    v[0] = start;
                    v[1] = finish;


                    chainShape.createChain(v);
                    fdef.density = 1f;
                    fdef.shape = chainShape;

                    setCollisionVariables(main_cell);
                    world.createBody(bdef).createFixture(fdef).setUserData("ground");
                    chainShape.dispose();
                }
            }
        }
    }

    public void setCollisionVariables(TiledMapTileLayer.Cell main_cell) {
        if (main_cell != null) {

            int id = main_cell.getTile().getId();
            if (id == TileIDs.DOWN_SPIKE || id == TileIDs.LEFT_SPIKE || id == TileIDs.RIGHT_SPIKE || id == TileIDs.UP_SPIKE) {
                fdef.filter.categoryBits = Box2DVars.BIT_SPIKE;
            } else {
                fdef.filter.categoryBits = Box2DVars.BIT_WALL;
            }
        }
        fdef.filter.maskBits = Box2DVars.BIT_PLAYER | Box2DVars.BIT_BULLET | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY;
    }

    public int getMapWidth() {
        return tileSize * tileMapWidth;
    }

    public int getMapHeight() {
        return tileSize * tileMapHeight;
    }
}
