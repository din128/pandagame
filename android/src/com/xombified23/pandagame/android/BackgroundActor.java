package com.xombified23.pandagame.android;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 *  Created by Xombified on 8/9/2014.
 */
public class BackgroundActor extends BaseActor {
    private Texture texture;
    private float zOrder;

    public BackgroundActor(Texture texture) {
        zOrder = Parameters.Z_BACKGROUND;
        this.texture = texture;
        setBounds(0, 0, Parameters.SCREEN_WIDTH, Parameters.SCREEN_HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, 0, 0, Parameters.SCREEN_WIDTH, Parameters.SCREEN_HEIGHT);
    }

    @Override
    // Not used
    public int getXTile() {
        return 0;
    }

    @Override
    // Not used
    public int getYTile() {
        return 0;
    }

    @Override
    public float getZ() {
        return zOrder;
    }
}
