package com.yellowbytestudios.spacedoctor.media;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class MapEditorAssets {

    public static AssetManager manager = new AssetManager();

    //Map Editor
    public static final String ZOOM_IN = "mapeditor/zoom_in.png";
    public static final String ZOOM_OUT = "mapeditor/zoom_out.png";
    public static final String PLAY_MAP = "mapeditor/play_map.png";
    public static final String SAVE_MAP = "mapeditor/save_map.png";


    public static final String EXIT_EDITOR = "mapeditor/exit.png";
    public static final String SIDE_MENU = "mapeditor/side_menu.png";
    public static final String BLOCK_TAB = "mapeditor/block_tab.png";
    public static final String ENEMY_TAB = "mapeditor/enemy_tab.png";
    public static final String ITEM_TAB = "mapeditor/item_tab.png";
    public static final String OBSTACLE_TAB = "mapeditor/obstacle_tab.png";

    public static final String MOVE_BUTTON = "mapeditor/move_button.png";
    public static final String MOVE_BUTTON_SEL = "mapeditor/move_button_selected.png";
    public static final String ERASE = "mapeditor/erase.png";
    public static final String ERASE_SEL = "mapeditor/erase_selected.png";
    public static final String BOTTOM_BAR = "mapeditor/bottom_bar.png";
    public static final String BOX = "game_objects/box.png";

    public static final String PLAYER_SPAWN = "mapeditor/player_spawn.png";
    public static final String EXIT_SPAWN = "mapeditor/exit_icon.png";
    public static final String PLATTY_SPAWN = "mapeditor/platty_spawn.png";
    public static final String PLATTY_ICON = "mapeditor/platty_icon.png";
    public static final String EYE_GUY_SPAWN = "mapeditor/eye_guy_spawn.png";
    public static final String EYE_GUY_ICON = "mapeditor/eye_guy_icon.png";
    public static final String PLATFORM_ICON = "mapeditor/platform_icon.png";
    public static final String PLATFORM_ICON_VER = "mapeditor/platform_icon_ver.png";
    public static final String ITEM_SHEET = "mapeditor/item_sheet.png";
    public static final String TILESHEET = "maps/tileset.png";
    public static final String COIN_ICON = "mapeditor/coin_icon.png";
    public static final String TILE_SELECTOR = "mapeditor/tile_buttons_selector.png";

    public static final String HOR_PLATFORM = "game_objects/hor_platform_medium.png";
    public static final String VER_PLATFORM = "game_objects/ver_platform_medium.png";


    //public static final String EDITOR_THEME = "music/editor_theme.mp3";


    public static void load() {
        manager = new AssetManager();

        manager.load(ZOOM_IN, Texture.class);
        manager.load(ZOOM_OUT, Texture.class);
        manager.load(MOVE_BUTTON, Texture.class);
        manager.load(MOVE_BUTTON_SEL, Texture.class);
        manager.load(ERASE, Texture.class);
        manager.load(ERASE_SEL, Texture.class);
        manager.load(PLAY_MAP, Texture.class);
        manager.load(SAVE_MAP, Texture.class);

        manager.load(PLATTY_SPAWN, Texture.class);
        manager.load(PLATTY_ICON, Texture.class);
        manager.load(EYE_GUY_SPAWN, Texture.class);
        manager.load(EYE_GUY_ICON, Texture.class);
        manager.load(ITEM_SHEET, Texture.class);
        manager.load(COIN_ICON, Texture.class);
        manager.load(PLAYER_SPAWN, Texture.class);
        manager.load(EXIT_SPAWN, Texture.class);
        manager.load(BLOCK_TAB, Texture.class);
        manager.load(ENEMY_TAB, Texture.class);
        manager.load(ITEM_TAB, Texture.class);
        manager.load(OBSTACLE_TAB, Texture.class);
        manager.load(HOR_PLATFORM, Texture.class);
        manager.load(VER_PLATFORM, Texture.class);
        manager.load(PLATFORM_ICON, Texture.class);
        manager.load(PLATFORM_ICON_VER, Texture.class);
        manager.load(BOX, Texture.class);


        manager.load(EXIT_EDITOR, Texture.class);
        manager.load(SIDE_MENU, Texture.class);
        manager.load(BOTTOM_BAR, Texture.class);
        manager.load(TILESHEET, Texture.class);
        manager.load(TILE_SELECTOR, Texture.class);

        //manager.load(EDITOR_THEME, Music.class);
    }

    public static void dispose() {
        manager.dispose();
    }

    public static boolean update() {
        return manager.update();
    }
}
