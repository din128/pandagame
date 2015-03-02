package com.xombified23.pandagame.android.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.xombified23.pandagame.android.Tools.Parameters;
import com.xombified23.pandagame.android.Tools.SpineObject;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

// TODO: Need to add confront Enemies (face enemies) during combat
public class PlayerActor extends BaseActor {
    private int xTile;
    private int yTile;
    private PlayerStatus playerStatus;
    private float zOrder;
    private Skeleton skeleton;
    private AnimationState animState;
    private SkeletonRenderer renderer;
    private float marginX = Parameters.PLAYER_MARGINX_DEFAULT;
    private Queue<Action> queueAction;

    public PlayerActor(int x, int y, SpineObject spineObject) {
        xTile = x;
        yTile = y;
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(false);
        skeleton = spineObject.skeleton;
        animState = spineObject.animState;
        zOrder = Parameters.Z_CHARACTERS - getY(); // Dynamic Z-Order for players
        queueAction = new ConcurrentLinkedQueue<Action>();

        animState.setAnimation(0, "Standing", true);

        playerStatus = PlayerStatus.STANDING;
        setBounds(xTile * Parameters.TILE_PIXEL_WIDTH, yTile * Parameters.TILE_PIXEL_HEIGHT, Parameters.TILE_PIXEL_WIDTH,
                Parameters.TILE_PIXEL_HEIGHT);

        SingletonActors.GetMainTileActorMap()[xTile][yTile].setRevealed(true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // TODO: Fix bug moving too fast sometimes
        if (!queueAction.isEmpty() && this.getActions().size == 0) {
            this.addAction(queueAction.poll());
        }

        zOrder = Parameters.Z_CHARACTERS - getY();
        animState.update(Gdx.graphics.getDeltaTime());

        animState.apply(skeleton);
        skeleton.updateWorldTransform();
        skeleton.setPosition(getX() + marginX, getY() + Parameters.PLAYER_MARGINY);
        renderer.draw(batch, skeleton);
    }

    public void movePlayer (int[][] mapSteps, int destXTile, int destYTile) {
        queueAction.clear();

        int shortestStep = Integer.MAX_VALUE;
        int currXTile = xTile;
        int currYTile = yTile;
        int nextXTile = xTile;
        int nextYTile = yTile;
        PlayerStatus nextMoveStatus = PlayerStatus.STANDING;

        while ((currXTile != destXTile) || (currYTile != destYTile)) {
            if (currXTile - 1 >= 0 && shortestStep > mapSteps[currXTile - 1][currYTile] && mapSteps[currXTile - 1][currYTile] >= 0) {
                shortestStep = mapSteps[currXTile - 1][currYTile];
                nextXTile = currXTile - 1;
                nextYTile = currYTile;
                nextMoveStatus = PlayerStatus.MOVINGLEFT;
            }

            if (currXTile + 1 < Parameters.NUM_X_TILES && shortestStep > mapSteps[currXTile + 1][currYTile] && mapSteps[currXTile +
                    1][currYTile] >= 0) {
                shortestStep = mapSteps[currXTile + 1][currYTile];
                nextXTile = currXTile + 1;
                nextYTile = currYTile;
                nextMoveStatus = PlayerStatus.MOVINGRIGHT;
            }

            if (currYTile - 1 >= 0 && shortestStep > mapSteps[currXTile][currYTile - 1] && mapSteps[currXTile][currYTile - 1] >= 0) {
                shortestStep = mapSteps[currXTile][currYTile - 1];
                nextXTile = currXTile;
                nextYTile = currYTile - 1;
                nextMoveStatus = PlayerStatus.MOVINGDOWN;
            }

            if (currYTile + 1 < Parameters.NUM_Y_TILES && shortestStep > mapSteps[currXTile][currYTile + 1] && mapSteps[currXTile][currYTile +
                    1] >= 0) {
                shortestStep = mapSteps[currXTile][currYTile + 1];
                nextXTile = currXTile;
                nextYTile = currYTile + 1;
                nextMoveStatus = PlayerStatus.MOVINGUP;
            }
            queueAction.add(constructMoveAction(nextXTile, nextYTile, nextMoveStatus));
            currXTile = nextXTile;
            currYTile = nextYTile;
        }

        ParallelAction standAction = new ParallelAction();
        standAction.addAction(run(new Runnable() {
            @Override
            public void run() {
                playerStatus = PlayerStatus.STANDING;
                animState.setAnimation(0, "Standing", true);
            }
        }));

        queueAction.add(standAction);
    }

    private Action constructMoveAction(final int nextXTile, final int nextYTile, final PlayerStatus nextMoveStatus) {
        ParallelAction action = new ParallelAction();
        action.addAction(parallel(moveTo(nextXTile * Parameters.TILE_PIXEL_WIDTH,
                nextYTile * Parameters.TILE_PIXEL_HEIGHT,
                Parameters.PLAYER_MOVE_SPEED), run(new Runnable() {
            @Override
            public void run() {
                xTile = nextXTile;
                yTile = nextYTile;

                switch (nextMoveStatus) {
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

        return action;
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

    public void setInCombat(boolean inCombat) {
        SingletonActors.GetMainTileActorMap()[xTile][yTile].togglePlayerTile(inCombat);
    }

    public void attack() {
        animState.setAnimation(0, "Attack", false);
    }
}
