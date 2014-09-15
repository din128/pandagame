package com.xombified23.pandagame.android;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 *  Created by Xombified on 9/1/2014.
 */
public class FloorActor extends Actor {
    private TextureRegion floorTexture;

    public FloorActor(int xTile, int yTile, TextureRegion floorTexture) {
        this.floorTexture = floorTexture;

        setBounds(xTile * Parameters.TILE_PIXEL_WIDTH, yTile * Parameters.TILE_PIXEL_HEIGHT, Parameters.TILE_PIXEL_WIDTH,
                Parameters.TILE_PIXEL_HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(floorTexture, getX(), getY(), Parameters.TILE_PIXEL_WIDTH, Parameters.TILE_PIXEL_HEIGHT);
    }
}
