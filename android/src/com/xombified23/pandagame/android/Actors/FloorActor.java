package com.xombified23.pandagame.android.Actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.xombified23.pandagame.android.Parameters;

public class FloorActor extends BaseActor {
    private TextureRegion floorTexture;
    private int xTile;
    private int yTile;
    private float zOrder;

    public FloorActor(int xTile, int yTile, TextureRegion floorTexture) {
        this.floorTexture = floorTexture;
        this.xTile = xTile;
        this.yTile = yTile;
        zOrder = Parameters.Z_FLOOR;

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

    @Override
    public float getZ() {
        return zOrder;
    }
}
