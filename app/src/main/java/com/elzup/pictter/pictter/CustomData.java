package com.elzup.pictter.pictter;

import android.graphics.Bitmap;

/**
 * Created by mike on 15/04/25.
 */
public class CustomData {
    private Bitmap imageData_;
    private String textData_;

    public void setImagaData(Bitmap image) {
        imageData_ = image;
    }

    public Bitmap getImageData() {
        return imageData_;
    }

    public void setTextData(String text) {
        textData_ = text;
    }

    public String getTextData() {
        return textData_;
    }
}