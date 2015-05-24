package com.elzup.pictter.pictter.model.pojo;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.elzup.pictter.pictter.BuildConfig;
import com.elzup.pictter.pictter.model.pojo.PictureStatus;
import com.elzup.pictter.pictter.view.adapter.PictureStatusGridAdapter;
import com.elzup.pictter.pictter.view.adapter.PictureStatusListAdapter;
import com.elzup.pictter.pictter.controller.util.StringUtils;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class TwitterManager {

    private TwitterSession session;

    private TwitterCore twitter;

    private boolean isLogin;

    private String nextQueryStr;

    private ArrayList<PictureStatus> statusList;
    private PictureStatusListAdapter pictureStatusListAdapter;
    private PictureStatusGridAdapter pictureStatusGridAdapter;
    private ArrayAdapter<String> trendAdapter;
    private int searchLimit;

    private static final String SEARCH_IGNORE_OPERATOR = "-";
    private static final String SEARCH_FILTER_OPTION_RT = "RT";
    private static final String SEARCH_FILTER_OPTION_NORT = SEARCH_IGNORE_OPERATOR + SEARCH_FILTER_OPTION_RT;
    private static final String SEARCH_FILTER_OPTION_IMAGE = "filter:images";

    private static final String PARAM_MAX_ID = "max_id";
    private static final String PARAM_Q = "q";
    private static final String PARAM_COUNT = "count";

    public static int WOEID_JAPAN = 23424856;

    public TwitterManager(Context context) {
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
    }

    private void setupSession(Context context) {
        if (this.hasSession()) {
            return;
        }
        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
        Fabric.with(context, new com.twitter.sdk.android.Twitter(authConfig));
        this.session = com.twitter.sdk.android.Twitter.getSessionManager().getActiveSession();
    }

    public void setup(ArrayList<PictureStatus> statusList,
                      PictureStatusListAdapter pictureStatusListAdapter,
                      PictureStatusGridAdapter pictureStatusGridAdapter,
                      final ArrayAdapter<String> trendAdapter,
                      int searchLimit) {
        this.statusList = statusList;
        this.pictureStatusListAdapter = pictureStatusListAdapter;
        this.pictureStatusGridAdapter = pictureStatusGridAdapter;
        this.trendAdapter = trendAdapter;
        this.searchLimit = searchLimit;
    }

    public void searchTweets(final String keyword, final Long maxId, int count, boolean isNext) {
        String q = keyword;
        if (!isNext) {
            q = StringUtils.join(" ", new String[]{keyword, SEARCH_FILTER_OPTION_NORT, SEARCH_FILTER_OPTION_IMAGE});
        }

        twitter.getApiClient().getSearchService().tweets(q, null, null, null, null, count, null, null, maxId, true, new Callback<Search>() {
            @Override
            public void success(Result<Search> searchResult) {
                for (Tweet tweet : filterImageTweet(searchResult.data.tweets)) {
                    PictureStatus pictureStatus = new PictureStatus(tweet);
                    pictureStatus.asyncImage(statusList, pictureStatusListAdapter, pictureStatusGridAdapter);
                }
                nextQueryStr = searchResult.data.searchMetadata.nextResults;
            }

            @Override
            public void failure(TwitterException e) {

            }
        });
    }

    public void searchTweets(final String keyword, final Long maxId, int count) {
        this.searchTweets(keyword, maxId, this.searchLimit, false);
    }

    public void searchTweets(final String keyword, final Long maxId) {
        this.searchTweets(keyword, maxId, this.searchLimit);
    }


    public boolean searchTweetsNext() {
        if (null == this.nextQueryStr || "".equals(nextQueryStr)) {
            return false;
        }
        Map<String, String> map = StringUtils.getQueryMap(nextQueryStr);
        String q = URLDecoder.decode(map.get(PARAM_Q));
        long maxId = Long.parseLong(map.get(PARAM_MAX_ID));
        int count = Integer.parseInt(map.get(PARAM_COUNT));
        searchTweets(q, maxId, count, true);
        return true;
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

    public static long getOldId(List<Tweet> tweets) {
        long oldId = tweets.get(0).getId();
        for (Tweet tweet : tweets) {
            oldId = Math.min(oldId, tweet.getId());
        }
        return oldId - 1;
    }

    public static String TAG_API = "TwitterAPI";

    public static void logApiRemining(int remining, int limit) {
        // TODO:
    }
}
