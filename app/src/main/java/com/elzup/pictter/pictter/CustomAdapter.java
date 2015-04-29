package com.elzup.pictter.pictter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import twitter4j.Status;

/**
 * Created by mike on 15/04/25.
 */
public class CustomAdapter extends ArrayAdapter<Status> {
    private Activity activity;
    List<Status> tweets;
    private LayoutInflater layoutInflater_;
    private static final float BUTTON_WIDTH_DP = 70f;
    private int margin;

    public CustomAdapter(Context context, int textViewResourceId, List<Status> tweets) {
        super(context, textViewResourceId, tweets);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tweets = tweets;
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
        Status tweet = tweets.get(position);

        //イメージをタップして詳細表示
        ImageView imageView;
        imageView = (ImageView) convertView.findViewById(R.id.image);

        this.asyncSetImage(tweet, imageView);

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
        textView.setText(tweet.getText());

        return convertView;
    }

    private void asyncSetImage(Status tweet, ImageView imageView) {

        class Param {
            Status tweet;
            ImageView imageView;
            Param(Status tweet, ImageView imageView) {
                this.tweet = tweet;
                this.imageView = imageView;
            }
        }
        final Param param = new Param(tweet, imageView);
        AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    URL url = new URL(param.tweet.getMediaEntities()[0].getMediaURL());
                    return BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap image) {
                param.imageView.setImageBitmap(image);
            }
        };
        task.execute();
    }


}