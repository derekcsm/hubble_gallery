package com.derek_s.spacegallery.utils.svg;

/**
 * Created by dereksmith on 15-05-04.
 */
import android.content.Context;
import android.graphics.*;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import com.caverock.androidsvg.PreserveAspectRatio;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

public class SvgHelper {
    private static final String LOG_TAG = "SVG";

    private final List<SvgPath> mPaths = new ArrayList<SvgPath>();
    private final Paint mSourcePaint;

    private SVG mSvg;

    public SvgHelper(Paint sourcePaint) {
        mSourcePaint = sourcePaint;
    }

    public void load(Context context, int svgResource) {
        if (mSvg != null) return;
        try {
            mSvg = SVG.getFromResource(context, svgResource);
            mSvg.setDocumentPreserveAspectRatio(PreserveAspectRatio.UNSCALED);
        } catch (SVGParseException e) {
            Log.e(LOG_TAG, "Could not load specified SVG resource", e);
        }
    }

    public static class SvgPath {
        private static final Region sRegion = new Region();
        private static final Region sMaxClip = new Region(
                Integer.MIN_VALUE, Integer.MIN_VALUE,
                Integer.MAX_VALUE, Integer.MAX_VALUE);

        final Path path;
        final Path renderPath = new Path();
        final Paint paint;
        final float length;
        final Rect bounds;
        final PathMeasure measure;

        SvgPath(Path path, Paint paint) {
            this.path = path;
            this.paint = paint;

            measure = new PathMeasure(path, false);
            this.length = measure.getLength();

            sRegion.setPath(path, sMaxClip);
            bounds = sRegion.getBounds();
        }
    }

    public List<SvgPath> getPathsForViewport(final int width, final int height) {
        mPaths.clear();

        Canvas canvas = new Canvas() {
            private final Matrix mMatrix = new Matrix();

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public void drawPath(Path path, Paint paint) {
                Path dst = new Path();

                //noinspection deprecation
                getMatrix(mMatrix);
                path.transform(mMatrix, dst);

                mPaths.add(new SvgPath(dst, new Paint(mSourcePaint)));
            }
        };

        RectF viewBox = mSvg.getDocumentViewBox();
        float scale = Math.min(width / viewBox.width(), height / viewBox.height());

        canvas.translate(
                (width - viewBox.width() * scale) / 2.0f,
                (height - viewBox.height() * scale) / 2.0f);
        canvas.scale(scale, scale);

        mSvg.renderToCanvas(canvas);

        return mPaths;
    }
}