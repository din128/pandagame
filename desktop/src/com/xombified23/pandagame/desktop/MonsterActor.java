package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Xombified on 8/9/2014.
 */
public class MonsterActor extends Actor {
    private boolean isRevealed = false;
    private MainTileActor[][] mainTileActorMap;
    private int xTile;
    private int yTile;
    private Texture monsterTexture;

    public MonsterActor(int xTile, int yTile, MainTileActor[][] mainTileActorMap) {
        if (mainTileActorMap == null)
            throw new Error();

        this.xTile = xTile;
        this.yTile = yTile;

        monsterTexture = new Texture(Gdx.files.internal("playerSprite.png"));
        setBounds(xTile * Parameters.TILE_PIXEL_WIDTH, yTile * Parameters.TILE_PIXEL_HEIGHT, Parameters.TILE_PIXEL_WIDTH,
                Parameters.TILE_PIXEL_HEIGHT);
    }

    public void dispose() {
        monsterTexture.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isRevealed) {
            batch.draw(monsterTexture, getX(), getY(), monsterTexture.getWidth() * 5, monsterTexture.getHeight() * 5);
        }
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }
}
