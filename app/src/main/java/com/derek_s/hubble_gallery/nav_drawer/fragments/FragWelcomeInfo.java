package com.derek_s.hubble_gallery.nav_drawer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery.base.FragBase;
import com.derek_s.hubble_gallery.nav_drawer.ui.activities.ActMain;
import com.derek_s.hubble_gallery.utils.ui.FontFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragWelcomeInfo extends FragBase {
  private static String TAG = "FragWelcomeInfo";
  private static String PAGE_KEY = "page_number";
  private static final String CURRENT_PAGE = "current_page";

  private static int pagePos = 1;

  public static final FragWelcomeInfo newInstance(int position) {
    final FragWelcomeInfo fragment = new FragWelcomeInfo();
    // Supply int position as an argument.
    Bundle args = new Bundle();
    args.putInt(PAGE_KEY, position);
    fragment.setArguments(args);

    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      pagePos = getArguments() != null ? getArguments().getInt(PAGE_KEY) : 1;
    } else {
      pagePos = savedInstanceState.getInt(CURRENT_PAGE);
    }
  }

  @Bind(R.id.tv_welcome_info)
  TextView tvInfo;
  @Bind(R.id.tv_enter)
  TextView tvEnter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.frag_welcome_info, container, false);
    ButterKnife.bind(this, rootView);

    tvInfo.setTypeface(FontFactory.getCondensedRegular(getActivity()));
    tvInfo.setVisibility(View.VISIBLE);
    tvEnter.setVisibility(View.INVISIBLE);

    Log.i(TAG, "onCreateView pagePos = " + pagePos);

    switch (pagePos) {
      case (1):
        tvInfo.setText(getActivity().getResources().getText(R.string.info_1));
        break;
      case (2):
        tvInfo.setText(getActivity().getResources().getText(R.string.info_2));
        break;
      case (3):
        tvInfo.setText(getActivity().getResources().getText(R.string.info_3));
        break;
      case (4):
        tvInfo.setVisibility(View.INVISIBLE);
        tvEnter.setTypeface(FontFactory.getCondensedLight(getActivity()));
        tvEnter.setVisibility(View.VISIBLE);
        tvEnter.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent i = new Intent(getActivity(), ActMain.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            getActivity().overridePendingTransition(android.R.anim.fade_in, R.anim.zoom_in_exit);
          }
        });
        break;
    }

    return rootView;
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putInt(CURRENT_PAGE, pagePos);
    super.onSaveInstanceState(outState);
  }


}