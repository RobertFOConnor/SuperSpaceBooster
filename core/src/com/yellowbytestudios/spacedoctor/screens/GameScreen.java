package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.BodyFactory;
import com.yellowbytestudios.spacedoctor.Box2DContactListeners;
import com.yellowbytestudios.spacedoctor.objects.Bullet;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.ParticleManager;
import com.yellowbytestudios.spacedoctor.SpacemanPlayer;
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
    private SpacemanPlayer player;
    private float PPM = 100;

    private Box2DContactListeners contactListener;
    private Vector2 playerPos;

    private Array<Bullet> bullets;
    private Texture bg;

    public static ParticleManager particleManager;


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

        player = new SpacemanPlayer(BodyFactory.createBody(world, "PLAYER"), contactListener);

        bullets = new Array<Bullet>();

        particleManager = new ParticleManager();

        bg = new Texture(Gdx.files.internal("bg.png"));
    }


    public void addBullet() {

        Bullet bullet = new Bullet(BodyFactory.createBody(world, "BULLET"));

        float speed = Bullet.SPEED;

        if(player.facingLeft()) {
            bullet.getBody().setLinearVelocity(-speed, 0f);
            bullet.getBody().setTransform(player.getBody().getPosition().x-1.2f, player.getBody().getPosition().y, 0);
        } else {
            bullet.getBody().setLinearVelocity(speed, 0f);
            bullet.getBody().setTransform(player.getBody().getPosition().x+1.2f, player.getBody().getPosition().y, 0);
        }
        bullets.add(bullet);
    }

    @Override
    public void update(float step) {
        world.step(step, 8, 3);

        updateCameras();

        player.update();

        if (contactListener.getBodies().size > 0) {
            for(Body b : contactListener.getBodies()) {
                bullets.removeValue((Bullet) b.getUserData(), true);
                world.destroyBody(b);
            }
        }

        if(player.isShooting()) {
            addBullet();
            player.setShooting(false);
        }
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


        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - bg.getWidth() / 2, cam.position.y - bg.getHeight() / 2);
        sb.end();


        //Render tiles.
        tmr.setView(cam);
        tmr.render();


        sb.begin();
        player.render(sb);

        for(Bullet b : bullets) { //DRAW BULLETS.
            b.render(sb);

            if(Math.abs(b.getBody().getPosition().x-player.getBody().getPosition().x) > 100) {
                world.destroyBody(b.getBody());
                bullets.removeValue(b, true);
            }
        }

        particleManager.render(sb);
        sb.end();


        //Render Box2D world.
        //b2dr.render(world, b2dCam.combined);
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