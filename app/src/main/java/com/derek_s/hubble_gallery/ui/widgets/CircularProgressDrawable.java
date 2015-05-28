package com.derek_s.hubble_gallery.ui.widgets;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class CircularProgressDrawable extends Drawable implements Animatable {

    public enum Style {
        NORMAL, ROUNDED
    }

    private static final ArgbEvaluator COLOR_EVALUATOR = new ArgbEvaluator();
    private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator SWEEP_INTERPOLATOR = new DecelerateInterpolator();
    protected static final int ANGLE_ANIMATOR_DURATION = 2000;
    protected static final int SWEEP_ANIMATOR_DURATION = 600;
    private final RectF fBounds = new RectF();

    private ObjectAnimator mObjectAnimatorSweepAppearing;
    private ObjectAnimator mObjectAnimatorSweepDisappearing;
    private ObjectAnimator mObjectAnimatorAngle;
    private boolean mModeAppearing;
    private Paint mPaint;
    private float mCurrentGlobalAngleOffset = 0;
    private float mCurrentGlobalAngle = 0;
    private float mCurrentSweepAngle;
    private boolean mRunning;
    private int mCurrentIndexColor;
    private int mCurrentColor;
    private Rect mCirclePadding = new Rect(0, 0, 0, 0);

    // params
    private float mStrokeWidth;
    private int[] mColors;
    private float mSpeed;
    private int mMinSweepAngle;
    private int mMaxSweepAngle;

    public CircularProgressDrawable(int[] colors, float strokeWidth, float speed, int minSweepAngle,
                                    int maxSweepAngle, Style style) {
        mStrokeWidth = strokeWidth;
        mSpeed = speed;
        mMinSweepAngle = minSweepAngle;
        mMaxSweepAngle = maxSweepAngle;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        setStrokeStyle(style);
        setStrokeWidth(strokeWidth);
        setColors(colors);

        setupAnimations();
    }

    public int getMinSweepAngle() {
        return mMinSweepAngle;
    }

    public void setMinSweepAngle(int mMinSweepAngle) {
        this.mMinSweepAngle = mMinSweepAngle;
    }

    public int getMaxSweepAngle() {
        return mMaxSweepAngle;
    }

    public void setMaxSweepAngle(int mMaxSweepAngle) {
        this.mMaxSweepAngle = mMaxSweepAngle;
    }

    public void setStrokeStyle(Style style) {
        mPaint.setStrokeCap(style == Style.ROUNDED ? Paint.Cap.ROUND : Paint.Cap.BUTT);
    }

    /**
     * Set the addition circle padding. It's the space between the edge of the drawable (canvas) and
     * the circle that will be drawn
     */
    public void setCirclePadding(int left, int top, int right, int bottom) {
        mCirclePadding.left = left;
        mCirclePadding.top = top;
        mCirclePadding.right = right;
        mCirclePadding.bottom = bottom;
    }

    public Rect getCirclePadding() {
        return mCirclePadding;
    }

    public void setStrokeWidth(float strokeWidth) {
        mStrokeWidth = strokeWidth;
        mPaint.setStrokeWidth(strokeWidth);
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setColors(int colors[]) {
        mColors = colors;
        mCurrentIndexColor = 0;
        mCurrentColor = mColors[0];
        mPaint.setColor(mCurrentColor);
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
    }

    public float getSpeed() {
        return mSpeed;
    }

    @Override
    public void draw(Canvas canvas) {
        float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
        float sweepAngle = mCurrentSweepAngle;
        if (!mModeAppearing) {
            startAngle = startAngle + (360 - sweepAngle);
            // sweepAngle = 360 - sweepAngle - mMinSweepAngle;
        } else {
            // sweepAngle += mMinSweepAngle;
        };

        startAngle %= 360;

        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        fBounds.left = bounds.left + mStrokeWidth / 2f + .5f + mCirclePadding.left;
        fBounds.right = bounds.right - mStrokeWidth / 2f - .5f - mCirclePadding.right;
        fBounds.top = bounds.top + mStrokeWidth / 2f + .5f + mCirclePadding.top;
        fBounds.bottom = bounds.bottom - mStrokeWidth / 2f - .5f - mCirclePadding.bottom;
    }

    private void setAppearing() {
        mModeAppearing = true;
        mCurrentGlobalAngleOffset += mMinSweepAngle;
    }

    private void setDisappearing() {
        mModeAppearing = false;
        mCurrentGlobalAngleOffset = mCurrentGlobalAngleOffset + (360 - mMaxSweepAngle);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // ////////////// Animation

    public static final Property<CircularProgressDrawable, Float> ROTATION_PROPERTY =
            new Property<CircularProgressDrawable, Float>(Float.class, "rotation") {
                @Override
                public Float get(CircularProgressDrawable object) {
                    return object.getCurrentGlobalAngle();
                }

                @Override
                public void set(CircularProgressDrawable object, Float value) {
                    object.setCurrentGlobalAngle(value);
                }
            };

    private Property<CircularProgressDrawable, Float> SWEEP_PROPERTY =
            new Property<CircularProgressDrawable, Float>(Float.class, "sweep") {
                @Override
                public Float get(CircularProgressDrawable object) {
                    return object.getCurrentSweepAngle();
                }

                @Override
                public void set(CircularProgressDrawable object, Float value) {
                    object.setCurrentSweepAngle(value);
                }
            };

    private void setupAnimations() {
        mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, ROTATION_PROPERTY, 360f);
        mObjectAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
        mObjectAnimatorAngle.setDuration(ANGLE_ANIMATOR_DURATION);
        mObjectAnimatorAngle.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);

        mObjectAnimatorSweepAppearing =
                ObjectAnimator.ofFloat(this, SWEEP_PROPERTY, mMinSweepAngle, mMaxSweepAngle);
        mObjectAnimatorSweepAppearing.setInterpolator(SWEEP_INTERPOLATOR);
        mObjectAnimatorSweepAppearing.setDuration((long) (SWEEP_ANIMATOR_DURATION / mSpeed));
        mObjectAnimatorSweepAppearing.addListener(new Animator.AnimatorListener() {
            boolean cancelled = false;

            @Override
            public void onAnimationStart(Animator animation) {
                cancelled = false;
                mModeAppearing = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!cancelled) {
                    setDisappearing();
                    mObjectAnimatorSweepDisappearing.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                cancelled = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        mObjectAnimatorSweepDisappearing =
                ObjectAnimator.ofFloat(this, SWEEP_PROPERTY, mMaxSweepAngle, mMinSweepAngle);
        mObjectAnimatorSweepDisappearing.setInterpolator(SWEEP_INTERPOLATOR);
        mObjectAnimatorSweepDisappearing.setDuration((long) (SWEEP_ANIMATOR_DURATION / mSpeed));
        mObjectAnimatorSweepDisappearing.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long duration = animation.getDuration();
                long played = animation.getCurrentPlayTime();
                float fraction = (float) played / duration;
                if (mColors.length > 1 && fraction > .7f) {
                    int prevColor = mCurrentColor;
                    int nextColor = mColors[(mCurrentIndexColor + 1) % mColors.length];
                    mCurrentColor =
                            (Integer) COLOR_EVALUATOR
                                    .evaluate((fraction - .7f) / (1 - .7f), prevColor, nextColor);
                    mPaint.setColor(mCurrentColor);
                }
            }
        });
        mObjectAnimatorSweepDisappearing.addListener(new Animator.AnimatorListener() {
            boolean cancelled;

            @Override
            public void onAnimationStart(Animator animation) {
                cancelled = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!cancelled) {
                    setAppearing();
                    mCurrentIndexColor = (mCurrentIndexColor + 1) % mColors.length;
                    mCurrentColor = mColors[mCurrentIndexColor];
                    mPaint.setColor(mCurrentColor);
                    // mCurrentGlobalAngle -= mMinSweepAngle;
                    mObjectAnimatorSweepAppearing.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                cancelled = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

    @Override
    public void start() {
        if (isRunning()) {
            return;
        }
        mRunning = true;
        mObjectAnimatorAngle.start();
        mObjectAnimatorSweepAppearing.start();
        invalidateSelf();
    }

    @Override
    public void stop() {
        if (!isRunning()) {
            return;
        }
        mRunning = false;
        mObjectAnimatorAngle.cancel();
        mObjectAnimatorSweepAppearing.cancel();
        mObjectAnimatorSweepDisappearing.cancel();
        invalidateSelf();
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    public void setCurrentGlobalAngle(float currentGlobalAngle) {
        mCurrentGlobalAngle = currentGlobalAngle;
        invalidateSelf();
    }

    public float getCurrentGlobalAngle() {
        return mCurrentGlobalAngle;
    }

    public void setCurrentSweepAngle(float currentSweepAngle) {
        mCurrentSweepAngle = currentSweepAngle;
        invalidateSelf();
    }

    public float getCurrentSweepAngle() {
        return mCurrentSweepAngle;
    }

    public void reset() {
        mCurrentColor = 0;
        mCurrentGlobalAngleOffset = 0;
        mCurrentGlobalAngle = 0;
        mCurrentSweepAngle = 0;
        mCurrentIndexColor = 0;
        mPaint.setColor(mColors[0]);
        invalidateSelf();
    }

    public void setProgress(float progress) {
        float progressMapped = mapPoint(progress, 0f, 1f, 0f, -360f);
        setCurrentGlobalAngle(0);
        setCurrentSweepAngle(progressMapped);

        if (progressMapped == -360) {
            start();
        } else {
            stop();
        }
    }

    /**
     * This method maps a number x, which is in the range [sourceStart, sourceEnd], to a new range
     * [targetStart, targetEnd]
     *
     * <p>
     * sourceStart <= x <= sourceEnd <br/>
     * targetStart <= returnValue <= targetEnd
     * </p>
     *
     * @param x The value that should be mapped
     * @param sourceStart The source range start (inclusive)
     * @param sourceEnd The source range end (inclusive)
     * @param targetStart The target range start (inclusive)
     * @param targetEnd The target range end (inclusive)
     * @return The corresponding value of x in the target range
     */
    protected float mapPoint(float x, float sourceStart, float sourceEnd, float targetStart,
                             float targetEnd) {

        if (x <= sourceStart) {
            return targetStart;
        }

        if (x >= sourceEnd) {
            return targetEnd;
        }

        return (x - sourceStart) / (sourceEnd - sourceStart) * (targetEnd - targetStart) + targetStart;
    }
}