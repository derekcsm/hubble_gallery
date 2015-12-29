package com.derek_s.hubble_gallery.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery.ui.activities.ActMain;
import com.derek_s.hubble_gallery.ui.activities.ActWelcome;
import com.derek_s.hubble_gallery.utils.ui.FontFactory;

/**
 * Created by derek on 15-06-01.
 */

// private String TAG = getClass().getSimpleName();
public class DialogAbout {
    private Context context;
    private TextView tvTitle, tvBuiltBy, tvSourceCode, tvBuiltContent, tvShowIntro;
    private View divider;

    public DialogAbout(Context context) {
        this.context = context;
    }

    public void displayDialog() {

        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_about, false)
                .backgroundColorRes(R.color.background)
                .theme(Theme.DARK)
                .build();
        dialog.show();
        View rootView = dialog.getCustomView();

        divider = rootView.findViewById(R.id.divider);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvTitle.setTypeface(FontFactory.getMedium(context));
        tvBuiltBy = (TextView) rootView.findViewById(R.id.tv_built_by);
        tvBuiltBy.setTypeface(FontFactory.getMedium(context));
        tvBuiltContent = (TextView) rootView.findViewById(R.id.tv_built_content);
        tvBuiltContent.setTypeface(FontFactory.getRegular(context));
        tvBuiltContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvShowIntro = (TextView) rootView.findViewById(R.id.tv_show_intro);
        tvShowIntro.setTypeface(FontFactory.getMedium(context));
        tvSourceCode = (TextView) rootView.findViewById(R.id.tv_source_code);
        tvSourceCode.setTypeface(FontFactory.getMedium(context));

        tvTitle.setTextColor(context.getResources().getColor(R.color.title_dark_theme));
        tvBuiltBy.setTextColor(context.getResources().getColor(R.color.body_dark_theme));
        tvBuiltContent.setTextColor(context.getResources().getColor(R.color.caption_dark_theme));
        divider.setBackgroundColor(context.getResources().getColor(R.color.divider_dark_theme));

        tvBuiltContent.setText(Html.fromHtml("Derek Smith <br><a href=\"http://www.derek-s.com/\">derek-s.com</a>"));

        tvShowIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActMain.instance, ActWelcome.class);
                ActMain.instance.startActivity(intent);
            }
        });

        tvSourceCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/derekcsm/hubble_gallery"));
                ActMain.instance.startActivity(browserIntent);
            }
        });

    }
}