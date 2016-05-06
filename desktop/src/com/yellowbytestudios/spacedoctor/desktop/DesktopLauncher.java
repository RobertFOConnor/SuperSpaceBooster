package com.yellowbytestudios.spacedoctor.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.yellowbytestudios.spacedoctor.MainGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Super Space Booster");
        config.setWindowedMode(1920, 1080);
        config.setResizable(true);
        config.useVsync(true);
        config.disableAudio(false);

        new Lwjgl3Application(new MainGame("DESKTOP"), config);
    }
}
