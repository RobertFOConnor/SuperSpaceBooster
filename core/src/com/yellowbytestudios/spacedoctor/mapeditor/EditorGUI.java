package com.yellowbytestudios.spacedoctor.mapeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.game.Button;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.CoreLevelSaver;
import com.yellowbytestudios.spacedoctor.media.MapEditorAssets;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.screens.ScreenManager;
import com.yellowbytestudios.spacedoctor.screens.menu.MainMenuScreen;
import com.yellowbytestudios.spacedoctor.tween.SpriteButton;
import com.yellowbytestudios.spacedoctor.utils.Metrics;

public class EditorGUI {

    private OrthographicCamera camera;
    private FitViewport viewport;
    private MapManager mapManager;
    private Button zoomIn, zoomOut, moveButton, eraseButton, playMap, saveMap, exitButton;

    private Texture tileButtonSelector;
    private NinePatch bottom_bg;
    private int tileID = -1;
    private int enemyID = -1;
    private int itemID = -1;
    private int obstacleID = -1;

    private ItemSideMenu sideMenu;
    private Array<MenuButton> menuButtons;

    private MapNameInput mapNameInput;

    public EditorGUI(MapManager mapManager) {
        this.mapManager = mapManager;
        camera = new OrthographicCamera();
        viewport = new FitViewport(Metrics.WIDTH, Metrics.HEIGHT, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        zoomIn = new Button(MapEditorAssets.ZOOM_IN, new Vector2(Metrics.WIDTH - 170, Metrics.HEIGHT - 170), true);
        zoomOut = new Button(MapEditorAssets.ZOOM_OUT, new Vector2(Metrics.WIDTH - 170, Metrics.HEIGHT - 285), true);
        moveButton = new Button(MapEditorAssets.manager.get(MapEditorAssets.MOVE_BUTTON, Texture.class), MapEditorAssets.manager.get(MapEditorAssets.MOVE_BUTTON_SEL, Texture.class), new Vector2(20, 20));
        eraseButton = new Button(MapEditorAssets.manager.get(MapEditorAssets.ERASE, Texture.class), MapEditorAssets.manager.get(MapEditorAssets.ERASE_SEL, Texture.class), new Vector2(180, 20));
        playMap = new Button(MapEditorAssets.PLAY_MAP, new Vector2(Metrics.WIDTH - 280 - 140, 20), true);
        saveMap = new Button(MapEditorAssets.SAVE_MAP, new Vector2(Metrics.WIDTH - 280, 20), true);
        exitButton = new Button(MapEditorAssets.EXIT_EDITOR, new Vector2(Metrics.WIDTH - 140, 20), true);

        tileButtonSelector = MapEditorAssets.manager.get(MapEditorAssets.TILE_SELECTOR, Texture.class);

        bottom_bg = new NinePatch(MapEditorAssets.manager.get(MapEditorAssets.BOTTOM_BAR, Texture.class), 10, 10, 10, 10);


        sideMenu = new ItemSideMenu();
        menuButtons = new Array<MenuButton>();

        mapNameInput = new MapNameInput();
    }

    public Vector2 getTouchPos() {
        Vector3 rawtouch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(rawtouch);
        return new Vector2(rawtouch.x, rawtouch.y);
    }

    public void resize(int w, int h) {
        viewport.update(w, h);
        camera.update();
    }

    public void update(float step) {
        if (!mapNameInput.isShowing()) {

            if (Gdx.input.isTouched()) {
                Vector2 touch = getTouchPos();

                if (zoomIn.checkTouch(touch)) { //ZOOM IN
                    mapManager.zoomIn();

                } else if (zoomOut.checkTouch(touch)) { //ZOOM OUT
                    mapManager.zoomOut();

                } else if (playMap.checkTouch(touch)) { //TEST PLAY MAP
                    SoundManager.play(Assets.BUTTON_CLICK);
                    ScreenManager.setScreen(new GameScreen(mapManager.getCustomMap("test")));

                } else if (saveMap.checkTouch(touch) && Gdx.input.justTouched()) { //SAVE MAP
                    SoundManager.play(Assets.BUTTON_CLICK);
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
                        } else if (!eraseButton.isPressed()) {
                            if (sideMenu.state.equals(sideMenu.BLOCK_STATE)) {
                                mapManager.checkForTilePlacement(tileID);
                            } else if (sideMenu.state.equals(sideMenu.ENEMY_STATE)) {
                                mapManager.addEnemy(enemyID);
                            } else if (sideMenu.state.equals(sideMenu.ITEM_STATE)) {
                                mapManager.addItem(itemID);
                            } else if (sideMenu.state.equals(sideMenu.OBSTACLE_STATE)) {
                                mapManager.addObstacle(obstacleID);
                            }
                        }
                    }
                }
            }


            if (Gdx.input.justTouched()) {
                Vector2 touch = getTouchPos();

                if (moveButton.checkTouch(touch)) {
                    SoundManager.play(Assets.BUTTON_CLICK);
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
                    SoundManager.play(Assets.BUTTON_CLICK);
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
    }

    private void saveMap() {
        mapNameInput.setShowing(true);
    }

    public void render(SpriteBatch sb) {
        zoomIn.render(sb);
        zoomOut.render(sb);

        bottom_bg.draw(sb, 0, 0, Metrics.WIDTH, 160);
        sideMenu.render(sb);
        moveButton.render(sb);
        eraseButton.render(sb);
        playMap.render(sb);
        saveMap.render(sb);
        exitButton.render(sb);
        mapNameInput.render(sb);
    }

    private class MapNameInput {

        private Skin skin;
        private Stage stage;
        private boolean showing = false;

        public MapNameInput() {
            skin = Assets.manager.get(Assets.SKIN, Skin.class);
            stage = new Stage();

            final TextField mapName = new TextField("", skin);
            mapName.setWidth(500f);
            mapName.setHeight(100f);
            mapName.setMaxLength(30);
            mapName.setMessageText("Enter Map Name");
            mapName.setPosition(Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2);
            stage.addActor(mapName);

            final TextButton saveButton = new TextButton("Save", skin, "default");
            saveButton.setWidth(200f);
            saveButton.setHeight(100f);
            saveButton.setPosition(Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2 - 120);

            saveButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    SoundManager.play(Assets.BUTTON_CLICK);
                    saveMap(mapName.getText());
                    setShowing(false);
                }
            });
            stage.addActor(saveButton);

            final TextButton cancelButton = new TextButton("Cancel", skin, "default");
            cancelButton.setWidth(200f);
            cancelButton.setHeight(100f);
            cancelButton.setPosition(Gdx.graphics.getWidth() / 2 + 50, Gdx.graphics.getHeight() / 2 - 120);

            cancelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    SoundManager.play(Assets.BUTTON_CLICK);
                    setShowing(false);
                }
            });
            stage.addActor(cancelButton);
        }

        public void render(SpriteBatch sb) {
            if (showing) {
                sb.draw(Assets.manager.get(Assets.ALPHA, Texture.class), 0, 0, Metrics.WIDTH, Metrics.HEIGHT);
                sb.end();
                sb.begin();
                stage.draw();
            }
        }

        public boolean isShowing() {
            return showing;
        }

        public void setShowing(boolean showing) {
            this.showing = showing;

            if (showing) {
                Gdx.input.setInputProcessor(stage);
            } else {
                Gdx.input.setInputProcessor(null);
            }
        }
    }


    private class MenuButton extends SpriteButton {

        private TextureRegion texture;
        private int id;
        private boolean selected = false;

        public MenuButton(TextureRegion texture, Vector2 pos, int id) {
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


    private class ItemSideMenu {

        //Menu States.
        private final String BLOCK_STATE = "BLOCK_STATE";
        private final String ENEMY_STATE = "ENEMY_STATE";
        private final String ITEM_STATE = "ITEM_STATE";
        private final String OBSTACLE_STATE = "OBSTACLE_STATE";

        private String state = BLOCK_STATE;

        private boolean showing = true;

        private Button blockTab, enemyTab, itemTab, obstacleTab;

        private Array<MenuButton> tileButtons, enemyButtons, itemButtons, obstacleButtons;
        private NinePatch sideMenuBG = new NinePatch(MapEditorAssets.manager.get(MapEditorAssets.SIDE_MENU, Texture.class), 10, 10, 10, 10);

        private float bottomY = 290;

        //Side Menu Constructor.
        public ItemSideMenu() {

            blockTab = new Button(MapEditorAssets.BLOCK_TAB, new Vector2(270, 830), true);
            enemyTab = new Button(MapEditorAssets.ENEMY_TAB, new Vector2(270, 710), true);
            itemTab = new Button(MapEditorAssets.ITEM_TAB, new Vector2(270, 590), true);
            obstacleTab = new Button(MapEditorAssets.OBSTACLE_TAB, new Vector2(270, 470), true);


            tileButtons = new Array<MenuButton>();

            //Setup TILES menu.
            Texture sheet = MapEditorAssets.manager.get(MapEditorAssets.TILESHEET, Texture.class);
            addMenuButton(tileButtons, sheet, 0, 0, TileIDs.LIGHT_PURPLE);
            addMenuButton(tileButtons, sheet, 200, 100, TileIDs.DARK_PURPLE);
            addMenuButton(tileButtons, sheet, 100, 0, TileIDs.CAGED_WALL);
            addMenuButton(tileButtons, sheet, 200, 0, TileIDs.DOWN_SPIKE);
            addMenuButton(tileButtons, sheet, 300, 200, TileIDs.UP_SPIKE);
            addMenuButton(tileButtons, sheet, 300, 100, TileIDs.RIGHT_SPIKE);
            addMenuButton(tileButtons, sheet, 400, 100, TileIDs.LEFT_SPIKE);

            tileButtons.get(0).selected = true;
            tileID = tileButtons.get(0).id;


            //Setup ENEMY menu.
            bottomY = 290;
            enemyButtons = new Array<MenuButton>();
            addMenuButton(enemyButtons, MapEditorAssets.manager.get(MapEditorAssets.EYE_GUY_ICON, Texture.class), 0, 0, IDs.EYEGUY);
            addMenuButton(enemyButtons, MapEditorAssets.manager.get(MapEditorAssets.PLATTY_ICON, Texture.class), 0, 0, IDs.PLATTY);
            enemyButtons.get(0).selected = true;
            enemyID = enemyButtons.get(0).id;

            //Setup PICK-UP menu.
            sheet = MapEditorAssets.manager.get(MapEditorAssets.ITEM_SHEET, Texture.class);
            bottomY = 290;
            itemButtons = new Array<MenuButton>();
            addMenuButton(itemButtons, sheet, 0, 0, IDs.GAS);
            addMenuButton(itemButtons, sheet, 100, 0, IDs.AMMO);
            addMenuButton(itemButtons, sheet, 200, 0, IDs.CLOCK);
            addMenuButton(itemButtons, sheet, 300, 0, IDs.COIN);

            itemButtons.get(0).selected = true;
            itemID = itemButtons.get(0).id;

            bottomY = 290;
            obstacleButtons = new Array<MenuButton>();
            addMenuButton(obstacleButtons, MapEditorAssets.manager.get(MapEditorAssets.PLATFORM_ICON, Texture.class), 0, 0, IDs.HORIZONTAL_SPIKER);
            addMenuButton(obstacleButtons, MapEditorAssets.manager.get(MapEditorAssets.PLATFORM_ICON_VER, Texture.class), 0, 0, IDs.VERTICAL_SPIKER);
            addMenuButton(obstacleButtons, MapEditorAssets.manager.get(MapEditorAssets.BOX, Texture.class), 0, 0, IDs.BOX);


            obstacleButtons.get(0).selected = true;
            obstacleID = obstacleButtons.get(0).id;

            menuButtons = tileButtons;
        }

        public boolean checkTouch() {

            if (showing) {
                Vector2 touch = getTouchPos();
                if (blockTab.checkTouch(touch)) {
                    switchTab(BLOCK_STATE, tileButtons);
                } else if (enemyTab.checkTouch(touch)) {
                    switchTab(ENEMY_STATE, enemyButtons);
                } else if (itemTab.checkTouch(touch)) {
                    switchTab(ITEM_STATE, itemButtons);
                } else if (obstacleTab.checkTouch(touch)) {
                    switchTab(OBSTACLE_STATE, obstacleButtons);
                }


                if (state.equals(BLOCK_STATE)) {
                    int id = getButtonSelectedId(tileButtons);
                    if (id != -1) {
                        tileID = id;
                        return true;
                    }
                } else if (state.equals(ENEMY_STATE)) {
                    int id = getButtonSelectedId(enemyButtons);
                    if (id != -1) {
                        enemyID = id;
                        return true;
                    }
                } else if (state.equals(ITEM_STATE)) {

                    int id = getButtonSelectedId(itemButtons);
                    if (id != -1) {
                        itemID = id;
                        return true;
                    }
                } else if (state.equals(OBSTACLE_STATE)) {

                    int id = getButtonSelectedId(obstacleButtons);
                    if (id != -1) {
                        obstacleID = id;
                        return true;
                    }
                }
                return touch.x < 360;
            }
            return false;
        }

        private void switchTab(String s, Array<MenuButton> mb) {
            state = s;
            menuButtons = mb;
            eraseButton.setPressed(false);
        }

        private int getButtonSelectedId(Array<MenuButton> buttonArray) {
            Vector2 touch = getTouchPos();
            for (MenuButton button : buttonArray) {
                if (button.checkTouch(touch)) {

                    for (MenuButton otherButtons : buttonArray) {
                        otherButtons.selected = false;
                    }
                    eraseButton.setPressed(false);
                    button.selected = true;
                    return button.id;
                }
            }
            return -1;
        }

        public void render(SpriteBatch sb) {
            if (showing) {

                sideMenuBG.draw(sb, 10, 200, 260, 760);
                blockTab.render(sb);
                enemyTab.render(sb);
                itemTab.render(sb);
                obstacleTab.render(sb);

                for (MenuButton menuButton : menuButtons) {
                    menuButton.render(sb);
                }
            }
        }

        public void setShowing(boolean showing) {
            this.showing = showing;
        }

        private void addMenuButton(Array<MenuButton> array, Texture sheet, int sheetX, int sheetY, int id) {

            if (array.size % 2 == 0 && array.size != 0) {
                bottomY += 120;
            }

            array.add(new MenuButton(new TextureRegion(sheet, sheetX, sheetY, (int) Box2DVars.PPM, (int) Box2DVars.PPM), new Vector2(30 + (120 * (array.size % 2)), bottomY), id));
        }
    }

    private void saveMap(String text) {
        if (text.startsWith("lvl")) {
            saveCoreLevel(text);
            CoreLevelSaver levelSaver = new CoreLevelSaver(Gdx.files.external("SSB_Maps/" + text + ".json"), true);
            levelSaver.saveDataValue("LEVEL", mapManager.getCustomMap(text));
        } else {

            if (text.startsWith("export_")) { //Saves to external so core maps can be designed on android.
                //SAVE TO EXTERNAL STORAGE.
                CoreLevelSaver levelSaver = new CoreLevelSaver(Gdx.files.external("SSB_Maps/" + text + ".json"), true);
                levelSaver.saveDataValue("LEVEL", mapManager.getCustomMap(text));
            }

            if (!mapNameExists(text)) {
                if (MainGame.saveData.getMyMaps().size < 12) {
                    MainGame.saveData.getMyMaps().add(mapManager.getCustomMap(text));
                } else {
                    MainGame.saveData.getMyMaps().set(11, mapManager.getCustomMap(text));
                }
            }
            MainGame.saveManager.saveDataValue("PLAYER", MainGame.saveData);
            //MAP SAVED MESSAGE DISPLAY HERE!!!!
        }
    }

    private void saveCoreLevel(String text) {
        //TEMP!!!!
        CoreLevelSaver levelSaver = new CoreLevelSaver("levels/level_" + text.substring(4) + ".json", true);
        levelSaver.saveDataValue("LEVEL", mapManager.getCustomMap(text));


        if (levelSaver.loadDataValue("LEVEL", CustomMap.class) == null) {
            levelSaver.saveDataValue("LEVEL", mapManager.getCustomMap(text));
        }
    }

    private boolean mapNameExists(String mapName) {
        for (int i = 0; i < MainGame.saveData.getMyMaps().size; i++) {
            if (MainGame.saveData.getMyMaps().get(i).getName().equals(mapName)) {
                MainGame.saveData.getMyMaps().set(i, mapManager.getCustomMap(mapName));
                return true;
            }
        }
        return false;
    }
}
