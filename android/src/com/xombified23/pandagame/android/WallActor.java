package com.xombified23.pandagame.android;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WallActor extends BaseActor {
    private TextureRegion wallTexture;
    private int xTile;
    private int yTile;
    private float zOrder;

    public WallActor(int xTile, int yTile, TextureRegion wallTexture) {
        this.wallTexture = wallTexture;
        this.xTile = xTile;
        this.yTile = yTile;
        setBounds(xTile * Parameters.TILE_PIXEL_WIDTH, yTile * Parameters.TILE_PIXEL_HEIGHT, Parameters.TILE_PIXEL_WIDTH,
                Parameters.TILE_PIXEL_HEIGHT);
        zOrder = Parameters.Z_CHARACTERS - getY();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(wallTexture, getX(), getY(), Parameters.TILE_PIXEL_WIDTH, Parameters.TILE_PIXEL_HEIGHT);
    }

    @Override
    public int getXTile() {
        return xTile;
    }

    @Override
    public int getYTile() {
        return yTile;
    }

    @Override
    public float getZ() {
        return zOrder;
    }
}
