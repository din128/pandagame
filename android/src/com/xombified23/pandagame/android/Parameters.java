package com.xombified23.pandagame.android;

/**
 * Created by Xombified on 8/9/2014.
 */
public final class Parameters {
    public final static int NUM_X_TILES = 5;
    public final static int NUM_Y_TILES = 6;
    public final static int TILE_PIXEL_WIDTH = 216;
    public final static int TILE_PIXEL_HEIGHT = 216;
    public final static int NUM_MONSTERS = 10;
    public final static int MAX_NUM_TILES = NUM_X_TILES * NUM_Y_TILES - 3;
    public final static int SCREEN_WIDTH = 1080;
    public final static int SCREEN_HEIGHT = 1920;

    // Z-Order Constant
    public final static int Z_BACKGROUND = 1;
    public final static int Z_FLOOR = 2;
    public final static int Z_MAIN = 3;
    public final static int Z_CHARACTERS = 10000; // TODO: Need to find a better way for this
}
