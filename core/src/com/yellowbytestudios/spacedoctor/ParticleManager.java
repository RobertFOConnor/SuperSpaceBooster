package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class ParticleManager {

	//Particle Objects
	private ParticleEffectPool bubblePool;
	private Array<PooledEffect> effects = new Array<PooledEffect>();
	
	public ParticleManager() {
		ParticleEffect particleEffect = new ParticleEffect();
		particleEffect.load(Gdx.files.internal("effects/jetpack.p"), Gdx.files.internal("effects"));
		bubblePool = new ParticleEffectPool(particleEffect, 1, 2);
	}
	
	public void addEffect(float x, float y) {
		PooledEffect effect;
		effect = bubblePool.obtain();

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
