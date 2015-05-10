package com.elzup.pictter.pictter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.PointF;

import java.io.File;


public class AboutImage extends Activity {

    private CustomView customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CustomView(this));
//        gesture  = new ScaleGestureDetector(this,j);
//        ImageView imageView = (ImageView) findViewById(R.id.imageView3);
//        imageView.setImageBitmap(PictureStatusAdapter.img);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_image, menu);
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

    class CustomView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

        private float scale;
        private ScaleGestureDetector gesture;
        private SurfaceHolder mHolder;
        private Matrix mMatrix;
        private float mTranslateX, mTranslateY;
        float postScalePosX;
        float postScalePosY;

        public CustomView(Context context) {
            super(context);
            getHolder().addCallback(this);
            scale = 1.0f;
            gesture = new ScaleGestureDetector(context, mOnScaleListener);
            mMatrix = new Matrix();
            setOnTouchListener(this);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mHolder = holder;
            mTranslateX = width / 2;
            mTranslateY = height / 2;
            present();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        private ScaleGestureDetector.SimpleOnScaleGestureListener mOnScaleListener
                = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return super.onScaleBegin(detector);
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                super.onScaleEnd(detector);
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scale *= detector.getScaleFactor();
                return true;
            }

            ;
        };

        public void present() {
            Canvas canvas = mHolder.lockCanvas();

            mMatrix.reset();
            mMatrix.postScale(scale, scale, postScalePosX, postScalePosY);
            mMatrix.postTranslate(-PictureStatusAdapter.img.getWidth() / 2, -PictureStatusAdapter.img.getHeight() / 2);
            mMatrix.postTranslate(mTranslateX, mTranslateY);

            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(PictureStatusAdapter.img, mMatrix, null);
            mHolder.unlockCanvasAndPost(canvas);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gesture.onTouchEvent(event);
            if(event.getPointerCount() >= 2) {
                postScalePosX = Math.abs((event.getX(0) + event.getX(1)) / 2);
                postScalePosY = Math.abs((event.getY(0) + event.getY(1)) / 2);
                present();
            }
            return true;
        }
    }
}
