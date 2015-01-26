package com.xombified23.pandagame.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.spine.*;

import java.util.Random;

/**
 * References:
 * Most Actors are singleton. Call this Reference to get their instances
 */
public class References {
    // Singleton Objects
    private static PlayerActor playerActor = null;
    private static MainTileActor[][] mainTileActorMap = null;
    private static MonsterActor[][] monsterActorMap = null;
    private static Stage stage = null;
    private static Group gameAreaGroup = null;
    private static WallActor[][] wallActorMap = null;

    // Textures
    private static TextureAtlas playerAtlas;
    private static TextureAtlas floorAtlas;
    private static Texture fogTexture;
    private static Texture blueTexture;
    private static Texture redTexture;
    private static Texture greenTexture;

    public static void dispose() {
        playerAtlas.dispose();
        fogTexture.dispose();
        blueTexture.dispose();
        redTexture.dispose();
        greenTexture.dispose();
        floorAtlas.dispose();

        playerActor = null;
        mainTileActorMap = null;
        monsterActorMap = null;
        stage = null;
        gameAreaGroup = null;
        wallActorMap = null;
    }

    public static Stage GetStage() {
        if (stage != null) {
            return stage;
        } else {
            stage = new Stage();
            return stage;
        }
    }

    public static Group GetUIGroup() {
        if (gameAreaGroup != null) {
            return gameAreaGroup;
        } else {
            gameAreaGroup = new Group();
            return gameAreaGroup;
        }
    }

    public static WallActor[][] GetWallActorMap() {
        if (wallActorMap != null) {
            return wallActorMap;
        } else {
            wallActorMap = new WallActor[Parameters.NUM_X_TILES][Parameters.NUM_Y_TILES];
            floorAtlas = new TextureAtlas(Gdx.files.internal("jei/StandardTiles/StandardTiles.atlas"));
            Random rand = new Random();
            int xTile;
            int yTile;
            int count = 0;

            // TODO: Need a better way to spawn walls based on counter
            while (count < Parameters.NUM_WALLS) {
                xTile = rand.nextInt(Parameters.NUM_X_TILES);
                yTile = rand.nextInt(Parameters.NUM_Y_TILES);

                if (!GetMainTileActorMap()[xTile][yTile].isRevealed() && !GetMainTileActorMap()[xTile][yTile].itContainsWall()
                        && !GetMainTileActorMap()[xTile][yTile].itContainsMonster()) {
                    TextureRegion floorTile;
                    switch (rand.nextInt(2)) {
                        case 0:
                            floorTile = floorAtlas.findRegion("wood");
                            break;
                        default:
                            floorTile = floorAtlas.findRegion("locker");
                    }

                    wallActorMap[xTile][yTile] = new WallActor(xTile, yTile, floorTile);
                    GetMainTileActorMap()[xTile][yTile].setContainsWall(true);
                    GetUIGroup().addActor(wallActorMap[xTile][yTile]);
                    count++;
                }
            }
            return wallActorMap;
        }
    }

    public static MainTileActor[][] GetMainTileActorMap() {
        if (mainTileActorMap != null) {
            return mainTileActorMap;
        } else {
            fogTexture = new Texture(Gdx.files.internal("others/blacktile.png"));
            redTexture = new Texture(Gdx.files.internal("others/redtile.png"));
            blueTexture = new Texture(Gdx.files.internal("others/bluetile.png"));
            greenTexture = new Texture(Gdx.files.internal("others/greentile.png"));
            mainTileActorMap = new MainTileActor[Parameters.NUM_X_TILES][Parameters.NUM_Y_TILES];

            for (int j = 0; j < Parameters.NUM_Y_TILES; j++) {
                for (int i = 0; i < Parameters.NUM_X_TILES; i++) {
                    mainTileActorMap[i][j] = new MainTileActor(i, j, fogTexture, blueTexture, redTexture, greenTexture);
                    GetUIGroup().addActor(mainTileActorMap[i][j]);
                }
            }
            return mainTileActorMap;
        }
    }

    public static MonsterActor[][] GetMonsterActorMap() {
        if (monsterActorMap != null) {
            return monsterActorMap;
        } else {
            monsterActorMap = new MonsterActor[Parameters.NUM_X_TILES][Parameters.NUM_Y_TILES];
            Random rand = new Random();
            int xTile;
            int yTile;
            int count = 0;
            FileHandle jsonSkeleton = Gdx.files.internal("jei/Warrior2/skeleton.json");
            playerAtlas = new TextureAtlas(Gdx.files.internal("jei/Warrior2/atlas/Warrior_2_Atlas.atlas"));

            // TODO: Need a better way to spawn monsters based on counter
            while (count < Parameters.NUM_MONSTERS) {
                xTile = rand.nextInt(Parameters.NUM_X_TILES);
                yTile = rand.nextInt(Parameters.NUM_Y_TILES);

                if (!GetMainTileActorMap()[xTile][yTile].isRevealed() && !GetMainTileActorMap()[xTile][yTile].itContainsMonster()
                        && !GetMainTileActorMap()[xTile][yTile].itContainsWall()) {

                    SpineObject spineObject = createAnimations(playerAtlas, jsonSkeleton); // create animation skeleton
                    monsterActorMap[xTile][yTile] = new MonsterActor(xTile, yTile, spineObject);
                    GetMainTileActorMap()[xTile][yTile].setContainsMonster(true);
                    GetUIGroup().addActor(monsterActorMap[xTile][yTile]);
                    count++;
                }
            }
            return monsterActorMap;
        }
    }

    public static PlayerActor GetPlayerActor() {
        if (playerActor != null) {
            return playerActor;
        } else {
            Random rand = new Random();
            int randomInt = rand.nextInt(4);
            int x;
            int y;

            switch (randomInt) {
                case 0:
                    x = 0;
                    y = 0;
                    break;
                case 1:
                    x = 0;
                    y = Parameters.NUM_Y_TILES - 1;
                    break;
                case 2:
                    x = Parameters.NUM_X_TILES - 1;
                    y = 0;
                    break;
                default:
                    x = Parameters.NUM_X_TILES - 1;
                    y = Parameters.NUM_Y_TILES - 1;
                    break;
            }
            FileHandle jsonSkeleton = Gdx.files.internal("jei/Warrior2/skeleton.json");
            playerAtlas = new TextureAtlas(Gdx.files.internal("jei/Warrior2/atlas/Warrior_2_Atlas.atlas"));
            SpineObject spineObject = createAnimations(playerAtlas, jsonSkeleton); // create animation skeleton

            playerActor = new PlayerActor(x, y, spineObject);
            GetUIGroup().addActor(playerActor);
            return playerActor;
        }
    }

    private static SpineObject createAnimations(TextureAtlas textureAtlas, FileHandle jsonSkeleton) {
        SkeletonJson json = new SkeletonJson(textureAtlas);
        json.setScale(Parameters.CHARACTER_SCALE);
        SkeletonData skeletonData = json.readSkeletonData(jsonSkeleton);
        AnimationStateData stateData = new AnimationStateData(skeletonData);

        Skeleton skeleton = new Skeleton(skeletonData);
        AnimationState animState = new AnimationState(stateData);
        // animState.setTimeScale(0.5f);
        SpineObject spineObject = new SpineObject();
        spineObject.skeleton = skeleton;
        spineObject.animState = animState;

        return spineObject;
    }
}
