package com.red.justcode.learning;

import android.util.Log;

/**
 * Created by manidhar on 8/11/17.
 */

public class NeuralNetwork {

    private int mInputCount = 0;
    private int[] mHiddenNeuronsCount;
    private int mOutpoutCount = 0;
    private int[] mBias;

    private double[][][] mWeights;

    public NeuralNetwork(int inputCount, int[] hiddenNeuronsCount, int outputCount) {
        mInputCount = inputCount + 1;// 1 for bias
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

    public void setBias(int[] bias) {
        mBias = bias;
    }

    public int predictNextMove(int[] input) {
        int[] inputWithBias = new int[input.length+1];
        for(int i=0; i<input.length; i++) {
            inputWithBias[i] = input[i];
        }
        inputWithBias[input.length] = mBias[0];
        double[][] ip;
        double[][] op = null;
        for(int i=0; i<mHiddenNeuronsCount.length+1; i++) {
            if (i == 0) {
                ip = convertToDoubleArray(inputWithBias);
            } else {
                ip = op;
            }

            op = Utility.multiply(mWeights[i], ip);
            Log.i("NeuralNetwork", "predictNextMove printing matrix");
            Utility.printMatrix(op);

            applyReLU(op);


        }

        return maxValuePosition(op);
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

}




