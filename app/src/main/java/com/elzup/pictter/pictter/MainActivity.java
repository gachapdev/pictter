package com.elzup.pictter.pictter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirections;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private PictureStatusAdapter pictureStatusAdapter;
    private TwitterManager twitterManager;

    private EditText search_box;
    private SwipeActionAdapter swipeAdapter;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.twitterManager = new TwitterManager(this);
        if (!this.loginCheck()) {
            return;
        }

        setContentView(R.layout.activity_main);
        setupNavigation();

        String keyword = getString(R.string.debug_default_search_q);

        setupSearchForm();
        setupAdapter();

        this.twitterManager.searchTweets(keyword, null, getResources().getInteger(R.integer.search_tweet_limit), pictureStatusAdapter);
    }

    private void setupNavigation() {
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private void setupAdapter() {
        pictureStatusAdapter = new PictureStatusAdapter(this, 0, new ArrayList<PictureStatus>());
        ListView listView = (ListView) findViewById(R.id.list);

        swipeAdapter = new SwipeActionAdapter(pictureStatusAdapter);
        swipeAdapter.setListView(listView);
        listView.setAdapter(swipeAdapter);

        swipeAdapter.addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT, R.layout.row_bg_left)
                .addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT, R.layout.row_bg_right);
        swipeAdapter.setFarSwipeFraction(0);
        swipeAdapter.setFarSwipeFraction(1);
        swipeAdapter.setSwipeActionListener(new SwipeActionAdapter.SwipeActionListener() {
            @Override
            public boolean hasActions(int i) {
                return true;
            }

            @Override
            public boolean shouldDismiss(int position, int direction) {
                return true;
            }

            @Override
            public void onSwipe(int[] positionList, int[] directionList) {
                for (int i = 0; i < positionList.length; i++) {
                    this.onSwipeSingle(positionList[i], directionList[i]);
                }
            }

            public void onSwipeSingle(int position, int direction) {
                PictureStatus status = pictureStatusAdapter.getItem(position);
                pictureStatusAdapter.remove(status);
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
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.nav_main, menu);
            restoreActionbar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    class onClickSearchListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String keyword = search_box.getText().toString();
            if ("".equals(keyword)) {
                return;
            }
            mTitle = "Pictter - " + keyword;
            pictureStatusAdapter.clear();
            twitterManager.searchTweets(keyword, null, getResources().getInteger(R.integer.search_tweet_limit), pictureStatusAdapter);
            killFocus();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MainActivity.PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "Pictter - hoge";
                break;
            case 2:
                mTitle = "Pictter - fuga";
                break;
            case 3:
                mTitle = "Pictter - foo";
                break;
        }
    }

    public void restoreActionbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    private void killFocus() {
        TextView mainLayout = (TextView) findViewById(R.id.killFocus);
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        mainLayout.requestFocus();
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_nav_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
