package com.elzup.pictter.pictter.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import jp.ogwork.gesturetransformableview.view.GestureTransformableImageView;

public class ShowImageActivity extends Activity {

    public final static String EXTRA_STRING_URL = "url";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String url = getIntent().getStringExtra(EXTRA_STRING_URL);

        final GestureTransformableImageView image = new GestureTransformableImageView(this,
                GestureTransformableImageView.GESTURE_DRAGGABLE |
                        GestureTransformableImageView.GESTURE_SCALABLE);
        image.setLimitScaleMin(1F);
        image.setLimitScaleMax(3.5F);
        setContentView(image);

        AsyncTask<String, Void, Bitmap> task = new AsyncTask<String, Void, Bitmap>() {
            AlertDialog dialog;

            @Override
            protected void onPreExecute() {
                ProgressBar bar = new ProgressBar(ShowImageActivity.this);
                bar.setIndeterminate(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowImageActivity.this);
                builder.setView(bar);
                dialog = builder.create();
                dialog.show();
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    input.close();
                    return myBitmap;
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                if (result != null) {
                    dialog.dismiss();
                    image.setImageBitmap(result);
                } else {
//                      new ShowToast("画像の取得に失敗しました", ShowImage.this);
                }
            }
        };
        task.execute(url);
    }
}
