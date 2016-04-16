package com.yellowbytestudios.spacedoctor.effects;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.yellowbytestudios.spacedoctor.media.Assets;
import com.yellowbytestudios.spacedoctor.media.MapEditorAssets;

public class SoundManager {

    public static boolean soundFXEnabled = true;
    public static boolean musicEnabled = false;
    public static Music GAME_MUSIC;

    public static void play(String s) {
        if (soundFXEnabled) {
            Assets.manager.get(s, Sound.class).play();
        }
    }

    public static void stop(String s) {
        if (soundFXEnabled) {
            Assets.manager.get(s, Sound.class).stop();
        }
    }

    public static void loop(String s) {
        if (soundFXEnabled) {
            Assets.manager.get(s, Sound.class).loop();
        }
    }

    public static void switchMusic(String music) {
        if (musicEnabled) {
            if (GAME_MUSIC.isPlaying()) {
                GAME_MUSIC.stop();
            }
            setMusic(music);
        }
    }

    //TEMP METHOD
    public static void switchEditorMusic(String music) {
        if (musicEnabled) {
            if (GAME_MUSIC.isPlaying()) {
                GAME_MUSIC.stop();
            }
            GAME_MUSIC = MapEditorAssets.manager.get(music, Music.class);
            if (musicEnabled) {
                GAME_MUSIC.setLooping(true);
                GAME_MUSIC.play();
            }
        }
    }


    public static void setMusic(String music) {
            GAME_MUSIC = Assets.manager.get(music, Music.class);
        if (musicEnabled) {
            GAME_MUSIC.setLooping(true);
            GAME_MUSIC.play();
        }
    }

    public static void toggleMusic() {
        musicEnabled = !musicEnabled;
        if (musicEnabled) {
            GAME_MUSIC.setLooping(true);
            GAME_MUSIC.play();
        } else {
            GAME_MUSIC.stop();
        }
    }
}
