package com.derek_s.hubble_gallery.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery.base.Constants;
import com.derek_s.hubble_gallery.ui.fragments.FragDetails;

public class ActDetails extends ActionBarActivity implements FragDetails.OnFragmentInteractionListener {

    private String TAG = getClass().getSimpleName();
    FragDetails fragDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        fragDetails = fragDetails.newInstance(intent.getStringExtra(Constants.PARAM_TILE_KEY));

        setContentView(R.layout.act_details);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragDetails)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.fade_in_shadow, R.anim.slide_out_right);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO
    }

}
