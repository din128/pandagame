package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GameScreen implements Screen {
    private final MyGame game;
    private Stage stage;
    private OrthographicCamera camera;
    private int mapPixelWidth;
    private int mapPixelHeight;
    private MainTileActor[][] mainTileActorMap;
    private FloorActor[][] floorActorMap;
    private int[][] mapSteps;
    private PlayerActor playerActor;
    private TextureAtlas floorAtlas;

    //private TiledMap tiledMap;
    //private TiledMapRenderer tiledMapRenderer;

    public GameScreen(final MyGame game) {
        // Assign Game and SpriteBatch object
        this.game = game;

        // Create New Objects
        stage = new Stage();
        camera = new OrthographicCamera();
        // tiledMap = new TmxMapLoader().load("MainMap.tmx");
        // tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        floorAtlas = new TextureAtlas(Gdx.files.internal("jei/PurpleTiles/PurpleTiles.atlas"));

        // Get dimensions for the tiledMap
        // MapProperties prop = tiledMap.getProperties();
        //Parameters.NUM_X_TILES = 216;
        //Parameters.NUM_Y_TILES = prop.get("height", Integer.class);
        //Parameters.TILE_PIXEL_WIDTH = prop.get("tilewidth", Integer.class);
        //Parameters.TILE_PIXEL_HEIGHT = prop.get("tileheight", Integer.class);
        mapPixelWidth = Parameters.NUM_X_TILES * Parameters.TILE_PIXEL_WIDTH;
        mapPixelHeight = Parameters.NUM_Y_TILES * Parameters.TILE_PIXEL_HEIGHT;

        // Initialize mapSteps
        mapSteps = new int[Parameters.NUM_X_TILES][Parameters.NUM_Y_TILES];
    }

    @Override
    public void dispose() {
        // tiledMap.dispose();
        playerActor.dispose();
        floorAtlas.dispose();
        for (int j = 0; j < Parameters.NUM_Y_TILES; j++) {
            for (int i = 0; i < Parameters.NUM_X_TILES; i++) {
                mainTileActorMap[i][j].dispose();
            }
        }

        stage.dispose();
    }

    @Override
    public void render(float delta) {
        camera.update();
        //tiledMapRenderer.setView(camera);
        //tiledMapRenderer.render();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        revealAround();
    }

    @Override
    public void resize(int width, int height) {
        // TODO: Temporally zoom in to the map
        camera.viewportWidth = width * 3;
        camera.viewportHeight = height * 3;
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
        createFloorTilesActors();
        createMainTilesActors();
        spawnPlayer();
        revealAround();
        spawnMonsters(Parameters.NUM_MONSTERS);

        // Add Stage Touch
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
    private void createMainTilesActors() {
        if (Parameters.NUM_X_TILES == 0 || Parameters.NUM_Y_TILES == 0) {
            throw new Error();
        }

        mainTileActorMap = new MainTileActor[Parameters.NUM_X_TILES][Parameters.NUM_Y_TILES];

        for (int j = 0; j < Parameters.NUM_Y_TILES; j++) {
            for (int i = 0; i < Parameters.NUM_X_TILES; i++) {
                mainTileActorMap[i][j] = new MainTileActor(i, j);
                stage.addActor(mainTileActorMap[i][j]);
            }
        }
    }

    /**
     * Create Floor UI Tiles and store them in an array
     */
    private void createFloorTilesActors() {
        if (Parameters.NUM_X_TILES == 0 || Parameters.NUM_Y_TILES == 0 || floorAtlas == null) {
            throw new Error();
        }

        floorActorMap = new FloorActor[Parameters.NUM_X_TILES][Parameters.NUM_Y_TILES];

        for (int j = 0; j < Parameters.NUM_Y_TILES; j++) {
            for (int i = 0; i < Parameters.NUM_X_TILES; i++) {
                Random rand = new Random();
                TextureRegion floorTile;
                switch (rand.nextInt(3)) {
                    case 0:
                        floorTile = floorAtlas.findRegion("Single_flatjpg");
                        break;
                    case 1:
                        floorTile = floorAtlas.findRegion("Single_rugged");
                        break;
                    default:
                        floorTile = floorAtlas.findRegion("Single_clean");
                }

                if (floorTile == null) {
                    throw new Error();
                }

                floorActorMap[i][j] = new FloorActor(i, j, floorTile);
                stage.addActor(floorActorMap[i][j]);
            }
        }
    }

    /**
     * Spawn monsters
     */
    private void spawnMonsters(int numMonsters) {
        if (numMonsters > (Parameters.NUM_X_TILES * Parameters.NUM_Y_TILES)) {
            throw new Error();
        }

        Random rand = new Random();
        int xTile;
        int yTile;
        int count = 0;

        while (count < numMonsters) {
            xTile = rand.nextInt(Parameters.NUM_X_TILES);
            yTile = rand.nextInt(Parameters.NUM_Y_TILES);

            if (!mainTileActorMap[xTile][yTile].isRevealed() && !mainTileActorMap[xTile][yTile].itContainsMonster()) {
                MonsterActor monsterActor = new MonsterActor(xTile, yTile);
                mainTileActorMap[xTile][yTile].setContainsMonster(true);
                stage.addActor(monsterActor);
                count++;
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

        playerActor = new PlayerActor(x, y);
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
                    if (currActor instanceof MainTileActor) {
                        System.out.println("MapActor clicked");
                        if (((MainTileActor) currActor).isRevealed()) {
                            movePlayer(((MainTileActor) currActor).xTile, ((MainTileActor) currActor).yTile);
                        }
                    } else {
                        // TODO: Abilities?
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
        if (playerActor.getActions().size == 0) {
            int pX = playerActor.xTile;
            int pY = playerActor.yTile;

            playerActor.playerStatus = PlayerActor.PlayerStatus.STANDING;

            // Reveal tiles with the player
            mainTileActorMap[pX][pY].setRevealed(true);
            if (pX - 1 >= 0) {
                mainTileActorMap[pX - 1][pY].setRevealed(true);
            }
            if (pY - 1 >= 0) {
                mainTileActorMap[pX][pY - 1].setRevealed(true);
            }
            if (pX + 1 < Parameters.NUM_X_TILES) {
                mainTileActorMap[pX + 1][pY].setRevealed(true);
            }
            if (pY + 1 < Parameters.NUM_Y_TILES) {
                mainTileActorMap[pX][pY + 1].setRevealed(true);
            }
        }
    }

    private void movePlayer(int destXTile, int destYTile) {
        if (playerActor.getActions().size == 0) {
            class Point {
                int x;
                int y;
            }
            int count = 0;

            // Reset mapSteps to check for shortest path
            for (int j = 0; j < Parameters.NUM_Y_TILES; j++) {
                for (int i = 0; i < Parameters.NUM_X_TILES; i++) {
                    mapSteps[i][j] = -1;
                }
            }

            Queue<Point> queue = new LinkedList<Point>();
            Point initPos = new Point();
            initPos.x = destXTile;
            initPos.y = destYTile;
            queue.add(initPos);

            // Add first tile weighted as 0. Subsequent tiles away from initial point, will increase "count"
            mapSteps[initPos.x][initPos.y] = 0;

            while (!queue.isEmpty()) {
                Point currPos = queue.poll();

                // If path is found, pass the mapSteps to let PlayerActor animate
                if (currPos.x == playerActor.xTile && currPos.y == playerActor.yTile) {
                    playerActor.moveCoord(mapSteps, destXTile, destYTile, Parameters.NUM_X_TILES, Parameters.NUM_Y_TILES);
                    break;
                }

                // Add an extra count for each step away from destination
                ++count;

                if ((currPos.x - 1 >= 0) && (mainTileActorMap[currPos.x - 1][currPos.y].isRevealed())
                        && !mainTileActorMap[currPos.x - 1][currPos.y].itContainsMonster()
                        && (mapSteps[currPos.x - 1][currPos.y] == -1)) {
                    Point newPos = new Point();
                    newPos.x = currPos.x - 1;
                    newPos.y = currPos.y;
                    queue.add(newPos);
                    mapSteps[newPos.x][newPos.y] = count;
                }

                if (currPos.x + 1 < Parameters.NUM_X_TILES && mainTileActorMap[currPos.x + 1][currPos.y].isRevealed()
                        && !mainTileActorMap[currPos.x + 1][currPos.y].itContainsMonster()
                        && mapSteps[currPos.x + 1][currPos.y] == -1) {
                    Point newPos = new Point();
                    newPos.x = currPos.x + 1;
                    newPos.y = currPos.y;
                    queue.add(newPos);
                    mapSteps[newPos.x][newPos.y] = count;
                }

                if (currPos.y - 1 >= 0 && mainTileActorMap[currPos.x][currPos.y - 1].isRevealed()
                        && !mainTileActorMap[currPos.x][currPos.y - 1].itContainsMonster()
                        && mapSteps[currPos.x][currPos.y - 1] == -1) {
                    Point newPos = new Point();
                    newPos.x = currPos.x;
                    newPos.y = currPos.y - 1;
                    queue.add(newPos);
                    mapSteps[newPos.x][newPos.y] = count;
                }

                if (currPos.y + 1 < Parameters.NUM_Y_TILES && mainTileActorMap[currPos.x][currPos.y + 1].isRevealed()
                        && !mainTileActorMap[currPos.x][currPos.y + 1].itContainsMonster()
                        && mapSteps[currPos.x][currPos.y + 1] == -1) {
                    Point newPos = new Point();
                    newPos.x = currPos.x;
                    newPos.y = currPos.y + 1;
                    queue.add(newPos);
                    mapSteps[newPos.x][newPos.y] = count;
                }
            } // end of while loop
        } // end of if
    }
}

