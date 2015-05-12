package com.elzup.pictter.pictter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

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
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterListener;
import twitter4j.auth.AccessToken;

public class TwitterManager {

    private TwitterSession session;
    private Twitter twitter;
    private AsyncTwitter asyncTwitter;

    private boolean isLogin;
    private Query nextQuery;

    private PictureStatusAdapter pictureStatusAdapter;
    private ArrayAdapter<String> trendAdapter;

    private static String SEARCH_IGNORE_OPERATOR = "-";
    private static String SEARCH_FILTER_OPTION_RT = "RT";
    private static String SEARCH_FILTER_OPTION_NORT = SEARCH_IGNORE_OPERATOR + SEARCH_FILTER_OPTION_RT;
    private static String SEARCH_FILTER_OPTION_IMAGE = "filter:images";

    public static int WOEID_JAPAN = 23424856;

    TwitterManager(Context context) {
        setupClient(context);
    }

    public boolean isLogin() {
        return this.isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public void setupClient(Context context) {
        this.setupSession(context);
        this.twitter = null;
        this.setLogin(this.hasSession());
        if (!this.isLogin()) {
            return;
        }
        TwitterAuthToken token = this.session.getAuthToken();
        setupTwitter(token.token, token.secret);
        setupTwitterListener(token.token, token.secret);
    }

    private void setupTwitterListener(String token, String tokenSecret) {
        TwitterListener listener = new TwitterAdapter() {

            @Override
            public void gotPlaceTrends(Trends trends) {
                ArrayList<String> trendWords = new ArrayList<>();
                for (Trend trend : trends.getTrends()) {
                    Log.d("Trend", trend.getName() + " " + trend.getURL());
                    trendWords.add(trend.getName());
                }
                trendAdapter.addAll(trendWords);
            }
        };
        AsyncTwitterFactory factory = new AsyncTwitterFactory();
        asyncTwitter = factory.getInstance();
        asyncTwitter.addListener(listener);

        asyncTwitter.setOAuthConsumer(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
        asyncTwitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
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

    public void setListAdapters(PictureStatusAdapter pictureStatusAdapter, ArrayAdapter<String> trendAdapter) {
        this.pictureStatusAdapter = pictureStatusAdapter;
        this.trendAdapter = trendAdapter;
    }

    public void searchTweets(final String q, final Long maxId, final Integer count) {

        AsyncTask<Void, Void, List<Status>> task = new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                try {
                    Query query = new Query(StringUtils.join(" ", new String[]{q, SEARCH_FILTER_OPTION_NORT, SEARCH_FILTER_OPTION_IMAGE}));
                    if (maxId != null) {
                        query.maxId(maxId);
                    }
                    query.count(count);
                    QueryResult res = twitter.search(query);

                    RateLimitStatus rateLimitStatus = res.getRateLimitStatus();
                    logApiRemining(rateLimitStatus);

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
                for (twitter4j.Status status : TwitterManager.filterImageTweet(tweets)) {
                    PictureStatus pictureStatus = new PictureStatus(status);
                    pictureStatus.asyncImage(pictureStatusAdapter);
                }
            }
        };
        task.execute();
    }

    public void searchTweetsNext() {
        AsyncTask<Void, Void, List<Status>> task = new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                try {
                    QueryResult res = twitter.search(nextQuery);

                    RateLimitStatus rateLimitStatus = res.getRateLimitStatus();
                    logApiRemining(rateLimitStatus);

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
                for (twitter4j.Status status : TwitterManager.filterImageTweet(tweets)) {
                    PictureStatus pictureStatus = new PictureStatus(status);
                    pictureStatus.asyncImage(pictureStatusAdapter);
                }
            }
        };
        task.execute();
    }

    public void setTrends() {
        asyncTwitter.getPlaceTrends(WOEID_JAPAN);
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

    public static String TAG_API = "TwitterAPI";

    public static void logApiRemining(int remining, int limit) {
        Log.d(TAG_API, String.format("%3d/%3d", remining, limit));
    }

    public static void logApiRemining(RateLimitStatus rate) {
        logApiRemining(rate.getRemaining(), rate.getLimit());
    }
}
