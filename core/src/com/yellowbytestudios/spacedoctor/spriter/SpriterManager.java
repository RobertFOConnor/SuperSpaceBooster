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

    private Data data, cutsceneData;
    private Drawer<Sprite> drawer, cutSceneDrawer;

    public SpriterManager(SpriteBatch sb) {
        ShapeRenderer renderer = new ShapeRenderer();
        FileHandle handle = Gdx.files.internal("spaceman/spriter_project.scml");
        data = new SCMLReader(handle.read()).getData();

        LibGdxLoader loader = new LibGdxLoader(data);
        loader.load(handle.file());

        drawer = new LibGdxDrawer(loader, sb, renderer);


        //CUTSCENES
        handle = Gdx.files.internal("cutscenes/cutscenes.scml");
        cutsceneData = new SCMLReader(handle.read()).getData();

        loader = new LibGdxLoader(cutsceneData);
        loader.load(handle.file());

        cutSceneDrawer = new LibGdxDrawer(loader, sb, renderer);
    }

    public Player getCutscene(int sceneNum) {
        Player player = new Player(cutsceneData.getEntity("entity_000"));
        player.setAnimation("scene_" + sceneNum);
        return player;
    }

    public void showScene(Player p) {
        p.update();
        cutSceneDrawer.draw(p);
    }

    public void draw(Player p) {
        drawer.draw(p);
    }


    public Player getSpiter(String entityName, String animationName, float scale) {
        Player player = new Player(data.getEntity(entityName));
        player.setScale(scale);
        player.setAnimation(animationName);
        return player;
    }
}
