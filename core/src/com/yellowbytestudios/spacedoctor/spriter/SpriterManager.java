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

public class SpriterManager {

    private Data data;
    private Drawer<Sprite> drawer;

    public SpriterManager(SpriteBatch sb) {
        ShapeRenderer renderer = new ShapeRenderer();
        FileHandle handle = Gdx.files.internal("spaceman/spriter_project.scml");
        data = new SCMLReader(handle.read()).getData();

        LibGdxLoader loader = new LibGdxLoader(data);
        loader.load(handle.file());

        drawer = new LibGdxDrawer(loader, sb, renderer);
    }

    public void draw(Player p) {
        drawer.draw(p);
    }

    public Player initPlayer() {
        Player player = new Player(data.getEntity(0));
        player.setScale(0.6f);
        player.setAnimation("idle");
        return player;
    }

    public Player initDemon() {
        Player player = new Player(data.getEntity(1));
        player.setScale(0.6f);
        player.setAnimation("walking");
        return player;
    }

    public Player initGasPickUp() {
        Player player = new Player(data.getEntity(2));
        player.setScale(0.85f);
        player.setAnimation("default");
        return player;
    }

    public Player initAmmoPickUp() {
        Player player = new Player(data.getEntity(3));
        player.setScale(0.85f);
        player.setAnimation("default");
        return player;
    }

    public Player initTimePickUp() {
        Player player = new Player(data.getEntity(4));
        player.setScale(0.85f);
        player.setAnimation("default");
        return player;
    }

    public Player initExit() {
        Player player = new Player(data.getEntity(5));
        player.setScale(0.85f);
        player.setAnimation("default");
        return player;
    }

    public Player initSelector() {
        Player player = new Player(data.getEntity(6));
        player.setScale(1.6f);
        player.setAnimation("default");
        return player;
    }
}
