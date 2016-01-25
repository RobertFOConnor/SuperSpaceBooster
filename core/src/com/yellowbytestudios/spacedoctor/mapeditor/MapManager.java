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
import com.yellowbytestudios.spacedoctor.Box2DVars;
import com.yellowbytestudios.spacedoctor.Entity;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.cameras.BoundedCamera;

public class MapManager {

    private BoundedCamera cam;
    private Vector2 touch;
    private TiledMap map;
    private MapLayers layers;
    private TiledMapTileLayer layer1;
    private OrthogonalTiledMapRenderer tmr;
    private Cell darkCell, lightCell, spikeCell_U, spikeCell_D, spikeCell_L, spikeCell_R;

    public static final int customMapWidth = 50;
    public static final int customMapHeight = 25;
    private int tileSize = 100;

    //Dragging
    private Vector2 startTouch, endTouch;
    private boolean dragging = false;

    //EXIT
    private DraggableObject exit;
    public static float exitX = 6;
    public static float exitY = 1;


    public MapManager() {

        cam = new BoundedCamera();
        cam.setToOrtho(false, MainGame.WIDTH, MainGame.HEIGHT);

        float zoomBoundsX = 600;
        float zoomBoundsY = 400;
        cam.setBounds(-zoomBoundsX, customMapWidth * 100 + zoomBoundsX, -zoomBoundsY, customMapHeight * 100 + zoomBoundsY);

        map = new TiledMap();
        layers = map.getLayers();

        layer1 = new TiledMapTileLayer(customMapWidth, customMapHeight, tileSize, tileSize);
        layer1.setName("main");

        initTiles();
        setupMap();

        exit = new DraggableObject(new Texture(Gdx.files.internal("mapeditor/exit_icon.png")), new Vector2(exitX*Box2DVars.PPM, exitY*Box2DVars.PPM));
    }

    private void initTiles() {
        Texture tileSheet = new Texture(Gdx.files.internal("maps/tileset.png"));
        StaticTiledMapTile t;

        darkCell = new TiledMapTileLayer.Cell();
        t = new StaticTiledMapTile(new TextureRegion(tileSheet, 200, 100, tileSize, tileSize));
        t.setId(0);
        darkCell.setTile(t);

        lightCell = new TiledMapTileLayer.Cell();
        t = new StaticTiledMapTile(new TextureRegion(tileSheet, 0, 0, tileSize, tileSize));
        t.setId(0);
        lightCell.setTile(t);

        spikeCell_D = new TiledMapTileLayer.Cell();
        t = new StaticTiledMapTile(new TextureRegion(tileSheet, 200, 0, tileSize, tileSize));
        t.setId(TileIDs.DOWN_SPIKE);
        spikeCell_D.setTile(t);

        spikeCell_U = new TiledMapTileLayer.Cell();
        t = new StaticTiledMapTile(new TextureRegion(tileSheet, 300, 200, tileSize, tileSize));
        t.setId(TileIDs.UP_SPIKE);
        spikeCell_U.setTile(t);

        spikeCell_R = new TiledMapTileLayer.Cell();
        t = new StaticTiledMapTile(new TextureRegion(tileSheet, 300, 100, tileSize, tileSize));
        t.setId(TileIDs.RIGHT_SPIKE);
        spikeCell_R.setTile(t);

        spikeCell_L = new TiledMapTileLayer.Cell();
        t = new StaticTiledMapTile(new TextureRegion(tileSheet, 400, 100, tileSize, tileSize));
        t.setId(TileIDs.LEFT_SPIKE);
        spikeCell_L.setTile(t);
    }

    private void setupMap() {

        for (int row = 0; row < layer1.getHeight(); row++) {
            for (int col = 0; col < layer1.getWidth(); col++) {

                if (row == 0 || col == 0 || row == layer1.getHeight() - 1 || col == layer1.getWidth() - 1) { //Borders.
                    layer1.setCell(col, row, darkCell);
                }
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
            exit.selected = false;

        } else {
            touch = cam.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            if (exit.checkTouch(touch)) {
                exit.selected = true;
            }

            if (exit.selected) {
                exitX = touch.x / Box2DVars.PPM;
                exitY = touch.y / Box2DVars.PPM;
                exit.setPos(touch);
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

                        if (tileID == 0) {
                            layer1.setCell(col, row, lightCell);
                        } else if (tileID == TileIDs.DOWN_SPIKE) {
                            layer1.setCell(col, row, spikeCell_D);
                        } else if (tileID == TileIDs.UP_SPIKE) {
                            layer1.setCell(col, row, spikeCell_U);
                        } else if (tileID == TileIDs.LEFT_SPIKE) {
                            layer1.setCell(col, row, spikeCell_L);
                        } else if (tileID == TileIDs.RIGHT_SPIKE) {
                            layer1.setCell(col, row, spikeCell_R);
                        }
                    }
                }
            }
        }
    }

    public void eraseTiles() {
        if (Gdx.input.isTouched()) {
            touch = cam.unprojectCoordinates(Gdx.input.getX(),
                    Gdx.input.getY());

            for (int row = 0; row < layer1.getHeight(); row++) {
                for (int col = 0; col < layer1.getWidth(); col++) {

                    if (row == 0 || col == 0 || row == layer1.getHeight() - 1 || col == layer1.getWidth() - 1) { //Borders.

                    } else {
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
        sb.begin();
        exit.render(sb);
        sb.end();

        tmr.setView(cam);
        tmr.render();
    }

    public void zoomIn() {

        if (cam.zoom > 0.5f) {
            cam.zoom -= 0.03f;
        }
    }

    public void zoomOut() {

        if (cam.zoom < 3f) {
            cam.zoom += 0.03f;
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

        public DraggableObject(Texture texture, Vector2 pos) {
            super(texture, pos);
        }

        public void setPos(Vector2 pos) {
            this.pos = pos.add(-texture.getWidth() / 2, -texture.getHeight() / 2);
        }

        public boolean isSelected() {
            return selected;
        }
    }

    public DraggableObject getExit() {
        return exit;
    }
}

