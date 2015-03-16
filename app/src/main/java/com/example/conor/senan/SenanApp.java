package com.example.conor.senan;


import android.app.Application;
import android.content.Context;
import android.util.Log;

public class SenanApp extends Application {

    private static final String TAG = SenanApp.class.getSimpleName();
    private static SenanApp instance;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "TapCounterApp.onCreate was called");
        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
