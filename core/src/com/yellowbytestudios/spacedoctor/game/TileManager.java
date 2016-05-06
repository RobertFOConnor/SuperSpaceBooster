package com.yellowbytestudios.spacedoctor.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.effects.LightManager;
import com.yellowbytestudios.spacedoctor.mapeditor.TileIDs;

/**
 * Created by BobbyBoy on 26-Dec-15.
 */
public class TileManager {

    private int tileSize, tileMapWidth, tileMapHeight;
    private FixtureDef fdef;
    int wallCount = 0;

    boolean shouldDrawVector = false;
    boolean lineStarted = false;
    int lineCountX = 0, lineCountY = 0;

    private void setMapWidthHeight(TiledMap tm) {
        if (tm.getProperties().get("width", Integer.class) != null) { //Check for Custom Map.
            tileMapWidth = tm.getProperties().get("width", Integer.class);
            tileMapHeight = tm.getProperties().get("height", Integer.class);
        }
    }

    public void createWalls(World world, TiledMap tileMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get(0);
        setMapWidthHeight(tileMap);

        tileSize = (int) layer.getTileWidth();

        float PPM = Box2DVars.PPM;

        float leftSide = (-tileSize / 2) / (PPM);
        float rightSide = (tileSize / 2) / (PPM);

        //BOTTOM CORNERS
        Vector2 bot_L = new Vector2(leftSide, leftSide);
        Vector2 bot_R = new Vector2(rightSide, leftSide);

        //TOP CORNERS
        Vector2 top_L = new Vector2(leftSide, rightSide);
        Vector2 top_R = new Vector2(rightSide, rightSide);

        Vector2 start = new Vector2();
        Vector2 finish = new Vector2();

        BodyDef bdef = new BodyDef();
        fdef = new FixtureDef();


        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                //Get cell at (row, col) position.
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                //Get cell above current cell.
                TiledMapTileLayer.Cell above_cell = layer.getCell(col, row + 1);
                //Set the main cell to our current cell.
                TiledMapTileLayer.Cell main_cell = cell;


                shouldDrawVector = false;

                //Check if cell is a tile.
                if (cell != null) {
                    //If cell is a tile and there is nothing above it, we should draw.

                    if (above_cell == null) {
                        if (!lineStarted) {
                            start = top_L;
                            finish = top_R;
                        } else {
                            finish = top_R.cpy().add(lineCountX, 0);
                        }
                        lineStarted = true;
                        lineCountX++;

                        if (getTileId(main_cell) == TileIDs.DOWN_SPIKE) {
                            drawSpikeHitBox(world, top_L.cpy().add(0.1f, 0.01f), top_R.cpy().add(-0.1f, 0.01f), row, col);
                        }

                    } else {
                        checkDrawVector();
                    }

                } else {

                    //If cell is empty but there is a tile above it, we should draw.
                    if (above_cell != null) {
                        if (!lineStarted) {
                            start = top_L;
                            finish = top_R;
                        } else {
                            finish = top_R.cpy().add(lineCountX, 0);
                        }
                        lineStarted = true;
                        lineCountX++;
                        main_cell = above_cell;

                        if (getTileId(main_cell) == TileIDs.UP_SPIKE) {
                            drawSpikeHitBox(world, top_L.cpy().add(0.1f, -0.01f), top_R.cpy().add(-0.1f, -0.01f), row, col);
                        }
                    } else {
                        checkDrawVector();
                    }
                }


                if (shouldDrawVector) {
                    drawVector(world, start, finish, row, col);
                    lineCountX = 0;
                }
            }
        }
        lineStarted = false;
        lineCountX = 0;


        for (int col = 0; col < layer.getWidth(); col++) {
            for (int row = 0; row < layer.getHeight(); row++) {

                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                TiledMapTileLayer.Cell left_cell = layer.getCell(col - 1, row);
                TiledMapTileLayer.Cell main_cell = cell;
                shouldDrawVector = false;


                if (cell != null) {
                    if (left_cell == null && col != 0) {
                        if (!lineStarted) {
                            start = top_L;
                            finish = bot_L;
                        } else {
                            start = top_L.cpy().add(0, lineCountY);
                        }
                        lineStarted = true;
                        lineCountY++;

                        if (getTileId(main_cell) == TileIDs.LEFT_SPIKE) {
                            drawSpikeHitBox(world, top_L.cpy().add(-0.01f, -0.1f), bot_L.cpy().add(-0.01f, 0.1f), row, col);
                        }
                    } else {
                        checkDrawVector();
                    }
                } else {
                    //If cell is empty but there is a tile above it, we should draw.
                    if (left_cell != null) {
                        if (!lineStarted) {
                            start = top_L;
                            finish = bot_L;
                        } else {
                            start = top_L.cpy().add(0, lineCountY);
                        }
                        lineStarted = true;
                        lineCountY++;
                        main_cell = left_cell;

                        if (getTileId(main_cell) == TileIDs.RIGHT_SPIKE) {
                            drawSpikeHitBox(world, top_L.cpy().add(0.01f, -0.1f), bot_L.cpy().add(0.01f, 0.1f), row, col);
                        }

                    } else {
                        checkDrawVector();
                    }
                }

                if (shouldDrawVector) {
                    drawVector(world, start, finish, row, col);
                    lineCountY = 0;
                }
            }
        }
    }


    public void createLights(LightManager lightManager, TiledMap tileMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get(0);
        setMapWidthHeight(tileMap);

        tileSize = (int) layer.getTileWidth();

        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell != null) {
                    if (getTileId(cell) == TileIDs.DARK_PURPLE) {
                        lightManager.createTileLight(new Vector2(col+0.5f, row+0.5f));
                    } else if(getTileId(cell) == TileIDs.CAGED_WALL) {
                        //lightManager.createConeLight(new Vector2(col+0.5f, row+0.5f));
                    }
                }
            }
        }
    }

    private int getTileId(TiledMapTileLayer.Cell cell) {
        return cell.getTile().getId();
    }

    private void checkDrawVector() {
        if (lineStarted) {
            shouldDrawVector = true;
            lineStarted = false;
        }
    }

    private void drawVector(World world, Vector2 start, Vector2 finish, int row, int col) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(((col - lineCountX) + 0.5f), ((row - lineCountY) + 0.5f));

        ChainShape chainShape = new ChainShape();

        Vector2[] v;
        v = new Vector2[2];
        v[0] = start;
        v[1] = finish;


        chainShape.createChain(v);
        fdef.density = 1f;
        fdef.shape = chainShape;
        fdef.filter.categoryBits = Box2DVars.BIT_WALL;
        fdef.filter.maskBits = Box2DVars.BIT_PLAYER | Box2DVars.BIT_BULLET | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY;

        world.createBody(bdef).createFixture(fdef).setUserData("ground");
        chainShape.dispose();

        wallCount++;
        System.out.println(wallCount + ": WALL CREATED!");
    }


    private void drawSpikeHitBox(World world, Vector2 start, Vector2 finish, int row, int col) {

        BodyDef bdef = new BodyDef();
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
        fdef.filter.categoryBits = Box2DVars.BIT_SPIKE;
        fdef.filter.maskBits = Box2DVars.BIT_PLAYER | Box2DVars.BIT_BULLET | Box2DVars.BIT_BOX | Box2DVars.BIT_ENEMY;

        world.createBody(bdef).createFixture(fdef).setUserData("ground");
        chainShape.dispose();

        System.out.println("SPIKES CREATED!");
    }

    public int getMapWidth() {
        return tileSize * tileMapWidth;
    }

    public int getMapHeight() {
        return tileSize * tileMapHeight;
    }
}