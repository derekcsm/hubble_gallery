package com.derek_s.hubble_gallery.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.derek_s.hubble_gallery.internal.di.DaggerActivityComponent;
import com.derek_s.hubble_gallery.utils.network.NetworkUtil;

import javax.inject.Inject;

public class FragBase extends Fragment {

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        DaggerActivityComponent.builder()
                .appComponent(((HubbleApplication) getActivity().getApplication()).getAppComponent())
                .build()
                .inject(this);
    }

    @Inject
    public NetworkUtil networkUtil;

    @Inject
    public TinyDB db;

}
