package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "PandaGame";
        config.width = 1920;
        config.height = 1080;

        new LwjglApplication(new MyGame(), config);
    }
}
