package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.omg.Dynamic.Parameter;

/**
 * Created by Xombified on 8/9/2014.
 */
public class MonsterActor extends Actor {
    private int xTile;
    private int yTile;

    public MonsterActor (int x, int y) {
        xTile = x;
        yTile = y;
        setBounds(x * Parameters.tilePixelWidth, y * Parameters.tilePixelHeight, Parameters.tilePixelWidth,
                Parameters.tilePixelHeight);

    }
}
