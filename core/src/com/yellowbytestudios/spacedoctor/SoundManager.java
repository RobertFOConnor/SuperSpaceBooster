package com.yellowbytestudios.spacedoctor;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    public static boolean soundFXEnabled = true;
    public static boolean musicEnabled = false;

    public static void play(Sound s) {
        if (soundFXEnabled) {
            s.play();
        }
    }

    public static void stop(Sound s) {
        if (soundFXEnabled) {
            s.stop();
        }
    }

    public static void loop(Sound s) {
        if (soundFXEnabled) {
            s.loop();
        }
    }

	public static void switchMusic(String music) {
		if (musicEnabled) {
			if(MainGame.GAME_MUSIC.isPlaying()) {
				MainGame.GAME_MUSIC.stop();
			}
			setMusic(music);
		}
	}

	public static void setMusic(String music) {
		if (musicEnabled) {
			MainGame.GAME_MUSIC = Assets.manager.get(music, Music.class);
			MainGame.GAME_MUSIC.setLooping(true);
			MainGame.GAME_MUSIC.play();
		}
	}
}
