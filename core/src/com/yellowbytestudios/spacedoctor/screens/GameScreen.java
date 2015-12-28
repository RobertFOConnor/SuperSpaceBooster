package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.yellowbytestudios.spacedoctor.Box2DContactListeners;
import com.yellowbytestudios.spacedoctor.Box2DVars;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.Player;
import com.yellowbytestudios.spacedoctor.TileManager;
import com.yellowbytestudios.spacedoctor.cameras.BoundedCamera;

/**
 * Created by BobbyBoy on 15-Oct-15.
 */
public class GameScreen implements Screen {

    private BoundedCamera cam, b2dCam;
    private World world;
    private TiledMap tileMap;
    private Box2DDebugRenderer b2dr;
    private OrthogonalTiledMapRenderer tmr;

    private TileManager tileManager;
    private Player player;
    private float PPM = 100;

    private Box2DContactListeners contactListener;
    private Vector2 playerPos;


    @Override
    public void create() {

        //Setup camera.
        cam = new BoundedCamera();
        cam.setToOrtho(false, MainGame.WIDTH, MainGame.HEIGHT);
        world = new World(new Vector2(0, -9.8f), true);

        //Setup contact listeners.
        contactListener = new Box2DContactListeners();
        world.setContactListener(contactListener);

        b2dr = new Box2DDebugRenderer();
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, MainGame.WIDTH / PPM, MainGame.HEIGHT / PPM);

        //Set tile map using Tiled map path.
        tileMap = new TmxMapLoader().load("test.tmx");

        //Setup map renderer.
        tmr = new OrthogonalTiledMapRenderer(tileMap);

        tileManager = new TileManager();
        tileManager.createWalls(world, tileMap);

        // Set camera boundries.
        b2dCam.setBounds(0, tileManager.getMapWidth() / PPM, 0, tileManager.getMapHeight() / PPM);
        cam.setBounds(0, tileManager.getMapWidth(), 0, tileManager.getMapHeight());

        setupPlayer();
    }

    private void setupPlayer() {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;

        bdef.fixedRotation = true;
        bdef.linearVelocity.set(0f, 0f);
        bdef.position.set(2, 5);

        // create body from bodydef
        Body body = world.createBody(bdef);

        // create box shape for player collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(40 / PPM, 60 / PPM);

        // create fixturedef for player collision box
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.restitution = 0.03f;
        fdef.filter.categoryBits = Box2DVars.BIT_PLAYER;
        fdef.filter.maskBits = Box2DVars.BIT_WALL;
        body.createFixture(fdef).setUserData("player");

        //PLAYER FOOT

        shape = new PolygonShape();
        shape.setAsBox(37 / PPM, 20 / PPM, new Vector2(0, -45 / PPM), 0);

        // create fixturedef for player foot
        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Box2DVars.BIT_PLAYER;
        fdef.filter.maskBits = Box2DVars.BIT_WALL;

        // create player foot fixture
        body.createFixture(fdef).setUserData("foot");


        shape.dispose();

        player = new Player(body);
    }

    @Override
    public void update(float step) {
        world.step(step, 8, 3);

        updateCameras();

        player.update();
    }

    private void updateCameras() {
        playerPos = player.getBody().getPosition();
        float targetX = playerPos.x * PPM + MainGame.WIDTH / 50;
        float targetY = playerPos.y * PPM + MainGame.HEIGHT / 50;

        cam.setPosition(targetX, targetY);
        b2dCam.setPosition(playerPos.x + MainGame.WIDTH / 50 / PPM, playerPos.y + MainGame.HEIGHT / 50 / PPM);

        b2dCam.update();
        cam.update();
    }


    @Override
    public void render(SpriteBatch sb) {
        // Clear screen.
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glClearColor(0, 0, 0, 0);


        //Render tiles.
        tmr.setView(cam);
        tmr.render();

        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        player.render(sb);
        sb.end();


        //Render Box2D world.
        b2dr.render(world, b2dCam.combined);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void goBack() {

    }
}