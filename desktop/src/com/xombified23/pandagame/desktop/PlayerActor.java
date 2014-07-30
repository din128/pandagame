package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by Xombified on 7/27/2014.
 */
public class PlayerActor extends Actor {
    private float x;
    private float y;
    private int tilePixelWidth;
    private int tilePixelHeight;
    private Texture playerTexture;


    public PlayerActor(float x, float y, int tilePixelWidth, int tilePixelHeight, Texture playerTexture) {
        this.x = x;
        this.y = y;
        this.playerTexture = playerTexture;
        this.tilePixelWidth = tilePixelWidth;
        this.tilePixelHeight = tilePixelHeight;

        // Set Map Actor boundaries
        setBounds(x, y, tilePixelWidth, tilePixelHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(playerTexture, x, y);
    }
}
