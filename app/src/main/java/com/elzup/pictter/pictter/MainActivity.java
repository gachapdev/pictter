package com.elzup.pictter.pictter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;

public class MainActivity extends Activity {

    private TwitterManager twitterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.twitterManager = new TwitterManager();
        if (!this.loginCheck()) {
            return;
        }
        setContentView(R.layout.activity_main2);
        this.twitterManager.setupClient();
        String keyword = getString(R.string.debug_default_search_q);
        this.twitterManager.searchTweets(keyword, new SearchTweetsCallback<Search>());
    }

    private boolean loginCheck() {
        if (twitterManager.loginCheck(this)) {
            return true;
        }
        // 認証セッションが残っていなければログイン画面へ
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class SearchTweetsCallback<Search> extends Callback<Search> {

        @Override
        public void success(Result<Search> searchResult) {
            System.out.println(searchResult.response.getStatus());
//            searchResult.data.tweets;
        }

        @Override
        public void failure(TwitterException e) {

        }
    }


}
