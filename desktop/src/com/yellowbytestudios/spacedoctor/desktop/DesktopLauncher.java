package com.yellowbytestudios.spacedoctor.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.yellowbytestudios.spacedoctor.MainGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Super Space Booster");
        config.setWindowedMode(1600, 900);
        config.setResizable(false);
        config.useVsync(true);
        config.disableAudio(false);

        /*config.addIcon("icons/icon_256.png", FileType.Internal);
        config.addIcon("icons/icon_64.png", FileType.Internal);
        config.addIcon("icons/icon_32.png", FileType.Internal);

        if(!fullscreen) {
            //config.width = 1400;
            //config.height = 787;

            config.width = 200;
            config.height = 100;
        } else {
            config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
            config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
            config.fullscreen = true;
        }*/

        new Lwjgl3Application(new MainGame("DESKTOP"), config);
    }
}
