package com.xombified23.pandagame.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;

// TODO: Using Player's sprite and animation for testing
public class MonsterActor extends BaseActor {
    private int xTile;
    private int yTile;
    private Skeleton skeleton;
    private AnimationState animState;
    private SkeletonRenderer renderer;
    private boolean isRevealed;
    private float zOrder;

    public MonsterActor(int xTile, int yTile, SpineObject spineObject) {
        this.xTile = xTile;
        this.yTile = yTile;
        isRevealed = false;
        skeleton = spineObject.skeleton;
        animState = spineObject.animState;
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(false);

        animState.setAnimation(0, "Dead", false);

        setBounds(xTile * Parameters.TILE_PIXEL_WIDTH, yTile * Parameters.TILE_PIXEL_HEIGHT, Parameters.TILE_PIXEL_WIDTH,
                Parameters.TILE_PIXEL_HEIGHT);

        zOrder = Parameters.Z_CHARACTERS - getY();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isRevealed()) {
            animState.update(Gdx.graphics.getDeltaTime());
            animState.apply(skeleton);
            skeleton.updateWorldTransform();
            skeleton.setPosition(getX() + Parameters.PLAYER_MARGINX_DEFAULT, getY() + Parameters.PLAYER_MARGINY);
            renderer.draw(batch, skeleton);
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
        if (xTile - 1 >= 0 && !References.mainTileActorMap[xTile - 1][yTile].itContainsWall())
            References.mainTileActorMap[xTile - 1][yTile].addAggro(counter);

        if (xTile - 1 >= 0 && yTile - 1 >= 0 && !References.mainTileActorMap[xTile - 1][yTile - 1].itContainsWall())
            References.mainTileActorMap[xTile - 1][yTile - 1].addAggro(counter);

        if (yTile - 1 >= 0 && !References.mainTileActorMap[xTile][yTile - 1].itContainsWall())
            References.mainTileActorMap[xTile][yTile - 1].addAggro(counter);

        if (xTile + 1 < Parameters.NUM_X_TILES && yTile - 1 >= 0 && !References.mainTileActorMap[xTile + 1][yTile - 1]
                .itContainsWall())
            References.mainTileActorMap[xTile + 1][yTile - 1].addAggro(counter);

        if (xTile + 1 < Parameters.NUM_X_TILES && !References.mainTileActorMap[xTile + 1][yTile].itContainsWall())
            References.mainTileActorMap[xTile + 1][yTile].addAggro(counter);

        if (xTile + 1 < Parameters.NUM_X_TILES && yTile + 1 < Parameters.NUM_Y_TILES && !References
                .mainTileActorMap[xTile + 1][yTile + 1].itContainsWall())
            References.mainTileActorMap[xTile + 1][yTile + 1].addAggro(counter);

        if (yTile + 1 < Parameters.NUM_Y_TILES && !References.mainTileActorMap[xTile][yTile + 1].itContainsWall())
            References.mainTileActorMap[xTile][yTile + 1].addAggro(counter);

        if (xTile - 1 >= 0 && yTile + 1 < Parameters.NUM_Y_TILES && !References.mainTileActorMap[xTile - 1][yTile + 1]
                .itContainsWall())
            References.mainTileActorMap[xTile - 1][yTile + 1].addAggro(counter);
    }
}
