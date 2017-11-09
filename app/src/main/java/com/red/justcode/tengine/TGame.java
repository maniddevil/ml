package com.red.justcode.tengine;

import android.util.Log;

import com.red.justcode.TttActivity;
import com.red.justcode.learning.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by balu on 09-11-2017.
 */

public class TGame {
    Player mPlayer1;
    Player mPlayer2;
    Player currentPlayer;
    TttActivity activity;
    List<Integer[]> stateList = new ArrayList<Integer[]>();

    public TGame(TttActivity activity) {
        this.activity = activity;
    }

    public void setPlayers(Player p1, Player p2) {
        mPlayer1 = p1;
        mPlayer2 = p2;
        currentPlayer = p1;
    }

    public void startPlay() {
        currentPlayer.makemove();
    }

    public void nextMove(int pos) {
        Log.i("ttttttt=>", "pos = " + pos);
        Integer[] state;
        if (stateList.isEmpty()) {
            state = new Integer[9];
            for (int i = 0; i < 9; i++) {
                state[i] = 0;
            }
        } else {
            state = stateList.get(stateList.size() - 1).clone();
        }

        state[pos] = mPlayer1 == currentPlayer ? 1 : -1;
        stateList.add(state);
        if (currentPlayer == mPlayer1) {
            currentPlayer = mPlayer2;
        } else {
            currentPlayer = mPlayer1;
        }
        isGameOver();
        currentPlayer.makemove();
    }

    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean[] isGameOver() {
        boolean gameOver = false;
        boolean isDraw = false;
        Integer[] lastState = stateList.get(stateList.size() - 1);
        int[] lastStateIntArray = Utility.getIntFromIntegerArray(lastState);
        int[][] c = Utility.converToMatrix(lastStateIntArray, 3, 3);
        if ((c[0][0] == 1 && c[1][1] == 1 && c[2][2] == 1)
                || (c[0][2] == 1 && c[1][1] == 1 && c[2][0] == 1)
                || (c[0][1] == 1 && c[1][1] == 1 && c[2][1] == 1)
                || (c[0][2] == 1 && c[1][2] == 1 && c[2][2] == 1)
                || (c[0][0] == 1 && c[0][1] == 1 && c[0][2] == 1)
                || (c[1][0] == 1 && c[1][1] == 1 && c[1][2] == 1)
                || (c[2][0] == 1 && c[2][1] == 1 && c[2][2] == 1)
                || (c[0][0] == 1 && c[1][0] == 1 && c[2][0] == 1)) {
            gameOver = true;
        } else if ((c[0][0] == -1 && c[1][1] == -1 && c[2][2] == -1)
                || (c[0][2] == -1 && c[1][1] == -1 && c[2][0] == -1)
                || (c[0][1] == -1 && c[1][1] == -1 && c[2][1] == -1)
                || (c[0][2] == -1 && c[1][2] == -1 && c[2][2] == -1)
                || (c[0][0] == -1 && c[0][1] == -1 && c[0][2] == -1)
                || (c[1][0] == -1 && c[1][1] == -1 && c[1][2] == -1)
                || (c[2][0] == -1 && c[2][1] == -1 && c[2][2] == -1)
                || (c[0][0] == -1 && c[1][0] == -1 && c[2][0] == -1)) {
            gameOver = true;
        } else {
            boolean empty = false;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (c[i][j] == 0) {
                        empty = true;
                        break;
                    }
                }
            }
            if (!empty) {
                gameOver = true;
                isDraw = true;
            }
        }
        if (gameOver) {
            Utility.addStatesToLookupTable(activity.getApplicationContext(), stateList, isDraw);
        }
        return new boolean[]{gameOver,isDraw};
    }

}
