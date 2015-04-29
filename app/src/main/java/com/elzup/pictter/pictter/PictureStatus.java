package com.elzup.pictter.pictter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.net.URL;

import twitter4j.Status;

/**
 * Created by hiro on 4/29/15.
 */

public class PictureStatus {
    private Status status;
    private Bitmap image;

    PictureStatus(Status status) {
        this.status = status;
        this.image = null;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public String getText() {
        return this.status.getText();
    }

    public void asyncImage(final ArrayAdapter<PictureStatus> toriggerAdapter) {

        AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    URL url = new URL(PictureStatus.this.status.getMediaEntities()[0].getMediaURL());
                    return BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bimage) {
                image = bimage;
                toriggerAdapter.add(PictureStatus.this);
            }
        };
        task.execute();
    }
}

