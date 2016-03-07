package com.yellowbytestudios.spacedoctor.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.yellowbytestudios.spacedoctor.MainGame;
import com.yellowbytestudios.spacedoctor.screens.HelmetSelectScreen;

public class ParticleManager {

	//Particle Objects
	private ParticleEffectPool bubblePool, bubblePool2;
	private Array<PooledEffect> effects = new Array<PooledEffect>();
	private float[] color;
	
	public ParticleManager() {
		ParticleEffect particleEffect = new ParticleEffect();
		particleEffect.load(Gdx.files.internal("effects/jetpack.p"), Gdx.files.internal("effects"));
		ParticleEffect particleEffect2 = new ParticleEffect();
		particleEffect2.load(Gdx.files.internal("effects/coin.p"), Gdx.files.internal("effects"));
		bubblePool = new ParticleEffectPool(particleEffect, 1, 2);
		bubblePool2 = new ParticleEffectPool(particleEffect2, 1, 2);
		color = HelmetSelectScreen.CHAR_COLORS[MainGame.saveData.getHead()];
	}
	
	public void addEffect(float x, float y) {
		PooledEffect effect;
		effect = bubblePool.obtain();

		effect.setPosition(x, y);
        effect.getEmitters().get(0).getTint().setColors(color);
		effects.add(effect);
	}

	public void addCoinEffect(float x, float y) {
		PooledEffect effect;
		effect = bubblePool2.obtain();

		effect.setPosition(x, y);
		effects.add(effect);
	}
	
	public void render(SpriteBatch sb) {
		for (int i = effects.size - 1; i >= 0; i--) {
		    PooledEffect effect = effects.get(i);
			effect.update(Gdx.graphics.getDeltaTime());
			if (effect.isComplete()) {
				effect.free();
				effects.removeIndex(i);
				effect.dispose();
			} else {
				effect.draw(sb);
			}
		}
	}
}
