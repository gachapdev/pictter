package com.elzup.pictter.pictter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mike on 15/04/25.
 */
public class CustomAdapter extends ArrayAdapter<CustomData> {
    private Activity activity;
    List<CustomData> objects;
    private LayoutInflater layoutInflater_;
    private static final float BUTTON_WIDTH_DP = 70f;
    private int margin;

    public CustomAdapter(Context context, int textViewResourceId, List<CustomData> objects) {
        super(context, textViewResourceId, objects);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.objects = objects;
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
            convertView = layoutInflater_.inflate(R.layout.item_layout, null);
        }


        //イメージをタップして詳細表示
        ImageView imageView;
        imageView = (ImageView) convertView.findViewById(R.id.image);
        imageView.setImageBitmap(objects.get(position).getImageData());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main4 = new Intent();
                main4.setClassName("com.elzup.pictter.pictter", "com.elzup.pictter.pictter.AboutImage");
                main4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(main4);
            }


        });

        TextView textView;
        textView = (TextView) convertView.findViewById(R.id.text);
        textView.setText(objects.get(position).getTextData());
        //margin分スクロールしてViewを貼りつける
//        ViewPager viewPager = (ViewPager) convertView.findViewById(R.id.viewpager);
//        viewPager.setPageMargin(-margin);
//
//
//        adapter = new MyPagerAdapter(getContext(), getItem(position));
//        adapter.setActivity(activity);
//
//        viewPager.setAdapter(adapter);


        return convertView;
    }


}