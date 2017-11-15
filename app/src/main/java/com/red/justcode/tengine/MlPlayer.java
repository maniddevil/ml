package com.red.justcode.tengine;

import android.util.Log;

import com.red.justcode.MyApplication;
import com.red.justcode.TttActivity;
import com.red.justcode.learning.Utility;

/**
 * Created by balu on 09-11-2017.
 */

public class MlPlayer implements Player {
    private static String TAG = "MlPlayer";
    private TttActivity mActivity;
    private TGame mTgame;
    public MlPlayer(TttActivity activity, TGame tGame){
        Log.d(TAG,"MlPlayer created");
        this.mActivity = activity;
        this.mTgame = tGame;
    }

    public static boolean isTraining = false;

    public MlPlayer(TGame tGame) {
        mTgame = tGame;
    }
    @Override
    public void makemove() {
        //pridict move
        int player = mTgame.getCurrentPlayer().hashCode() == mTgame.mPlayer1.hashCode() ? 1 : -1;
        int position = Utility.predictNextPosition(MyApplication.getContext(),
                mTgame.getCurrentState(), player);

        if(mActivity != null) {
            mActivity.nextmove();
            mActivity.nextmove(position);
        }

        if(isTraining) {
            mTgame.nextMove(position);
        }
    }
}
