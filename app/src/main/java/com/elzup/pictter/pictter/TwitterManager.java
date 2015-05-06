package com.elzup.pictter.pictter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.fabric.sdk.android.Fabric;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterManager {

    private TwitterSession session;
    private Twitter twitter;

    private Query nextQuery;

    TwitterManager(Context context) {
        setupClient(context);
    }

    public boolean isLogin() {
        return this.twitter != null;
    }

    public void setupClient(Context context) {
        this.setupSession(context);
        this.twitter = null;
        if (!this.hasSession()) {
            return;
        }
        TwitterAuthToken token = this.session.getAuthToken();
        this.setupTwitter(token.token, token.secret);
    }

    public void setupTwitter(String token, String tokenSecret) {
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
        twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
    }

    private void setupSession(Context context) {
        if (this.hasSession()) {
            return;
        }
        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
        Fabric.with(context, new com.twitter.sdk.android.Twitter(authConfig));
        this.session = com.twitter.sdk.android.Twitter.getSessionManager().getActiveSession();
    }

    public void searchTweets(final String q, final Long maxId, final Integer count, final PictureStatusAdapter customAdapter) {

        AsyncTask<Void, Void, List<Status>> task = new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                try {
                    Query query = new Query(q + " -RT filter:images");
                    if (maxId != null) {
                        query.maxId(maxId);
                    }
                    query.count(count);
//                    query.resultType(Query.ResultType.popular);
                    QueryResult res = twitter.search(query);

                    RateLimitStatus rateLimitStatus = res.getRateLimitStatus();
                    Log.d("TwitterAPi", String.format("%3d/%3d", rateLimitStatus.getRemaining(), rateLimitStatus.getLimit()));

                    nextQuery = res.nextQuery();
                    return res.getTweets();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> tweets) {
                if (tweets == null) {
                    return;
                }
                List<PictureStatus> pictureStatusList = new ArrayList<>();
                for (twitter4j.Status status : TwitterManager.filterImageTweet(tweets)) {
                    PictureStatus pictureStatus = new PictureStatus(status);
                    pictureStatus.asyncImage(customAdapter);
                }
            }
        };
        task.execute();
    }

    public void searchTweetsNext(final PictureStatusAdapter customAdapter) {

        AsyncTask<Void, Void, List<Status>> task = new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                try {
                    QueryResult res = twitter.search(nextQuery);

                    RateLimitStatus rateLimitStatus = res.getRateLimitStatus();
                    Log.d("TwitterAPi", String.format("%3d/%3d", rateLimitStatus.getRemaining(), rateLimitStatus.getLimit()));

                    nextQuery = res.nextQuery();
                    return res.getTweets();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> tweets) {
                if (tweets == null) {
                    return;
                }
                List<PictureStatus> pictureStatusList = new ArrayList<>();
                for (twitter4j.Status status : TwitterManager.filterImageTweet(tweets)) {
                    PictureStatus pictureStatus = new PictureStatus(status);
                    pictureStatus.asyncImage(customAdapter);
                }
            }
        };
        task.execute();
    }

    public boolean hasSession() {
        return this.session != null;
    }

    public void clearSession() {
        if (!this.hasSession()) {
            return;
        }
        com.twitter.sdk.android.Twitter.getSessionManager().clearSession(session.getId());
    }

    public static List<Status> filterImageTweet(List<Status> tweets) {
        return ImmutableList.copyOf(Iterables.filter(tweets, new Predicate<Status>() {
            @Override
            public boolean apply(@Nullable Status input) {
                return input.getMediaEntities().length != 0;
            }
        }));
    }
}
