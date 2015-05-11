package com.elzup.pictter.pictter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class AboutImage extends Activity {

    private CustomView customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CustomView(this));
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

        private float mPrevX, mPrevY;

        float postScalePosX;
        float postScalePosY;

        private Bitmap image;

        private TranslationGestureDetector mTranslationGestureDetector;

        private TranslationGestureListener mTranslationListener
                = new TranslationGestureListener() {
            @Override
            public void onTranslationEnd(TranslationGestureDetector detector) {
            }

            @Override
            public void onTranslationBegin(TranslationGestureDetector detector) {
                mPrevX = detector.getX();
                mPrevY = detector.getY();
            }

            @Override
            public void onTranslation(TranslationGestureDetector detector) {
                float deltaX = detector.getX() - mPrevX;
                float deltaY = detector.getY() - mPrevY;
                mTranslateX += deltaX;
                mTranslateY += deltaY;
                mPrevX = detector.getX();
                mPrevY = detector.getY();
            }
        };


        public CustomView(Context context) {
            super(context);
            getHolder().addCallback(this);
            gesture = new ScaleGestureDetector(context, mOnScaleListener);
            mTranslationGestureDetector = new TranslationGestureDetector(mTranslationListener);
            mMatrix = new Matrix();
            setOnTouchListener(this);
            image = PictureStatusAdapter.img;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mHolder = holder;
            mTranslateX = width / 2;
            mTranslateY = height / 2;
            scale = (image.getWidth() * height > width * image.getHeight()) ? (float) width / image.getWidth() : height / (float) image.getHeight();
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
        };

        public void present() {
            Canvas canvas = mHolder.lockCanvas();

            mMatrix.reset();


//            mMatrix.postScale(scale, scale, w / 2, h / 2);
//            mMatrix.postScale(scale, scale, Math.abs(postScalePosX + mTranslateX - canvas.getWidth() / 2), Math.abs(postScalePosY + mTranslateY - canvas.getHeight() / 2));
//            mMatrix.postScale(scale, scale);
            mMatrix.postScale(scale, scale, postScalePosX, postScalePosY);
            mMatrix.postTranslate(-image.getWidth() / 2, -image.getHeight() / 2);
            mMatrix.postTranslate(mTranslateX, mTranslateY);

            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(PictureStatusAdapter.img, mMatrix, null);

//            this.drawGridLine(canvas);
            mHolder.unlockCanvasAndPost(canvas);
        }

        public void drawGridLine(Canvas canvas) {
            // Debug Grid line
            int h = canvas.getHeight();
            int w = canvas.getWidth();

            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(3);
            canvas.drawLine(w / 2, 0, w / 2, h - 1, paint);
            canvas.drawLine(0, h / 2, w - 1, h / 2, paint);

            paint.setColor(Color.argb(75, 100, 100, 100));
            paint.setStrokeWidth(1);
            for (int y = 0; y < h; y = y + 10) {
                paint.setStrokeWidth(y % 100 == 0 ? 2 : 1);
                canvas.drawLine(0, y, w - 1, y, paint);
            }
            for (int x = 0; x < w; x = x + 10) {
                paint.setStrokeWidth(x % 100 == 0 ? 2 : 1);
                canvas.drawLine(x, 0, x, h - 1, paint);
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mTranslationGestureDetector.onTouch(v, event);
            gesture.onTouchEvent(event);
            if (event.getPointerCount() >= 2) {
                postScalePosX = Math.abs(((event.getX(0) + event.getX(1)) / 2));
                postScalePosY = Math.abs(((event.getY(0) + event.getY(1)) / 2));
            }
            present();
            return true;
        }
    }
}
