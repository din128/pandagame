package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Xombified on 7/27/2014.
 */
public class PlayerActor extends Actor {
    public int xTile;
    public int yTile;
    public PlayerStatus playerStatus;

    private MapActor[][] mapActorList;
    private int destXTile;
    private int destYTile;
    private int nextXTile;
    private int nextYTile;
    private int tilePixelWidth;
    private int tilePixelHeight;
    private Texture playerTexture;
    private float moveSpeed;

    public PlayerActor(int x, int y, int tilePixelWidth, int tilePixelHeight, Texture playerTexture, MapActor[][] mapActorList) {
        xTile = x;
        yTile = y;
        this.mapActorList = mapActorList;
        this.playerTexture = playerTexture;
        this.tilePixelWidth = tilePixelWidth;
        this.tilePixelHeight = tilePixelHeight;
        playerStatus = PlayerStatus.STANDING;
        moveSpeed = 1.0f;
        setBounds(xTile * tilePixelWidth, yTile * tilePixelHeight, tilePixelWidth, tilePixelHeight);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        switch (playerStatus) {
            case MOVING:
                setBounds(nextXTile * tilePixelWidth, nextYTile * tilePixelHeight, tilePixelWidth, tilePixelHeight);
                this.addAction(Actions.moveTo(nextYTile * tilePixelWidth, nextYTile * tilePixelHeight, 0.5f));
                playerStatus = PlayerStatus.STANDING;
        }
        batch.draw(playerTexture, getX(), getY());
    }

    public enum PlayerStatus {
        STANDING, MOVING
    }

    public void moveCoord (int [][] mapSteps, int destXTile, int destYTile, int numXTiles, int numYTiles) {
        this.destXTile = destXTile;
        this.destYTile = destYTile;
        nextXTile = 99;
        nextYTile = 99;
        int count = 99;
        int currXTile = xTile;
        int currYTile = yTile;

        System.out.println ("moveCoord currXTile" + currXTile);
        System.out.println ("moveCoord currYTile" + currYTile);
        System.out.println ("moveCoord destXTile" + destXTile);
        System.out.println ("moveCoord destYTile" + destYTile);

        while ((currXTile != destXTile) || (currYTile != destYTile)) {
            System.out.println ("count" + count);

            // Move next path with lowest count
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

            playerStatus = PlayerStatus.MOVING;
            currXTile = nextXTile;
            currYTile = nextYTile;
            xTile = nextXTile;
            yTile = nextYTile;
        }
    }
}
