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
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.utils.Metrics;

public class LevelBackgroundManager {

    private ShapeRenderer shapeRenderer;
    private Array<BackgroundObject> farLayer;
    private Array<Texture> hills;
    private BoundedCamera camera;
    private int width, height;
    private OrthoCamera windowCamera;
    private Color skyTop, skyBot;

    public LevelBackgroundManager(BoundedCamera camera, int width, int height) {
        this.width = width;
        this.height = height;
        this.camera = camera;
        shapeRenderer = new ShapeRenderer();
        windowCamera = new OrthoCamera();
        windowCamera.resize();
        setSkyColor();

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


        //BG Hills or wateva.
        hills = new Array<Texture>();
        int hillCount = (width / Metrics.WIDTH) + 1;
        for (int i = 0; i < hillCount; i++) {
            hills.add(Assets.manager.get(Assets.HILLS, Texture.class));
        }
    }


    public void update() {
        for (BackgroundObject bo : farLayer) {
            bo.update();
        }
    }

    public void render(SpriteBatch sb) {

        shapeRenderer.setProjectionMatrix(windowCamera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.identity();

        shapeRenderer.rect(0, 0, Metrics.WIDTH, Metrics.HEIGHT, skyBot, skyBot, skyTop, skyTop);
        shapeRenderer.end();

        sb.end();
        sb.begin();

        for (int i = 0; i < hills.size; i++) {
            sb.draw(hills.get(0), (i * Metrics.WIDTH) + (camera.position.x - Metrics.WIDTH / 2) / 2, (camera.position.y - Metrics.HEIGHT / 2) / 2);
        }

        for (BackgroundObject bo : farLayer) {
            bo.render(sb);
        }
    }

    private void setSkyColor() {
        int TColors[];
        int BColors[];

        //BLUE SKY
        TColors = new int[]{25, 5, 74};
        BColors = new int[]{145, 9, 106};


        skyTop = new Color(convertColor(TColors[0]), convertColor(TColors[1]), convertColor(TColors[2]), 1);
        skyBot = new Color(convertColor(BColors[0]), convertColor(BColors[1]), convertColor(BColors[2]), 1);
    }

    private float convertColor(int colorVal) {
        return colorVal / 255f;
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
