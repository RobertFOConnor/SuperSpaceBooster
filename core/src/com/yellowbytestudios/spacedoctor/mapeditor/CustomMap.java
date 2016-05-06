package com.yellowbytestudios.spacedoctor.mapeditor;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;


public class CustomMap {

    private String name;

    private int[][] tileArray;

    private Vector2 exitPos = new Vector2(15, 4);
    private Vector2 startPos = new Vector2(3, 4);

    private Array<CustomMapObject> enemyArray = new Array<CustomMapObject>();
    private Array<CustomMapObject> itemArray = new Array<CustomMapObject>();
    private Array<CustomMapObject> obstacleArray = new Array<CustomMapObject>();

    public CustomMap() {

    }


    public CustomMap(String name, TiledMap tm, MapManager.DraggableObject exit, MapManager.DraggableObject playerSpawn, Array<Array<MapManager.DraggableObject>> draggableLists) {
        this.name = name;

        setupTileArray(tm);

        exitPos = new Vector2(exit.getCenter().x / Box2DVars.PPM, exit.getCenter().y / Box2DVars.PPM);
        startPos = new Vector2(playerSpawn.getCenter().x / Box2DVars.PPM, playerSpawn.getCenter().y / Box2DVars.PPM);

        draggableToMapObject(draggableLists.get(0), enemyArray);
        draggableToMapObject(draggableLists.get(1), itemArray);
        draggableToMapObject(draggableLists.get(2), obstacleArray);
    }

    public void setupTileArray(TiledMap tm) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tm.getLayers().get(0);
        tileArray = new int[layer.getWidth()][layer.getHeight()];

        //Cycle through layer from tiledmap and create a 2d array of the tile ids.
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {

                if (layer.getCell(x, y) != null) {
                    tileArray[x][y] = layer.getCell(x, y).getTile().getId();
                } else {
                    tileArray[x][y] = -1;
                }
            }
        }
    }

    public void addEnemy(CustomMapObject object) {
        enemyArray.add(object);
    }

    public void addItem(CustomMapObject object) {
        itemArray.add(object);
    }

    public void addObstacle(CustomMapObject object) {
        obstacleArray.add(object);
    }

    public void setStartPos(Vector2 pos) {
        startPos = pos;
    }

    public void setExitPos(Vector2 pos) {
        exitPos = pos;
    }

    private void draggableToMapObject(Array<MapManager.DraggableObject> draggableList, Array<CustomMapObject> mapObjectList) {
        for (MapManager.DraggableObject obj : draggableList) {
            mapObjectList.add(new CustomMapObject(obj.getId(), obj.getCenter()));
        }
    }

    public String getName() {
        return name;
    }

    public int[][] loadMap() {
        return tileArray;
    }

    public Vector2 getStartPos() {
        return startPos;
    }

    public Vector2 getExitPos() {
        return exitPos;
    }

    public Array<CustomMapObject> getEnemyArray() {
        return enemyArray;
    }

    public Array<CustomMapObject> getItemArray() {
        return itemArray;
    }

    public Array<CustomMapObject> getObstacleArray() {
        return obstacleArray;
    }

    public int getMapWidth() {
        return 100 * tileArray.length;
    }

    public int getMapHeight() {
        return 100 * tileArray[0].length;
    }
}
