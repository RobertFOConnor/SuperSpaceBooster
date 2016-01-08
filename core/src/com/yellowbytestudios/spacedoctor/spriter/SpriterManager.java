package com.yellowbytestudios.spacedoctor.spriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Drawer;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.SCMLReader;

/**
 * Created by BobbyBoy on 22-Nov-15.
 */
public class SpriterManager {

    private Data data;
    private ShapeRenderer renderer;
    private LibGdxLoader loader;
    private Drawer<Sprite> drawer;

    public SpriterManager(SpriteBatch sb) {
        renderer = new ShapeRenderer();
        FileHandle handle = Gdx.files.internal("spaceman/spriter_project.scml");
        data = new SCMLReader(handle.read()).getData();

        loader = new LibGdxLoader(data);
        loader.load(handle.file());

        drawer = new LibGdxDrawer(loader, sb, renderer);
    }

    public void draw(Player p) {
        p.update();
        drawer.draw(p);
    }

    public Player initPlayer() {
        Player player = new Player(data.getEntity(0));
        player.setScale(0.6f);
        player.setAnimation("idle");
        return player;
    }
}
