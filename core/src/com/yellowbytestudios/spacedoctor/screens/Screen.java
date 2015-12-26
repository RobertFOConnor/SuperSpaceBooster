package com.yellowbytestudios.spacedoctor.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Screen {	
	
	void create();
	
	void update(float step);
	
	void render(SpriteBatch sb);
	
	void resize(int w, int h);
	
	void dispose();
	
	void show();
	
	void hide();
	
	void pause();
	
	void resume();

	void goBack();
}
