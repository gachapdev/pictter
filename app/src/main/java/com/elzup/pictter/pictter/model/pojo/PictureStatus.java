package com.elzup.pictter.pictter.model.pojo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.elzup.pictter.pictter.view.adapter.PictureStatusGridAdapter;
import com.elzup.pictter.pictter.view.adapter.PictureStatusListAdapter;
import com.twitter.sdk.android.core.models.Tweet;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by hiro on 4/29/15.
 */

public class PictureStatus implements Serializable {
    private int type;
    private String url;
    private String text;
    private Bitmap image;

    private boolean isSelected;

    public PictureStatus(Tweet tweet) {
        this.image = null;
        this.text = tweet.text;
        this.url = tweet.entities.media.get(0).mediaUrl;
        this.isSelected = false;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public String getText() {
        return this.text;
    }

    public String getImageUrl() {
        return this.url;
    }

    public void asyncImage(final ArrayList<PictureStatus> statusList, final PictureStatusListAdapter pictureStatusListAdapter, final PictureStatusGridAdapter pictureStatusGridAdapter) {

        AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    URL mUrl = new URL(url);
                    return BitmapFactory.decodeStream(mUrl.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bimage) {
                image = bimage;
                statusList.add(0, PictureStatus.this);
                pictureStatusListAdapter.notifyDataSetChanged();
                pictureStatusGridAdapter.notifyDataSetChanged();
            }
        };
        task.execute();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void toggleSelected() {
        isSelected = !isSelected;
    }
}

