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
        SequenceAction sequenceAction = new SequenceAction();

        while ((currXTile != destXTile) || (currYTile != destYTile)) {

            // Move next path with lowest count
            if (currXTile - 1 >= 0) {
                if (count > mapSteps[currXTile-1][currYTile] && mapSteps[currXTile-1][currYTile] >= 0) {
                    count = mapSteps[currXTile-1][currYTile];
                    nextXTile = currXTile-1;
                    nextYTile = currYTile;
                    System.out.println("HERE1");
                }
            }

            if (currXTile + 1 < numXTiles) {
                if (count > mapSteps[currXTile+1][currYTile] && mapSteps[currXTile+1][currYTile] >= 0) {
                    count = mapSteps[currXTile+1][currYTile];
                    nextXTile = currXTile+1;
                    nextYTile = currYTile;
                    System.out.println("HERE2");
                }
            }

            if (currYTile - 1 >= 0) {
                if (count > mapSteps[currXTile][currYTile-1] && mapSteps[currXTile][currYTile-1] >= 0) {
                    count = mapSteps[currXTile][currYTile-1];
                    nextXTile = currXTile;
                    nextYTile = currYTile-1;
                    System.out.println("HERE3");
                }
            }

            System.out.println("NEW currYTile " + currYTile);
            if (currYTile + 1 < numYTiles) {
                if (count > mapSteps[currXTile][currYTile+1] && mapSteps[currXTile][currYTile+1] >= 0) {
                    count = mapSteps[currXTile][currYTile+1];
                    nextXTile = currXTile;
                    nextYTile = currYTile+1;
                    System.out.println("HERE4");
                }
            }

            currXTile = nextXTile;
            currYTile = nextYTile;

            System.out.println("nextXTile " + nextXTile);
            System.out.println("nextYTile " + nextYTile);

            // System.out.println("this.getActions().size =  " + this.getActions().size);
            sequenceAction.addAction(Actions.moveTo(nextXTile * tilePixelWidth, nextYTile * tilePixelHeight, 0.5f));


        }

        this.addAction(sequenceAction);
        xTile = nextXTile;
        yTile = nextYTile;
        // setBounds(nextXTile * tilePixelWidth, nextYTile * tilePixelHeight, tilePixelWidth, tilePixelHeight);
        System.out.println("################");
    }

}
