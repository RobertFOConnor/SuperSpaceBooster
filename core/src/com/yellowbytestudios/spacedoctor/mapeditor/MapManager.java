package com.yellowbytestudios.spacedoctor.mapeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.box2d.Box2DVars;
import com.yellowbytestudios.spacedoctor.cameras.BoundedCamera;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.MapEditorAssets;

public class MapManager {

    private BoundedCamera cam;
    private Vector2 touch;
    private TiledMap map;
    private MapLayers layers;
    private TiledMapTileLayer layer1;
    private OrthogonalTiledMapRenderer tmr;

    public static int customMapWidth = 30;
    public static int customMapHeight = 15;
    private static final int tileSize = 100;

    //Dragging / Navigating Map.
    private Vector2 startTouch, endTouch;
    private boolean dragging = false;

    //Object currently being used.
    private DraggableObject currObject;
    private boolean holdingObject = false;

    //EXIT
    private DraggableObject exit;
    public static float exitX = 15;
    public static float exitY = 4;

    //PLAYER SPAWN
    private DraggableObject playerSpawn;
    public static float startX = 3;
    public static float startY = 4;

    //ENEMIES.
    public static Array<DraggableObject> enemyList;

    //ITEMS
    public static Array<DraggableObject> itemList;


    //Tile Types.
    private static final Array<Cell> CELLS = new Array<Cell>();

    public static void initCells() {
        CELLS.add(buildTile(0, 0, TileIDs.LIGHT_PURPLE));
        CELLS.add(buildTile(200, 100, TileIDs.DARK_PURPLE));
        CELLS.add(buildTile(100, 0, TileIDs.CAGED_WALL));
        CELLS.add(buildTile(200, 0, TileIDs.DOWN_SPIKE));
        CELLS.add(buildTile(300, 200, TileIDs.UP_SPIKE));
        CELLS.add(buildTile(300, 100, TileIDs.RIGHT_SPIKE));
        CELLS.add(buildTile(400, 100, TileIDs.LEFT_SPIKE));
    }


    public MapManager(CustomMap savedMap) {

        int[][] savedArray = savedMap.loadMap();

        customMapWidth = savedArray.length;
        customMapHeight = savedArray[0].length;

        initObjects();
        setupMap(savedArray);

        Vector2 exitPos = savedMap.getExitPos().cpy();
        Vector2 startPos = savedMap.getStartPos().cpy();

        exitX = exitPos.x / 100;
        exitY = exitPos.y / 100;
        startX = startPos.x / 100;
        startY = startPos.y / 100;

        setupPlayerAndExit();
        enemyList = new Array<DraggableObject>();
        for (Vector2 enemy : savedMap.getEnemyArray()) {
            enemyList.add(new DraggableObject(MapEditorAssets.manager.get(MapEditorAssets.ENEMY_SPAWN, Texture.class), enemy.cpy()));
        }

        itemList = new Array<DraggableObject>();
        for (CustomMapObject item : savedMap.getItemArray()) {
            addItemWithId(item.getId(), item.getPos().cpy());
        }
    }


    public MapManager() {
        initObjects();
        setupMap();
        setupPlayerAndExit();
    }

    public MapManager(TiledMap tm) {
        initObjects();

        setupMap();
        setupPlayerAndExit();

        this.map = tm;
        layers = map.getLayers();
        layer1 = (TiledMapTileLayer) layers.get(0);
        tmr = new OrthogonalTiledMapRenderer(map);
    }

    private void setupPlayerAndExit() {
        exit = new DraggableObject(MapEditorAssets.manager.get(MapEditorAssets.EXIT_SPAWN, Texture.class), new Vector2(exitX * Box2DVars.PPM, exitY * Box2DVars.PPM));
        playerSpawn = new DraggableObject(MapEditorAssets.manager.get(MapEditorAssets.PLAYER_SPAWN, Texture.class), new Vector2(startX * Box2DVars.PPM, startY * Box2DVars.PPM));
    }


    private void initObjects() {

        cam = new BoundedCamera();
        cam.setToOrtho(false, MainGame.WIDTH, MainGame.HEIGHT);

        float zoomBoundsX = 600;
        float zoomBoundsY = 400;
        cam.setBounds(-zoomBoundsX, customMapWidth * 100 + zoomBoundsX, -zoomBoundsY, customMapHeight * 100 + zoomBoundsY);
        cam.zoom = 2f;

        map = new TiledMap();
        layers = map.getLayers();

        layer1 = new TiledMapTileLayer(customMapWidth, customMapHeight, tileSize, tileSize);
        layer1.setName("main");
    }

    private static TiledMapTileLayer.Cell buildTile(int regX, int regY, int ID) {

        Texture tileSheet = new Texture(Gdx.files.internal("maps/tileset.png"));
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        StaticTiledMapTile t = new StaticTiledMapTile(new TextureRegion(tileSheet, regX, regY, tileSize, tileSize));
        t.setId(ID);
        cell.setTile(t);
        return cell;
    }

    private void setupMap() {

        for (int row = 0; row < layer1.getHeight(); row++) {
            for (int col = 0; col < layer1.getWidth(); col++) {

                if (row < 3 || col == 0 || row == layer1.getHeight() - 1 || col == layer1.getWidth() - 1) { //Borders.
                    layer1.setCell(col, row, CELLS.get(0));
                }
            }
        }
        layers.add(layer1);


        tmr = new OrthogonalTiledMapRenderer(map);
    }

    private void setupMap(int[][] array) { //setup from saved map.

        for (int row = 0; row < layer1.getHeight(); row++) {
            for (int col = 0; col < layer1.getWidth(); col++) {

                layer1.setCell(col, row, findCellById(array[col][row]));
            }
        }
        layers.add(layer1);


        tmr = new OrthogonalTiledMapRenderer(map);
    }


    public void update() {

        if (!Gdx.input.isTouched()) {

            if (dragging) {
                startTouch = null;
                endTouch = null;
                dragging = false;
            }

            if (holdingObject) {

                for (DraggableObject enemy : enemyList) {
                    enemy.selected = false;
                }

                for (DraggableObject item : itemList) {
                    item.selected = false;
                }

                exit.selected = false;
                playerSpawn.selected = false;
                holdingObject = false;
                currObject = null;
            }

        } else {
            touch = cam.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (!dragging) {

                if (currObject == null) {

                    if (playerSpawn.checkTouch(touch)) {
                        setDraggableSelected(playerSpawn);
                    } else if (exit.checkTouch(touch)) {
                        setDraggableSelected(exit);
                    }

                    for (DraggableObject enemy : enemyList) {
                        if (enemy.checkTouch(touch)) {
                            setDraggableSelected(enemy);
                        }
                    }

                    for (DraggableObject item : itemList) {
                        if (item.checkTouch(touch)) {
                            setDraggableSelected(item);
                        }
                    }

                } else {

                    if (playerSpawn.selected) {
                        startX = (touch.x / Box2DVars.PPM);
                        startY = (touch.y / Box2DVars.PPM);
                    } else if (exit.selected) {
                        exitX = (touch.x / Box2DVars.PPM);
                        exitY = (touch.y / Box2DVars.PPM);
                    }

                    currObject.setPos(touch);
                }
            }
        }

        cam.update();
    }

    private void setDraggableSelected(DraggableObject dragObj) {
        dragObj.selected = true;
        holdingObject = true;
        currObject = dragObj;
    }

    public void checkForTilePlacement(int tileID) {
        if (Gdx.input.isTouched()) {
            touch = cam.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            for (int row = 0; row < layer1.getHeight(); row++) {
                for (int col = 0; col < layer1.getWidth(); col++) {

                    if (new Rectangle(col * 100, row * 100, 100, 100).contains(touch)) {

                        if (layer1.getCell(col, row) == null) {
                            SoundManager.play(Assets.TILE_PLACE);
                        }
                        layer1.setCell(col, row, findCellById(tileID));
                    }
                }
            }
        }
    }

    private Cell findCellById(int tileID) {

        for (Cell c : CELLS) {
            if (c.getTile().getId() == tileID) {
                return c;
            }
        }
        return null;
    }

    public void eraseTiles() {
        if (Gdx.input.isTouched()) {
            touch = cam.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            for (int row = 0; row < layer1.getHeight(); row++) {
                for (int col = 0; col < layer1.getWidth(); col++) {

                    if (row != 0 && col != 0 && row != layer1.getHeight() - 1 && col != layer1.getWidth() - 1) { //Borders.
                        if (new Rectangle(col * 100, row * 100, 100, 100).contains(touch)) {

                            if (layer1.getCell(col, row) != null) {
                                layer1.setCell(col, row, null);
                                SoundManager.play(Assets.TILE_ERASE);
                                System.out.println("ERASED");
                            }
                        }
                    }
                }
            }

            for (DraggableObject enemy : enemyList) {
                if (enemy.checkTouch(touch)) {
                    enemyList.removeValue(enemy, true);
                }
            }

            for (DraggableObject item : itemList) {
                if (item.checkTouch(touch)) {
                    itemList.removeValue(item, true);
                }
            }
        }
    }


    public void dragMap() {
        if (!dragging) {
            startTouch = cam.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());
            dragging = true;
        } else {
            endTouch = cam.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            Vector2 delta = endTouch.cpy().sub(startTouch);
            cam.setPosition(cam.position.x - delta.x, cam.position.y - delta.y);
        }
    }


    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);

        tmr.setView(cam);
        tmr.render();
        sb.begin();
        exit.draw(sb);
        playerSpawn.draw(sb);

        drawDraggableObjects(itemList, sb);
        drawDraggableObjects(enemyList, sb);

        sb.end();
    }

    private void drawDraggableObjects(Array<DraggableObject> array, SpriteBatch sb) {
        for (DraggableObject item : array) {
            item.draw(sb);
        }
    }

    public void zoomIn() {

        if (cam.zoom > 0.5f) {
            cam.zoom -= (1.8f * Gdx.graphics.getDeltaTime());
        }
    }

    public void zoomOut() {

        if (cam.zoom < 3f) {
            cam.zoom += (1.8f * Gdx.graphics.getDeltaTime());
        }
    }

    public void addEnemy() { // will take enemy id.
        if (touch.x > 0 && touch.x < customMapWidth * Box2DVars.PPM) {
            if (touch.y > 0 && touch.y < customMapHeight * Box2DVars.PPM) {
                enemyList.add(new DraggableObject(MapEditorAssets.manager.get(MapEditorAssets.ENEMY_SPAWN, Texture.class), touch));
            }
        }
    }

    public void addItem(int itemID) { //will take item id.
        if (touch.x > 0 && touch.x < customMapWidth * Box2DVars.PPM) {
            if (touch.y > 0 && touch.y < customMapHeight * Box2DVars.PPM) {

                addItemWithId(itemID, touch);
            }
        }
    }

    private void addItemWithId(int itemID, Vector2 pos) {

        Texture itemSheet = MapEditorAssets.manager.get(MapEditorAssets.ITEM_SHEET, Texture.class);

        if (itemID == IDs.COIN) {
            itemList.add(new DraggableObject(itemID, MapEditorAssets.manager.get(MapEditorAssets.COIN_ICON, Texture.class), pos));
        } else if (itemID == IDs.GAS) {
            itemList.add(new DraggableObject(itemID, new TextureRegion(itemSheet, 0, 0, 100, 100), pos));
        } else if (itemID == IDs.AMMO) {
            itemList.add(new DraggableObject(itemID, new TextureRegion(itemSheet, 100, 0, 100, 100), pos));
        } else if (itemID == IDs.CLOCK) {
            itemList.add(new DraggableObject(itemID, new TextureRegion(itemSheet, 200, 0, 100, 100), pos));
        }
    }


    public TiledMap getMap() {
        return map;
    }


    /**
     * DRAGGABLE OBJECT CLASS - used to represent entities in the editor.
     */

    public class DraggableObject extends Sprite {

        private boolean selected = false;
        private float boundX, boundY;
        private int id = -1;

        public DraggableObject(Texture texture, Vector2 pos) {
            super(texture);
            setBounds();
            setPos(pos);
        }

        public DraggableObject(int id, Texture texture, Vector2 pos) {
            super(texture);
            this.id = id;
            setBounds();
            setPos(pos);
        }

        public DraggableObject(int id, TextureRegion texture, Vector2 pos) {
            super(texture);
            this.id = id;
            setBounds();
            setPos(pos);
        }

        private void setBounds() {
            boundX = customMapWidth * Box2DVars.PPM - getWidth();
            boundY = customMapHeight * Box2DVars.PPM - getHeight();
        }


        public void setPos(Vector2 pos) {
            setCenter(pos.x, pos.y);
            checkMapBounds();
        }

        public void checkMapBounds() {
            if (getX() < 0) {
                setPosition(0, getY());
            } else if (getX() > boundX) {
                setPosition(boundX, getY());
            }

            if (getY() < 0) {
                setPosition(getX(), 0);
            } else if (getY() > boundY) {
                setPosition(getX(), boundY);
            }
        }

        public Rectangle getBounds() {
            return new Rectangle(getX(), getY(), getWidth(), getHeight());
        }

        public boolean checkTouch(Vector2 touch) {
            return getBounds().contains(touch);
        }

        public int getId() {
            return id;
        }
    }

    public DraggableObject getExit() {
        return exit;
    }

    public boolean isHoldingObject() {
        return holdingObject;
    }

    public static void reset() {
        exitX = 15;
        exitY = 4;
        startX = 3;
        startY = 4;
        enemyList = new Array<DraggableObject>();
        itemList = new Array<DraggableObject>();
    }
}

