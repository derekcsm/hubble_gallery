package com.derek_s.hubble_gallery.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.derek_s.hubble_gallery.utils.network.NetworkUtil;

import javax.inject.Inject;

/**
 * Created by derek on 15-08-09.
 */

public class FragBase extends Fragment {

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        HubbleApplication.component(getActivity()).inject(this);
    }

    @Inject
    public NetworkUtil networkUtil;

    @Inject
    public TinyDB db;

}
