package org.mifos.selfserviceapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.local.PreferencesHelper;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.utils.Constants;

/**
 * Created by SHIVAM on 07-03-2017.
 */

public class Passcode extends BaseActivity {


    AppCompatButton button;
    EditText et_passcode;
    EditText et_repasscode;
    TextInputLayout textInputlayout;
    PreferencesHelper pHelper;
    int passcodeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        et_repasscode = (EditText) findViewById(R.id.et_repasscode);
        et_passcode = (EditText) findViewById(R.id.et_passcode);
        button = (AppCompatButton) findViewById(R.id.btn_setbutton);
        textInputlayout = (TextInputLayout) findViewById(R.id.inputLayoutRepasscode);
        pHelper = new PreferencesHelper(Passcode.this);

        String statusKey = Constants.SP_KEY_PASSCODESTATUS;
        passcodeStatus = pHelper.getInt(statusKey, Constants.PASSCODE_NOT_SET);

    }

    @Override
    protected void onStart() {
        super.onStart();


        if (passcodeStatus == Constants.PASSCODE_NOT_SET) {

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //set and validate passcode + change passcodestatus  + login
                    actionWhenPasscodeNotSet();
                }
            });

        } else if (passcodeStatus == Constants.PASSCODE_SET) {
            //Change UI
            changeUI();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionWhenPasscodeSet();
                }
            });

        }

    }

    private void changeUI() {
        et_repasscode.setVisibility(View.GONE);
        textInputlayout.setVisibility(View.GONE);
        button.setText("LOGIN");
    }

    private void actionWhenPasscodeSet() {
        String passCodekey = Constants.SP_KEY_PASSCODE;
        String stored = pHelper.getString(passCodekey, "-1000");
        if (stored.equals("-1000")) {
            String message = "Passcode not set";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        String currentPasscode = et_passcode.getText().toString();

        if (currentPasscode.equals(stored) && currentPasscode.length() == 4) {

            //AUTHENTICATION SUCCESSFULL ...
            String username = pHelper.getString(Constants.USERNAME, "-2000");
            String password = pHelper.getString(Constants.PASSWORD, "-2000");

            Intent i = new Intent(Passcode.this, LoginActivity.class);
            i.putExtra(Constants.REQUESTLOGIN_KEY, true);
            startActivity(i);
            finish();


        }
    }

    private void actionWhenPasscodeNotSet() {
        String passcode = et_passcode.getText().toString();
        String rePasscode = et_repasscode.getText().toString();

        if (passcode.equals(rePasscode) && passcode.length() == 4) {
            pHelper.putString(Constants.SP_KEY_PASSCODE, passcode);
            pHelper.putInt(Constants.SP_KEY_PASSCODESTATUS, Constants.PASSCODE_SET);

            String username = pHelper.getString(Constants.USERNAME, "-100");
            String password = pHelper.getString(Constants.PASSWORD, "-100");
            Intent i = new Intent(Passcode.this, LoginActivity.class);
            i.putExtra(Constants.REQUESTLOGIN_KEY, true);
            startActivity(i);
            finish();

        } else {
            Context c = getApplicationContext();
            Toast.makeText(c, "Invalid Input !! ", Toast.LENGTH_LONG).show();
        }
    }

}

