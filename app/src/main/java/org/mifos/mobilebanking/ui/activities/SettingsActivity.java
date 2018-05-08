package org.mifos.mobilebanking.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.fragments.SettingsFragment;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setToolbarTitle(getString(R.string.settings));
        showBackButton();
        replaceFragment(SettingsFragment.newInstance(), false, R.id.container);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }
}
