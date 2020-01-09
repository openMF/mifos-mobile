package org.mifos.mobile.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mifos.mobile.passcode.utils.PasscodePreferencesHelper;

import org.mifos.mobile.R;
import org.mifos.mobile.utils.Constants;

public class splash extends AppCompatActivity {

    PasscodePreferencesHelper passcodePreferencesHelper;
    Intent intent;
    static int SPLASH_TIME_OUT=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


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
