package com.yellowbytestudios.spacedoctor.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.yellowbytestudios.spacedoctor.MainGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.addIcon("icons/icon_256.png", Files.FileType.Internal);
        config.addIcon("icons/icon_64.png", Files.FileType.Internal);
        config.addIcon("icons/icon_32.png", Files.FileType.Internal);

        config.vSyncEnabled=false;

        config.width = 960;
        config.height = 540;

        /*config.width = 1920;
        config.height = 1080;
        config.fullscreen = true;*/

        config.title = "Super Space Booster";

        new LwjglApplication(new MainGame("DESKTOP"), config);
    }
}
