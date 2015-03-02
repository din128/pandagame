package com.xombified23.pandagame.android.Logics;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.xombified23.pandagame.android.Actors.MainTileActor;
import com.xombified23.pandagame.android.Actors.MonsterActor;
import com.xombified23.pandagame.android.Actors.PlayerActor;
import com.xombified23.pandagame.android.Actors.WallActor;
import com.xombified23.pandagame.android.Tools.Parameters;
import com.xombified23.pandagame.android.Actors.SingletonActors;

import java.util.LinkedList;
import java.util.Queue;

public class MainLogic extends InputListener {
    private int[][] mapSteps; // Double array to handle shortest path
    private Stage stage;
    private PlayerActor playerActor;
    private MainTileActor mainTileActorMap[][];
    private MonsterActor monsterActorMap[][];
    private WallActor wallActorMap[][];
    private String debugText;
    private int monsterCount = 0;
    private boolean inCombat = false;

    public MainLogic() {
        stage = SingletonActors.GetStage();
        mainTileActorMap = SingletonActors.GetMainTileActorMap();
        playerActor = SingletonActors.GetPlayerActor();
        wallActorMap = SingletonActors.GetWallActorMap();
        monsterActorMap = SingletonActors.GetMonsterActorMap();
        mapSteps = new int[Parameters.NUM_X_TILES][Parameters.NUM_Y_TILES];
    }

    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Actor currActor = stage.hit(x, y, true);
        if (currActor != null) {
            if (currActor instanceof MainTileActor) {
                if (((MainTileActor) currActor).isRevealed()) {

                    if (!inCombat) {
                        movePlayer(((MainTileActor) currActor).getXTile(), ((MainTileActor) currActor).getYTile());
                    }
                }
            } else if (currActor instanceof MonsterActor) {
                System.out.println("MonsterActor clicked");
                int currXTile = ((MonsterActor) currActor).getXTile();
                int currYTile = ((MonsterActor) currActor).getYTile();

                if (mainTileActorMap[currXTile][currYTile].isRevealed()
                        && Math.abs(playerActor.getXTile() - currXTile) <= 1
                        && Math.abs(playerActor.getYTile() - currYTile) <= 1
                        && playerActor.getPlayerStatus() == PlayerActor.PlayerStatus.STANDING) {

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

        // TODO: Optmize this for loop
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
            if (currPos.x == playerActor.getXTile() && currPos.y == playerActor.getYTile()) {
                playerActor.movePlayer(mapSteps, destXTile, destYTile);
                break;
            }

            // Add an extra count for each step away from destination
            ++count;

            if ((currPos.x - 1 >= 0) && (mainTileActorMap[currPos.x - 1][currPos.y].isRevealed())
                    && !mainTileActorMap[currPos.x - 1][currPos.y].itContainsMonster()
                    && !mainTileActorMap[currPos.x - 1][currPos.y].itContainsWall()
                    && (mapSteps[currPos.x - 1][currPos.y] == -1)) {
                Point newPos = new Point();
                newPos.x = currPos.x - 1;
                newPos.y = currPos.y;
                queue.add(newPos);
                mapSteps[newPos.x][newPos.y] = count;
            }

            if (currPos.x + 1 < Parameters.NUM_X_TILES && mainTileActorMap[currPos.x + 1][currPos.y].isRevealed()
                    && !mainTileActorMap[currPos.x + 1][currPos.y].itContainsMonster()
                    && !mainTileActorMap[currPos.x + 1][currPos.y].itContainsWall()
                    && mapSteps[currPos.x + 1][currPos.y] == -1) {
                Point newPos = new Point();
                newPos.x = currPos.x + 1;
                newPos.y = currPos.y;
                queue.add(newPos);
                mapSteps[newPos.x][newPos.y] = count;
            }

            if (currPos.y - 1 >= 0 && mainTileActorMap[currPos.x][currPos.y - 1].isRevealed()
                    && !mainTileActorMap[currPos.x][currPos.y - 1].itContainsMonster()
                    && !mainTileActorMap[currPos.x][currPos.y - 1].itContainsWall()
                    && mapSteps[currPos.x][currPos.y - 1] == -1) {
                Point newPos = new Point();
                newPos.x = currPos.x;
                newPos.y = currPos.y - 1;
                queue.add(newPos);
                mapSteps[newPos.x][newPos.y] = count;
            }

            if (currPos.y + 1 < Parameters.NUM_Y_TILES && mainTileActorMap[currPos.x][currPos.y + 1].isRevealed()
                    && !mainTileActorMap[currPos.x][currPos.y + 1].itContainsMonster()
                    && !mainTileActorMap[currPos.x][currPos.y + 1].itContainsWall()
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
