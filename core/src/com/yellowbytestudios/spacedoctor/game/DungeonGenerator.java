package com.yellowbytestudios.spacedoctor.game;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.mapeditor.CustomMap;
import com.yellowbytestudios.spacedoctor.mapeditor.CustomMapObject;
import com.yellowbytestudios.spacedoctor.mapeditor.IDs;
import com.yellowbytestudios.spacedoctor.mapeditor.MapManager;

/**
 * Created by Robert on 05/03/16.
 */
public class DungeonGenerator {

    private static MapLayers layers;
    private static TiledMapTileLayer layer1;
    private static TiledMap tiledMap;
    private static Vector2 startPos;
    private static Vector2 newCenter;
    private static CustomMap map;

    public static CustomMap getGeneratedMap() {

        tiledMap = new TiledMap();
        layers = tiledMap.getLayers();

        layer1 = new TiledMapTileLayer(250, 150, 100, 100);
        layer1.setName("main");

        map = new CustomMap();

        setupMap();
        placeRooms();

        map.setupTileArray(tiledMap);
        map.setStartPos(startPos);
        map.setExitPos(newCenter);

        return map;
    }

    private static void setupMap() {

        for (int row = 0; row < layer1.getHeight(); row++) {
            for (int col = 0; col < layer1.getWidth(); col++) {
                layer1.setCell(col, row, MapManager.CELLS.get(0));
            }
        }
        layers.add(layer1);
    }

    private static void createRoom(Room room) {
        for (int row = 0; row < layer1.getHeight(); row++) {
            for (int col = 0; col < layer1.getWidth(); col++) {

                if (col > room.x1 && col < room.x2 && row > room.y1 && row < room.y2) {
                    layer1.setCell(col, row, null);
                }
            }
        }
    }


    private static void placeRooms() {
        // create array for room storage for easy access
        Array<Room> rooms = new Array<Room>();

        int minSize = 10;
        int maxSize = 30;

        // randomize values for each room
        for (int i = 0; i < 100; i++) {
            int w = minSize + (int) (Math.random() * (maxSize - minSize + 1));
            int h = minSize + (int) (Math.random() * (maxSize - minSize + 1));
            int x = (int) (Math.random() * ((250 - w - 1) + 1));
            int y = (int) (Math.random() * ((150 - h - 1) + 1));

            // create room with randomized values
            Room newRoom = new Room(x, y, w, h);

            boolean failed = false;
            for (Room r : rooms) {
                if (newRoom.intersects(r)) {
                    failed = true;
                    break;
                }
            }
            if (!failed) {
                // local function to carve out new room
                createRoom(newRoom);

                if (i == 0) {
                    startPos = new Vector2(newRoom.center.x, newRoom.y1+2);
                }

                if(rooms.size > 1) {
                    if((int)(Math.random()*2)==0) {
                        for (int e = 0; e < 5; e++) {
                            map.addEnemy(new CustomMapObject(IDs.EYEGUY, new Vector2((newRoom.center.x * 100) - 200 + (e * 100), newRoom.center.y * 100)));
                        }
                    } else {
                        map.addItem(new CustomMapObject(IDs.COIN, new Vector2((newRoom.center.x * 100)-100, newRoom.center.y * 100)));
                        map.addItem(new CustomMapObject(IDs.COIN, new Vector2((newRoom.center.x * 100)+100, newRoom.center.y * 100)));
                        map.addItem(new CustomMapObject(IDs.COIN, new Vector2((newRoom.center.x * 100), newRoom.center.y * 100+100)));
                        map.addItem(new CustomMapObject(IDs.COIN, new Vector2((newRoom.center.x * 100), newRoom.center.y * 100-100)));

                    }
                }


                // store center for new room
                newCenter = newRoom.center;

                if (rooms.size != 0) {
                    // store center of previous room
                    Vector2 prevCenter = rooms.get(rooms.size - 1).center;

                    // carve out corridors between rooms based on centers
                    // randomly start with horizontal or vertical corridors
                    if ((int) (Math.random() * 2) == 1) {
                        hCorridor((int) prevCenter.x, (int) newCenter.x, (int) prevCenter.y);
                        vCorridor((int) prevCenter.y, (int) newCenter.y, (int) newCenter.x);
                    } else {
                        vCorridor((int) prevCenter.y, (int) newCenter.y, (int) prevCenter.x);
                        hCorridor((int) prevCenter.x, (int) newCenter.x, (int) newCenter.y);
                    }
                }

                // push new room into rooms array
                rooms.add(newRoom);
            }
        }
    }

    private static void placeLight(int x, int y) {
        if (layer1.getCell(x, y) != null) {
            layer1.setCell(x, y, MapManager.CELLS.get(1));
        }
    }


    private static void hCorridor(int x1, int x2, int y) {
        int i = 0;
        for (int x = Math.min(x1, x2); x < Math.max(x1, x2) + 1; x++) {
            // destory the tiles to "carve" out corridor
            if (i % 6 == 0) {
                placeLight(x, y-2);
                placeLight(x, y+2);
            }
            layer1.setCell(x, y - 1, null);
            layer1.setCell(x, y, null);
            layer1.setCell(x, y + 1, null);
            i++;
        }
    }

    private static void vCorridor(int y1, int y2, int x) {
        int i = 0;
        for (int y = Math.min(y1, y2); y < Math.max(y1, y2) + 1; y++) {
            // destory the tiles to "carve" out corridor
            if (i % 6 == 0) {
                placeLight(x+2, y);
                placeLight(x-2, y);
            }
            layer1.setCell(x - 1, y, null);
            layer1.setCell(x, y, null);
            layer1.setCell(x + 1, y, null);

            i++;
        }
    }


    private static class Room {
        // these values hold grid coordinates for each corner of the room
        public int x1;
        public int x2;
        public int y1;
        public int y2;

        public int w;
        public int h;

        public Vector2 center;

        // constructor for creating new rooms
        public Room(int x, int y, int w, int h) {
            x1 = x;
            x2 = x + w;
            y1 = y;
            y2 = y + h;
            this.w = w;
            this.h = h;

            center = new Vector2((float) Math.floor((x1 + x2) / 2), (float) Math.floor((y1 + y2) / 2));
        }

        // return true if this room intersects provided room
        public boolean intersects(Room r) {
            return (x1 <= r.x2 && x2 >= r.x1 &&
                    y1 <= r.y2 && r.y2 >= r.y1);
        }
    }
}
