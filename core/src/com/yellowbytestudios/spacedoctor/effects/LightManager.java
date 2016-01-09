package com.yellowbytestudios.spacedoctor.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.Box2DVars;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.SpacemanPlayer;
import com.yellowbytestudios.spacedoctor.cameras.BoundedCamera;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LightManager {

	private BoundedCamera camera;
	private PointLight playerLight;
	
	private RayHandler handler;

	private float brightness = 0.7f;
	private SpacemanPlayer player;
	

	public LightManager(World world, SpacemanPlayer player, BoundedCamera camera) {
		this.camera = camera;
		this.player = player;
		handler = new RayHandler(world);
		handler.setCombinedMatrix(camera.combined);
		handler.setAmbientLight(brightness);
		playerLight = new PointLight(handler, 20, Color.BLACK, 200, player.getPos().x, player.getPos().y);
	}

	public void update() {
		playerLight.setPosition(player.getPos().x * Box2DVars.PPM + MainGame.WIDTH / 50, player.getPos().y * Box2DVars.PPM + MainGame.HEIGHT / 50);
        System.out.println(player.getPos().x+" , "+ player.getPos().y);
        handler.update();
	}
	
	public void render() {
		handler.render();
	}
}
