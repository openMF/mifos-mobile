package org.mifos.mobilebanking.utils;

import android.R.attr;
import android.R.id;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


/**
 * @author Rajan Maurya
 */
public class ProgressBarHandler {

    private ProgressBar mProgressBar;
    private Context mContext;

    public ProgressBarHandler(Context context) {
        mContext = context;

        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(id.content).getRootView();

        mProgressBar = new ProgressBar(context, null, attr.progressBarStyleInverse);
        mProgressBar.setIndeterminate(true);

        LayoutParams params = new
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        RelativeLayout rl = new RelativeLayout(context);

        rl.setGravity(Gravity.CENTER);
        rl.addView(mProgressBar);

        layout.addView(rl, params);

        hide();
    }

    public void show() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}