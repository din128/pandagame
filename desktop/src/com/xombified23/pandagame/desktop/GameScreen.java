package com.xombified23.pandagame.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.EmptyStackException;
import java.util.Random;

public class GameScreen implements Screen {
    private final MyGame game;
    private final SpriteBatch batch;
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

    public GameScreen(final MyGame game) {
        // Assign Game and SpriteBatch object
        this.game = game;
        this.batch = game.batch;

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
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Temporally zoom in to the map
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
     * Custom methods start here
     */

    /**
     * Create Actors for Tiled Map
     */
    public void createMapActors() {
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
    public void spawnPlayer() {
        Random rand = new Random();
        int randomInt = rand.nextInt(4);
        float x;
        float y;

        switch (randomInt) {
            case 0:
                x = mapActorList[0][0].getX();
                y = mapActorList[0][0].getY();
                break;
            case 1:
                x = mapActorList[0][numYTiles - 1].getX();
                y = mapActorList[0][numYTiles - 1].getY();
                break;
            case 2:
                x = mapActorList[numXTiles - 1][0].getX();
                y = mapActorList[numXTiles - 1][0].getY();
                break;
            default:
                x = mapActorList[numXTiles - 1][numYTiles - 1].getX();
                y = mapActorList[numXTiles - 1][numYTiles - 1].getY();
                break;
        }

        PlayerActor playerActor = new PlayerActor(x, y, tilePixelWidth, tilePixelHeight, playerTexture);
        stage.addActor(playerActor);
    }
}

