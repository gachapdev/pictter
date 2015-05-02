package com.elzup.pictter.pictter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirections;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity {

    private CustomAdapter customAdapater;
    private TwitterManager twitterManager;

    private EditText search_box;
    private SwipeActionAdapter swipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.twitterManager = new TwitterManager(this);
        if (!this.loginCheck()) {
            return;
        }

        setContentView(R.layout.activity_main2);

        String keyword = getString(R.string.debug_default_search_q);

        setupSearchForm();

        customAdapater = new CustomAdapter(this, 0, new ArrayList<PictureStatus>());
        ListView listView = (ListView) findViewById(R.id.list);

        String[] content = new String[20];
        for (int i = 0; i < 20; i++) {
            content[i] = "Row " + (i + 1);
        }
        swipeAdapter = new SwipeActionAdapter(customAdapater);
        swipeAdapter.setListView(listView);
        listView.setAdapter(swipeAdapter);

        swipeAdapter.addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT, R.layout.row_bg_left)
                    .addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT, R.layout.row_bg_right);
        swipeAdapter.setSwipeActionListener(new SwipeActionAdapter.SwipeActionListener() {
            @Override
            public boolean hasActions(int i) {
                return true;
            }

            @Override
            public boolean shouldDismiss(int position, int direction) {
                return direction == SwipeDirections.DIRECTION_FAR_LEFT;
            }

            @Override
            public void onSwipe(int[] positionList, int[] directionList) {
                for (int i = 0; i < positionList.length; i ++) {
                    this.onSwipeSingle(positionList[i], directionList[i]);
                }
            }

            public void onSwipeSingle(int position, int direction) {
                PictureStatus status = customAdapater.getItem(position);
                customAdapater.remove(status);
                switch (direction) {
                    case SwipeDirections.DIRECTION_NORMAL_LEFT:
                    case SwipeDirections.DIRECTION_FAR_LEFT:
                        break;
                    case SwipeDirections.DIRECTION_NORMAL_RIGHT:
                    case SwipeDirections.DIRECTION_FAR_RIGHT:
                        DeviceUtils.saveToFile(status.getImage());
                        break;
                }
            }
        });

        this.twitterManager.searchTweets(keyword, null, getResources().getInteger(R.integer.search_tweet_limit), customAdapater);
    }

    private boolean loginCheck() {
        if (twitterManager.isLogin()) {
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

    private void setupSearchForm() {
        this.search_box = (EditText) findViewById(R.id.searchBar);
        search_box.setFocusable(true);
        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new onClickSearchListener());
    }

    class onClickSearchListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String keyword = search_box.getText().toString();
            if ("".equals(keyword)) {
                return;
            }
            customAdapater.clear();
            twitterManager.searchTweets(keyword, null, getResources().getInteger(R.integer.search_tweet_limit), customAdapater);
        }
    }

}

