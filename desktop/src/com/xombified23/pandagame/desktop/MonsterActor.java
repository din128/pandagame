package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 *  Created by Xombified on 8/9/2014.
 */
public class MonsterActor extends Actor {
    private MainTileActor[][] mainTileActorMap;
    private int xTile;
    private int yTile;
    private Texture monsterTexture;

    public MonsterActor(int xTile, int yTile, Texture monsterTexture, MainTileActor[][] mainTileActorMap) {
        if (mainTileActorMap == null)
            throw new Error();

        this.mainTileActorMap = mainTileActorMap;
        this.xTile = xTile;
        this.yTile = yTile;

        this.monsterTexture = monsterTexture;
        setBounds(xTile * Parameters.TILE_PIXEL_WIDTH, yTile * Parameters.TILE_PIXEL_HEIGHT, Parameters.TILE_PIXEL_WIDTH,
                Parameters.TILE_PIXEL_HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (mainTileActorMap[xTile][yTile].isRevealed()) {
            batch.draw(monsterTexture, getX(), getY(), monsterTexture.getWidth() * 5, monsterTexture.getHeight() * 5);
        }
    }

    public int getXTile() {
        return xTile;
    }

    public int getYTile() {
        return yTile;
    }
}
