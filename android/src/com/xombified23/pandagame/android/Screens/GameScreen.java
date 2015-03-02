package com.xombified23.pandagame.android.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.xombified23.pandagame.android.Actors.BackgroundActor;
import com.xombified23.pandagame.android.Actors.FloorActor;
import com.xombified23.pandagame.android.MainGame;
import com.xombified23.pandagame.android.Logics.MainLogic;
import com.xombified23.pandagame.android.Tools.Parameters;
import com.xombified23.pandagame.android.Actors.SingletonActors;
import com.xombified23.pandagame.android.Tools.ActorComparator;

import java.util.Random;

public class GameScreen implements Screen {
    // General
    private Stage stage;
    private OrthographicCamera camera;
    private MainLogic mainLogic;

    // Textures
    private TextureAtlas floorAtlas;
    private Texture backTexture;

    private SpriteBatch batch;
    private BitmapFont font;
    private Label debugLabel;

    // Others
    // private FPSLogger fpsLogger;
    private Table UImainTable;
    private Group gameAreaGroup;
    private ActorComparator myComparator;

    public GameScreen(final MainGame game) {
        // Instantiate New Objects
        gameAreaGroup = SingletonActors.GetUIGroup();
        UImainTable = new Table();
        stage = SingletonActors.GetStage();

        // fpsLogger = new FPSLogger();
        camera = new OrthographicCamera();
        floorAtlas = new TextureAtlas(Gdx.files.internal("jei/StandardTiles/StandardTiles.atlas"));
        backTexture = new Texture(Gdx.files.internal("others/background.png"));
        myComparator = new ActorComparator();

        // TODO: Debug Stuff
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void dispose() {
        backTexture.dispose();
        floorAtlas.dispose();
        SingletonActors.dispose();

        // TODO: Debug Stuff
        batch.dispose();
        font.dispose();
    }

    @Override
    public void render(float delta) {
        camera.update();
        stage.act(Gdx.graphics.getDeltaTime());

        // TODO: Debug Stuff
        debugLabel.setText(mainLogic.getText());

        // Sort Z order
        gameAreaGroup.getChildren().sort(myComparator);
        mainLogic.revealAround();
        stage.draw();
        // fpsLogger.log();
    }

    @Override
    public void resize(int width, int height) {
        // TODO: Later: Temporal Zoom out for older devices
//        camera.viewportWidth = width * 2;
//        camera.viewportHeight = height * 2;
        camera.viewportWidth = width;
        camera.viewportHeight = height;

        camera.position.set(width / 2f, height / 2f, 0);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void show() {
        // Setup camera to align Stage with Tiled Map
        stage.getViewport().setCamera(camera);

        // Add input interface to the Stage
        Gdx.input.setInputProcessor(stage);

        createBackgroundActor();
        createFloorTilesActors();
        createGameLogic();
        createUIFrame();  // TODO: Later: UI Placeholder

        // Add actor to Stage
        stage.addActor(UImainTable);
    }

    @Override
    public void pause() {
        // Irrelevant on desktop, ignore this
    }

    @Override
    public void resume() {
        // Irrelevant on desktop, ignore this
    }

    private void createBackgroundActor() {
        BackgroundActor backgroundActor = new BackgroundActor(backTexture);
        gameAreaGroup.addActor(backgroundActor);
    }

    private void createUIFrame() {
        UImainTable.setBounds(0, 0, Parameters.SCREEN_WIDTH, Parameters.SCREEN_HEIGHT);
        UImainTable.add(gameAreaGroup).expand().left().bottom();
        UImainTable.row();
        font.setScale(4, 4);
        LabelStyle labelStyle = new LabelStyle(font, Color.BLUE);
        debugLabel = new Label("", labelStyle);
        UImainTable.add(debugLabel).expandX().left().height(420);
    }

    /**
     * Add Input Listener to the Stage
     */
    private void createGameLogic() {
        mainLogic = new MainLogic();
        stage.addListener(mainLogic);
    }

    /**
     * Create Floor UI Tiles and store them in an array
     */
    private void createFloorTilesActors() {
        if (floorAtlas == null) {
            throw new Error();
        }

        FloorActor[][] floorActorMap = new FloorActor[Parameters.NUM_X_TILES][Parameters.NUM_Y_TILES];

        for (int j = 0; j < Parameters.NUM_Y_TILES; j++) {
            for (int i = 0; i < Parameters.NUM_X_TILES; i++) {
                Random rand = new Random();
                TextureRegion floorTile;
                switch (rand.nextInt(5)) {
                    case 0:
                        floorTile = floorAtlas.findRegion("tile1");
                        break;
                    case 1:
                        floorTile = floorAtlas.findRegion("tile2");
                        break;
                    case 2:
                        floorTile = floorAtlas.findRegion("tile3");
                        break;
                    case 3:
                        floorTile = floorAtlas.findRegion("tile4");
                        break;
                    default:
                        floorTile = floorAtlas.findRegion("tile_cracked");
                }

                floorActorMap[i][j] = new FloorActor(i, j, floorTile);
                gameAreaGroup.addActor(floorActorMap[i][j]);
            }
        }
    }
}

