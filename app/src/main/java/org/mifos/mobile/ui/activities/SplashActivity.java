package org.mifos.mobile.ui.activities;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mifos.mobile.passcode.utils.PasscodePreferencesHelper;

import org.mifos.mobile.R;
import org.mifos.mobile.ui.activities.base.BaseActivity;
import org.mifos.mobile.utils.Constants;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    PasscodePreferencesHelper passcodePreferencesHelper;
    Intent intent;
    static int SPLASH_TIME_OUT=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                condition();
            }
        },SPLASH_TIME_OUT);
    }

    public void condition()
    {
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