package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Timer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Xombified on 7/27/2014.
 */
public class PlayerActor extends Actor {
    public int xTile;
    public int yTile;
    public int destXTile;
    public int destYTile;

    private int tilePixelWidth;
    private int tilePixelHeight;
    private Texture playerTexture;
    private float moveSpeed;

    public PlayerActor(int x, int y, int tilePixelWidth, int tilePixelHeight, Texture playerTexture) {
        xTile = x;
        yTile = y;
        this.playerTexture = playerTexture;
        this.tilePixelWidth = tilePixelWidth;
        this.tilePixelHeight = tilePixelHeight;
        moveSpeed = 0.2f;
        setBounds(xTile * tilePixelWidth, yTile * tilePixelHeight, tilePixelWidth, tilePixelHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(playerTexture, getX(), getY());
    }

    public void moveCoord (int [][] mapSteps, int destXTile, int destYTile, int numXTiles, int numYTiles) {
        this.destXTile = destXTile;
        this.destYTile = destYTile;
        int nextXTile = 999;
        int nextYTile = 999;
        int count = 999;
        SequenceAction sequenceAction = new SequenceAction();

        // Find shortest path
        while ((xTile != destXTile) || (yTile != destYTile)) {
            if (xTile - 1 >= 0) {
                if (count > mapSteps[xTile-1][yTile] && mapSteps[xTile-1][yTile] >= 0) {
                    count = mapSteps[xTile-1][yTile];
                    nextXTile = xTile-1;
                    nextYTile = yTile;
                }
            }

            if (xTile + 1 < numXTiles) {
                if (count > mapSteps[xTile+1][yTile] && mapSteps[xTile+1][yTile] >= 0) {
                    count = mapSteps[xTile+1][yTile];
                    nextXTile = xTile+1;
                    nextYTile = yTile;
                }
            }

            if (yTile - 1 >= 0) {
                if (count > mapSteps[xTile][yTile-1] && mapSteps[xTile][yTile-1] >= 0) {
                    count = mapSteps[xTile][yTile-1];
                    nextXTile = xTile;
                    nextYTile = yTile-1;
                }
            }

            if (yTile + 1 < numYTiles) {
                if (count > mapSteps[xTile][yTile+1] && mapSteps[xTile][yTile+1] >= 0) {
                    count = mapSteps[xTile][yTile+1];
                    nextXTile = xTile;
                    nextYTile = yTile+1;
                }
            }
            xTile = nextXTile;
            yTile = nextYTile;

            // Add one action at a time for smooth walking
            sequenceAction.addAction(Actions.moveTo(nextXTile * tilePixelWidth, nextYTile * tilePixelHeight, moveSpeed));
        }
        this.addAction(sequenceAction);
    }
}
