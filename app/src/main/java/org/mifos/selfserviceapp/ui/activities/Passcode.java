package org.mifos.selfserviceapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.views.PasscordView;
import org.mifos.selfserviceapp.utils.Constants;

/**
 * Created by SHIVAM on 07-03-2017.
 */

public class Passcode extends BaseActivity implements PasscordView {

    AppCompatButton button;
    EditText et_passcode;
    EditText et_repasscode;
    TextInputLayout textInputlayout;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int passcodeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        et_repasscode = (EditText) findViewById(R.id.et_repasscode);
        et_passcode = (EditText) findViewById(R.id.et_passcode);
        button = (AppCompatButton) findViewById(R.id.btn_setbutton);
        textInputlayout = (TextInputLayout) findViewById(R.id.inputLayoutRepasscode);

        sp = getApplicationContext().getSharedPreferences(Constants.SHAREDPREF_NAME, MODE_PRIVATE);
        editor = sp.edit();

        passcodeStatus = sp.getInt(Constants.SP_KEY_PASSCODESTATUS, Constants.PASSCODE_NOT_SET);
        Log.i("abcde", passcodeStatus + " PasscodeActivity");

    }

    @Override
    protected void onStart() {
        super.onStart();


        if (passcodeStatus == Constants.PASSCODE_NOT_SET) {

            //set and validate passcode + change passcodestatus  + login
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String passcode = et_passcode.getText().toString();
                    String rePasscode = et_repasscode.getText().toString();

                    if (passcode.equals(rePasscode) && passcode.length() == 4) {
                        editor.putString(Constants.SP_KEY_PASSCODE, passcode);
                        editor.putInt(Constants.SP_KEY_PASSCODESTATUS, Constants.PASSCODE_SET);
                        editor.commit();

                        //AUTHENTICATION SUCCESSFULL ...
                        String username = sp.getString(Constants.USERNAME, "-100");
                        String password = sp.getString(Constants.PASSWORD, "-100");
                        Intent i = new Intent(Passcode.this, LoginActivity.class);
                        i.putExtra("requestlogin", true);
                        startActivity(i);
                        finish();

                    } else {
                        Context c = getApplicationContext();
                        Toast.makeText(c, "Invalid Input !! ", Toast.LENGTH_LONG).show();
                    }


                }
            });

        } else if (passcodeStatus == Constants.PASSCODE_SET) {
            //Change UI
            et_repasscode.setVisibility(View.GONE);
            textInputlayout.setVisibility(View.GONE);
            button.setText("LOGIN");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String passCodekey = Constants.SP_KEY_PASSCODE;
                    String stored = sp.getString(passCodekey, "-1000");
                    if (stored.equals("-1000")) {
                        String message = "Passcode not set";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                    String currentPasscode = et_passcode.getText().toString();

                    if (currentPasscode.equals(stored) && currentPasscode.length() == 4) {

                        //AUTHENTICATION SUCCESSFULL ...
                        String username = sp.getString(Constants.USERNAME, "-2000");
                        String password = sp.getString(Constants.PASSWORD, "-2000");

                        Intent i = new Intent(Passcode.this, LoginActivity.class);
                        i.putExtra("requestlogin", true);
                        startActivity(i);
                        finish();


                    }
                }
            });

        }

    }


    @Override
    public void login(String username, String password) {
        LoginActivity.getInstance().loginRequest(username, password);
    }
}

