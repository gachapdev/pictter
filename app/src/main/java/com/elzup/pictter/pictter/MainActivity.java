package com.elzup.pictter.pictter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity {

    private CustomAdapter customAdapater;
    private TwitterManager twitterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.twitterManager = new TwitterManager(this);
        if (!this.loginCheck()) {
            return;
        }

        setContentView(R.layout.activity_main2);

        String keyword = getString(R.string.debug_default_search_q);

        //EditTextのフォーカスをきる
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setFocusable(false);

        //ボタンのでインスタンスを移動するまで
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main4 = new Intent();
                main4.setClassName("com.elzup.pictter.pictter", "com.elzup.pictter.pictter.ShowGridItem");
                startActivity(main4);
            }
        });

        customAdapater = new CustomAdapter(this, 0, new ArrayList<PictureStatus>());
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(customAdapater);
        SwipeAction touchListener =
                new SwipeAction(
                        listView,
                        new SwipeAction.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    customAdapater.remove(customAdapater.getItem(position));
                                    PictureStatus pictureStatus = customAdapater.getItem(position);
                                    DeviceUtils.saveToFile(pictureStatus.getImage());
                                }
                                customAdapater.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());

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

}

