package com.elzup.pictter.pictter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private TwitterManager twitterManager;
    private PictureStatusAdapter pictureStatusAdapter;
    private SwipeActionAdapter swipeAdapter;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private View searchBar;
    private EditText searchEditText;
    private CharSequence mTitle;

    private InputMethodManager inputMethodManager;

    public static String PREFERENCE_KEYWORDS = "keywords";
    public static String PREFERENCE_KEYWORDS_DELIMITER = ":::";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.twitterManager = new TwitterManager(this);
        if (!this.loginCheck()) {
            return;
        }

        setContentView(R.layout.activity_main);
        List<String> initKeywords = this.loadPreferenceKeywords();
        setupNavigation(initKeywords);
        setupSearchForm();
        setupAdapter();
        setupSwipeRefreshLayout();
        if (initKeywords.size() > 0) {
            searchKeyword(initKeywords.get(0));
        }
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

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MainActivity.PlaceholderFragment.newInstance(position + 1))
                .commit();
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

    private void setupNavigation(List<String> initKeywords) {
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setNavDrawerListClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                searchKeyword(textView.getText().toString());
            }
        });
        mNavigationDrawerFragment.setToggleListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferenceKeywords();
            }
        });
        mNavigationDrawerFragment.setLogoutListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterManager.clearSession();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        mNavigationDrawerFragment.addFavoriteKeywordAll(initKeywords);
    }

    private void setupSearchForm() {
        this.searchBar = getLayoutInflater().inflate(R.layout.search_bar, null);
        searchEditText = (EditText) this.searchBar.findViewById(R.id.searchBar);
        searchEditText.setFocusable(true);
        final Button searchButton = (Button) this.searchBar.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchSubmit();
            }
        });

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        searchEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchSubmit();
                    return true;
                }
                return false;
            }
        });
    }

    private void setupAdapter() {
        pictureStatusAdapter = new PictureStatusAdapter(this, 0, new ArrayList<PictureStatus>());
        ListView listView = (ListView) findViewById(R.id.list);

        swipeAdapter = new SwipeActionAdapter(pictureStatusAdapter);
        swipeAdapter.setListView(listView);
        listView.setAdapter(swipeAdapter);

        listView.addHeaderView(this.searchBar);

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
                PictureStatus status = pictureStatusAdapter.getItem(position - 1);
                pictureStatusAdapter.remove(status);
                switch (direction) {
                    case SwipeDirections.DIRECTION_NORMAL_LEFT:
                    case SwipeDirections.DIRECTION_FAR_LEFT:
                        break;
                    case SwipeDirections.DIRECTION_NORMAL_RIGHT:
                    case SwipeDirections.DIRECTION_FAR_RIGHT:
                        DeviceUtils.saveToFile(MainActivity.this, status.getImage());
                        break;
                }
            }
        });

    }



    private void searchSubmit(String keyword) {
        if ("".equals(keyword)) {
            return;
        }
        searchKeyword(keyword);
        killFocus();
    }

    private void searchSubmit() {
        this.searchSubmit(searchEditText.getText().toString());
    }

    private void searchKeyword(String keyword) {
        pictureStatusAdapter.clear();
        mTitle = "Pictter - " + keyword;
        getSupportActionBar().setTitle(mTitle);
        twitterManager.searchTweets(keyword, null, getResources().getInteger(R.integer.search_tweet_limit), pictureStatusAdapter);
        mNavigationDrawerFragment.addSearchKeyword(keyword);
        this.savePreferenceKeywords();
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

    public void setupSwipeRefreshLayout() {
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_1, R.color.refresh_2, R.color.refresh_3);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                twitterManager.searchTweetsNext(pictureStatusAdapter);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private List<String> loadPreferenceKeywords() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String keywords = preferences.getString(PREFERENCE_KEYWORDS, "");
        if ("".equals(keywords)) {
            return new ArrayList<>();
        }
        return Arrays.asList(keywords.split(getResources().getString(R.string.separator_save_keywords)));
    }

    private void savePreferenceKeywords(List<String> keywords) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String keywordStr = StringUtils.join(getResources().getString(R.string.separator_save_keywords), keywords);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFERENCE_KEYWORDS, keywordStr);
        editor.commit();
    }

    private void savePreferenceKeywords() {
        this.savePreferenceKeywords(mNavigationDrawerFragment.getFavorteKeywords());
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
        }
    }

}
