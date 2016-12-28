package com.derek_s.hubble_gallery.utils.svg;

/**
 * Created by dereksmith on 15-05-04.
 */

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.derek_s.hubble_gallery.R;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"ForLoopReplaceableByForEach", "UnusedDeclaration"})
public class SvgView extends View {
  private static final String LOG_TAG = "IntroView";

  private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

  private final SvgHelper mSvg = new SvgHelper(mPaint);
  private final Object mSvgLock = new Object();
  private int mSvgResource;
  private List<SvgHelper.SvgPath> mPaths = new ArrayList<>(0);
  private Thread mLoader;

  private float mPhase;
  private float mWait;
  private float mDrag;

  private int mDuration;
  private float mFadeFactor;

  private int mRadius;

  private ObjectAnimator mSvgAnimator;
  private ObjectAnimator mWaitAnimator;

  private OnReadyListener mListener;

  public SvgView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SvgView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IntroView, defStyle, 0);
    try {
      if (a != null) {
        mPaint.setStrokeWidth(a.getFloat(R.styleable.IntroView_strokeWidth, 1.0f));
        mPaint.setColor(a.getColor(R.styleable.IntroView_strokeColor, 0xff000000));
        mPhase = a.getFloat(R.styleable.IntroView_phase, 1.0f);
        mDuration = a.getInt(R.styleable.IntroView_duration, 4000);
        mFadeFactor = a.getFloat(R.styleable.IntroView_fadeFactor, 10.0f);
        mRadius = a.getDimensionPixelSize(R.styleable.IntroView_waitRadius, 50);
      }
    } finally {
      if (a != null) a.recycle();
    }

    init();
  }

  private static PathEffect createPathEffect(float pathLength, float phase, float offset) {
    return new DashPathEffect(new float[]{pathLength, pathLength},
        Math.max(phase * pathLength, offset));
  }

  private void init() {
    mPaint.setStyle(Paint.Style.STROKE);

    // Note: using a software layer here is an optimization. This view works with
    // hardware accelerated rendering but every time a path is modified (when the
    // dash path effect is modified), the graphics pipeline will rasterize the path
    // again in a new texture. Since we are dealing with dozens of paths, it is much
    // more efficient to rasterize the entire view into a single re-usable texture
    // instead. Ideally this should be toggled using a heuristic based on the number
    // and or dimensions of paths to render.
    // Note that PathDashPathEffects can lead to clipping issues with hardware rendering.
    setLayerType(LAYER_TYPE_SOFTWARE, null);

    mSvgAnimator = ObjectAnimator.ofFloat(this, "phase", 0.0f, 1.0f).setDuration(mDuration);

    mWaitAnimator = ObjectAnimator.ofFloat(this, "wait", 1.0f, 0.0f).setDuration(mDuration);
    mWaitAnimator.setRepeatMode(ObjectAnimator.RESTART);
    mWaitAnimator.setRepeatCount(ObjectAnimator.INFINITE);
    mWaitAnimator.setInterpolator(new LinearInterpolator());
    mWaitAnimator.start();
  }

  public void setSvgResource(int resource) {
    if (mSvgResource == 0) {
      mSvgResource = resource;
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    synchronized (mSvgLock) {
      canvas.save();
      canvas.translate(getPaddingLeft(), getPaddingTop() - getPaddingBottom());
      final int count = mPaths.size();
      for (int i = 0; i < count; i++) {
        SvgHelper.SvgPath svgPath = mPaths.get(i);

        // We use the fade factor to speed up the alpha animation
        int alpha = (int) (Math.min(mPhase * mFadeFactor, 1.0f) * 255.0f);
        svgPath.paint.setAlpha(alpha);

        canvas.drawPath(svgPath.renderPath, svgPath.paint);
      }
      canvas.restore();
    }

    canvas.save();
    canvas.translate(0.0f, getHeight() - getPaddingBottom() - mRadius * 3.0f);

    canvas.restore();
  }

  @Override
  protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    if (mLoader != null) {
      try {
        mLoader.join();
      } catch (InterruptedException e) {
        Log.e(LOG_TAG, "Unexpected error", e);
      }
    }

    mLoader = new Thread(new Runnable() {
      @Override
      public void run() {
        mSvg.load(getContext(), mSvgResource);
        synchronized (mSvgLock) {
          mPaths = mSvg.getPathsForViewport(
              w - getPaddingLeft() - getPaddingRight(),
              h - getPaddingTop() - getPaddingBottom());
          updatePathsPhaseLocked();
        }
        post(new Runnable() {
          @Override
          public void run() {
            invokeReadyListener();
            if (mSvgAnimator.isRunning()) mSvgAnimator.cancel();
            mSvgAnimator.start();
          }
        });
      }
    }, "SVG Loader");
    mLoader.start();
  }

  private void invokeReadyListener() {
    if (mListener != null) mListener.onReady();
  }

  public void setOnReadyListener(OnReadyListener listener) {
    mListener = listener;
  }

  private void updatePathsPhaseLocked() {
    final int count = mPaths.size();
    for (int i = 0; i < count; i++) {
      SvgHelper.SvgPath svgPath = mPaths.get(i);
      svgPath.renderPath.reset();
      svgPath.measure.getSegment(0.0f, svgPath.length * mPhase, svgPath.renderPath, true);
      // Required only for Android 4.4 and earlier
      svgPath.renderPath.rLineTo(0.0f, 0.0f);
    }
  }

  public float getPhase() {
    return mPhase;
  }

  public void setPhase(float phase) {
    mPhase = phase;
    synchronized (mSvgLock) {
      updatePathsPhaseLocked();
    }
    invalidate();
  }

  public float getWait() {
    return mWait;
  }

  public void setWait(float wait) {
    mWait = wait;

    invalidate();
  }

  public interface OnReadyListener {
    void onReady();
  }


}