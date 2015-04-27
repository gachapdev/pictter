package com.elzup.pictter.pictter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.Twitter;

import java.util.ArrayList;
import java.util.List;

public class
        MainActivity extends Activity {

    private CustomAdapter customAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Example: single kit
        TwitterAuthConfig authConfig = new TwitterAuthConfig("consumerKey", "consumerSecret");

        Fabric.with(this, new Twitter(authConfig));

        // Example: multiple kits
        Fabric.with(this, new Twitter(authConfig));

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


        //ここからはサンプル処理
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

        customAdapater = new CustomAdapter(this, 0, objects);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(customAdapater);


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




