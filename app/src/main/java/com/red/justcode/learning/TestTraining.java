package com.red.justcode.learning;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manidhar on 5/11/17.
 */

public class TestTraining {

    public void dummyTest(Context context) {
        //testing utility
        Utility learningUtility = new Utility();
        List<Integer[]> stateList = new ArrayList<Integer[]>();
        stateList.add(new Integer[] {0,0,-1, 0,0,0, 0,0,0});
        stateList.add(new Integer[] {1,0,-1, 0,0,0, 0,0,0});
        stateList.add(new Integer[] {1,0,-1, 0,0,0, -1,0,0});
        stateList.add(new Integer[] {1,0,-1, 0,1,0, -1,0,0});
        stateList.add(new Integer[] {1,0,-1, 0,1,0, -1,0,-1});
        stateList.add(new Integer[] {1,0,-1, 0,1,0, -1,1,-1});
        stateList.add(new Integer[] {1,0,-1, 0,1,-1, -1,1,-1});
        //stateList.add(new Integer[] {1,0,0,1,1,1,0,-1,-1});
        Log.i("MANI:", "hohoho");
        learningUtility.addStatesToLookupTable(context, stateList, false);
        printLookUp(context);

        int prediction = Utility.predictNextPosition(context, new Integer[] {1,0,-1, 0,1,0, -1,1,-1}, -1);
        Log.i("MANI:", "next prediction from lookup table prediction="+prediction);
        double[][] testW = new double[][] {{1,2,3}, {2,3,4}};
        double[][] testX = new double[][] {{1},{2}, {3}};
        double[][] output = Utility.multiply(testW, testX);
        Utility.printMatrix(output);
    }

    public void printLookUp(Context context) {
        Cursor cursor = context.getContentResolver().query(TrainingDB.LOOKUP_TABLE.CONTENT_URI,
                null, null,null,null);
        if(cursor == null || cursor.getCount() ==0) {
            Log.i("MANI","printLookUp, cursor is empty");
        }
        if(cursor != null & cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                double x1 = cursor.getDouble(cursor.getColumnIndex(TrainingDB.LOOKUP_TABLE.COLUMN_O_STATE));
                double x2 = cursor.getDouble(cursor.getColumnIndex(TrainingDB.LOOKUP_TABLE.COLUMN_x_STATE));
                float prob = cursor.getFloat(cursor.getColumnIndex(TrainingDB.LOOKUP_TABLE.COLUMN_PROBABILITY));
                Log.i("TestTraining", id+ " " + x1 + " " + x2 + " " + prob);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    public void neuralNetworkTests(Context context) {
        NeuralNetwork nn = new NeuralNetwork(10, new int[] {10},9);
        nn.initWeights(0.5);
        nn.randomizeWeights();
        nn.setBias(new double[] {1,1});
        nn.trainNetwork(context, Utility.getTrainingData(context));
        int prediction = nn.predictNextMove(new int[] {1, 1,0,-1, 0,1,0, -1,1,-1});
        Log.i("TestTraining", "neuralNetworkTests: prediction="+prediction);
    }
}
