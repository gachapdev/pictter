package com.elzup.pictter.pictter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by mike on 15/04/25.
 */
public class CustomAdapter extends ArrayAdapter<CustomData> {
    private Activity activity;
    private MyPagerAdapter adapter;
    private LayoutInflater layoutInflater_;
    private static final float BUTTON_WIDTH_DP = 70f;
    private int margin;

    public CustomAdapter(Context context, int textViewResourceId, List<CustomData> objects) {
        super(context, textViewResourceId, objects);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.activity = (Activity) this.getContext();
        //ページ2のRelativeLayoutの幅を計算してmarginへ格納する。
        float density = getContext().getResources().getDisplayMetrics().density;
        int buttonWidthPX = (int) (BUTTON_WIDTH_DP * density + 0.5f);

        WindowManager wm = (WindowManager) getContext().getSystemService(getContext().WINDOW_SERVICE);
        Display dp = wm.getDefaultDisplay();
        margin = dp.getWidth() - buttonWidthPX;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (null == convertView) {
            convertView = layoutInflater_.inflate(R.layout.row, null);
        }

        //margin分スクロールしてViewを貼りつける
        ViewPager viewPager = (ViewPager) convertView.findViewById(R.id.viewpager);
        viewPager.setPageMargin(-margin);


        adapter = new MyPagerAdapter(getContext(), getItem(position));
        adapter.setActivity(activity);

        viewPager.setAdapter(adapter);


        return convertView;
    }



}