package com.yellowbytestudios.spacedoctor.mapeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.objects.Button;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;

/**
 * Created by BobbyBoy on 25-Jan-16.
 */
public class EditorGUI {

    private OrthoCamera camera;
    private Vector2 touch;
    private MapManager mapManager;
    private Button zoomIn, zoomOut, moveButton, eraseButton, playMap;

    //TILE TYPE BUTTONS
    private Texture tileButtonSelector = new Texture(Gdx.files.internal("mapeditor/tile_buttons_selector.png"));
    private Texture tileButtonBG;
    private TextureRegion tileset;
    private Array<TileButton> tileButtons;
    private int tileID = -1;

    private boolean buttonSelected = false;


    public EditorGUI(MapManager mapManager) {
        this.mapManager = mapManager;
        camera = new OrthoCamera();
        camera.resize();

        zoomIn = new Button(Assets.ZOOM_IN, new Vector2(MainGame.WIDTH - 170, MainGame.HEIGHT - 170));
        zoomOut = new Button(Assets.ZOOM_OUT, new Vector2(MainGame.WIDTH - 170, MainGame.HEIGHT -360));
        moveButton = new Button(Assets.manager.get(Assets.MOVE_BUTTON, Texture.class), Assets.manager.get(Assets.MOVE_BUTTON_SEL, Texture.class), new Vector2(30, 30));
        eraseButton = new Button(Assets.manager.get(Assets.ERASE, Texture.class), Assets.manager.get(Assets.ERASE_SEL, Texture.class), new Vector2(30 + 140 + 30, 30));
        playMap = new Button(Assets.PLAY_MAP, new Vector2(MainGame.WIDTH - 170, 30));


        //Tile buttons
        tileButtonBG = new Texture(Gdx.files.internal("mapeditor/tile_buttons_bg.png"));
        tileset = new TextureRegion(new Texture(Gdx.files.internal("maps/tileset.png")));
        tileButtons = new Array<TileButton>();


        addTileButton(0, 0, 0);
        addTileButton(200, 100, 7);
        addTileButton(200, 0, TileIDs.DOWN_SPIKE);
        addTileButton(300, 200, TileIDs.UP_SPIKE);
        addTileButton(300, 100, TileIDs.RIGHT_SPIKE);
        addTileButton(400, 100, TileIDs.LEFT_SPIKE);
    }

    private void addTileButton(int sheetX, int sheetY, int tileID) {
        int PPM = (int) Box2DVars.PPM;
        float listX = 320;

        tileButtons.add(new TileButton(new TextureRegion(tileset, sheetX, sheetY, PPM, PPM), new Vector2(listX + (120 * tileButtons.size), MainGame.HEIGHT - 120), tileID));
    }

    public void update(float step) {
        if (Gdx.input.isTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (zoomIn.checkTouch(touch)) {
                mapManager.zoomIn(step);
            } else if (zoomOut.checkTouch(touch)) {
                mapManager.zoomOut(step);
            } else if (playMap.checkTouch(touch)) {
                ScreenManager.setScreen(new GameScreen(mapManager.getMap()));

            } else if (!moveButton.checkTouch(touch)) {
                //CHECK MAP FOR INTERACTION.

                buttonSelected = false;
                for (TileButton tb : tileButtons) {
                    if (tb.checkTouch(touch)) {

                        for (TileButton untb : tileButtons) {
                            untb.selected = false;
                        }
                        tb.selected = true;
                        tileID = tb.id;
                        buttonSelected = true;
                        break;
                    }
                }

                if (!buttonSelected && !mapManager.getExit().isSelected() && !mapManager.getPlayerSpawn().isSelected()) {
                    if (moveButton.isPressed()) {
                        mapManager.dragMap();
                    } else if (eraseButton.isPressed()) {
                        mapManager.eraseTiles();
                    } else {
                        mapManager.checkForTilePlacement(tileID);
                    }
                }
            }
        }


        if (Gdx.input.justTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (moveButton.checkTouch(touch)) {
                if (moveButton.isPressed()) {
                    moveButton.setPressed(false);
                } else {
                    moveButton.setPressed(true);
                    eraseButton.setPressed(false);
                }
            }

            if (eraseButton.checkTouch(touch)) {
                if (eraseButton.isPressed()) {
                    eraseButton.setPressed(false);
                } else {
                    eraseButton.setPressed(true);
                    moveButton.setPressed(false);
                }
            }
        }
    }

    public void render(SpriteBatch sb) {
        zoomIn.render(sb);
        zoomOut.render(sb);
        moveButton.render(sb);
        eraseButton.render(sb);
        playMap.render(sb);

        sb.draw(tileButtonBG, 300, MainGame.HEIGHT - 140);
        for (TileButton tb : tileButtons) {
            tb.render(sb);
        }
    }

    private class TileButton {
        private TextureRegion texture;
        private Vector2 pos;

        private int id;
        private boolean selected = false;

        public TileButton(TextureRegion texture, Vector2 pos, int id) {
            this.texture = texture;
            this.pos = pos;
            this.id = id;
        }

        public void render(SpriteBatch sb) {
            if (selected) {
                sb.draw(tileButtonSelector, pos.x - 10, pos.y - 10);
            }
            sb.draw(texture, pos.x, pos.y);
        }

        public Rectangle getBounds() {
            return new Rectangle(pos.x, pos.y, texture.getRegionWidth(), texture.getRegionHeight());
        }

        public boolean checkTouch(Vector2 touch) {
            return getBounds().contains(touch);
        }
    }
}
