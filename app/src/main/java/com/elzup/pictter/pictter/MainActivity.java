package com.elzup.pictter.pictter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import io.fabric.sdk.android.Fabric;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;

public class MainActivity extends Activity {

    private TwitterSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        session = Twitter.getSessionManager().getActiveSession();
        if (session == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_main2);
    }

    private void clearSession() {
        Twitter.getSessionManager().clearSession(session.getId());
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
}
