package com.red.justcode.learning;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by manidhar on 8/11/17.
 */

public class NeuralNetwork {

    private int mInputCount = 0;
    private int[] mHiddenNeuronsCount;
    private int mOutpoutCount = 0;
    //bias contians the weight information, bias input is always 1, so we simply add bias weight in calculations
    private double[] mBias;

    private double[][][] mWeights;

    public NeuralNetwork(int inputCount, int[] hiddenNeuronsCount, int outputCount) {
        mInputCount = inputCount;
        mHiddenNeuronsCount = hiddenNeuronsCount;
        mOutpoutCount = outputCount;
    }

    public void randomizeWeights() {
        double random;
        mWeights = new double[mHiddenNeuronsCount.length +1][][];
        for(int i=0; i<=mHiddenNeuronsCount.length; i++) {
            int jlength = mHiddenNeuronsCount.length != i ? mHiddenNeuronsCount[i] : mOutpoutCount;
            mWeights[i] = new double[jlength][];
            for(int j=0; j<jlength; j++) {
                int klength = i !=0 ? mHiddenNeuronsCount[i-1] : mInputCount;
                mWeights[i][j] = new double[klength];
                for(int k=0; k< klength; k++) {
                    mWeights[i][j][k] = Math.random();
                }
            }
        }
    }

    public void initWeights(double d) {
        double random;
        mWeights = new double[mHiddenNeuronsCount.length +1][][];
        for(int i=0; i<=mHiddenNeuronsCount.length; i++) {
            int jlength = mHiddenNeuronsCount.length != i ? mHiddenNeuronsCount[i] : mOutpoutCount;
            mWeights[i] = new double[jlength][];
            for(int j=0; j<jlength; j++) {
                int klength = i !=0 ? mHiddenNeuronsCount[i-1] : mInputCount;
                mWeights[i][j] = new double[klength];
                for(int k=0; k< klength; k++) {
                    mWeights[i][j][k] = d;
                }
            }
        }
    }

    public void setWeights(double[][][] weights) {
        mWeights = weights;
    }

    public void setBias(double[] bias) {
        mBias = bias;
    }

    public int predictNextMove(int[] input) {
        double[][] ip;
        double[][] op = null;
        for(int i=0; i<mHiddenNeuronsCount.length+1; i++) {
            if (i == 0) {
                ip = convertToDoubleArray(input);
            } else {
                ip = op;
            }

            op = Utility.multiply(mWeights[i], ip);
            Log.i("NeuralNetwork", "predictNextMove without bias");
            Utility.printMatrix(op);
            addBias(op, mBias[i]);
            Log.i("NeuralNetwork", "predictNextMove printing matrix with bias");
            Utility.printMatrix(op);

            applyReLU(op);
        }

        return maxValuePosition(op);
    }

    public double[][] addBias(double[][]op, double bias) {
        for (int i=0; i<op.length; i++) {
            for(int j=0; j<op[i].length; j++) {
                op[i][j] = op[i][j] + bias;
            }
        }
        return op;
    }

    public int maxValuePosition(double[][] op) {
        int position = -1;
        double maxValue = 0;
        for(int i=0; i<op.length; i++) {
            if(maxValue < op[i][0]) {
                maxValue = op[i][0];
                position = i;
            }
        }
        return position+1;
    }

    public void applyReLU(double[][] op) {
        for(int i=0; i<op.length; i++) {
            op[i][0] = Utility.activationFunction(op[i][0]);
        }
        Log.i("NeuralNetwork", "applyReLU --");
        Utility.printMatrix(op);
    }

    public static double[][] convertToDoubleArray(double[] a) {
        double[][] b = new double[a.length][1];
        for(int i=0; i<a.length; i++) {
            b[i][0] = a[i];
        }
        return b;
    }
    public static double[][] convertToDoubleArray(int[] a) {
        double[][] b = new double[a.length][1];
        for(int i=0; i<a.length; i++) {
            b[i][0] = a[i];
        }
        return b;
    }

    public static double[] convertToSingleArray(double[][] a) {
        double[] op = new double[a.length];
        for(int i=0; i<a.length; i++) {
            op[i] = a[i][0];
        }
        return op;
    }

    ///////////////////////////////////////////////////////////////////////////
    ///////////////TRAINING////////////////////////////////////////////////////
    public void trainNetwork(Context context, List<Integer[]> trainingList){
        int length = trainingList.size();
        for(int i=0; i<length; i++) {
            int player = 1;
            int op = Utility.predictNextPosition(context, trainingList.get(i), player);
            if(op != -1) {
                train(integerToIntArrayWithPlayer(trainingList.get(i), player), op);
            }
            player = -1;
            op = Utility.predictNextPosition(context, trainingList.get(i), player);
            if(op != -1) {
                train(integerToIntArrayWithPlayer(trainingList.get(i), player), op);
            }
        }
    }

    public int[] integerToIntArrayWithPlayer(Integer[] ip, int player) {
        int length = ip.length;
        int[] intArray = new int[length+1];
        intArray[0] = player;
        for(int i=1; i<=length; i++) {
            intArray[i] = ip[i-1];
        }
        return intArray;
    }

    public void train(int[] input, int op) {
        int[] targetOutput = getOutputArrayFromPosition(op);
        double[] predictedOutput = predictOutput(input);
        double error = squaredError(targetOutput, predictedOutput);
        Log.i("NeuralNetwork", "train: error="+error);
    }

    public double squaredError(int[] targetOp, double[] predictedOp) {

        double error = 0;
        for(int i=0; i<targetOp.length; i++) {
            // error = 1/2 * (target - predicted) power 2 -> repeated over all outputs in array
            error = error + ((Math.pow(targetOp[i] - predictedOp[i], 2)) / 2);
        }

        return error;
    }

    private int[] getOutputArrayFromPosition(int position) {
        int[] output = new int[mOutpoutCount];
        for(int i=0; i<mOutpoutCount; i++) {
            output[i] = 0;
        }
        output[position-1] = 1;
        return output;
    }

    public double[] predictOutput(int[] input) {
        double[][] ip;
        double[][] op = null;
        for(int i=0; i<mHiddenNeuronsCount.length+1; i++) {
            if (i == 0) {
                ip = convertToDoubleArray(input);
            } else {
                ip = op;
            }

            op = Utility.multiply(mWeights[i], ip);
            Log.i("NeuralNetwork", "predictNextMove without bias");
            Utility.printMatrix(op);
            addBias(op, mBias[i]);
            Log.i("NeuralNetwork", "predictNextMove printing matrix with bias");
            Utility.printMatrix(op);

            applyReLU(op);
        }

        return convertToSingleArray(op);
    }


}


