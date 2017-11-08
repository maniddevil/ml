package com.red.justcode.learning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.Collections;
import java.util.List;

/**
 * Created by manidhar on 11/4/2017.
 */

public class Utility {

    //NOTE: TODO: yet to remove the symmetric states. study on how to rotate & mirror the matrix

    // O player value is 1, X player value is -1
    private static final String TAG = "Utilitly";
    //test this
    public static class State {
        int state[] = new int[3*3];
        int stateLength = state.length;
        public State(Integer[] stateArray) {
            for(int i=0; i<stateLength; i++) {
                    state[i] = stateArray[i];
            }
        }

        public State(int[] stateArray) {
            state = stateArray;
        }

        public double getOCumulativeState() {
            double oStateCumulative = 0;
            for(int i=0; i<state.length; i++) {
                int enabling = state[i] == 1 ? 1 : 0;
                oStateCumulative += enabling * (Math.pow(0.5, i+1));
            }
            return oStateCumulative;
        }

        public double getXCumulativeState() {
            double xStateCumulative = 0;
            for(int i=0; i<state.length; i++) {
                int enabling = state[i] == -1 ? 1 : 0;
                xStateCumulative += enabling * (Math.pow(0.5, i+1));
            }
            return xStateCumulative;
        }

        public void printState() {
            System.out.println("==========");
            for (int i=0;i < stateLength; i++) {
                System.out.print(state[i]);
                if((i+1)%3 != 0) {
                    System.out.print("|");
                } else {
                    System.out.println();
                    for (int k = 0; k < 9; k++) {
                        System.out.print("-");
                    }
                    System.out.println();
                }
            }
            System.out.println("==========");
        }
    }

    //Later keep below code in non-UI thread
    public void addStatesToLookupTable(Context context, List<Integer[]> stateList, boolean isDraw) {
        if(null == stateList) {
            return;
        }
        int noOfStates = stateList.size();

        boolean isOddIndexStatesWon;
        //below two naming are not correct, first vairable is the player who played last,
        //second variable is the other player. If last player is one, his prob is 1, if game is draw, 2nd player prob also becomes 1
        float tempWonProb = 1;
        float tempLostProb = 0;
        if(isDraw) {
            tempLostProb = 1;
        }
        //index always goes from 0 to n, so length-1
        if ((noOfStates-1) % 2 == 1) {
            isOddIndexStatesWon = true;
        } else {
            isOddIndexStatesWon = false;
        }

        //for last two states we already have win / loose probability
        State lastState = new State(stateList.get(noOfStates-1));
        insertOneStateToLookup(context, lastState.getOCumulativeState(),
                lastState.getXCumulativeState(), tempWonProb);
        Log.i(TAG, "MANI: ocum="+lastState.getOCumulativeState()+" xcum="+lastState.getXCumulativeState());

        State previousToLastState = new State(stateList.get(noOfStates-2));
        insertOneStateToLookup(context, previousToLastState.getOCumulativeState(),
                previousToLastState.getXCumulativeState(), tempLostProb);
        Log.i(TAG, "MANI: ocum="+previousToLastState.getOCumulativeState()+" xcum="+previousToLastState.getXCumulativeState());

        //now loop form last but not 2 to beginning of the states and insert them to lookup
        for(int i=noOfStates-3; i>-1; i--) {
            Integer[] stateArrayIntegers = stateList.get(i);
            State state = new State(stateArrayIntegers);
            state.printState();
            Log.i(TAG, "MANI: ocum="+state.getOCumulativeState()+" xcum="+state.getXCumulativeState());
            boolean isOddIndex;
            if(i%2 == 1) {
                isOddIndex = true;
            } else {
                isOddIndex =false;
            }
            float currentProb  ;
            if(isOddIndex) {
                if(isOddIndexStatesWon) {
                    currentProb = tempWonProb;
                } else {
                    currentProb = tempLostProb;
                }
            } else {
                if(isOddIndexStatesWon) {
                    currentProb = tempLostProb;
                } else {
                    currentProb = tempWonProb;
                }
            }

            float winProbability = calculatePreviousStateProbability(currentProb, 0.5f);
            if(isOddIndex) {
                if(isOddIndexStatesWon) {
                    tempWonProb = winProbability;
                } else {
                    tempLostProb = winProbability;
                }
            } else {
                if(isOddIndexStatesWon){
                    tempLostProb = winProbability;
                } else {
                    tempWonProb = winProbability;
                }
            }

            insertOneStateToLookup(context, state.getOCumulativeState(),
                    state.getXCumulativeState(), winProbability);
        }
    }

    private float calculatePreviousStateProbability(float currentStateProb, float learningRate) {
        float previousStateProb = 0.5f; //previous state prob is unknown, lets keep it 0.5f;

        return previousStateProb + (learningRate * (currentStateProb - previousStateProb));
    }

    private void insertOneStateToLookup(Context context, double x1, double x2, float p) {

        float previouslyExisitingProbability = -1;
        Cursor cursor = context.getContentResolver().query(TrainingDB.LOOKUP_TABLE.CONTENT_URI,
                new String[] {TrainingDB.LOOKUP_TABLE.COLUMN_PROBABILITY},
                TrainingDB.LOOKUP_TABLE.COLUMN_O_STATE+"=? AND "+TrainingDB.LOOKUP_TABLE.COLUMN_x_STATE+"=? ",
                new String[] {String.valueOf(x1), String.valueOf(x2)},null);
        if(null != cursor && cursor.moveToFirst()) {
            previouslyExisitingProbability = cursor.getFloat(0);
        }

        if(previouslyExisitingProbability != -1 && previouslyExisitingProbability < p) {
            return;
        }

        //below is not needed actually. Just for testing purpose
        getInputFromOXCumulatives(x1, x2);

        ContentValues values = new ContentValues();
        values.put(TrainingDB.LOOKUP_TABLE.COLUMN_O_STATE, x1);
        values.put(TrainingDB.LOOKUP_TABLE.COLUMN_x_STATE, x2);
        values.put(TrainingDB.LOOKUP_TABLE.COLUMN_PROBABILITY, p);
        context.getContentResolver().insert(TrainingDB.LOOKUP_TABLE.CONTENT_URI, values);
    }

    public static float getProbabilityFromLookup(Context context, State state) {
        double x1 = state.getOCumulativeState();
        double x2 = state.getXCumulativeState();

        Cursor cursor = context.getContentResolver().query(TrainingDB.LOOKUP_TABLE.CONTENT_URI,
                new String[] {TrainingDB.LOOKUP_TABLE.COLUMN_PROBABILITY},
                TrainingDB.LOOKUP_TABLE.COLUMN_O_STATE+"=? AND "+TrainingDB.LOOKUP_TABLE.COLUMN_x_STATE+"=? ",
                new String[] {String.valueOf(x1), String.valueOf(x2)},null);
        if(null != cursor && cursor.moveToFirst()) {
            float fromCursor = cursor.getFloat(0);
            if(fromCursor >= 0) {
                return fromCursor;
            }
        }
        return 0.5f; //if not found in db, by default we will give 0.5 as probability of winning
    }

    //player is 1 for O and -1 for X, so we can predict the best move for that player
    public static int predictNextPosition(Context context, Integer[] state, int player) {
        State currentState = new State(state);

        int predictedPosition = -1;
        float predictedPositionProbability = 0;
        int[] stateArray = currentState.state;

        for(int i=0; i<currentState.stateLength; i++) {
            if(stateArray[i] == 0) {
                int[] tempAfterState = stateArray.clone();
                tempAfterState[i] = player;
                State afterState = new State(tempAfterState);
                float probability = getProbabilityFromLookup(context, afterState);
                if(probability > predictedPositionProbability) {
                    predictedPositionProbability = probability;
                    predictedPosition = i;
                }
            }
        }
        return predictedPosition;
    }

    public static double activationFunction(double d) {
        //ReLU
        if(d > 0) {
            return d;
        } else {
            return 0;
        }
    }

    public static void printMatrix(double[][] a) {
        for(int i=0; i<a.length; i++ ){
            for(int j=0; j<a[0].length; j++) {
                System.out.print(" "+a[i][j]);
            }
            System.out.println();
        }
    }

    public static int[][] converToMatrix(int[] a, int m, int n){
        int[][] result = new int[m][n];
        for(int i=0; i<m; i++) {
            for(int j=0; j<n; j++) {
                result[i][j] = a[i*m +j];
            }
        }
        return result;
    }

    public static double[][] multiply(double[][] a, double[][] b) {
        int rowsInA = a.length;
        int columnsInA = a[0].length;
        int rowsInB = b.length;
        int columnsInB = b[0].length;
        Log.i("Utility", "rowsInA="+rowsInA+" columnsInA="+columnsInA+" rowsInB="+rowsInB+" columnsInB="+columnsInB);
        if (columnsInA != rowsInB) {
            return null;
        }
        double[][] c = new double[rowsInA][columnsInB];
        for (int i = 0; i < rowsInA; i++) {
            for (int j = 0; j < columnsInB; j++) {
                for (int k = 0; k < columnsInA; k++) {
                    c[i][j] = c[i][j] + a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }

    public static int[] getInputFromOXCumulatives(double ocum, double xcum) {
        int length = 9;
        int[] inputArray = new int[length];

        //ocumulative refactor to get array enablings and assign them 1 values
        double temp = 0;
        for (int i=0; i<length; i++) {
            double pow = Math.pow(0.5, i+1);
            if(ocum >= temp + pow) {
                inputArray[i] = 1;
                temp = temp + pow;
            } else {
                inputArray[i] = 0;
            }
        }
        //xcumulative refactor to get array enablings and assign them -1 values
        temp = 0;
        for(int i=0; i<length; i++) {
            double pow = Math.pow(0.5, i+1);
            if(xcum >= temp + pow) {
                inputArray[i] = -1;
                temp = temp + pow;
            }
        }
        for(int i=0; i<length; i++) {
            System.out.print(" "+inputArray[i]);
        }
        System.out.println();
        return inputArray;
    }

}
