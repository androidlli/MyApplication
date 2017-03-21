package com.cango.mvpdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by cango on 2017/3/17.
 */

public class MtApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
    }
    public static Context getContext(){
        return mContext;
    }
}
