package com.example.loadmoredemo;

import android.app.Application;

/**
 * Created by Administrator on 2016/8/31.
 */
public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static App getContext() {
        return app;
    }
}
