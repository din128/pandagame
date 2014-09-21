package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 *  Created by Xombified on 8/9/2014.
 */
public class BackgroundActor extends Actor {
    private Texture texture;

    public BackgroundActor(Texture texture) {
        this.texture = texture;

        // TODO: Use Gdx.graphics.getHeight() and getWidth()
        setBounds(0, 0, Parameters.SCREEN_WIDTH, Parameters.SCREEN_HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, 0, 0, Parameters.SCREEN_WIDTH, Parameters.SCREEN_HEIGHT);
    }


}
