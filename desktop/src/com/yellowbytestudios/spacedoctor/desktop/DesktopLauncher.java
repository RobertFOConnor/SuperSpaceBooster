package com.yellowbytestudios.spacedoctor.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.yellowbytestudios.spacedoctor.MainGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.addIcon("icons/icon_256.png", FileType.Internal);
        config.addIcon("icons/icon_64.png", FileType.Internal);
        config.addIcon("icons/icon_32.png", FileType.Internal);

        config.vSyncEnabled=true;

        boolean fullscreen = false;

        if(!fullscreen) {
            //config.width = 1400;
            //config.height = 787;

            config.width = 200;
            config.height = 100;
        } else {
            config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
            config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
            config.fullscreen = true;
        }

        config.title = "Super Space Booster";

        new LwjglApplication(new MainGame("DESKTOP"), config);
    }
}
