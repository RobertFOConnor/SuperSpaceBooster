package com.yellowbytestudios.spacedoctor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.BoundedCamera;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.media.Assets;

public class LevelBackgroundManager {

    private ShapeRenderer shapeRenderer;
    private Array<BackgroundObject> farLayer;
    private Array<Texture> hills;
    private BoundedCamera camera;
    private int width, height;
    private OrthoCamera windowCamera;
    private Color skyTop, skyBot;

    public LevelBackgroundManager(BoundedCamera camera, int width, int height) {
        this.camera = camera;
        windowCamera = new OrthoCamera();
        windowCamera.resize();


        //GREEN SKY
        int TColors[] = {212, 208, 128}; //TOP OF BG GRADIENT
        int BColors[] = {40, 102, 83}; //BOTTOM OF BG GRADIENT

        //RED SKY
        /*int TColors[] = {255, 45, 45}; //TOP OF BG GRADIENT
        int BColors[] = {79 , 0, 0}; //BOTTOM OF BG GRADIENT*/


        skyTop = new Color((float) (TColors[0] / 255), (float) TColors[1] / 255, (float) TColors[2] / 255, 1);
        skyBot = new Color((float) (BColors[0] / 255), (float) BColors[1] / 255, (float) BColors[2] / 255, 1);

        this.width = width;
        this.height = height;
        shapeRenderer = new ShapeRenderer();


        farLayer = new Array<BackgroundObject>();

        int temp = ((width * height) / 1000000);

        for (int i = 0; i < temp - 3; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);

            farLayer.add(new BackgroundObject(Assets.ASTEROIDS, new Vector2(x, y), -60));
        }

        for (int i = 0; i < temp; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);

            farLayer.add(new BackgroundObject(Assets.ASTEROIDS2, new Vector2(x, y), -170));
        }

        hills = new Array<Texture>();
        for (int i = 0; i < width / MainGame.WIDTH; i++) {
            hills.add(Assets.manager.get(Assets.HILLS, Texture.class));
        }
    }


    public void render(SpriteBatch sb) {

        shapeRenderer.setProjectionMatrix(windowCamera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.identity();

        shapeRenderer.rect(0, 0, MainGame.WIDTH, MainGame.HEIGHT, skyTop, skyTop, skyBot, skyBot);
        shapeRenderer.end();

        sb.end();
        sb.begin();

        for (int i = 0; i < hills.size; i++) {
            sb.draw(hills.get(0), (i * MainGame.WIDTH) + (camera.position.x - MainGame.WIDTH / 2) / 2, (camera.position.y - MainGame.HEIGHT / 2) / 2);
        }

        for (BackgroundObject bo : farLayer) {
            bo.update();
            bo.render(sb);
        }
    }

    private class BackgroundObject {

        private Texture texture;
        private Vector2 pos;
        private float speedX;

        public BackgroundObject(String textureRef, Vector2 pos, float speedX) {
            this.texture = Assets.manager.get(textureRef, Texture.class);
            this.pos = pos;
            this.speedX = speedX;
        }

        public void update() {
            pos.add(speedX * Gdx.graphics.getDeltaTime(), 0);

            if (pos.x < -texture.getWidth()) {
                pos.set(width, pos.y);
            }
        }

        public void render(SpriteBatch sb) {
            sb.draw(texture, pos.x, pos.y);
        }
    }
}
