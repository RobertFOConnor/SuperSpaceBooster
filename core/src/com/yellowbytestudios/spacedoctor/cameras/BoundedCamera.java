package com.yellowbytestudios.spacedoctor.cameras;

/**
 * Created by BobbyBoy on 23-Sep-15.
 */
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * OrthographicCamera that cannot go beyond specified rectangle.
 */
public class BoundedCamera extends OrthographicCamera {

    private float xmin;
    private float xmax;
    private float ymin;
    private float ymax;

    public BoundedCamera() {
        this(0, 0, 0, 0);
    }

    public BoundedCamera(float xmin, float xmax, float ymin, float ymax) {
        super();
        setBounds(xmin, xmax, ymin, ymax);
    }

    public Vector2 unprojectCoordinates(float x, float y) {
        Vector3 rawtouch = new Vector3(x, y, 0);
        unproject(rawtouch);
        return new Vector2(rawtouch.x, rawtouch.y);
    }

    public void setBounds(float xmin, float xmax, float ymin, float ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }


    public void setPosition(float x, float y) {
        setPosition(x, y, 0);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        fixBounds();
    }

    private void fixBounds() {
        if(position.x < (xmin + viewportWidth / 2)) {
            position.x = (xmin + viewportWidth / 2);
        }
        if(position.x > (xmax - viewportWidth / 2)) {
            position.x = (xmax - viewportWidth / 2);
        }
        if(position.y < (ymin + viewportHeight / 2)) {
            position.y = (ymin + viewportHeight / 2);
        }
        if(position.y > (ymax - viewportHeight / 2)) {
            position.y = (ymax - viewportHeight / 2);
        }
    }
}
