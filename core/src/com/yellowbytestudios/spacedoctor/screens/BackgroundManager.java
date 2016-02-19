package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.media.Assets;

/**
 * Created by BobbyBoy on 19-Feb-16.
 */
public class BackgroundManager {

    private Texture sky, stars1, stars2;
    private static float stars1X = 0;
    private static float stars2X = 0;

    public BackgroundManager() {
        sky = Assets.manager.get(Assets.MENU_BG, Texture.class);
        stars1 = Assets.manager.get(Assets.STARS1, Texture.class);
        stars2 = Assets.manager.get(Assets.STARS2, Texture.class);
    }

    public void update() {
        stars1X = stars1X - 4;

        if (stars1X < -MainGame.WIDTH) {
            stars1X = 0;
        }

        stars2X = stars2X - 2;

        if (stars2X < -MainGame.WIDTH) {
            stars2X = 0;
        }
    }

    public void render(SpriteBatch sb) {
        sb.draw(sky, 0, 0);
        sb.draw(stars1, stars1X, 0);
        sb.draw(stars1, stars1X + MainGame.WIDTH, 0);
        sb.draw(stars2, stars2X, 0);
        sb.draw(stars2, stars2X + MainGame.WIDTH, 0);
    }
}
