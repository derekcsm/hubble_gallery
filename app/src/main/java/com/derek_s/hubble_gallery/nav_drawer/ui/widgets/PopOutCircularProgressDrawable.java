package com.derek_s.hubble_gallery.nav_drawer.ui.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * @author Hannes Dorfmann
 */
public class PopOutCircularProgressDrawable extends CircularProgressDrawable {

    // TODO make private and final
    public static float POPOUT_SHOW_DURATION = 800;
    public static float POPOUT_HIDE_DURATION = ANGLE_ANIMATOR_DURATION;

    private boolean mPopOut = true;
    private int mPopOutColor;
    private float mTargetRadius;
    private float mCurrentRadius;
    private float mCircleCenterX;
    private float mCircleCenterY;
    private ObjectAnimator mShowPopOutAnimator;
    private ObjectAnimator mHidePopOutAnimator;
    private float mProgress = 0f;
    private int mHidePopOutProgress = 255;
    private boolean mPopOutComplete = false;
    private boolean mPopOutFading = true;
    private boolean mOriginalPopOutFading;
    private Paint mPaint = new Paint();

    public PopOutCircularProgressDrawable(int popOutColor, int[] colors, float strokeWidth,
                                          float popOutStrokeWidth, float speed, int minSweepAngle, int maxSweepAngle,
                                          boolean popOutFading, Style style) {

        super(colors, strokeWidth, speed, minSweepAngle, maxSweepAngle, style);
        mPopOutColor = popOutColor;
        mPopOutFading = popOutFading;
        mOriginalPopOutFading = popOutFading;
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(popOutStrokeWidth);
        mPaint.setColor(mPopOutColor);

        int diff = (int) ((popOutStrokeWidth - strokeWidth) / 2 + 0.5f);

        setCirclePadding(diff, diff, diff, diff);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        mCircleCenterX = bounds.exactCenterX();
        mCircleCenterY = bounds.exactCenterY();
        mTargetRadius = (bounds.height() - mPaint.getStrokeWidth()) / 2;
    }

    public PopOutCircularProgressDrawable setPopOutColor(int color) {
        mPopOutColor = color;
        mPaint.setColor(color);
        return this;
    }

    public int getPopOutColor() {
        return mPaint.getColor();
    }

    public PopOutCircularProgressDrawable setPopOut(boolean enabled) {
        mPopOut = enabled;
        return this;
    }

    public boolean isPopOutEnabled() {
        return mPopOut;
    }

    @Override
    public void reset() {
        super.reset();
        mPopOutComplete = false;
        mCurrentRadius = 0;
        mPaint.setAlpha(255);
        mPaint.setColor(mPopOutColor);
        mPopOutFading = mOriginalPopOutFading;
    }


    private void startPopOutAnimation() {
        stop();

        mShowPopOutAnimator =
                ObjectAnimator.ofFloat(this, "progress", 0f, 1f).setDuration(
                        (int) (POPOUT_SHOW_DURATION / getSpeed() + 0.5));
        mShowPopOutAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        mShowPopOutAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                reset();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startCircleAndPopHiding();
            }
        });

        mShowPopOutAnimator.start();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mCurrentRadius, mPaint);
    }

    @Override
    public void setProgress(float progress) {
        mProgress = progress;

        if (mPopOutFading) {
            int targetAlpha = Color.alpha(mPopOutColor);
            int currentAlpha = (int) (mapPoint(progress, 0, 0.85f, targetAlpha / 10, targetAlpha) + 0.5);
            mPaint.setAlpha(currentAlpha);
            if (currentAlpha >= targetAlpha) {
                mPopOutFading = false;
            }
        }

        mCurrentRadius = mapPoint(progress, 0, 1, 0, mTargetRadius);
        if (progress == 1f) {
            mPopOutComplete = true;
        }
        invalidateSelf();
    }

    public float getProgress() {
        return mProgress;
    }

    public float getHidePopOut() {
        return mHidePopOutProgress;
    }

    public void setHidePopOut(int progress) {
        mHidePopOutProgress = progress;
        mPaint.setAlpha(progress);
        invalidateSelf();
    }

    protected void startCircleAndPopHiding() {
        super.start();
        mHidePopOutAnimator =
                ObjectAnimator.ofInt(this, "hidePopOut", Color.alpha(mPopOutColor), 0).setDuration(
                        (int) (POPOUT_HIDE_DURATION / getSpeed() + 0.5f));

        mHidePopOutAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                reset();
            }
        });
        mHidePopOutAnimator.start();
    }

    @Override
    public void start() {
        if (!mPopOutComplete) {
            startPopOutAnimation();
        } else {
            startCircleAndPopHiding();
        }
    }

    @Override
    public void stop() {
        if (mShowPopOutAnimator != null) {
            mShowPopOutAnimator.cancel();
        }

        if (mHidePopOutAnimator != null) {
            mHidePopOutAnimator.cancel();
        }

        super.stop();
    }
}