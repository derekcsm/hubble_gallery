package com.derek_s.hubble_gallery.utils.Animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by dereksmith on 15-04-13.
 */
public class SquareFlipper {
    private View flippingView;
    private FlipAnimator flipAnimator;
    boolean stop = false;
    boolean justStarted = false;

    public void startAnimation(final View view) {
        if (flipAnimator != null) {
            flipAnimator.cancel();
            flipAnimator = null;
        }
        stop = false;
        justStarted = true;
        flippingView = view;
        flippingView.setVisibility(View.VISIBLE);
        flippingView.setAlpha(0);

        flipAnimator = new FlipAnimator();
        flipAnimator.setDuration(600);
        flipAnimator.setRepeatCount(-1);
        flipAnimator.setFillAfter(true);
        flipAnimator.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(flipAnimator);
    }

    public void stopAnimation() {
        stop = true;
    }

    public class FlipAnimator extends Animation {

        private Camera camera;
        private float centerX;
        private float centerY;

        public FlipAnimator() {
            setFillAfter(true);
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            camera = new Camera();
            this.centerX = width / 2;
            this.centerY = height / 2;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            // Angle around the y-axis of the rotation at the given time. It is
            // calculated both in radians and in the equivalent degrees.
            final double radians = Math.PI * interpolatedTime;
            float degrees = (float) (180.0 * radians / Math.PI);

            if (interpolatedTime > 0.4 && interpolatedTime < 0.6) {
                if (justStarted) {
                    flippingView.setAlpha(255);
                    YoYo.with(Techniques.FadeIn).duration(200).playOn(flippingView);
                    justStarted = false;
                } else if (stop) {
                    flippingView.setVisibility(View.INVISIBLE);
                    flippingView.clearAnimation();
                }
            }

            final Matrix matrix = t.getMatrix();

            camera.save();
            camera.translate(0.0f, 0.0f, (float) (150.0 * Math.sin(radians)));
            camera.rotateX(true ? degrees : 0);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}
