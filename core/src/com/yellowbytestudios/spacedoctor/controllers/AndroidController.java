package com.yellowbytestudios.spacedoctor.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.objects.Button;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.LevelSelectScreen;
import com.yellowbytestudios.spacedoctor.screens.MainMenuScreen;
import com.yellowbytestudios.spacedoctor.screens.MapEditorScreen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;

/**
 * Created by BobbyBoy on 20-Jan-16.
 */
public class AndroidController implements BasicController {

    private OrthoCamera camera;
    private Button left, right, up, shoot, mapEditor, exit;
    private Vector2 touch1, touch2;

    public AndroidController() {
        camera = new OrthoCamera();
        camera.resize();


        left = new Button(Assets.manager.get(Assets.LEFT, Texture.class), Assets.manager.get(Assets.LEFT_PRESSED, Texture.class), new Vector2(30, 30));
        right = new Button(Assets.manager.get(Assets.RIGHT, Texture.class), Assets.manager.get(Assets.RIGHT_PRESSED, Texture.class), new Vector2(250, 30));
        up = new Button(Assets.manager.get(Assets.UP, Texture.class), Assets.manager.get(Assets.UP_PRESSED, Texture.class), new Vector2(MainGame.WIDTH - 330, 30));
        shoot = new Button(Assets.manager.get(Assets.SHOOT, Texture.class), Assets.manager.get(Assets.SHOOT_PRESSED, Texture.class), new Vector2(MainGame.WIDTH - 330 - 280, 30));
        mapEditor = new Button(new Texture(Gdx.files.internal("mapeditor/map_editor.png")), new Texture(Gdx.files.internal("mapeditor/map_editor.png")), new Vector2(MainGame.WIDTH - 170, MainGame.HEIGHT - 170));

        exit = new Button(Assets.manager.get(Assets.EXIT, Texture.class), Assets.manager.get(Assets.EXIT, Texture.class), new Vector2(MainGame.WIDTH-80, MainGame.HEIGHT-80));


        touch1 = new Vector2();
        touch2 = new Vector2();
    }

    public void update() {
        if (Gdx.input.isTouched(0)) {
            touch1 = camera.unprojectCoordinates(Gdx.input.getX(0),
                    Gdx.input.getY(0));

        } else {
            touch1.set(0, 0);
        }
        if (Gdx.input.isTouched(1)) {
            touch2 = camera.unprojectCoordinates(Gdx.input.getX(1),
                    Gdx.input.getY(1));
        } else {
            touch2.set(0, 0);
        }
    }

    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();

        left.render(sb);
        right.render(sb);
        up.render(sb);
        shoot.render(sb);

        if (GameScreen.isCustomMap) { // RETURN TO MAP EDITOR.
            if (mapEditor.checkTouch(touch1) || mapEditor.checkTouch(touch2)) {
                ScreenManager.setScreen(new MapEditorScreen(GameScreen.customMap));
            }
            mapEditor.render(sb);
        } else {
            if(exit.checkTouch(touch1) || exit.checkTouch(touch2)) {
                ScreenManager.setScreen(new LevelSelectScreen());
            }
            exit.render(sb);
        }

        sb.end();
    }

    @Override
    public boolean leftPressed() {
        if (Gdx.input.isTouched() && (left.checkTouch(touch1) || left.checkTouch(touch2))) {
            left.setPressed(true);
            return true;
        } else {
            left.setPressed(false);
        }
        return false;
    }

    @Override
    public boolean rightPressed() {
        if (Gdx.input.isTouched() && (right.checkTouch(touch1) || right.checkTouch(touch2))) {
            right.setPressed(true);
            return true;
        } else {
            right.setPressed(false);
        }
        return false;
    }

    @Override
    public boolean upPressed() {
        if (Gdx.input.isTouched() && (up.checkTouch(touch1) || up.checkTouch(touch2))) {
            up.setPressed(true);
            return true;
        } else {
            up.setPressed(false);
        }
        return false;
    }


    @Override
    public boolean downPressed() {
        return false;
    }

    @Override
    public boolean shootPressed() {
        if (Gdx.input.justTouched() && (shoot.checkTouch(touch1) || shoot.checkTouch(touch2))) {
            shoot.setPressed(true);
            return true;
        } else {
            shoot.setPressed(false);
        }
        return false;
    }


    @Override
    public boolean pausePressed() {
        return false;
    }
}
