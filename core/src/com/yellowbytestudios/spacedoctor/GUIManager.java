package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by BobbyBoy on 10-Jan-16.
 */
public class GUIManager {

    private OrthoCamera camera;
    private SpacemanPlayer player;


    public GUIManager(SpacemanPlayer player) {
        camera = new OrthoCamera();
        camera.resize();
        this.player = player;
    }

    public void update() {

    }

    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        Fonts.GUIFont.draw(sb, "HEALTH", 50, MainGame.HEIGHT - 50);
        Fonts.GUIFont.draw(sb, player.getHealth() + "%", 50, MainGame.HEIGHT - 100);

        Fonts.GUIFont.draw(sb, "JETPACK", 300, MainGame.HEIGHT - 50);

        if(player.getCurrGas() < 100) {
            Fonts.GUIFont.setColor(Color.RED);
        }
        Fonts.GUIFont.draw(sb, (int) player.getCurrGas() / 10 + "%", 300, MainGame.HEIGHT - 100);
        Fonts.GUIFont.setColor(Color.WHITE);

        sb.end();
    }
}
