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
        Player player = new Player(data.getEntity("player"));
        player.setScale(0.6f);
        player.setAnimation("idle");
        return player;
    }

    public Player initDemon() {
        Player player = new Player(data.getEntity("platty"));
        player.setScale(0.8f);
        player.setAnimation("walking");
        return player;
    }

    public Player initGasPickUp() {
        Player player = new Player(data.getEntity("gas_pickup"));
        player.setScale(0.85f);
        player.setAnimation("default");
        return player;
    }

    public Player initAmmoPickUp() {
        Player player = new Player(data.getEntity("ammo_pickup"));
        player.setScale(0.85f);
        player.setAnimation("default");
        return player;
    }

    public Player initTimePickUp() {
        Player player = new Player(data.getEntity("time_pickup"));
        player.setScale(0.85f);
        player.setAnimation("default");
        return player;
    }

    public Player initExit() {
        Player player = new Player(data.getEntity("exit"));
        player.setScale(0.85f);
        player.setAnimation("default");
        return player;
    }

    public Player initSelector() {
        Player player = new Player(data.getEntity("selector"));
        player.setScale(1.6f);
        player.setAnimation("default");
        return player;
    }
}
