package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by BobbyBoy on 24-Jan-16.
 */
public class BackgroundManager {

    public BackgroundManager() {

    }



    private class BackgroundLayer {

        private Array<BackgroundObject> bgObjects;
        private float speed;

        public BackgroundLayer(float speed) {
            this.speed = speed;
        }

        public void update() {

        }

        public void render(SpriteBatch sb) {
            for(BackgroundObject bo : bgObjects) {
                bo.render(sb);
            }
        }

    }


    private class BackgroundObject {

        private Texture texture;
        private Vector2 pos;

        public BackgroundObject(Texture texture, Vector2 pos) {
            this.texture = texture;
            this.pos = pos;
        }

        public void update() {

        }

        public void render(SpriteBatch sb) {
            sb.draw(texture, pos.x, pos.y);
        }
    }

}
