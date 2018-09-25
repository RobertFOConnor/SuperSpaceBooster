package com.yellowbytestudios.spacedoctor.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.game.player.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.cameras.BoundedCamera;
import com.yellowbytestudios.spacedoctor.game.objects.Exit;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LightManager {

	private BoundedCamera camera;
	private RayHandler handler;
    private Array<PointLight> coinLights;
    private Array<SpacemanPlayer> players;
    private Array<ConeLight> playerTorches;
    private Array<PointLight> playerLights;

	private float brightness = 0.17f;
	

	public LightManager(World world, Array<SpacemanPlayer> players, Exit e, BoundedCamera camera) {
		this.camera = camera;
        this.players = players;
		handler = new RayHandler(world);
		handler.setCombinedMatrix(camera);
		handler.setAmbientLight(brightness);
        handler.setBlur(true);
        handler.setCulling(true);

        //SETUP PLAYER LIGHTS.
        playerLights = new Array<PointLight>();
        playerTorches = new Array<ConeLight>();
        for(SpacemanPlayer player : players) {
            ConeLight p  = new ConeLight(handler, 50, Color.BLACK, 8, player.getPos().x, player.getPos().y, 0, 20);
            p.setSoftnessLength(1f);
            p.setXray(false);
            playerTorches.add(p);
            playerLights.add(new PointLight(handler, 50, Color.BLACK, 1.35f, player.getPos().x, player.getPos().y));
        }

        //SETUP EXIT LIGHTS.
        PointLight exitLight = new PointLight(handler, 1000, Color.GREEN, 5, e.getBody().getPosition().x, e.getBody().getPosition().y);
        exitLight.setSoft(true);

        coinLights = new Array<PointLight>();
	}

    public PointLight createTileLight(Vector2 pos) {

        Color c = new Color((float)(Math.random()*255)/255, (float)(Math.random()*255)/255, (float)(Math.random()*255)/255, 1);

        PointLight light = new PointLight(handler, 50, Color.GOLD, 1, pos.x, pos.y);
        light.setSoft(true);
        light.setXray(true);
        light.setSoftnessLength(10);
        return light;
    }

    public ConeLight createConeLight(Vector2 pos) {

        Color c = new Color((float)(Math.random()*255)/255, (float)(Math.random()*255)/255, (float)(Math.random()*255)/255, 1);

        ConeLight light = new ConeLight(handler, 200, Color.BLACK, 40, pos.x, pos.y, -90, 30);
        light.setSoft(true);
        light.setXray(false);
        return light;
    }

    public PointLight createLight(Vector2 pos) {
        PointLight light = new PointLight(handler, 5, Color.YELLOW, 1, pos.x, pos.y);
        light.setSoft(true);
        coinLights.add(light);
        return light;
    }

    public void removeLight(PointLight l) {
        l.setActive(false);
    }

	public void update() {
        handler.setCombinedMatrix(camera);

        if(players.size > 0) {
            for (int i = 0; i < players.size; i++) {
                playerTorches.get(i).setPosition(players.get(i).getPos());
                if(players.get(i).facingLeft()) {
                    playerTorches.get(i).setDirection(180);
                } else {
                    playerTorches.get(i).setDirection(0);
                }
                playerLights.get(i).setPosition(players.get(i).getPos());
            }
        }
        handler.update();
	}
	
	public void render() {
		handler.render();
	}

    public void dispose() {
        handler.dispose();
    }
}
