package com.xombified23.pandagame.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;

import java.util.LinkedList;
import java.util.Queue;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

// TODO: Need to add confront Enemies (face enemies) during combat
public class PlayerActor extends BaseActor {
    private int xTile;
    private int yTile;
    private PlayerStatus playerStatus;
    private PlayerStatus nextMoveStatus;
    private Queue<PlayerStatus> queueMoves;
    private SequenceAction moveAction;
    private float zOrder;
    private Skeleton skeleton;
    private AnimationState animState;
    private SkeletonRenderer renderer;
    private float marginX = Parameters.PLAYER_MARGINX_DEFAULT;
    private boolean inCombat = false;

    public PlayerActor(int x, int y, SpineObject spineObject) {
        xTile = x;
        yTile = y;
        nextMoveStatus = null;
        queueMoves = new LinkedList<PlayerStatus>();
        moveAction = new SequenceAction();
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(false);
        skeleton = spineObject.skeleton;
        animState = spineObject.animState;
        zOrder = Parameters.Z_CHARACTERS - getY(); // Dynamic Z-Order for players

        animState.setAnimation(0, "Standing", true);

        playerStatus = PlayerStatus.STANDING;
        setBounds(xTile * Parameters.TILE_PIXEL_WIDTH, yTile * Parameters.TILE_PIXEL_HEIGHT, Parameters.TILE_PIXEL_WIDTH,
                Parameters.TILE_PIXEL_HEIGHT);

        if (References.GetMainTileActorMap() == null) {
            throw new Error();
        } else {
            References.GetMainTileActorMap()[xTile][yTile].setRevealed(true);
            revealAround();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        zOrder = Parameters.Z_CHARACTERS - getY();
        animState.update(Gdx.graphics.getDeltaTime());

        animState.apply(skeleton);
        skeleton.updateWorldTransform();
        skeleton.setPosition(getX() + marginX, getY() + Parameters.PLAYER_MARGINY);
        renderer.draw(batch, skeleton);
    }

    public void movePlayer(int[][] mapSteps, int destXTile, int destYTile) {
        if (inCombat) {
            return;
        }

        int nextXTile = xTile;
        int nextYTile = yTile;
        int count = 1000; // TODO: Need to make this count more stable

        moveAction.reset();
        queueMoves.clear();

        // Find shortest path
        while ((xTile != destXTile) || (yTile != destYTile)) {
            if (xTile - 1 >= 0 && count > mapSteps[xTile - 1][yTile] && mapSteps[xTile - 1][yTile] >= 0) {
                count = mapSteps[xTile - 1][yTile];
                nextXTile = xTile - 1;
                nextYTile = yTile;
                nextMoveStatus = PlayerStatus.MOVINGLEFT;
            }

            if (xTile + 1 < Parameters.NUM_X_TILES && count > mapSteps[xTile + 1][yTile] && mapSteps[xTile +
                    1][yTile] >= 0) {
                count = mapSteps[xTile + 1][yTile];
                nextXTile = xTile + 1;
                nextYTile = yTile;
                nextMoveStatus = PlayerStatus.MOVINGRIGHT;
            }

            if (yTile - 1 >= 0 && count > mapSteps[xTile][yTile - 1] && mapSteps[xTile][yTile - 1] >= 0) {
                count = mapSteps[xTile][yTile - 1];
                nextXTile = xTile;
                nextYTile = yTile - 1;
                nextMoveStatus = PlayerStatus.MOVINGDOWN;
            }

            if (yTile + 1 < Parameters.NUM_Y_TILES && count > mapSteps[xTile][yTile + 1] && mapSteps[xTile][yTile +
                    1] >= 0) {
                count = mapSteps[xTile][yTile + 1];
                nextXTile = xTile;
                nextYTile = yTile + 1;
                nextMoveStatus = PlayerStatus.MOVINGUP;
            }

            queueMoves.add(nextMoveStatus);

            // Add one action at a time for smooth walking
            moveAction.addAction(parallel(moveTo(nextXTile * Parameters.TILE_PIXEL_WIDTH,
                    nextYTile * Parameters.TILE_PIXEL_HEIGHT,
                    Parameters.PLAYER_MOVE_SPEED), run(new Runnable() {
                @Override
                public void run() {
                    PlayerStatus moveStatus = queueMoves.poll();

                    switch (moveStatus) {
                        case MOVINGDOWN:
                            if (playerStatus == PlayerStatus.STANDING) {
                                animState.setAnimation(0, "Walking", true);
                            }

                            playerStatus = PlayerStatus.MOVINGDOWN;
                            break;

                        case MOVINGLEFT:
                            if (playerStatus == PlayerStatus.STANDING) {
                                animState.setAnimation(0, "Walking", true);
                            }
                            skeleton.setFlipX(true);
                            marginX = Parameters.PLAYER_MARGINX_FLIPPED;
                            playerStatus = PlayerStatus.MOVINGLEFT;
                            break;

                        case MOVINGRIGHT:
                            if (playerStatus == PlayerStatus.STANDING) {
                                animState.setAnimation(0, "Walking", true);
                            }
                            skeleton.setFlipX(false);
                            marginX = Parameters.PLAYER_MARGINX_DEFAULT;
                            playerStatus = PlayerStatus.MOVINGRIGHT;
                            break;

                        case MOVINGUP:
                            if (playerStatus == PlayerStatus.STANDING) {
                                animState.setAnimation(0, "Walking", true);
                            }
                            playerStatus = PlayerStatus.MOVINGUP;
                            break;
                        default:
                            if (playerStatus != PlayerStatus.STANDING) {
                                animState.setAnimation(0, "Standing", true);
                            }

                            playerStatus = PlayerStatus.STANDING;
                    }
                }
            })));

            // Update tiles
            xTile = nextXTile;
            yTile = nextYTile;
        }

        // Queue Standing action after movements are completed
        moveAction.addAction(run(new Runnable() {
            @Override
            public void run() {
                playerStatus = PlayerStatus.STANDING;
                animState.setAnimation(0, "Standing", true);
                revealAround();
            }
        }));

        this.addAction(moveAction); // Add the moveAction to the main action
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

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public enum PlayerStatus {
        STANDING, MOVINGLEFT, MOVINGRIGHT, MOVINGUP, MOVINGDOWN
    }

    /**
     * Reveal around the player
     */
    private void revealAround() {
        if (xTile - 1 >= 0 && !References.GetMainTileActorMap()[xTile - 1][yTile].isRevealed()) {
            References.GetMainTileActorMap()[xTile - 1][yTile].setRevealed(true);

            if (References.GetMainTileActorMap()[xTile - 1][yTile].itContainsMonster()) {
                References.GetMonsterActorMap()[xTile - 1][yTile].setRevealed(true);
            }
        }
        if (yTile - 1 >= 0 && !References.GetMainTileActorMap()[xTile][yTile - 1].isRevealed()) {
            References.GetMainTileActorMap()[xTile][yTile - 1].setRevealed(true);

            if (References.GetMainTileActorMap()[xTile][yTile - 1].itContainsMonster()) {
                References.GetMonsterActorMap()[xTile][yTile - 1].setRevealed(true);
            }
        }
        if (xTile + 1 < Parameters.NUM_X_TILES && !References.GetMainTileActorMap()[xTile + 1][yTile].isRevealed()) {
            References.GetMainTileActorMap()[xTile + 1][yTile].setRevealed(true);

            if (References.GetMainTileActorMap()[xTile + 1][yTile].itContainsMonster()) {
                References.GetMonsterActorMap()[xTile + 1][yTile].setRevealed(true);
            }
        }
        if (yTile + 1 < Parameters.NUM_Y_TILES && !References.GetMainTileActorMap()[xTile][yTile + 1].isRevealed()) {
            References.GetMainTileActorMap()[xTile][yTile + 1].setRevealed(true);

            if (References.GetMainTileActorMap()[xTile][yTile + 1].itContainsMonster()) {
                References.GetMonsterActorMap()[xTile][yTile + 1].setRevealed(true);
            }
        }
    }

    public void setInCombat(boolean inCombat) {
        this.inCombat = inCombat;
        References.GetMainTileActorMap()[xTile][yTile].togglePlayerTile(inCombat);
    }

    public void attack() {
        animState.setAnimation(0, "Attack", false);
    }
}
