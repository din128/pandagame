package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Xombified on 7/27/2014.
 */
public class PlayerActor extends Actor {
    public int xTile;
    public int yTile;

    private int tilePixelWidth;
    private int tilePixelHeight;
    private Texture playerTexture;

    public PlayerActor(int x, int y, int tilePixelWidth, int tilePixelHeight, Texture playerTexture) {
        xTile = x;
        yTile = y;
        this.playerTexture = playerTexture;
        this.tilePixelWidth = tilePixelWidth;
        this.tilePixelHeight = tilePixelHeight;

        // Set Map Actor boundaries
        setBounds(xTile * tilePixelWidth, yTile * tilePixelHeight, tilePixelWidth, tilePixelHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(playerTexture, xTile * tilePixelWidth, yTile * tilePixelHeight);
    }
}
