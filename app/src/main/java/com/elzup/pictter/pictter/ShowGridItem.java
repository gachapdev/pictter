package com.elzup.pictter.pictter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


public class ShowGridItem extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);


        //ここからはSample
        Bitmap image;
        image = BitmapFactory.decodeResource(getResources(), R.drawable.ingatya);

        List<GridData> objects = new ArrayList<GridData>();

        int num = 3;
        GridData [] item = new GridData[num];
        for(int i = 1; i < num; i++) {
            item[i] = new GridData();
            item[i].setCheck(true);
            item[i].setImagaData(image);

            objects.add(item[i]);
        }

        GridAdapter gridAdapter = new GridAdapter(this,0,objects);

        GridView gridView =(GridView)findViewById(R.id.gridView);
        gridView.setAdapter(gridAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_grid_item, menu);
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
