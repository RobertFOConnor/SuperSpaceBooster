package com.yellowbytestudios.spacedoctor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.media.Assets;

public class LevelBackgroundManager {

    private Array<BackgroundObject> farLayer;
    private int width, height;

    public LevelBackgroundManager(int width, int height) {
        this.width = width;
        this.height = height;

        farLayer = new Array<BackgroundObject>();

        int temp = ((width*height)/1000000);
        System.out.println(temp);

        for (int i = 0; i < temp-3; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);

            farLayer.add(new BackgroundObject(Assets.ASTEROIDS, new Vector2(x, y), -60));
        }

        for (int i = 0; i < temp; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);

            farLayer.add(new BackgroundObject(Assets.ASTEROIDS2, new Vector2(x, y), -170));
        }

    }


    public void render(SpriteBatch sb) {
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
