package com.red.justcode.tengine;

import android.util.Log;

import com.red.justcode.TttActivity;

/**
 * Created by balu on 09-11-2017.
 */

public class MlPlayer implements Player {
    private static String TAG = "MlPlayer";
    private TttActivity mActivity;
    public MlPlayer(TttActivity activity){
        Log.d(TAG,"MlPlayer created");
        this.mActivity = activity;
    }

    @Override
    public void makemove() {
        //pridict move
        if(mActivity != null) {
            mActivity.nextmove(5);
        }
        mActivity.nextmove();
    }
}
