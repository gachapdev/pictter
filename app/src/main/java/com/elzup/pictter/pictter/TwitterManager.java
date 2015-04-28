package com.elzup.pictter.pictter;

import android.content.Context;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Search;

import io.fabric.sdk.android.Fabric;

public class TwitterManager {

    private TwitterSession session;
    private static boolean debug_session_loaded = false;
    private TwitterApiClient client;

    public boolean loginCheck(Context context) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
        session = Twitter.getSessionManager().getActiveSession();
        if (BuildConfig.DEBUG) {
            this.debug_login();
        }
        return this.isLogin();
    }

    public void setupClient() {
        if (!this.isLogin() || this.client != null) {
            return;
        }
        this.client = TwitterCore.getInstance().getApiClient(this.session);
    }

    public void searchTweets(String q, Long maxId, Callback<Search> callback) {
        // TODO; order = {mixed, recent, popuer}
        int count = 100;
        client.getSearchService().tweets(q, null, "ja", null, "recent", count, null, null, maxId, false, callback);
    }

    public void searchTweets(String q, Callback<Search> callback) {
        this.searchTweets(q, null, callback);
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
        debug_session_loaded = true;
        this.clearSession();
    }

}
