package com.mifos.mobile.passcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.mifos.mobile.passcode.utils.PasscodePreferencesHelper;
import com.mifos.mobile.passcode.utils.EncryptionUtil;

import org.mifos.mobile.utils.Toaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;


public class TransferVerificationActivity extends
        AppCompatActivity implements
        MifosPassCodeView.
                PassCodeListener {

    NestedScrollView clRootview;
    AppCompatButton btnForgotPasscode;
    MifosPassCodeView mifosPassCodeView;
    AppCompatButton btnSkip;
    AppCompatButton btnSave;
    TextView tvPasscodeIntro;
    ImageView ivVisibility;
    ImageView ivLogo;

    private PasscodePreferencesHelper passcodePreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);

        clRootview = findViewById(R.id.cl_rootview);
        btnForgotPasscode = findViewById(R.id.btn_forgot_passcode);
        mifosPassCodeView = findViewById(R.id.pv_passcode);
        btnSkip = findViewById(R.id.btn_skip);
        btnSkip.setText(getString(org.mifos.mobile.R.string.cancel_transaction));
        btnSave = findViewById(R.id.btn_save);
        btnSave.setText(getString(org.mifos.mobile.R.string.transfer));
        tvPasscodeIntro = findViewById(R.id.tv_passcode);
        tvPasscodeIntro.setText(getString(org.mifos.mobile.R.string.transfer_verify_passcode));
        ivVisibility = findViewById(R.id.iv_visibility);
        ivLogo = findViewById(R.id.iv_logo);
        ivLogo.setImageResource(org.mifos.mobile.R.drawable.mifos_logo);

        passcodePreferencesHelper = new PasscodePreferencesHelper(this);

    }

    private String encryptPassCode(String passCode) {
        String encryptedPassCode = EncryptionUtil.getMobileBankingHash(passCode);
        return encryptedPassCode;
    }


    public void savePassCode(View view) {
        if (isPassCodeLengthCorrect()) {
            if (encryptPassCode(mifosPassCodeView.getPasscode())
                    .equals(passcodePreferencesHelper
                            .getPassCode())
            ) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            } else {
                Toaster.show(view, org.mifos.mobile.R.string.incorrect_passcode);
            }
        } else {
            Toaster.show(view, org.mifos.mobile.R.string.incorrect_passcode);
        }
    }

    @Override
    public void passCodeEntered(String passcode) { }

    public void clickedOne(View v) {
        mifosPassCodeView.enterCode(getString(R.string.one));
    }

    public void clickedTwo(View v) {
        mifosPassCodeView.enterCode(getString(R.string.two));
    }

    public void clickedThree(View v) {
        mifosPassCodeView.enterCode(getString(R.string.three));
    }

    public void clickedFour(View v) {
        mifosPassCodeView.enterCode(getString(R.string.four));
    }

    public void clickedFive(View v) {
        mifosPassCodeView.enterCode(getString(R.string.five));
    }

    public void clickedSix(View v) {
        mifosPassCodeView.enterCode(getString(R.string.six));
    }

    public void clickedSeven(View v) {
        mifosPassCodeView.enterCode(getString(R.string.seven));
    }

    public void clickedEight(View v) {
        mifosPassCodeView.enterCode(getString(R.string.eight));
    }

    public void clickedNine(View v) {
        mifosPassCodeView.enterCode(getString(R.string.nine));
    }

    public void clickedZero(View v) {
        mifosPassCodeView.enterCode(getString(R.string.zero));
    }

    public void clickedBackSpace(View v) {
        mifosPassCodeView.backSpace();
    }

    public void skip(View v) {
        finish();
    }

    /**
     * @param view PasscodeView that changes to text if it was hidden and vice a versa
     */
    public void visibilityChange(View view) {
        mifosPassCodeView.revertPassCodeVisibility();
        if (!mifosPassCodeView.passcodeVisible()) {
            ivVisibility.setColorFilter(
                    ContextCompat.getColor(
                            TransferVerificationActivity.this,
                    R.color.light_grey));
        } else {
            ivVisibility.setColorFilter(
                    ContextCompat.getColor(
                            TransferVerificationActivity.this,
                    R.color.gray_dark));
        }
    }

    /**
     * Checks whether passcode entered is of correct length
     *
     * @return Returns true if passcode lenght is 4 else shows message
     */
    private boolean isPassCodeLengthCorrect() {
        if (mifosPassCodeView.getPasscode().length() == 4) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() { }

    @Override
    protected void onResume() {
        super.onResume();
    }
}