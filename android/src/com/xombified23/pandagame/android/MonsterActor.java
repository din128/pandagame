package com.xombified23.pandagame.android;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class MonsterActor extends BaseActor {
    private int xTile;
    private int yTile;
    private Texture monsterTexture;
    private boolean isRevealed;
    private float zOrder;

    // TODO: USE INTERFACE for MainTileActor
    public MonsterActor(int xTile, int yTile, Texture monsterTexture) {
        this.xTile = xTile;
        this.yTile = yTile;
        isRevealed = false;
        this.monsterTexture = monsterTexture;
        setBounds(xTile * Parameters.TILE_PIXEL_WIDTH, yTile * Parameters.TILE_PIXEL_HEIGHT, Parameters.TILE_PIXEL_WIDTH,
                Parameters.TILE_PIXEL_HEIGHT);

        zOrder = Parameters.Z_CHARACTERS - getY();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isRevealed()) {
            batch.draw(monsterTexture, getX(), getY(), monsterTexture.getWidth() * 5, monsterTexture.getHeight() * 7);
        }
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
        setAggroPerimeter(1);
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

    public void removeMonster() {
        setAggroPerimeter(-1);
    }

    private void setAggroPerimeter(int counter) {
        if (xTile-1 >= 0 && !References.mainTileActorMap[xTile-1][yTile].itContainsWall())
            References.mainTileActorMap[xTile-1][yTile].addAggro(counter);

        if (xTile-1 >= 0 && yTile-1 >= 0 && !References.mainTileActorMap[xTile-1][yTile-1].itContainsWall())
            References.mainTileActorMap[xTile-1][yTile-1].addAggro(counter);

        if (yTile-1 >= 0 && !References.mainTileActorMap[xTile][yTile-1].itContainsWall())
            References.mainTileActorMap[xTile][yTile-1].addAggro(counter);

        if (xTile+1 < Parameters.NUM_X_TILES && yTile-1 >= 0 && !References.mainTileActorMap[xTile+1][yTile-1]
                .itContainsWall())
            References.mainTileActorMap[xTile+1][yTile-1].addAggro(counter);

        if (xTile+1 < Parameters.NUM_X_TILES && !References.mainTileActorMap[xTile+1][yTile].itContainsWall())
            References.mainTileActorMap[xTile+1][yTile].addAggro(counter);

        if (xTile+1 < Parameters.NUM_X_TILES && yTile+1 < Parameters.NUM_Y_TILES && !References
                .mainTileActorMap[xTile+1][yTile+1].itContainsWall())
            References.mainTileActorMap[xTile+1][yTile+1].addAggro(counter);

        if (yTile+1 < Parameters.NUM_Y_TILES && !References.mainTileActorMap[xTile][yTile+1].itContainsWall())
            References.mainTileActorMap[xTile][yTile+1].addAggro(counter);

        if (xTile-1 >= 0 && yTile+1 < Parameters.NUM_Y_TILES && !References.mainTileActorMap[xTile-1][yTile+1]
                .itContainsWall())
            References.mainTileActorMap[xTile-1][yTile+1].addAggro(counter);
    }
}
