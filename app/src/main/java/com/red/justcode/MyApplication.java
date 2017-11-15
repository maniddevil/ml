package com.red.justcode;

import android.app.Application;
import android.content.Context;

import com.red.justcode.learning.Utility;

/**
 * Created by manidhar on 10/11/17.
 */

public class MyApplication extends Application{
    static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        new Thread() {
            @Override
            public void run() {
                //Utility.prepareTrainingData();
                //Utility.getTrainingData(mContext);
                //Utility.prepareTrainingData2();
            }
        }.start();
    }

    public static Context getContext() {
        return mContext;
    }
}
