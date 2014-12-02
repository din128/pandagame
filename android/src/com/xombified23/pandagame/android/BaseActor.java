package com.xombified23.pandagame.android;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Xombified on 12/1/2014.
 */
public abstract class BaseActor extends Actor {
    abstract public int getXTile();
    abstract public int getYTile();
    abstract public float getZ();
}
