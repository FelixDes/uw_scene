package com.uw;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("uw_scene");
        config.setWindowedMode(1600, 1000);
        config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 40);
        new Lwjgl3Application(new UwScene(), config);
    }
}
