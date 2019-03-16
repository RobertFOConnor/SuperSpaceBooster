package com.yellowbytestudios.spacedoctor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.cameras.BoundedCamera;
import com.yellowbytestudios.spacedoctor.game.player.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.utils.Metrics;

public class GameCamera {

    private BoundedCamera cam, b2dCam;
    private int PPM = 100;

    public GameCamera() {
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, Metrics.WIDTH / PPM, Metrics.HEIGHT / PPM);
        cam = new BoundedCamera();
        cam.setToOrtho(false, Metrics.WIDTH, Metrics.HEIGHT);
    }

    public void setBounds(int mapWidth, int mapHeight, float startX, float startY) {
        b2dCam.setBounds(0, mapWidth / PPM, 0, mapHeight / PPM);
        cam.setBounds(0, mapWidth, 0, mapHeight);

        cam.setPosition(startX * PPM + Metrics.WIDTH / 50, startY * PPM + Metrics.WIDTH / 50);
        b2dCam.setPosition(startX * PPM + Metrics.WIDTH / 50 / PPM, startY * PPM + Metrics.WIDTH / 50 / PPM);

    }

    public void update(Array<SpacemanPlayer> players) {

        float zoom = 1f;
        float targetX;
        float targetY;


        if (players.size > 0) {
            Vector2 playerPos = players.get(0).getPos();

            if (players.size == 1) { // (1P) Center camera on player.
                if (players.get(0).facingLeft()) {
                    targetX = (playerPos.x - 5) * PPM + Metrics.WIDTH / 50;
                    targetY = playerPos.y * PPM + Metrics.HEIGHT / 50;
                } else {
                    targetX = (playerPos.x + 5) * PPM + Metrics.WIDTH / 50;
                    targetY = playerPos.y * PPM + Metrics.HEIGHT / 50;
                }

            } else { // (2P) Center camera on mid-point between players.
                targetX = ((playerPos.x * PPM) + (players.get(1).getPos().x * PPM)) / 2;
                targetY = ((playerPos.y * PPM) + (players.get(1).getPos().y * PPM)) / 2;

                float distance = (float) Math.sqrt(Math.pow((players.get(1).getPos().x - players.get(0).getPos().x), 2) + Math.pow((players.get(1).getPos().y - players.get(0).getPos().y), 2));

                if (distance > 10) {
                    zoom = 1 + ((distance - 10) / 20) * (distance / 10);
                }
            }

            float SPEEDX = Gdx.graphics.getDeltaTime() * (Math.abs(targetX - cam.position.x) * 1.8f);
            float SPEEDY = Gdx.graphics.getDeltaTime() * (Math.abs(targetY - cam.position.y) * 3);
            float newX = newCamPos(cam.position.x, targetX, SPEEDX);
            float newY = newCamPos(cam.position.y, targetY, SPEEDY);

            cam.setPosition(newX, newY);
            b2dCam.setPosition(newX / PPM, newY / PPM);
            cam.zoom = zoom;
            b2dCam.zoom = zoom;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
            cam.zoom = 20f;
            b2dCam.zoom = 20f;
        }

        b2dCam.update();
        cam.update();
    }

    private float newCamPos(float camPos, float target, float speed) {
        if (camPos < target - speed) {
            return camPos + speed;
        } else if (camPos > target + speed) {
            return camPos - speed;
        } else {
            return target;
        }
    }

    public BoundedCamera getCam() {
        return cam;
    }

    public BoundedCamera getB2dCam() {
        return b2dCam;
    }
}
