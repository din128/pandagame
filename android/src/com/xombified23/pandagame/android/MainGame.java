package com.xombified23.pandagame.android;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.xombified23.pandagame.android.Screens.GameScreen;

/**
 * The {@link com.badlogic.gdx.ApplicationListener} for this project, create(), resize() and
 * render() are the only methods that are relevant
 *
 * @author Dan Lee Jo
 */
public class MainGame extends Game {
    private GameScreen gamescreen;

    public void create() {
        gamescreen = new GameScreen(this);
        this.setScreen(gamescreen);
    }

    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    public void dispose() {
        gamescreen.hide();
        gamescreen.dispose();
    }
}
