package com.derek_s.hubble_gallery.utils.ui;

import android.os.Handler;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.derek_s.hubble_gallery.nav_drawer.ui.widgets.CircleProgressView;

/**
 * Created by dereksmith on 15-03-22.
 */
public class IndeterminateAnimator {

    public static void show(final CircleProgressView prog, Techniques technique) {
        prog.setVisibility(View.VISIBLE);
        YoYo.with(technique).duration(300).playOn(prog);
    }

    public static void stop(final CircleProgressView prog, Techniques technique) {
        YoYo.with(technique).duration(300).playOn(prog);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                prog.setVisibility(View.GONE);
            }
        }, 320);
    }

}
