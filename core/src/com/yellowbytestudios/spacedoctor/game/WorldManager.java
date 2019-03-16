package com.yellowbytestudios.spacedoctor.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.box2d.BodyFactory;
import com.yellowbytestudios.spacedoctor.box2d.Box2DContactListeners;
import com.yellowbytestudios.spacedoctor.effects.SoundManager;
import com.yellowbytestudios.spacedoctor.game.enemy.Enemy;
import com.yellowbytestudios.spacedoctor.game.objects.Box;
import com.yellowbytestudios.spacedoctor.game.objects.Box2DSprite;
import com.yellowbytestudios.spacedoctor.game.objects.Bullet;
import com.yellowbytestudios.spacedoctor.game.objects.Character;
import com.yellowbytestudios.spacedoctor.game.objects.Exit;
import com.yellowbytestudios.spacedoctor.game.objects.PickUp;
import com.yellowbytestudios.spacedoctor.game.objects.Platform;
import com.yellowbytestudios.spacedoctor.game.player.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.screens.GameScreen;
import com.yellowbytestudios.spacedoctor.spriter.MySpriterAnimationListener;

/**
 * Created by Robert on 12-May-16.
 * Class for managing world and all objects and bodies which exist within.
 */
public class WorldManager {

    private GameScreen gameScreen;

    private World world;
    private Box2DContactListeners contactListener;

    //GAME-OBJECT ARRAYS.
    private Array<Box2DSprite> bullets;
    private Array<Box2DSprite> boxes;
    private Array<Box2DSprite> pickups;
    private Array<Box2DSprite> enemies;
    private Array<Box2DSprite> platforms;
    private Exit exit;

    private Array<SpacemanPlayer> players;

    public WorldManager(GameScreen gameScreen, float startX, float startY) {
        this.gameScreen = gameScreen;
        world = new World(new Vector2(0, -9.8f), true);

        //Setup contact listeners.
        contactListener = new Box2DContactListeners();
        world.setContactListener(contactListener);

        //Setup map walls.
        TileManager tileManager = new TileManager();
        tileManager.createWalls(world, gameScreen.getTileMap());
        //tileManager.createLights(lightManager, tileMap);

        //Setup world objects.
        bullets = new Array<Box2DSprite>();
        boxes = BodyFactory.createBoxes(world);
        pickups = BodyFactory.createPickups(world);
        enemies = BodyFactory.createEnemies(world);
        platforms = BodyFactory.createPlatforms(world);
        exit = BodyFactory.createExits(world);

        //Setup player(s).
        players = new Array<SpacemanPlayer>();
        players.add(BodyFactory.createPlayer(world, 0, MainGame.saveData.getHead()));
        players.get(0).setPos(new Vector2(startX, startY));

//        if (Controllers.getControllers().size > 1) {
//            for(int i = 1; i < Controllers.getControllers().size; i++) {
//                players.add(BodyFactory.createPlayer(world, i, i+4));
//                players.get(i).setPos(new Vector2(GameScreen.customMap.getStartPos().cpy()));
//            }
//        }
    }

    public void update(float delta) {
        world.step(delta, 8, 3);

        for (SpacemanPlayer player : players) {
            player.update();

            if (player.isShooting()) {
                addBullet(player, player.getGun().getBullet());
                player.setShooting(false);
            }

            if (player.isDead() || gameScreen.getGUI().timeIsUp()) {
                killPlayer(player);
            }

            if (player.isFinished()) {
                world.destroyBody(player.getBody());
                players.removeValue(player, true);

                if (players.size == 0) {
                    gameScreen.exitLevel();
                }
            }
        }
        updateObjects(delta);
        removeObjects();
        removeEnemies();
        exit.update();
    }

    private void updateGroup(Array<Box2DSprite> array, float delta) {
        for (Box2DSprite obj : array) {
            obj.update(delta);
        }
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        exit.render(sb);
        renderObjects(sb);
        for (SpacemanPlayer player : players) {
            player.render();
        }
        sb.end();
    }

    private void renderObjects(SpriteBatch sb) {
        renderGroup(platforms, sb);
        renderGroup(boxes, sb);
        renderGroup(pickups, sb);
        renderGroup(enemies, sb);
        renderGroup(bullets, sb);
    }

    private void renderGroup(Array<Box2DSprite> array, SpriteBatch sb) {
        for (Box2DSprite obj : array) {
            obj.render(sb);
        }
    }

    private void updateObjects(float delta) {

        updateGroup(platforms, delta);
        updateGroup(bullets, delta);

        if (players.size != 0) {
            for (int i = 0; i < enemies.size; i++) {
                Enemy e = (Enemy) enemies.get(i);
                e.update(players.get(0));

                if (e.isShooting()) {
                    addBullet(e, 0);
                    e.setShooting(false);
                }
            }
        }
    }

    private void removeObjects() {
        if (contactListener.getBodies().size > 0) {
            for (Fixture f : contactListener.getBodies()) {
                Body b = f.getBody();
                if (f.getUserData().equals("bullet")) {
                    bullets.removeValue((Bullet) b.getUserData(), true);
                } else if (f.getUserData().equals("pickup")) {
                    PickUp p = (PickUp) b.getUserData();
                    //lightManager.removeLight(p.getLight());
                    pickups.removeValue(p, true);
                }
                world.destroyBody(b);
            }
        }
    }

    private void killPlayer(final com.yellowbytestudios.spacedoctor.game.player.SpacemanPlayer p) {
        if (!p.isDieing()) {
            SoundManager.stop(Assets.JETPACK_SOUND);
            SoundManager.play(Assets.ENEMY_DEATH);

            Player.PlayerListener myListener = new MySpriterAnimationListener() {
                @Override
                public void animationFinished(Animation animation) {
                    gameScreen.setupMap();
                }
            };
            p.startDeath(myListener);
            p.setDieing(true);
        }
    }

    public void addBullet(Character player, int bulletId) {

        Bullet bullet;
        int dir = 1;
        if (player.facingLeft()) {
            dir = -1;
        }
        bullet = new Bullet(BodyFactory.createBullet(world), dir, bulletId);
        bullet.getBody().setTransform(player.getPos().x + (dir * 1.2f), player.getPos().y - 0.1f, 0);
        bullets.add(bullet);
    }

    private void removeEnemies() {
        if (contactListener.getEnemy() != null) {

            Body enemyBody = contactListener.getEnemy();
            Enemy e = (Enemy) enemyBody.getUserData();
            e.setHealth(e.getHealth() - 1);

            if (e.getHealth() <= 0) {
                killEnemy(e);
                world.destroyBody(enemyBody);
            }
            contactListener.nullifyEnemy();
        }
    }

    private void killEnemy(final Enemy e) {

        SoundManager.play(Assets.ENEMY_DEATH);

        PickUp p = BodyFactory.createPickUp(world, e.getPos(), "coin");
        //p.setLight(lightManager.createLight(p.getBody().getPosition()));
        pickups.add(p);

        if (!e.isDead()) {

            Player.PlayerListener myListener = new MySpriterAnimationListener() {
                @Override
                public void animationFinished(Animation animation) {
                    enemies.removeValue(e, true);
                }
            };
            e.startDeath(myListener);
            e.setDead(true);
        }
    }

    public Array<SpacemanPlayer> getPlayers() {
        return players;
    }

    public void dispose() {
        world.dispose();
    }

    public World getWorld() {
        return world;
    }
}
