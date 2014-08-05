package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.*;

public class GameScreen implements Screen {
    private final MyGame game;
    private Stage stage;
    private OrthographicCamera camera;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private int mapPixelWidth;
    private int mapPixelHeight;
    private int numXTiles;
    private int numYTiles;
    private int tilePixelWidth;
    private int tilePixelHeight;
    private MapActor[][] mapActorList;
    private Texture mapTexture;
    private Texture playerTexture;
    private PlayerActor playerActor;

    public GameScreen(final MyGame game) {
        // Assign Game and SpriteBatch object
        this.game = game;

        // Create New Objects
        stage = new Stage();
        camera = new OrthographicCamera();
        tiledMap = new TmxMapLoader().load("MainMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        mapTexture = new Texture(Gdx.files.internal("blacktile.png"));
        playerTexture = new Texture(Gdx.files.internal("playerSprite.png"));

        // Get dimensions for the tiledMap
        MapProperties prop = tiledMap.getProperties();
        numXTiles = prop.get("width", Integer.class);
        numYTiles = prop.get("height", Integer.class);
        tilePixelWidth = prop.get("tilewidth", Integer.class);
        tilePixelHeight = prop.get("tileheight", Integer.class);
        mapPixelWidth = numXTiles * tilePixelWidth;
        mapPixelHeight = numYTiles * tilePixelHeight;
    }

    @Override
    public void dispose() {
        stage.dispose();
        tiledMap.dispose();
        mapTexture.dispose();
        playerTexture.dispose();
    }

    @Override
    public void render(float delta) {
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        revealAround();
        keepTrackMoves();
    }

    @Override
    public void resize(int width, int height) {
        // TODO: Temporally zoom in to the map
        camera.viewportWidth = width / 2;
        camera.viewportHeight = height / 2;
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void show() {
        // Setup camera to align Stage with Tiled Map
        camera.position.set(mapPixelWidth / 2, mapPixelHeight / 2, 0);
        stage.getViewport().setCamera(camera);

        // Add input interface to the Stage
        Gdx.input.setInputProcessor(stage);

        // Inflate rest of Actors
        createMapActors();
        spawnPlayer();
        addStageTouch();
    }

    @Override
    public void pause() {
        // Irrelevant on desktop, ignore this
    }

    @Override
    public void resume() {
        // Irrelevant on desktop, ignore this
    }

    /**
     * Create Actors for Tiled Map
     */
    private void createMapActors() {
        if (numXTiles == 0 || numYTiles == 0) {
            throw new EmptyStackException();
        }

        mapActorList = new MapActor[numXTiles][numYTiles];

        for (int j = 0; j < numYTiles; j++) {
            for (int i = 0; i < numXTiles; i++) {
                mapActorList[i][j] = new MapActor(i, j, tilePixelWidth, tilePixelHeight, mapTexture);
                stage.addActor(mapActorList[i][j]);
            }
        }
    }

    /**
     * Spawn player at either corner
     */
    private void spawnPlayer() {
        Random rand = new Random();
        int randomInt = rand.nextInt(4);
        int x;
        int y;

        switch (0) {
            case 0:
                x = 0;
                y = 0;
                break;
            case 1:
                x = 0;
                y = numYTiles - 1;
                break;
            case 2:
                x = numXTiles - 1;
                y = 0;
                break;
            default:
                x = numXTiles - 1;
                y = numYTiles - 1;
                break;
        }

        playerActor = new PlayerActor(x, y, tilePixelWidth, tilePixelHeight, playerTexture, mapActorList);
        stage.addActor(playerActor);
    }

    /**
     * Add Input Listener to the Stage
     */
    private void addStageTouch() {
        stage.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Actor currActor = stage.hit(x, y, true);
                if (currActor != null) {
                    if (currActor instanceof MapActor) {
                        System.out.println("MapActor clicked");
                        if (((MapActor) currActor).isRevealed()) {
                            movePlayer(((MapActor) currActor).xTile, ((MapActor) currActor).yTile);
                        }
                    } else {
                        // TODO
                        System.out.println("PlayerActor clicked");
                    }
                }
                return true;
            }
        });
    }

    /**
     * Reveal around the player
     */
    private void revealAround() {
        int pX = playerActor.xTile;
        int pY = playerActor.yTile;

        // Reveal tiles with the player
        mapActorList[pX][pY].setRevealed(true);
        if (pX - 1 >= 0) {
            mapActorList[pX - 1][pY].setRevealed(true);
        }
        if (pY - 1 >= 0) {
            mapActorList[pX][pY - 1].setRevealed(true);
        }
        if (pX + 1 < numXTiles) {
            mapActorList[pX + 1][pY].setRevealed(true);
        }
        if (pY + 1 < numYTiles) {
            mapActorList[pX][pY + 1].setRevealed(true);
        }
    }

    private void keepTrackMoves() {
        int pX = playerActor.xTile;
        int pY = playerActor.yTile;

        mapActorList[pX][pY].setVisited(true);
    }

    private void movePlayer(int destXTile, int destYTile) {
        if (playerActor.getActions().size == 0) {
            class Point {
                int x;
                int y;
            }
            int count = 0;
            int[][] mapSteps = new int[numXTiles][numYTiles];
            for (int j = 0; j < numYTiles; j++) {
                for (int i = 0; i < numXTiles; i++) {
                    mapSteps[i][j] = -1;
                }
            }

            Stack stack = new Stack();
            Point initPos = new Point();
            initPos.x = destXTile;
            initPos.y = destYTile;
            stack.push(initPos);

            // Make new destination point as visited to allow pathfinding
            mapActorList[initPos.x][initPos.y].setVisited(true);
            mapSteps[initPos.x][initPos.y] = 0;

            while (!stack.isEmpty()) {
                Point currPos = (Point) stack.pop();

                //System.out.println ("currPos.x = " + currPos.x);
                //System.out.println ("currPos.y = " + currPos.y);
                //System.out.println ("#########");
                //System.out.println ("mapActorList[currPos.x - 1][currPos.y].isVisited() = "
                //        + mapActorList[currPos.x - 1][currPos.y].isVisited());
                //System.out.println ("mapSteps[currPos.x - 1][currPos.y] = "
                //        + mapSteps[currPos.x - 1][currPos.y]);

                if (currPos.x == playerActor.xTile && currPos.y == playerActor.yTile) {
                    playerActor.moveCoord(mapSteps, destXTile, destYTile, numXTiles, numYTiles);
                    break;
                }

                // Add an extra count for each step away from destination
                ++count;

                if ((currPos.x - 1 >= 0) && (mapActorList[currPos.x - 1][currPos.y].isVisited())
                        && (mapSteps[currPos.x - 1][currPos.y] == -1)) {
                    Point newPos = new Point();
                    newPos.x = currPos.x - 1;
                    newPos.y = currPos.y;
                    stack.push(newPos);
                    mapSteps[newPos.x][newPos.y] = count;
                }

                if (currPos.x + 1 < numXTiles && mapActorList[currPos.x + 1][currPos.y].isVisited()
                        && mapSteps[currPos.x + 1][currPos.y] == -1) {
                    Point newPos = new Point();
                    newPos.x = currPos.x + 1;
                    newPos.y = currPos.y;
                    stack.push(newPos);
                    mapSteps[newPos.x][newPos.y] = count;

                    System.out.println("newPos.x" + newPos.x);
                    System.out.println("newPos.y" + newPos.y);
                }

                if (currPos.y - 1 >= 0 && mapActorList[currPos.x][currPos.y - 1].isVisited()
                        && mapSteps[currPos.x][currPos.y - 1] == -1) {
                    Point newPos = new Point();
                    newPos.x = currPos.x;
                    newPos.y = currPos.y - 1;
                    stack.push(newPos);
                    mapSteps[newPos.x][newPos.y] = count;

                    System.out.println("newPos.x" + newPos.x);
                    System.out.println("newPos.y" + newPos.y);
                }

                if (currPos.y + 1 < numYTiles && mapActorList[currPos.x][currPos.y + 1].isVisited()
                        && mapSteps[currPos.x][currPos.y + 1] == -1) {
                    Point newPos = new Point();
                    newPos.x = currPos.x;
                    newPos.y = currPos.y + 1;
                    stack.push(newPos);
                    mapSteps[newPos.x][newPos.y] = count;

                    System.out.println("newPos.x" + newPos.x);
                    System.out.println("newPos.y" + newPos.y);
                }

                // System.out.println ("Loop ends");
            }
        }
    }
}

