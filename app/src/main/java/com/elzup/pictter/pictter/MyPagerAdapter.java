package com.elzup.pictter.pictter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by mike on 15/04/26.
 */
public class MyPagerAdapter extends PagerAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private static final int PAGE_NUM = 2;
    private String str;
    private CustomData item;

    public MyPagerAdapter(Context context,CustomData item) {
        super();
        inflater = LayoutInflater.from(context);
        this.item = item;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinearLayout layout = null;


        //謎複数のViewを重ねられる
        if(position == 0 ) {
            layout = (LinearLayout)inflater.inflate(R.layout.item_layout, null);

            //イメージをタップして詳細表示
            ImageView imageView;
            imageView = (ImageView) layout.findViewById(R.id.image);
            imageView.setImageBitmap(item.getImageData());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent main4 = new Intent();
                    main4.setClassName("com.elzup.pictter.pictter","com.elzup.pictter.pictter.AboutImage");
                    main4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(main4);
                }



            });

            TextView textView;
            textView = (TextView) layout.findViewById(R.id.text);
            textView.setText(item.getTextData());
        }
        else{
            layout = (LinearLayout)inflater.inflate(R.layout.page2, null);

        }



        container.addView(layout);

        return layout;
    }




    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ListView) object);
    }

    @Override
    public int getCount() {

        return PAGE_NUM;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {

        return view.equals(obj);
    }


    public  void setActivity(Activity activity){
        this.activity = activity;
    }

}