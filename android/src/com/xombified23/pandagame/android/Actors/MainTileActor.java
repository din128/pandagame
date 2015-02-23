package com.xombified23.pandagame.android.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.xombified23.pandagame.android.Parameters;

public class MainTileActor extends BaseActor {
    private int xTile;
    private int yTile;
    private Texture fogTexture;
    private boolean isRevealed;
    private float transparencyLvl;
    private float fadeSpeed;
    private boolean containsMonster;
    private boolean containsWall;
    private int aggroCount;
    private float zOrder;

    // TODO: Debug Stuff
    private Texture blueTexture;
//    private Texture redTexture;
//    private Texture greenTexture;
    private boolean togglePlayerTile;

    public MainTileActor(int x, int y, Texture fogTexture, Texture blueTexture, Texture redTexture,
                         Texture greenTexture) {
        zOrder = Parameters.Z_MAIN;
        xTile = x;
        yTile = y;
        isRevealed = false;
        containsMonster = false;
        containsWall = false;
        transparencyLvl = 0.5f;
        fadeSpeed = 1.5f;
        this.fogTexture = fogTexture;
        this.blueTexture = blueTexture;
//        this.redTexture = redTexture;
//        this.greenTexture = greenTexture;
        togglePlayerTile = false;
        aggroCount = 0;

        // Set Map Actor boundaries
        setBounds(xTile * Parameters.TILE_PIXEL_WIDTH, yTile * Parameters.TILE_PIXEL_HEIGHT,
                Parameters.TILE_PIXEL_WIDTH, Parameters.TILE_PIXEL_HEIGHT);
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

        // TODO: Test Start: Debug Stuff
        if (togglePlayerTile) {
            batch.setColor(Color.WHITE);
            batch.draw(blueTexture, getX(), getY(), Parameters.TILE_PIXEL_WIDTH, Parameters.TILE_PIXEL_HEIGHT);
        }

//        else if (this.isRevealed() && aggroCount > 0 && !togglePlayerTile) {
//            batch.setColor(Color.WHITE);
//            batch.draw(redTexture, getX(), getY(), Parameters.TILE_PIXEL_WIDTH, Parameters.TILE_PIXEL_HEIGHT);
        else {
            batch.draw(fogTexture, getX(), getY(), Parameters.TILE_PIXEL_WIDTH, Parameters.TILE_PIXEL_HEIGHT);
        }
        // TODO: Test End: Debug Stuff

        // batch.draw(fogTexture, getX(), getY(), Parameters.TILE_PIXEL_WIDTH, Parameters.TILE_PIXEL_HEIGHT);
        // Reset color to default to allow other textures render properly
        batch.setColor(Color.WHITE);
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public boolean itContainsMonster() {
        return containsMonster;
    }

    public void setContainsMonster(boolean contains) {
        containsMonster = contains;
    }

    public boolean itContainsWall() {
        return containsWall;
    }

    public void setContainsWall(boolean contains) {
        containsWall = contains;
    }

    public int getXTile() {
        return xTile;
    }

    public int getYTile() {
        return yTile;
    }

    @Override
    public float getZ() {
        return zOrder;
    }

    public void addAggro(int counter) {
        aggroCount += counter;
    }

    // TODO: Debug stuff
    public void togglePlayerTile(boolean toggle) {
        togglePlayerTile = toggle;
    }
}
