package com.xombified23.pandagame.android;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.LinkedList;
import java.util.Queue;

public class MainLogic extends InputListener {
    private int[][] mapSteps; // Double array to handle shortest path
    private Stage stage;
    private PlayerActor playerActor;
    private MainTileActor mainTileActorMap[][];
    private MonsterActor monsterActorMap[][];
    private String debugText;
    private int monsterCount = 0;
    private boolean inCombat = false;

    public MainLogic(Stage stage, PlayerActor playerActor, MainTileActor[][] mainTileActorMap, MonsterActor[][]
            monsterActorMap) {
        this.stage = stage;
        this.playerActor = playerActor;
        this.mainTileActorMap = mainTileActorMap;
        this.monsterActorMap = monsterActorMap;
        mapSteps = new int[Parameters.NUM_X_TILES][Parameters.NUM_Y_TILES];
    }

    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Actor currActor = stage.hit(x, y, true);
        if (currActor != null) {
            if (currActor instanceof MainTileActor) {
                if (((MainTileActor) currActor).isRevealed() && References.playerActor.getPlayerStatus() == PlayerActor
                        .PlayerStatus.STANDING) {

                    if (!inCombat) {
                        movePlayer(((MainTileActor) currActor).getXTile(), ((MainTileActor) currActor).getYTile());
                    }
                }
            } else if (currActor instanceof MonsterActor) {
                System.out.println("MonsterActor clicked");
                int currXTile = ((MonsterActor) currActor).getXTile();
                int currYTile = ((MonsterActor) currActor).getYTile();

                if (References.mainTileActorMap[currXTile][currYTile].isRevealed()
                        && Math.abs(References.playerActor.getXTile() - currXTile) <= 1
                        && Math.abs(References.playerActor.getYTile() - currYTile) <= 1
                        && References.playerActor.getPlayerStatus() == PlayerActor.PlayerStatus.STANDING) {

                    if (!inCombat) {
                        startCombat();
                    } else {
                        playerActor.attack();
                        mainTileActorMap[currXTile][currYTile].setContainsMonster(false);
                        ((MonsterActor) currActor).removeMonster();
                        currActor.remove();
                        --monsterCount;
                        updateCombatStatus();
                    }
                }

            } else {
                // TODO: Abilities?
                System.out.println("Nothing clicked");
            }
        }
        return true;
    }

    /**
     * Move players algorithm
     *
     * @param destXTile destination X
     * @param destYTile destination Y
     */
    private void movePlayer(int destXTile, int destYTile) {
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

            // If path is found, pass the mapSteps to let PlayerActor animate and render
            if (currPos.x == References.playerActor.getXTile() && currPos.y == References.playerActor.getYTile()) {
                References.playerActor.movePlayer(mapSteps, destXTile, destYTile);
                break;
            }

            // Add an extra count for each step away from destination
            ++count;

            if ((currPos.x - 1 >= 0) && (References.mainTileActorMap[currPos.x - 1][currPos.y].isRevealed())
                    && !References.mainTileActorMap[currPos.x - 1][currPos.y].itContainsMonster()
                    && !References.mainTileActorMap[currPos.x - 1][currPos.y].itContainsWall()
                    && (mapSteps[currPos.x - 1][currPos.y] == -1)) {
                Point newPos = new Point();
                newPos.x = currPos.x - 1;
                newPos.y = currPos.y;
                queue.add(newPos);
                mapSteps[newPos.x][newPos.y] = count;
            }

            if (currPos.x + 1 < Parameters.NUM_X_TILES && References.mainTileActorMap[currPos.x + 1][currPos.y].isRevealed()
                    && !References.mainTileActorMap[currPos.x + 1][currPos.y].itContainsMonster()
                    && !References.mainTileActorMap[currPos.x + 1][currPos.y].itContainsWall()
                    && mapSteps[currPos.x + 1][currPos.y] == -1) {
                Point newPos = new Point();
                newPos.x = currPos.x + 1;
                newPos.y = currPos.y;
                queue.add(newPos);
                mapSteps[newPos.x][newPos.y] = count;
            }

            if (currPos.y - 1 >= 0 && References.mainTileActorMap[currPos.x][currPos.y - 1].isRevealed()
                    && !References.mainTileActorMap[currPos.x][currPos.y - 1].itContainsMonster()
                    && !References.mainTileActorMap[currPos.x][currPos.y - 1].itContainsWall()
                    && mapSteps[currPos.x][currPos.y - 1] == -1) {
                Point newPos = new Point();
                newPos.x = currPos.x;
                newPos.y = currPos.y - 1;
                queue.add(newPos);
                mapSteps[newPos.x][newPos.y] = count;
            }

            if (currPos.y + 1 < Parameters.NUM_Y_TILES && References.mainTileActorMap[currPos.x][currPos.y + 1].isRevealed()
                    && !References.mainTileActorMap[currPos.x][currPos.y + 1].itContainsMonster()
                    && !References.mainTileActorMap[currPos.x][currPos.y + 1].itContainsWall()
                    && mapSteps[currPos.x][currPos.y + 1] == -1) {
                Point newPos = new Point();
                newPos.x = currPos.x;
                newPos.y = currPos.y + 1;
                queue.add(newPos);
                mapSteps[newPos.x][newPos.y] = count;
            }
        } // end of while loop
    }

    private void startCombat() {
        inCombat = true;
        playerActor.setInCombat(true);
        debugText = "Kill nearby enemies before moving";

        int xTile = playerActor.getXTile();
        int yTile = playerActor.getYTile();

        if (xTile - 1 >= 0 && mainTileActorMap[xTile - 1][yTile].itContainsMonster() && mainTileActorMap[xTile -
                1][yTile].isRevealed()) {
            monsterActorMap[xTile - 1][yTile].confrontPlayer();
            monsterCount++;
        }

        if (xTile - 1 >= 0 && yTile - 1 >= 0 && mainTileActorMap[xTile - 1][yTile - 1].itContainsMonster() &&
                mainTileActorMap[xTile - 1][yTile - 1].isRevealed()) {
            monsterActorMap[xTile - 1][yTile - 1].confrontPlayer();
            monsterCount++;
        }

        if (yTile - 1 >= 0 && mainTileActorMap[xTile][yTile - 1].itContainsMonster() && mainTileActorMap[xTile][yTile
                - 1].isRevealed()) {
            monsterActorMap[xTile][yTile - 1].confrontPlayer();
            monsterCount++;
        }

        if (xTile + 1 < Parameters.NUM_X_TILES && yTile - 1 >= 0 && mainTileActorMap[xTile + 1][yTile - 1]
                .itContainsMonster() && mainTileActorMap[xTile + 1][yTile - 1].isRevealed()) {
            monsterActorMap[xTile + 1][yTile - 1].confrontPlayer();
            monsterCount++;
        }

        if (xTile + 1 < Parameters.NUM_X_TILES && mainTileActorMap[xTile + 1][yTile].itContainsMonster() &&
                mainTileActorMap[xTile + 1][yTile].isRevealed()) {
            monsterActorMap[xTile + 1][yTile].confrontPlayer();
            monsterCount++;
        }

        if (xTile + 1 < Parameters.NUM_X_TILES && yTile + 1 < Parameters.NUM_Y_TILES && mainTileActorMap[xTile +
                1][yTile + 1].itContainsMonster() && mainTileActorMap[xTile + 1][yTile + 1].isRevealed()) {
            monsterActorMap[xTile + 1][yTile + 1].confrontPlayer();
            monsterCount++;
        }

        if (yTile + 1 < Parameters.NUM_Y_TILES && mainTileActorMap[xTile][yTile + 1].itContainsMonster() &&
                mainTileActorMap[xTile][yTile + 1].isRevealed()) {
            monsterActorMap[xTile][yTile + 1].confrontPlayer();
            monsterCount++;
        }

        if (xTile - 1 >= 0 && yTile + 1 < Parameters.NUM_Y_TILES && mainTileActorMap[xTile - 1][yTile + 1]
                .itContainsMonster() && mainTileActorMap[xTile - 1][yTile + 1].isRevealed()) {
            monsterActorMap[xTile - 1][yTile + 1].confrontPlayer();
            monsterCount++;
        }
    }

    public String getText() {
        return debugText;
    }

    private void updateCombatStatus() {
        if (monsterCount == 0) {
            inCombat = false;
            playerActor.setInCombat(false);
            debugText = "";
        }
    }
}
