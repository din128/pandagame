package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by Xombified on 7/27/2014.
 */
public class MapActor extends Actor {
    private int x;
    private int y;
    private int tilePixelWidth;
    private int tilePixelHeight;
    private Texture mapTexture;
    private boolean isRevealed;

    public MapActor(int x, int y, int tilePixelWidth, int tilePixelHeight, Texture mapTexture) {
        this.x = x;
        this.y = y;
        this.tilePixelWidth = tilePixelWidth;
        this.tilePixelHeight = tilePixelHeight;
        this.mapTexture = mapTexture;
        isRevealed = false;

        // Set Map Actor boundaries
        setBounds(x * tilePixelWidth, y * tilePixelHeight, tilePixelWidth, tilePixelHeight);

        // Add Inputlistener
        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isRevealed) {
                    isRevealed = false;
                } else {
                    isRevealed = true;
                }
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isRevealed) {
            batch.setColor(0, 0, 0, 0f);
        } else {
            batch.setColor(0, 0, 0, 0.5f);
        }
        batch.draw(mapTexture, x * tilePixelWidth, y * tilePixelHeight);
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public boolean isRevealed() {
        return isRevealed;
    }


}
