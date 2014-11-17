package com.xombified23.pandagame.android;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 *  Created by Xombified on 9/1/2014.
 */
public class FloorActor extends Actor implements TileInterface{
    private TextureRegion floorTexture;
    private int xTile;
    private int yTile;

    public FloorActor(int xTile, int yTile, TextureRegion floorTexture) {
        this.floorTexture = floorTexture;
        this.xTile = xTile;
        this.yTile = yTile;

        setBounds(xTile * Parameters.TILE_PIXEL_WIDTH, yTile * Parameters.TILE_PIXEL_HEIGHT, Parameters.TILE_PIXEL_WIDTH,
                Parameters.TILE_PIXEL_HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(floorTexture, getX(), getY(), Parameters.TILE_PIXEL_WIDTH, Parameters.TILE_PIXEL_HEIGHT);
    }

    @Override
    public int getXTile() {
        return xTile;
    }

    @Override
    public int getYTile() {
        return yTile;
    }
}
