package com.red.justcode.tengine;

import android.util.Log;

import com.red.justcode.TttActivity;

/**
 * Created by balu on 09-11-2017.
 */

public class HPlayer implements Player{
    private static String TAG = "HPlayer";
    TttActivity activity;
    public HPlayer(TttActivity activity){
        this.activity = activity;
        Log.d(TAG,"hplayer created");
    }

    @Override
    public void makemove() {
        activity.nextmove();
    }
}
