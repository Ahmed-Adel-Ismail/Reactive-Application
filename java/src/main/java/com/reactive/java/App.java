package com.reactive.java;

import android.app.Application;

import java.lang.ref.WeakReference;

public class App extends Application {

    private static WeakReference<App> instance;

    public static App getInstance() {
        return instance.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = new WeakReference<>(this);
    }
}
