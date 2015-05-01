package com.elzup.pictter.pictter;

import android.app.Application;

public class AppControl extends Application {

    @Override public void onCreate() {
        super.onCreate();

//        if (BuildConfig.DEBUG) {
//            TwitterManager twitterManager = new TwitterManager(this);
//            twitterManager.clearSession();
//        }
    }

    @Override public void onTerminate() {
        super.onTerminate();
    }
}
