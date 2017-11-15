package com.red.justcode.tengine;

import android.util.Log;

import com.red.justcode.MyApplication;
import com.red.justcode.TttActivity;
import com.red.justcode.learning.NeuralNetwork;
import com.red.justcode.learning.Utility;

/**
 * Created by manidhar on 10/11/17.
 */

public class NNPlayer implements Player {
    private static String TAG = "NNPlayer";
    private TttActivity mActivity;
    private TGame mTgame;
    private NeuralNetwork mNeuralNetwork;
    public NNPlayer(TttActivity activity, TGame tGame){
        Log.d(TAG,"MlPlayer created");
        this.mActivity = activity;
        this.mTgame = tGame;
        mNeuralNetwork = new NeuralNetwork(10, new int[] {10},9);
        mNeuralNetwork.initWeights(0.5);
        mNeuralNetwork.randomizeWeights();
        mNeuralNetwork.setBias(new double[] {1,1});
    }

    public static boolean isTraining = false;

    public NNPlayer(TGame tGame) {
        mTgame = tGame;
        mNeuralNetwork = new NeuralNetwork(10, new int[] {10},9);
        mNeuralNetwork.initWeights(0.5);
        mNeuralNetwork.randomizeWeights();
        mNeuralNetwork.setBias(new double[] {1,1});
    }
    @Override
    public void makemove() {
        //pridict move
        int player = mTgame.getCurrentPlayer().hashCode() == mTgame.mPlayer1.hashCode() ? 1 : -1;
        Integer[] ip = mTgame.getCurrentState();
        int[] input = new int[ip.length+1];
        for(int i=0; i<input.length-1; i++) {
            input[i] = ip[i];
        }

        input[input.length-1] = player;

        int position = mNeuralNetwork.predictNextMove(input);

        if(mActivity != null) {
            mActivity.nextmove();
            mActivity.nextmove(position-1);
        }

        if(isTraining) {
            mTgame.nextMove(position-1);
        }
    }
}
