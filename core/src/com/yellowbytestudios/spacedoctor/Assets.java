package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Assets {
	
    public static AssetManager manager = new AssetManager();

    //Images
    public static final String TITLESCREEN = "titlescreen.png";


    public static final String MENU_BG = "menu_bg.png";
    public static final String LEVEL_LOCKED = "levelLocked.png";
    public static final String LEVEL_COMPLETE = "levelComplete.png";
    public static final String LEVEL_BUTTON = "levelButton.png";
    public static final String LEVEL_BORDER = "levelBorder.png";


    //Sounds
    public static final String JETPACK_SOUND = "sounds/jetpack.wav";
    public static final String GUN_SOUND = "sounds/blaster.wav";
    public static final String FOOTSTEP_SOUND = "sounds/footstep.wav";
    public static final String FOOTSTEP2_SOUND = "sounds/footstep2.wav";
    public static final String FOOTSTEP3_SOUND = "sounds/footstep3.wav";
    public static final String FINISHED_SOUND = "sounds/finished.wav";
    public static final String PICKUP_SOUND = "sounds/pickup.wav";
    public static final String DEATH_SOUND = "sounds/death.wav";

    //public static final String THEME = "sounds/theme.mp3";

    
    public static void load() {// Loads Assets

    	manager = new AssetManager();

        manager.load(TITLESCREEN, Texture.class);
        manager.load(MENU_BG, Texture.class);
        manager.load(LEVEL_LOCKED, Texture.class);
        manager.load(LEVEL_COMPLETE, Texture.class);
        manager.load(LEVEL_BUTTON, Texture.class);
        manager.load(LEVEL_BORDER, Texture.class);

        manager.load(JETPACK_SOUND, Sound.class);
        manager.load(GUN_SOUND, Sound.class);
        manager.load(FOOTSTEP_SOUND, Sound.class);
        manager.load(FOOTSTEP2_SOUND, Sound.class);
        manager.load(FOOTSTEP3_SOUND, Sound.class);
        manager.load(FINISHED_SOUND, Sound.class);
        manager.load(PICKUP_SOUND, Sound.class);
        manager.load(DEATH_SOUND, Sound.class);

        //manager.load(THEME, Music.class);
    }

    public static void dispose() {
    	manager.dispose();
    }

    public static boolean update() {
        return manager.update();
    }
}