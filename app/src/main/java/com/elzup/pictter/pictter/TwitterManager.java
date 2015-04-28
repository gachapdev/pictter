package com.elzup.pictter.pictter;

import android.content.Context;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;

import io.fabric.sdk.android.Fabric;

public class TwitterManager {

    private TwitterSession session;
    private static boolean debug_session_loaded = false;

    public boolean loginCheck(Context context) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
        session = Twitter.getSessionManager().getActiveSession();
        if (BuildConfig.DEBUG) {
            this.debug_login();
        }
        return this.isLogin();
    }

    public boolean isLogin() {
        return this.session != null;
    }

    public void clearSession() {
        if (this.isLogin()) {
            Twitter.getSessionManager().clearSession(session.getId());
        }
    }

    private void debug_login() {
        if (debug_session_loaded) {
            return;
        }
        debug_session_loaded = false;
        this.clearSession();
    }

}
