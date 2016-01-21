package com.yellowbytestudios.spacedoctor.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.Button;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;

/**
 * Created by BobbyBoy on 20-Jan-16.
 */
public class AndroidController implements BasicController {

    private OrthoCamera camera;
    private Button left, right, up, shoot;
    private Vector2 touch1, touch2;

    public AndroidController() {
        camera = new OrthoCamera();
        camera.resize();


        left = new Button(new Texture(Gdx.files.internal("left.png")), new Vector2(30, 30));
        right = new Button(new Texture(Gdx.files.internal("right.png")), new Vector2(230, 30));
        up = new Button(new Texture(Gdx.files.internal("up.png")), new Vector2(MainGame.WIDTH - 330, 30));
        shoot = new Button(new Texture(Gdx.files.internal("shoot.png")), new Vector2(MainGame.WIDTH - 330 - 280, 30));

        touch1 = new Vector2();
        touch2 = new Vector2();
    }

    public void update() {
        if (Gdx.input.isTouched(0)) {
            touch1 = camera.unprojectCoordinates(Gdx.input.getX(0),
                    Gdx.input.getY(0));
        } else {
            touch1.set(0, 0);
        }
        if (Gdx.input.isTouched(1)) {
            touch2 = camera.unprojectCoordinates(Gdx.input.getX(1),
                    Gdx.input.getY(1));
        } else {
            touch2.set(0, 0);
        }
    }

    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();

        left.render(sb);
        right.render(sb);
        up.render(sb);
        shoot.render(sb);

        sb.end();
    }

    @Override
    public boolean leftPressed() {
        return Gdx.input.isTouched() && (left.checkTouch(touch1) || left.checkTouch(touch2));
    }

    @Override
    public boolean rightPressed() {
        return Gdx.input.isTouched() && (right.checkTouch(touch1) || right.checkTouch(touch2));
    }

    @Override
    public boolean upPressed() {
        return Gdx.input.isTouched() && (up.checkTouch(touch1) || up.checkTouch(touch2));
    }


    @Override
    public boolean downPressed() {
        return false;
    }

    @Override
    public boolean shootPressed() {
        return Gdx.input.justTouched() && (shoot.checkTouch(touch1) || shoot.checkTouch(touch2));
    }


    @Override
    public boolean pausePressed() {
        return false;
    }
}
