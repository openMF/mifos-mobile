package org.mifos.mobilebanking.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.fragments.SettingsFragment;
import org.mifos.mobilebanking.utils.Constants;

public class SettingsActivity extends BaseActivity {

    private boolean hasSettingsChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setToolbarTitle(getString(R.string.settings));
        showBackButton();
        replaceFragment(SettingsFragment.newInstance(), false, R.id.container);
        if (getIntent().hasExtra(Constants.HAS_SETTINGS_CHANGED)) {
            hasSettingsChanged = getIntent().getBooleanExtra(Constants.HAS_SETTINGS_CHANGED,
                    false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (hasSettingsChanged) {
            ActivityCompat.finishAffinity(this);
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }
    }
}
