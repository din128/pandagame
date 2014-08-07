package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;


/**
 * Created by Xombified on 7/27/2014.
 */
public class PlayerActor extends Actor {
    public int xTile;
    public int yTile;
    public int destXTile;
    public int destYTile;
    public PlayerStatus playerStatus;
    public Animation moveUpAnim;
    public Animation moveDownAnim;
    public Animation moveLeftAnim;
    public Animation moveRightAnim;
    private TextureAtlas textureAtlas;
    private int tilePixelWidth;
    private int tilePixelHeight;
    private float moveSpeed;
    private PlayerStatus nextMoveStatus = null;
    private float elapsedTime = 0;

    public PlayerActor(int x, int y, int tilePixelWidth, int tilePixelHeight) {
        xTile = x;
        yTile = y;
        this.tilePixelWidth = tilePixelWidth;
        this.tilePixelHeight = tilePixelHeight;
        moveSpeed = 0.2f;
        playerStatus = PlayerStatus.STANDING;
        setBounds(xTile * tilePixelWidth, yTile * tilePixelHeight, tilePixelWidth, tilePixelHeight);
        textureAtlas = new TextureAtlas(Gdx.files.internal("hero/heropack.atlas"));
        createAnimations(textureAtlas);
    }

    public void dispose() {
        textureAtlas.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        switch (playerStatus) {
            case STANDING:
                batch.draw(textureAtlas.findRegion("slice10"), getX(), getY());
                break;

            case MOVINGDOWN:
                batch.draw(moveDownAnim.getKeyFrame(elapsedTime, true), getX(), getY());
                break;

            case MOVINGLEFT:
                batch.draw(moveLeftAnim.getKeyFrame(elapsedTime, true), getX(), getY());
                break;

            case MOVINGRIGHT:
                batch.draw(moveRightAnim.getKeyFrame(elapsedTime, true), getX(), getY());
                break;

            case MOVINGUP:
                batch.draw(moveUpAnim.getKeyFrame(elapsedTime, true), getX(), getY());
                break;

            default:
                batch.draw(textureAtlas.findRegion("slice10"), getX(), getY());
        }
    }

    public void moveCoord(int[][] mapSteps, int destXTile, int destYTile, int numXTiles, int numYTiles) {
        this.destXTile = destXTile;
        this.destYTile = destYTile;
        int nextXTile = 999;
        int nextYTile = 999;
        int count = 999;
        SequenceAction sequenceAction = new SequenceAction();

        // Find shortest path
        while ((xTile != destXTile) || (yTile != destYTile)) {
            if (xTile - 1 >= 0) {
                if (count > mapSteps[xTile - 1][yTile] && mapSteps[xTile - 1][yTile] >= 0) {
                    count = mapSteps[xTile - 1][yTile];
                    nextXTile = xTile - 1;
                    nextYTile = yTile;
                    nextMoveStatus = PlayerStatus.MOVINGLEFT;
                }
            }

            if (xTile + 1 < numXTiles) {
                if (count > mapSteps[xTile + 1][yTile] && mapSteps[xTile + 1][yTile] >= 0) {
                    count = mapSteps[xTile + 1][yTile];
                    nextXTile = xTile + 1;
                    nextYTile = yTile;
                    nextMoveStatus = PlayerStatus.MOVINGRIGHT;
                }
            }

            if (yTile - 1 >= 0) {
                if (count > mapSteps[xTile][yTile - 1] && mapSteps[xTile][yTile - 1] >= 0) {
                    count = mapSteps[xTile][yTile - 1];
                    nextXTile = xTile;
                    nextYTile = yTile - 1;
                    nextMoveStatus = PlayerStatus.MOVINGDOWN;
                }
            }

            if (yTile + 1 < numYTiles) {
                if (count > mapSteps[xTile][yTile + 1] && mapSteps[xTile][yTile + 1] >= 0) {
                    count = mapSteps[xTile][yTile + 1];
                    nextXTile = xTile;
                    nextYTile = yTile + 1;
                    nextMoveStatus = PlayerStatus.MOVINGUP;
                }
            }

            // Add one action at a time for smooth walking
            sequenceAction.addAction(parallel(moveTo(nextXTile * tilePixelWidth, nextYTile * tilePixelHeight,
                    moveSpeed), run(new Runnable() {
                @Override
                public void run() {
                    switch (nextMoveStatus) {
                        case MOVINGDOWN:
                            playerStatus = PlayerStatus.MOVINGDOWN;
                            break;
                        case MOVINGLEFT:
                            playerStatus = PlayerStatus.MOVINGLEFT;
                            break;
                        case MOVINGRIGHT:
                            playerStatus = PlayerStatus.MOVINGRIGHT;
                            break;
                        case MOVINGUP:
                            playerStatus = PlayerStatus.MOVINGUP;
                            break;
                        default:
                            playerStatus = PlayerStatus.STANDING;
                    }
                }
            })));
            xTile = nextXTile;
            yTile = nextYTile;
        }
        this.addAction(sequenceAction);
    }

    private void createAnimations(TextureAtlas textureAtlas) {
        TextureRegion[] moveUp = new TextureRegion[3];
        TextureRegion[] moveDown = new TextureRegion[3];
        TextureRegion[] moveLeft = new TextureRegion[3];
        TextureRegion[] moveRight = new TextureRegion[3];
        float moveSpeed = 0.1f;

        // Move Up
        moveUp[0] = (textureAtlas.findRegion("slice13"));
        moveUp[1] = (textureAtlas.findRegion("slice14"));
        moveUp[2] = (textureAtlas.findRegion("slice15"));
        moveUpAnim = new Animation(moveSpeed, moveUp);

        // Move Down
        moveDown[0] = (textureAtlas.findRegion("slice09"));
        moveDown[1] = (textureAtlas.findRegion("slice10"));
        moveDown[2] = (textureAtlas.findRegion("slice11"));
        moveDownAnim = new Animation(moveSpeed, moveDown);

        // Move Left
        moveLeft[0] = (textureAtlas.findRegion("slice02"));
        moveLeft[1] = (textureAtlas.findRegion("slice03"));
        moveLeft[2] = (textureAtlas.findRegion("slice04"));
        moveLeftAnim = new Animation(moveSpeed, moveLeft);

        // Move Right
        moveRight[0] = (textureAtlas.findRegion("slice06"));
        moveRight[1] = (textureAtlas.findRegion("slice07"));
        moveRight[2] = (textureAtlas.findRegion("slice08"));
        moveRightAnim = new Animation(moveSpeed, moveRight);
    }

    public enum PlayerStatus {
        STANDING, MOVINGLEFT, MOVINGRIGHT, MOVINGUP, MOVINGDOWN
    }


}
