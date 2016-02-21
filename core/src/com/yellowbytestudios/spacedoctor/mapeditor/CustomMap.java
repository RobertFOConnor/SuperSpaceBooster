package com.yellowbytestudios.spacedoctor.mapeditor;

import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Created by BobbyBoy on 20-Feb-16.
 */
public class CustomMap extends TiledMap {

    private float exitX = 15;
    private float exitY = 4;

    private float startX = 3;
    private float startY = 4;

    public CustomMap() {

    }



    public float getExitX() {
        return exitX;
    }

    public float getExitY() {
        return exitY;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }
}
