package com.derek_s.spacegallery.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.derek_s.spacegallery.R;
import com.derek_s.spacegallery.utils.svg.SvgView;
import com.derek_s.spacegallery.utils.ui.FontFactory;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dereksmith on 15-05-15.
 */
public class FragWelcomeOutline extends Fragment {

    @InjectView(R.id.hubble_outline)
    SvgView hubbleOutline;
    @InjectView(R.id.tv_app_title)
    TextView tvTitle;
    @InjectView(R.id.iv_scroll_more_down)
    ImageView ivScrollDown;
    Handler showTitleHandler;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedState) {
        View rootView = inflater.inflate(R.layout.frag_welcome_outline, container, false);
        ButterKnife.inject(this, rootView);

        /*
        show initial intro animation
        */
        YoYo.with(Techniques.FadeIn).duration(800).playOn(hubbleOutline);
        hubbleOutline.setSvgResource(R.raw.hubble_outline);
        hubbleOutline.setVisibility(View.VISIBLE);
        tvTitle.setTypeface(FontFactory.getCondensedLight(getActivity()));

        showTitleHandler = new Handler();
        showTitleHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvTitle.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeInUp).duration(1000).playOn(tvTitle);
                ivScrollDown.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeInUp).duration(1200).playOn(ivScrollDown);
            }
        }, 4000);


        return rootView;
    }

}