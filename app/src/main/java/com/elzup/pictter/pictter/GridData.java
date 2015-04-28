package com.elzup.pictter.pictter;

import android.graphics.Bitmap;

/**
 * Created by mike on 15/04/26.
 */
public class GridData {
    private boolean check;
    private Bitmap imageData_;

    public void setCheck(boolean nowcheck) {
        check = nowcheck;
    }

    public boolean getCheck() {
        return check;
    }

    public void setImagaData(Bitmap image) {
        imageData_ = image;
    }

    public Bitmap getImageData() {
        return imageData_;
    }
}
