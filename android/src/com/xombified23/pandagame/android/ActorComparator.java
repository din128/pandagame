package com.xombified23.pandagame.android;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Comparator;

public class ActorComparator implements Comparator<Actor> {
    @Override
    public int compare(Actor first, Actor second) {
        if (((BaseActor) first).getZ() < ((BaseActor) second).getZ()) {
            return -1;
        } else if (((BaseActor) first).getZ() > ((BaseActor) second).getZ()) {
            return 1;
        } else {
            return 0;
        }
    }
}
