package com.yellowbytestudios.spacedoctor.mapeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.cameras.BoundedCamera;

/**
 * Created by BobbyBoy on 25-Jan-16.
 */
public class MapViewer {

    //pinching
    private BoundedCamera camera;
    private Vector2 touch1, touch2;
    private float initDist, currDist;
    private boolean zooming = false;
    private float initZoom;

    public MapViewer(BoundedCamera camera) {
        this.camera = camera;
        touch1 = new Vector2();
        touch2 = new Vector2();
        initDist = 0;
        currDist = 0;
    }

    public void onPinch() {

        if (Gdx.input.isTouched(0)) {
            touch1 = camera.unprojectCoordinates(Gdx.input.getX(0),
                    Gdx.input.getY(0));
        } else {
            touch1.set(0, 0);
        }
        if (Gdx.input.isTouched(1)) {
            touch2 = camera.unprojectCoordinates(Gdx.input.getX(1),
                    Gdx.input.getY(1));

            if(!zooming) {
                initDist = (float) Math.sqrt(touch2.x * touch1.x + touch2.y * touch1.y);
                initZoom = camera.zoom;
                zooming = true;
            } else {
                currDist = (float) Math.sqrt(touch2.x * touch1.x + touch2.y * touch1.y);

                float dist = currDist-initDist;

                //camera.zoom = initZoom+(dist/200);

                setZoom(dist);

                System.out.println(dist + ": DISTANCE");
            }

        } else {
            touch2.set(0, 0);
            initDist = 0;
            currDist = 0;
            zooming = false;
        }
    }

    public void setZoom(float distance) {
        camera.zoom = initZoom+(distance/200);
    }

    public void zoomIn() {

        if (camera.zoom > 0.5f) {
            camera.zoom -= 0.03f;
        }
    }

    public void zoomOut() {

        if (camera.zoom < 3f) {
            camera.zoom += 0.03f;
        }
    }
}
