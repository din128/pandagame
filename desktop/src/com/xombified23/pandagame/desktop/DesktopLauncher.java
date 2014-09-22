package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "PandaGame";
        // config.width = Parameters.SCREEN_WIDTH;
        // config.height = Parameters.SCREEN_HEIGHT;
        config.width = Parameters.SCREEN_HEIGHT;
        config.height = Parameters.SCREEN_WIDTH;

        new LwjglApplication(new MainGame(), config);
    }
}
