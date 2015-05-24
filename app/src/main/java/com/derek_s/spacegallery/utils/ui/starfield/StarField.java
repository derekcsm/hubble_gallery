package com.derek_s.spacegallery.utils.ui.starfield;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

public class StarField {

    private String TAG = getClass().getSimpleName();
    private SurfaceHolder surfaceHolder;
    private boolean mVisible = true;
    private Handler mHandler = new Handler();

    private int mWidth, mHeight, xWidth;
    private int mOffsetSpan;
    private int mOffset = 1;
    private int mTiles = TILES_NORMAL;
    private int mDirection = Stars.RIGHT;
    private float mAmmount = AMMOUNT_NORMAL;

    private Stars[] stars;

    private static final int TILES_NORMAL = 1;
    private static final int TILES_LARGE = 5;
    private static final int TILES_HUGE = 7;

    private static final float AMMOUNT_FEW = 3f;
    private static final float AMMOUNT_NORMAL = 2f;
    private static final float AMMOUNT_LOTS = 0.2f;

    private Runnable mDraw = new Runnable() {
        @Override
        public void run() {
            drawFrame();
        }
    };

    Paint mPaintFill = new Paint();
    Paint mPaintStar = new Paint();
    Paint mPaintText = new Paint();

    public StarField(SurfaceHolder surfaceHolder, int mWidth, int mHeight) {
        this.surfaceHolder = surfaceHolder;

        this.mWidth = mWidth;
        this.mHeight = mHeight;

        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(Color.BLACK);

        mPaintStar.setStyle(Paint.Style.FILL);
        mPaintStar.setColor(Color.WHITE);
        mPaintStar.setAntiAlias(true);
    }

    public void start() {
        drawFrame();
    }

    public void stop() {
        mVisible = false;
        mHandler.removeCallbacks(mDraw);
    }

    private void updateXWidthAndOffsetSpan() {
        xWidth = mWidth * mTiles;
        mOffsetSpan = mWidth * (mTiles - 1);
    }

    private boolean firstTime = true;

    private void drawFrame() {
        Canvas c = null;
        if (firstTime) {
            firstTime = false;
            updateXWidthAndOffsetSpan();
            try {
                c = surfaceHolder.lockCanvas();
                if (c != null) {
                    c.drawRect(0, 0, mWidth, mHeight, mPaintFill);
                    c.drawText("Loading", 5, 75, mPaintText);
                }
            } finally {
                if (c != null) surfaceHolder.unlockCanvasAndPost(c);
                c = null;
            }
            Stars far = new Stars(1f, 1f, Math.round(3 * mAmmount), xWidth, mHeight);
            Stars middle = new Stars(2.1f, 1.5f, Math.round(5 * mAmmount), xWidth, mHeight);
            Stars near = new Stars(2.9f, 2.5f, Math.round(7 * mAmmount), xWidth, mHeight);
            Stars close = new Stars(4f, 15f, Math.round(40 * mAmmount), xWidth, mHeight);

            stars = new Stars[]{far, middle, near, close};

            int steps = mWidth * 2;
            for (Stars star : stars) {
                for (int i = 0; i < steps; i++) {
                    star.step(mDirection);
                }
            }
        }

        try {
            c = surfaceHolder.lockCanvas();
            if (c != null) {
                c.drawRect(0, 0, mWidth, mHeight, mPaintFill);

                for (Stars star : stars) {
                    star.step(mDirection);
                    star.draw(c, mWidth, mOffset, mPaintStar);
                }
            }
        } finally {
            if (c != null) surfaceHolder.unlockCanvasAndPost(c);
        }

        mHandler.removeCallbacks(mDraw);
        if (mVisible) {
            mHandler.postDelayed(mDraw, 40);
        }
    }
}