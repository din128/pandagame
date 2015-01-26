package com.xombified23.pandagame.android;

public final class Parameters {
    public final static int NUM_X_TILES = 5;
    public final static int NUM_Y_TILES = 6;
    public final static int TILE_PIXEL_WIDTH = 216;
    public final static int TILE_PIXEL_HEIGHT = 216;
    public final static int NUM_MONSTERS = 10;
    public final static int NUM_WALLS = 2;
    public final static int SCREEN_WIDTH = 1080;
    public final static int SCREEN_HEIGHT = 1920;
    public final static float CHARACTER_SCALE = 0.25f;

    // Player Constants
    public final static float PLAYER_MOVE_SPEED = 0.5f;
    public final static float PLAYER_MARGINY = 20;
    public final static float PLAYER_MARGINX_DEFAULT = 80f;
    public final static float PLAYER_MARGINX_FLIPPED = 120f;

    // Z-Order Constant
    public final static int Z_BACKGROUND = 1;
    public final static int Z_FLOOR = 2;
    public final static int Z_MAIN = 3;
    public final static int Z_CHARACTERS = 10000; // TODO: Need to find a better way for this
}
