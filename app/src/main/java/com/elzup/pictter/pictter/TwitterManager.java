package com.elzup.pictter.pictter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.fabric.sdk.android.Fabric;

public class TwitterManager {

    private TwitterSession session;

    private TwitterCore twitter;

    private boolean isLogin;

    private ArrayList<PictureStatus> statusList;
    private PictureStatusListAdapter pictureStatusListAdapter;
    private PictureStatusGridAdapter pictureStatusGridAdapter;
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
        this.setLogin(this.hasSession());
        if (!this.isLogin()) {
            return;
        }
        twitter = TwitterCore.getInstance();
//        setupTwitter(token.token, token.secret);
//        setupTwitterListener(token.token, token.secret);
    }

//    private void setupTwitterListener(String token, String tokenSecret) {
//        TwitterListener listener = new TwitterAdapter() {
//
//            @Override
//            public void gotPlaceTrends(Trends trends) {
//                ArrayList<String> trendWords = new ArrayList<>();
//                for (Trend trend : trends.getTrends()) {
//                    Log.d("Trend", trend.getName() + " " + trend.getURL());
//                    trendWords.add(trend.getName());
//                }
//                trendAdapter.addAll(trendWords);
//            }
//        };
//        AsyncTwitterFactory factory = new AsyncTwitterFactory();
//        asyncTwitter = factory.getInstance();
//        asyncTwitter.addListener(listener);
//
//        asyncTwitter.setOAuthConsumer(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
//        asyncTwitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
//    }
//
//    public void setupTwitter(String token, String tokenSecret) {
//        ConfigurationBuilder builder = new ConfigurationBuilder();
//        builder.setOAuthConsumerKey(BuildConfig.CONSUMER_KEY);
//        builder.setOAuthConsumerSecret(BuildConfig.CONSUMER_SECRET);
//        builder.setOAuthAccessToken(token);
//        builder.setOAuthAccessTokenSecret(tokenSecret);
//        Configuration conf = builder.build();
//        TwitterFactory factory = new TwitterFactory(conf);
//        twitter = factory.getInstance();
//    }

    private void setupSession(Context context) {
        if (this.hasSession()) {
            return;
        }
        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
        Fabric.with(context, new com.twitter.sdk.android.Twitter(authConfig));
        this.session = com.twitter.sdk.android.Twitter.getSessionManager().getActiveSession();
    }

    public void setListAdapters(ArrayList<PictureStatus> statusList, PictureStatusListAdapter pictureStatusListAdapter, PictureStatusGridAdapter pictureStatusGridAdapter, final ArrayAdapter<String> trendAdapter) {
        this.statusList = statusList;
        this.pictureStatusListAdapter = pictureStatusListAdapter;
        this.pictureStatusGridAdapter = pictureStatusGridAdapter;
        this.trendAdapter = trendAdapter;
    }

    public void searchTweets(final String keyword, final Long maxId, final Integer count) {

        String q = StringUtils.join(" ", new String[]{keyword, SEARCH_FILTER_OPTION_NORT, SEARCH_FILTER_OPTION_IMAGE});

        twitter.getApiClient().getSearchService().tweets(q, null, null, null, null, count, null, maxId, null, null, new Callback<Search>() {
            @Override
            public void success(Result<Search> searchResult) {
                for (Tweet tweet : filterImageTweet(searchResult.data.tweets)) {
                    PictureStatus pictureStatus = new PictureStatus(tweet);
                    pictureStatus.asyncImage(statusList, pictureStatusListAdapter, pictureStatusGridAdapter);
                }
                // TODO: log nextQuery
            }

            @Override
            public void failure(TwitterException e) {

            }
        });
    }

    public boolean searchTweetsNext() {
        return false;
    }

    public void setTrends() {
        // TODO:
        // asyncTwitter.getPlaceTrends(WOEID_JAPAN);
    }

    public boolean hasSession() {
        return this.session != null;
    }

    public void clearSession() {
        if (!this.hasSession()) {
            return;
        }
//        com.twitter.sdk.android.Twitter.getSessionManager().clearSession(session.getId());
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
    }

    public static List<Tweet> filterImageTweet(List<Tweet> tweets) {
        List<Tweet> resList = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if (tweet.entities.media != null && tweet.entities.media.size() != 0) {
                resList.add(tweet);
            }
        }
        return tweets;
    }

    public static String TAG_API = "TwitterAPI";

    public static void logApiRemining(int remining, int limit) {
        // TODO:
    }
}
