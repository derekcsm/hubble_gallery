package com.derek_s.spacegallery.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.derek_s.spacegallery.R;
import com.derek_s.spacegallery.base.Constants;
import com.derek_s.spacegallery.model.TileObject;
import com.derek_s.spacegallery.ui.fragments.FragDetails;

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
