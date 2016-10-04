package org.mifos.selfserviceapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;

import javax.inject.Inject;

/**
 * This is the First Activity which can be used for initial checks, inits at app Startup
 */
public class SplashScreenActivity extends BaseActivity {

    @Inject
    DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getActivityComponent().inject(this);
        if (!dataManager.getPrefManager().isAuthenticated()) {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(SplashScreenActivity.this, ClientListActivity.class));
        }
        finish();
    }
}