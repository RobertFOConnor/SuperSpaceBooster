package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Fonts {

	public static BitmapFont GUIFont;
	
	public static void loadFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/basic_font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 45;
        GUIFont = generator.generateFont(parameter);
        
        //parameter.size = 55;
        //SubFont = generator.generateFont(parameter);
	}
	
	//public static float getWidth(BitmapFont f, String s) {
	//	return f.getBounds(s).width;
	//}
}
