package com.derek_s.hubble_gallery.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.derek_s.hubble_gallery.utils.network.NetworkUtil;

import javax.inject.Inject;

/**
 * Created by derek on 15-08-03.
 */

public class ActBase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        HubbleApplication.component(this).inject(this);
    }

    @Inject
    public NetworkUtil networkUtil;

    @Inject
    public TinyDB db;

}
