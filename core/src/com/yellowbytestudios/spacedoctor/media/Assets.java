package com.yellowbytestudios.spacedoctor.media;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

public class Assets {

    public static AssetManager manager = new AssetManager();

    public static final String LANGUAGE_FILE = "i18n/MyBundle";

    public static final String LOADSCREEN = "loadscreen.png";
    public static final String LOADWHEEL = "loadwheel.png";

    public static final String MENU_BG = "menu_bg.png";
    public static final String LEVEL_LOCKED = "levelLocked.png";
    public static final String LEVEL_COMPLETE = "levelComplete.png";
    public static final String LEVEL_BUTTON = "levelButton.png";
    public static final String LEVEL_BORDER = "levelBorder.png";
    public static final String ALPHA = "alpha.png";

    //Android Controls
    public static final String LEFT = "android_controls/left.png";
    public static final String RIGHT = "android_controls/right.png";
    public static final String UP = "android_controls/up.png";
    public static final String SHOOT = "android_controls/shoot.png";
    public static final String LEFT_PRESSED = "android_controls/left_pressed.png";
    public static final String RIGHT_PRESSED = "android_controls/right_pressed.png";
    public static final String UP_PRESSED = "android_controls/up_pressed.png";
    public static final String SHOOT_PRESSED = "android_controls/shoot_pressed.png";

    //Menu screen.
    public static final String START_GAME = "menu/start_game.png";
    public static final String LEVEL_BUILDER = "menu/level_builder.png";
    public static final String SWITCH_ON = "menu/switch_on.png";
    public static final String SWITCH_OFF = "menu/switch_off.png";
    public static final String GO_BACK = "menu/go_back.png";
    public static final String EXIT = "menu/exit.png";
    public static final String SETTINGS = "menu/settings.png";
    public static final String STATS = "menu/statistics.png";

    public static final String CHARACTER = "title/character.png";
    public static final String TITLE = "title/title.png";

    public static final String STARS1 = "title/stars1.png";
    public static final String STARS2 = "title/stars2.png";


    public static final String SMALL_MAP = "mapeditor/small_map.png";
    public static final String MEDIUM_MAP = "mapeditor/medium_map.png";
    public static final String LARGE_MAP = "mapeditor/large_map.png";
    public static final String NEW_MAP = "mapeditor/new_map.png";
    public static final String LOAD_MAP = "mapeditor/load_map.png";


    //Game objects.
    public static final String BOX = "game_objects/box.png";
    public static final String BULLET = "game_objects/bullet.png";
    public static final String BULLET_ENEMY = "game_objects/bullet_enemy.png";
    public static final String HOR_PLATFORM = "game_objects/hor_platform_medium.png";
    public static final String VER_PLATFORM = "game_objects/ver_platform_medium.png";

    //Background.
    public static final String BG = "background/stars.png";
    public static final String ASTEROIDS = "background/asteroids.png";
    public static final String ASTEROIDS2 = "background/asteroids2.png";
    public static final String HILLS = "background/hills.png";

    //Game GUI.
    public static final String LOCKED_HEAD = "menu/locked_head.png";


    //Sounds.
    public static final String JETPACK_SOUND = "sounds/jetpack.wav";
    public static final String GUN_SOUND_1 = "sounds/gun/blaster_1.wav";
    public static final String GUN_SOUND_2 = "sounds/gun/blaster_2.wav";
    public static final String GUN_SOUND_3 = "sounds/gun/blaster_3.wav";
    public static final String GUN_SOUND_EMPTY = "sounds/gun/blaster_empty.wav";
    public static final String FOOTSTEP_SOUND = "sounds/footstep.wav";
    public static final String FOOTSTEP2_SOUND = "sounds/footstep2.wav";
    public static final String FOOTSTEP3_SOUND = "sounds/footstep3.wav";
    public static final String FINISHED_SOUND = "sounds/finished.wav";
    public static final String PICKUP_SOUND = "sounds/pickup.wav";
    public static final String DEATH_SOUND = "sounds/death.wav";
    public static final String ENEMY_DEATH = "sounds/enemy_death.wav";
    public static final String TILE_PLACE = "sounds/tile_place.wav";
    public static final String TILE_ERASE = "sounds/tile_erase.wav";


    public static final String MAIN_THEME = "music/main_theme.mp3";
    public static final String LEVEL_THEME = "music/level_theme.mp3";


    public static void load() {// Loads Assets

        manager = new AssetManager();

        manager.load(LOADSCREEN, Texture.class);
        manager.load(LOADWHEEL, Texture.class);

        manager.load(MENU_BG, Texture.class);
        manager.load(LEVEL_LOCKED, Texture.class);
        manager.load(LEVEL_COMPLETE, Texture.class);
        manager.load(LEVEL_BUTTON, Texture.class);
        manager.load(LEVEL_BORDER, Texture.class);

        manager.load(START_GAME, Texture.class);
        manager.load(LEVEL_BUILDER, Texture.class);
        manager.load(SWITCH_ON, Texture.class);
        manager.load(SWITCH_OFF, Texture.class);
        manager.load(GO_BACK, Texture.class);
        manager.load(EXIT, Texture.class);
        manager.load(SETTINGS, Texture.class);
        manager.load(STATS, Texture.class);
        manager.load(ALPHA, Texture.class);

        manager.load(LEFT, Texture.class);
        manager.load(RIGHT, Texture.class);
        manager.load(UP, Texture.class);
        manager.load(SHOOT, Texture.class);
        manager.load(LEFT_PRESSED, Texture.class);
        manager.load(RIGHT_PRESSED, Texture.class);
        manager.load(UP_PRESSED, Texture.class);
        manager.load(SHOOT_PRESSED, Texture.class);
        manager.load(BG, Texture.class);
        manager.load(ASTEROIDS, Texture.class);
        manager.load(ASTEROIDS2, Texture.class);
        manager.load(HILLS, Texture.class);
        manager.load(BOX, Texture.class);
        manager.load(BULLET, Texture.class);
        manager.load(BULLET_ENEMY, Texture.class);
        manager.load(HOR_PLATFORM, Texture.class);
        manager.load(VER_PLATFORM, Texture.class);
        manager.load(LOCKED_HEAD, Texture.class);
        manager.load(CHARACTER, Texture.class);
        manager.load(TITLE, Texture.class);
        manager.load(STARS1, Texture.class);
        manager.load(STARS2, Texture.class);
        manager.load(SMALL_MAP, Texture.class);
        manager.load(MEDIUM_MAP, Texture.class);
        manager.load(LARGE_MAP, Texture.class);
        manager.load(NEW_MAP, Texture.class);
        manager.load(LOAD_MAP, Texture.class);

        manager.load(JETPACK_SOUND, Sound.class);
        manager.load(GUN_SOUND_1, Sound.class);
        manager.load(GUN_SOUND_2, Sound.class);
        manager.load(GUN_SOUND_3, Sound.class);
        manager.load(GUN_SOUND_EMPTY, Sound.class);
        manager.load(FOOTSTEP_SOUND, Sound.class);
        manager.load(FOOTSTEP2_SOUND, Sound.class);
        manager.load(FOOTSTEP3_SOUND, Sound.class);
        manager.load(FINISHED_SOUND, Sound.class);
        manager.load(PICKUP_SOUND, Sound.class);
        manager.load(DEATH_SOUND, Sound.class);
        manager.load(ENEMY_DEATH, Sound.class);
        manager.load(TILE_PLACE, Sound.class);
        manager.load(TILE_ERASE, Sound.class);

        manager.load(MAIN_THEME, Music.class);
        manager.load(LEVEL_THEME, Music.class);

        manager.load(LANGUAGE_FILE, I18NBundle.class);
    }

    public static void dispose() {
        manager.dispose();
    }

    public static boolean update() {
        return manager.update();
    }
}