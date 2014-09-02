package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Xombified on 7/27/2014.
 */
public class MainTileActor extends Actor {
    public int xTile;
    public int yTile;

    private Texture mapTexture;
    private boolean isRevealed;
    private float transparencyLvl;
    private float fadeSpeed;
    private boolean containsMonster;

    public MainTileActor(int x, int y) {
        xTile = x;
        yTile = y;
        isRevealed = false;
        containsMonster = false;
        transparencyLvl = 0.5f;
        fadeSpeed = 1.5f;
        mapTexture = new Texture(Gdx.files.internal("blacktile.png"));

        // Set Map Actor boundaries
        setBounds(xTile * Parameters.TILE_PIXEL_WIDTH, yTile * Parameters.TILE_PIXEL_HEIGHT, Parameters.TILE_PIXEL_WIDTH, Parameters.TILE_PIXEL_HEIGHT);
    }

    public void dispose() {
        mapTexture.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isRevealed) {
            if (transparencyLvl > 0) {
                transparencyLvl -= Gdx.graphics.getDeltaTime() * fadeSpeed;
            }
            if (transparencyLvl <= 0) {
                transparencyLvl = 0;
            }
        } else {
            if (transparencyLvl < 0.5f) {
                transparencyLvl += Gdx.graphics.getDeltaTime() * fadeSpeed;
            }
            if (transparencyLvl >= 0.5f) {
                transparencyLvl = 0.5f;
            }
        }
        batch.setColor(0, 0, 0, transparencyLvl);
        batch.draw(mapTexture, getX(), getY(), Parameters.TILE_PIXEL_WIDTH, Parameters.TILE_PIXEL_HEIGHT);

        // Reset color to default to allow other textures render properly
        batch.setColor(Color.WHITE);
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public void setContainsMonster(boolean contains) {
        containsMonster = contains;
    }

    public boolean itContainsMonster() {
        return containsMonster;
    }
}
