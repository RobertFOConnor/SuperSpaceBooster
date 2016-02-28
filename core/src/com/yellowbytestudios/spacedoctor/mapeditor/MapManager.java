package com.yellowbytestudios.spacedoctor.mapeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
import com.yellowbytestudios.spacedoctor.objects.Entity;

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

    //Dragging
    private Vector2 startTouch, endTouch;
    private boolean dragging = false;

    //EXIT
    private DraggableObject exit;
    public static float exitX = 15;
    public static float exitY = 4;

    //PLAYER SPAWN
    private DraggableObject playerSpawn;
    public static float startX = 3;
    public static float startY = 4;


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

        cam = new BoundedCamera();
        cam.setToOrtho(false, MainGame.WIDTH, MainGame.HEIGHT);
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

        exit = new DraggableObject(new Texture(Gdx.files.internal("mapeditor/exit_icon.png")), exitPos);
        playerSpawn = new DraggableObject(new Texture(Gdx.files.internal("mapeditor/player_spawn.png")), startPos);
    }


    public MapManager() {

        cam = new BoundedCamera();
        cam.setToOrtho(false, MainGame.WIDTH, MainGame.HEIGHT);

        initObjects();

        setupMap();

        exit = new DraggableObject(new Texture(Gdx.files.internal("mapeditor/exit_icon.png")), new Vector2(exitX * Box2DVars.PPM, exitY * Box2DVars.PPM));
        playerSpawn = new DraggableObject(new Texture(Gdx.files.internal("mapeditor/player_spawn.png")), new Vector2(startX * Box2DVars.PPM, startY * Box2DVars.PPM));
    }

    private void initObjects() {
        float zoomBoundsX = 600;
        float zoomBoundsY = 400;
        cam.setBounds(-zoomBoundsX, customMapWidth * 100 + zoomBoundsX, -zoomBoundsY, customMapHeight * 100 + zoomBoundsY);

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


    public void update(float step) {

        if (!Gdx.input.isTouched()) {

            if (dragging) {
                startTouch = null;
                endTouch = null;
                dragging = false;
            }
            exit.selected = false;
            playerSpawn.selected = false;

        } else {
            touch = cam.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (!dragging) {

                if (playerSpawn.checkTouch(touch)) {
                    playerSpawn.selected = true;
                } else if (exit.checkTouch(touch)) {
                    exit.selected = true;
                }

                if (playerSpawn.selected) {
                    startX = (touch.x / Box2DVars.PPM);
                    startY = (touch.y / Box2DVars.PPM);
                    playerSpawn.setPos(touch);
                } else if (exit.selected) {
                    exitX = (touch.x / Box2DVars.PPM);
                    exitY = (touch.y / Box2DVars.PPM);
                    exit.setPos(touch);
                }
            }
        }

        cam.update();
    }

    public void checkForTilePlacement(int tileID) {
        if (Gdx.input.isTouched()) {
            touch = cam.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            for (int row = 0; row < layer1.getHeight(); row++) {
                for (int col = 0; col < layer1.getWidth(); col++) {

                    if (new Rectangle(col * 100, row * 100, 100, 100).contains(touch)) {

                        layer1.setCell(col, row, findCellById(tileID));
                    }
                }
            }
        }
    }

    private Cell findCellById(int tileID) {

        for(Cell c : CELLS) {
            if(c.getTile().getId() == tileID) {
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

                    if (row != 0 || col != 0 || row != layer1.getHeight() - 1 || col != layer1.getWidth() - 1) { //Borders.
                        if (new Rectangle(col * 100, row * 100, 100, 100).contains(touch)) {
                            layer1.setCell(col, row, null);
                        }
                    }
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
        exit.render(sb);
        playerSpawn.render(sb);
        sb.end();
    }

    public void zoomIn() {

        if (cam.zoom > 0.5f) {
            cam.zoom -= (1.8f*Gdx.graphics.getDeltaTime());
        }
    }

    public void zoomOut() {

        if (cam.zoom < 3f) {
            cam.zoom += (1.8f*Gdx.graphics.getDeltaTime());
        }
    }

    public void setMap(TiledMap map) {
        this.map = map;
        layers = map.getLayers();
        layer1 = (TiledMapTileLayer) layers.get(0);
        tmr = new OrthogonalTiledMapRenderer(map);
    }

    public TiledMap getMap() {
        return map;
    }


    public class DraggableObject extends Entity {

        private boolean selected = false;
        private float boundX, boundY;

        public DraggableObject(Texture texture, Vector2 pos) {
            super(texture, pos);

            boundX = customMapWidth * Box2DVars.PPM - texture.getWidth();
            boundY = customMapHeight * Box2DVars.PPM - texture.getHeight();

            setPos(pos);
        }

        public void setPos(Vector2 pos) {
            this.pos = pos.add(-texture.getWidth() / 2, -texture.getHeight() / 2);
            checkMapBounds();
        }

        public void checkMapBounds() {
            if (pos.x < 0) {
                pos.set(0, pos.y);
            } else if (pos.x > boundX) {
                pos.set(boundX, pos.y);
            }

            if (pos.y < 0) {
                pos.set(pos.x, 0);
            } else if (pos.y > boundY) {
                pos.set(pos.x, boundY);
            }
        }

        public boolean isSelected() {
            return selected;
        }
    }

    public DraggableObject getExit() {
        return exit;
    }

    public DraggableObject getPlayerSpawn() {
        return playerSpawn;
    }

    public static void reset() {
        exitX = 15;
        exitY = 4;

        startX = 3;
        startY = 4;
    }
}

