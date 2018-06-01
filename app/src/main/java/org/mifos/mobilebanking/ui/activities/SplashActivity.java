package org.mifos.mobilebanking.ui.activities;

/*
 * Created by saksham on 01/June/2018
 */

import android.content.Intent;
import android.os.Bundle;

import com.mifos.mobile.passcode.utils.PasscodePreferencesHelper;

import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.utils.Constants;

public class SplashActivity extends BaseActivity {

    PasscodePreferencesHelper passcodePreferencesHelper;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        passcodePreferencesHelper = new PasscodePreferencesHelper(this);
        if (!passcodePreferencesHelper.getPassCode().isEmpty()) {
            intent = new Intent(this, PassCodeActivity.class);
            intent.putExtra(Constants.INTIAL_LOGIN, true);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
