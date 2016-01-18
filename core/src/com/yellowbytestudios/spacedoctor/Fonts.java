package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Fonts {

	public static BitmapFont GUIFont, timerFont;
	
	public static void loadFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/basic_font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 45;
        GUIFont = generator.generateFont(parameter);
        
        parameter.size = 60;
        timerFont = generator.generateFont(parameter);
	}
}
