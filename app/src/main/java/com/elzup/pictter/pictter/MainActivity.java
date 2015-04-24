package com.elzup.pictter.pictter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import de.timroes.android.listview.EnhancedListView;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.Twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private CustomAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Example: single kit
        TwitterAuthConfig authConfig = new TwitterAuthConfig("consumerKey", "consumerSecret");

        Fabric.with(this, new Twitter(authConfig));

        // Example: multiple kits
        Fabric.with(this, new Twitter(authConfig));


        // リソースに準備した画像ファイルからBitmapを作成しておく
        Bitmap image;
        image = BitmapFactory.decodeResource(getResources(), R.drawable.ingatya);

        // データの作成
        List<CustomData> objects = new ArrayList<CustomData>();
        CustomData item1 = new CustomData();
        item1.setImagaData(image);
        item1.setTextData("１つ目〜");

        CustomData item2 = new CustomData();
        item2.setImagaData(image);
        item2.setTextData("The second");

        CustomData item3 = new CustomData();
        item3.setImagaData(image);
        item3.setTextData("Il terzo");

        objects.add(item1);
        objects.add(item2);
        objects.add(item3);
        objects.add(item3);
        objects.add(item3);
        objects.add(item3);
        objects.add(item3);
        objects.add(item3);
        objects.add(item3);


        mAdapter = new CustomAdapter(this, 0, objects);

        EnhancedListView mListView = (EnhancedListView)findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);

        mListView.setDismissCallback(new de.timroes.android.listview.EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {

                final CustomData item = (CustomData) mAdapter.getItem(position);
                /// 消す処理
                mAdapter.remove(item);
                mAdapter.notifyDataSetChanged();
//                return new EnhancedListView.Undoable() {
//                    @Override
//                    public void undo() {
                        // 元に戻す処理
//                        mItemList.add(position, item);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                };
                return null;
            }
        });
        mListView.enableSwipeToDismiss();
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
