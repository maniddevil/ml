package com.red.justcode.learning;

import android.content.Context;

import java.util.List;

/**
 * Created by manidhar on 11/4/2017.
 */

public class Utility {

    //test this
    public static class State {
        int state[][] = new int[3][3];
        public State(int[] stateArray) {
            for(int i=0; i<state.length; i++) {
                int length = state[i].length;
                for(int j=0; j<length; j++) {
                    state[i][j] = stateArray[i*length + j];
                }
            }
        }

        public float getOFloatState() {
            return 0;
        }
        public float getXFloatState() {
            return 0;
        }
    }

    public void addStatesToLookupTable(Context context, List<Integer[]> stateList, boolean hasWon) {
        final boolean isWon = hasWon;
        final boolean isDraw = !hasWon;

    }
}
