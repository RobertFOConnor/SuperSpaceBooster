package com.yellowbytestudios.spacedoctor.mapeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.cameras.OrthoCamera;
import com.yellowbytestudios.spacedoctor.game.Button;
import com.yellowbytestudios.spacedoctor.media.MapEditorAssets;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.MainMenuScreen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;

public class EditorGUI {

    private OrthoCamera camera;
    private Vector2 touch;
    private MapManager mapManager;
    private Button zoomIn, zoomOut, moveButton, eraseButton, playMap, saveMap, exitButton;

    private Texture tileButtonSelector;
    private TextureRegion tileset;
    private NinePatch bottom_bg;
    private int tileID = -1;

    private ItemSideMenu sideMenu;

    public EditorGUI(MapManager mapManager) {
        this.mapManager = mapManager;
        camera = new OrthoCamera();
        camera.resize();
        touch = new Vector2();

        zoomIn = new Button(MapEditorAssets.ZOOM_IN, new Vector2(MainGame.WIDTH - 170, MainGame.HEIGHT - 170), true);
        zoomOut = new Button(MapEditorAssets.ZOOM_OUT, new Vector2(MainGame.WIDTH - 170, MainGame.HEIGHT - 285), true);
        moveButton = new Button(MapEditorAssets.manager.get(MapEditorAssets.MOVE_BUTTON, Texture.class), MapEditorAssets.manager.get(MapEditorAssets.MOVE_BUTTON_SEL, Texture.class), new Vector2(20, 20));
        eraseButton = new Button(MapEditorAssets.manager.get(MapEditorAssets.ERASE, Texture.class), MapEditorAssets.manager.get(MapEditorAssets.ERASE_SEL, Texture.class), new Vector2(180, 20));
        playMap = new Button(MapEditorAssets.PLAY_MAP, new Vector2(MainGame.WIDTH - 280 - 140, 20), true);
        saveMap = new Button(MapEditorAssets.SAVE_MAP, new Vector2(MainGame.WIDTH - 280, 20), true);
        exitButton = new Button(MapEditorAssets.EXIT_EDITOR, new Vector2(MainGame.WIDTH - 140, 20), true);

        tileButtonSelector = MapEditorAssets.manager.get(MapEditorAssets.TILE_SELECTOR, Texture.class);
        tileset = new TextureRegion(MapEditorAssets.manager.get(MapEditorAssets.TILESHEET, Texture.class));
        bottom_bg = new NinePatch(MapEditorAssets.manager.get(MapEditorAssets.BOTTOM_BAR, Texture.class), 10, 10, 10, 10);


        sideMenu = new ItemSideMenu();
    }

    public void update(float step) {
        if (Gdx.input.isTouched()) {
            touch = camera.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (zoomIn.checkTouch(touch)) { //ZOOM IN
                mapManager.zoomIn();

            } else if (zoomOut.checkTouch(touch)) { //ZOOM OUT
                mapManager.zoomOut();

            } else if (playMap.checkTouch(touch)) { //TEST PLAY MAP
                ScreenManager.setScreen(new GameScreen(mapManager.getMap()));

            } else if (saveMap.checkTouch(touch) && Gdx.input.justTouched()) { //SAVE MAP
                saveMap();

            } else if (exitButton.checkTouch(touch)) { //EXIT EDITOR
                ScreenManager.setScreen(new MainMenuScreen());
                MapEditorAssets.dispose();

            } else if (!moveButton.checkTouch(touch) && (!sideMenu.checkTouch() && touch.y > 160)) {
                //CHECK MAP FOR INTERACTION.

                if (eraseButton.isPressed()) {
                    mapManager.eraseTiles();
                }

                if (!mapManager.isHoldingObject()) {
                    if (moveButton.isPressed()) {
                        mapManager.dragMap();
                    } else if (!eraseButton.isPressed() && sideMenu.state.equals(sideMenu.BLOCK_STATE)) {
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
                    sideMenu.setShowing(true);
                } else {
                    moveButton.setPressed(true);
                    eraseButton.setPressed(false);
                    sideMenu.setShowing(false);
                }
            }

            if (eraseButton.checkTouch(touch)) {
                if (eraseButton.isPressed()) {
                    eraseButton.setPressed(false);
                } else {
                    eraseButton.setPressed(true);
                    moveButton.setPressed(false);
                    sideMenu.setShowing(true);
                }
            }
        }
    }

    private void saveMap() {
        MyTextInputListener listener = new MyTextInputListener();
        Gdx.input.getTextInput(listener, "Save Map", "", "Enter Map Name");
    }

    public void render(SpriteBatch sb) {
        zoomIn.render(sb);
        zoomOut.render(sb);

        bottom_bg.draw(sb, 0, 0, MainGame.WIDTH, 160);
        sideMenu.render(sb);
        moveButton.render(sb);
        eraseButton.render(sb);
        playMap.render(sb);
        saveMap.render(sb);
        exitButton.render(sb);
    }

    private class TileButton extends SpriteButton {

        private TextureRegion texture;
        private int id;
        private boolean selected = false;

        public TileButton(TextureRegion texture, Vector2 pos, int id) {
            super(texture, pos);
            this.texture = texture;
            this.id = id;
        }

        public void render(SpriteBatch sb) {
            if (selected) {
                sb.draw(tileButtonSelector, getX() - 10, getY() - 10);
            }
            sb.draw(texture, getX(), getY());
        }

        public boolean checkTouch(Vector2 touch) {
            return getBounds().contains(touch);
        }
    }

    private class EnemyButton extends SpriteButton {

        private int id;
        private boolean selected = false;

        public EnemyButton(String texture, Vector2 pos, int id) {
            super(texture, pos, true);
            this.id = id;
        }

        public void render(SpriteBatch sb) {
            if (selected) {
                sb.draw(tileButtonSelector, getX() - 10, getY() - 10);
            }
            sb.draw(getTexture(), getX(), getY());
        }

        public boolean checkTouch(Vector2 touch) {
            return getBounds().contains(touch);
        }
    }

    private class ItemSideMenu {

        //Menu States.
        private final String BLOCK_STATE = "BLOCK_STATE";
        private final String ENEMY_STATE = "ENEMY_STATE";
        private final String ITEM_STATE = "ITEM_STATE";

        private String state = BLOCK_STATE;

        private boolean showing = true;

        private Button blockTab, enemyTab;

        private Array<TileButton> tileButtons;
        private Array<EnemyButton> enemyButtons;
        private NinePatch sideMenuBG = new NinePatch(MapEditorAssets.manager.get(MapEditorAssets.SIDE_MENU, Texture.class), 10, 10, 10, 10);

        private float bottomY = 290;

        public ItemSideMenu() {

            blockTab = new Button(MapEditorAssets.BLOCK_TAB, new Vector2(270, 830), true);
            enemyTab = new Button(MapEditorAssets.ENEMY_TAB, new Vector2(270, 710), true);

            enemyButtons = new Array<EnemyButton>();
            enemyButtons.add(new EnemyButton(MapEditorAssets.ENEMY_ICON, new Vector2(30, bottomY), 0));

            tileButtons = new Array<TileButton>();

            addTileButton(0, 0, TileIDs.LIGHT_PURPLE);
            addTileButton(200, 100, TileIDs.DARK_PURPLE);
            addTileButton(100, 0, TileIDs.CAGED_WALL);
            addTileButton(200, 0, TileIDs.DOWN_SPIKE);
            addTileButton(300, 200, TileIDs.UP_SPIKE);
            addTileButton(300, 100, TileIDs.RIGHT_SPIKE);
            addTileButton(400, 100, TileIDs.LEFT_SPIKE);

            tileButtons.get(0).selected = true;
            tileID = tileButtons.get(0).id;
        }

        public boolean checkTouch() {

            if (showing) {

                if (blockTab.checkTouch(touch)) {
                    state = BLOCK_STATE;
                    eraseButton.setPressed(false);
                } else if (enemyTab.checkTouch(touch)) {
                    state = ENEMY_STATE;
                    eraseButton.setPressed(false);
                }


                if (state.equals(BLOCK_STATE)) {
                    for (TileButton tb : tileButtons) {
                        if (tb.checkTouch(touch)) {

                            for (TileButton untb : tileButtons) {
                                untb.selected = false;
                            }
                            eraseButton.setPressed(false);
                            tb.selected = true;
                            tileID = tb.id;
                            return true;
                        }
                    }
                } else if (state.equals(ENEMY_STATE)) {
                    for (EnemyButton eb : enemyButtons) {
                        if (eb.checkTouch(touch)) {

                            if (Gdx.input.justTouched()) {
                                mapManager.addEnemy();
                            }
                        }
                    }
                }

                return touch.x < 260;
            }
            return false;
        }

        public void render(SpriteBatch sb) {
            if (showing) {

                sideMenuBG.draw(sb, 10, 200, 260, 760);
                blockTab.render(sb);
                enemyTab.render(sb);


                if (state.equals(BLOCK_STATE)) {
                    for (TileButton tb : tileButtons) {
                        tb.render(sb);
                    }
                } else if (state.equals(ENEMY_STATE)) {

                    //DRAW ENEMIES
                    for (EnemyButton eb : enemyButtons) {
                        eb.render(sb);
                    }

                } else if (state.equals(ITEM_STATE)) {

                    //DRAW ITEMS

                }
            }
        }

        public void setShowing(boolean showing) {
            this.showing = showing;
        }

        private void addTileButton(int sheetX, int sheetY, int tileID) {

            if (tileButtons.size % 2 == 0 && tileButtons.size != 0) {
                bottomY += 120;
            }

            tileButtons.add(new TileButton(new TextureRegion(tileset, sheetX, sheetY, (int) Box2DVars.PPM, (int) Box2DVars.PPM), new Vector2(30 + (120 * (tileButtons.size % 2)), bottomY), tileID));
        }
    }

    public class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input (String text) {

            if(MainGame.saveData.getMyMaps().size < 12) {
                MainGame.saveData.getMyMaps().add(new CustomMap(text, mapManager.getMap()));
            } else {
                MainGame.saveData.getMyMaps().set(11, new CustomMap(text, mapManager.getMap()));
            }
            MainGame.saveManager.saveDataValue("PLAYER", MainGame.saveData);

            //MAP SAVED MESSAGE DISPLAY HERE!!!!
        }

        @Override
        public void canceled () {
        }
    }
}
