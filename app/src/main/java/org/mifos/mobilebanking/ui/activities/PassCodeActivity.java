package org.mifos.mobilebanking.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.BaseApiManager;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.utils.CheckSelfPermissionAndRequest;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.EncryptionUtil;
import org.mifos.mobilebanking.utils.Network;
import org.mifos.mobilebanking.utils.PassCodeView;
import org.mifos.mobilebanking.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PassCodeActivity extends BaseActivity implements PassCodeView.PassCodeListener {

    @BindView(R.id.cl_rootview)
    NestedScrollView clRootview;

    @BindView(R.id.btn_forgot_passcode)
    AppCompatButton btnForgotPasscode;

    @BindView(R.id.pv_passcode)
    PassCodeView passCodeView;

    @BindView(R.id.btn_skip)
    AppCompatButton btnSkip;

    @BindView(R.id.btn_save)
    AppCompatButton btnSave;

    @BindView(R.id.tv_passcode)
    TextView tvPasscodeIntro;

    @BindView(R.id.iv_visibility)
    ImageView ivVisibility;

    @Inject
    PreferencesHelper preferencesHelper;

    private int counter = 1;
    private boolean isInitialScreen;
    private boolean isPassCodeVerified;
    private String strPassCodeEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        isInitialScreen = getIntent().getBooleanExtra(Constants.INTIAL_LOGIN, false);
        isPassCodeVerified = false;
        strPassCodeEntered = "";

        if (!preferencesHelper.getPasscode().isEmpty()) {
            btnSkip.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
            tvPasscodeIntro.setVisibility(View.GONE);
            btnForgotPasscode.setVisibility(View.VISIBLE);
            //enabling passCodeListener only when user has already setup PassCode
            passCodeView.setPassCodeListener(this);
        }

        if (!CheckSelfPermissionAndRequest.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)) {
            requestPermission();
        }
    }

    /**
     * Uses {@link CheckSelfPermissionAndRequest} to check for runtime permissions
     */
    private void requestPermission() {
        CheckSelfPermissionAndRequest.requestPermission(
                this,
                Manifest.permission.READ_PHONE_STATE,
                Constants.PERMISSIONS_REQUEST_READ_PHONE_STATE,
                getResources().getString(
                        R.string.dialog_message_phone_state_permission_denied_prompt),
                getResources().getString(R.string.
                        dialog_message_phone_state_permission_never_ask_again),
                Constants.PERMISSIONS_READ_PHONE_STATE_STATUS);
    }

    /**
     * Called when Skip button clicked, starts {@link HomeActivity}
     */
    @OnClick(R.id.btn_skip)
    public void skip() {
        startHomeActivity();
    }

    /**
     * Saves the passcode by encrypting it which we got from {@link PassCodeView}
     */
    @OnClick(R.id.btn_save)
    public void savePassCode() {
        if (isPassCodeLengthCorrect()) {
            if (isPassCodeVerified) {
                if (strPassCodeEntered.compareTo(passCodeView.getPasscode()) == 0) {
                    preferencesHelper.setPasscode(EncryptionUtil.getHash(passCodeView.
                            getPasscode()));
                    startHomeActivity();
                } else {
                    Toaster.show(clRootview, R.string.passcode_does_not_match);
                    passCodeView.clearPasscodeField();
                }
            } else {
                btnSkip.setVisibility(View.INVISIBLE);
                btnSave.setText(getString(R.string.save));
                tvPasscodeIntro.setText(getString(R.string.reenter_passcode));
                strPassCodeEntered = passCodeView.getPasscode();
                passCodeView.clearPasscodeField();
                isPassCodeVerified = true;
            }
        }
    }


    /**
     * It is a callback for {@link PassCodeView}, provides with the passcode entered by user
     * @param passcode
     */
    @Override
    public void passCodeEntered(String passcode) {

        if (!isInternetAvailable()) {
            passCodeView.clearPasscodeField();
            return;
        }

        if (counter == 3) {
            Toast.makeText(getApplicationContext(), R.string.incorrect_passcode_more_than_three,
                    Toast.LENGTH_SHORT).show();
            preferencesHelper.clear();
            startLoginActivity();
            return;
        }

        if (isPassCodeLengthCorrect()) {
            String passwordEntered = EncryptionUtil.getHash(passCodeView.getPasscode());
            if (preferencesHelper.getPasscode().equals(passwordEntered)) {
                BaseApiManager.createService(preferencesHelper.getToken());
                startHomeActivity();
            } else {
                counter++;
                passCodeView.clearPasscodeField();
                Toaster.show(clRootview, R.string.incorrect_passcode);
            }
        }
    }

    /**
     * Clears preferences data and starts {@link LoginActivity}
     */
    @OnClick(R.id.btn_forgot_passcode)
    public void forgotPassCode() {
        preferencesHelper.clear();
        startLoginActivity();
    }

    /**
     * Checks for internet availability
     * @return Returns true if connected else returns false
     */
    private boolean isInternetAvailable() {
        if (Network.isConnected(this)) {
            return true;
        } else {
            Toaster.show(clRootview, getString(R.string.no_internet_connection));
            return false;
        }
    }

    @OnClick(R.id.btn_one)
    public void clickedOne() {
        passCodeView.enterCode(getString(R.string.one));
    }

    @OnClick(R.id.btn_two)
    public void clickedTwo() {
        passCodeView.enterCode(getString(R.string.two));
    }

    @OnClick(R.id.btn_three)
    public void clickedThree() {
        passCodeView.enterCode(getString(R.string.three));
    }

    @OnClick(R.id.btn_four)
    public void clickedFour() {
        passCodeView.enterCode(getString(R.string.four));
    }

    @OnClick(R.id.btn_five)
    public void clickedFive() {
        passCodeView.enterCode(getString(R.string.five));
    }

    @OnClick(R.id.btn_six)
    public void clickedSix() {
        passCodeView.enterCode(getString(R.string.six));
    }

    @OnClick(R.id.btn_seven)
    public void clickedSeven() {
        passCodeView.enterCode(getString(R.string.seven));
    }

    @OnClick(R.id.btn_eight)
    public void clickedEight() {
        passCodeView.enterCode(getString(R.string.eight));
    }

    @OnClick(R.id.btn_nine)
    public void clickedNine() {
        passCodeView.enterCode(getString(R.string.nine));
    }

    @OnClick(R.id.btn_zero)
    public void clickedZero() {
        passCodeView.enterCode(getString(R.string.zero));
    }

    @OnClick(R.id.btn_back)
    public void clickedBackSpace() {
        passCodeView.backSpace();
    }

    /**
     * Changes PasscodeView to text if it was hidden and vice a versa
     */
    @OnClick(R.id.iv_visibility)
    public void visibilityChange() {
        passCodeView.revertPassCodeVisibility();
        if (!passCodeView.passcodeVisible()) {
            ivVisibility.setColorFilter(ContextCompat.getColor(PassCodeActivity.this,
                    R.color.light_grey));
        } else {
            ivVisibility.setColorFilter(ContextCompat.getColor(PassCodeActivity.this,
                    R.color.gray_dark));
        }
    }

    /**
     * Checks whether passcode entered is of correct length
     * @return Returns true if passcode lenght is 4 else shows message
     */
    private boolean isPassCodeLengthCorrect() {
        if (passCodeView.getPasscode().length() == 4) {
            return true;
        }
        Toaster.show(clRootview, getString(R.string.error_passcode));
        return false;
    }

    /**
     * Starts {@link HomeActivity} only if {@code isInitialScreen} is true and ends this activity
     */
    private void startHomeActivity() {
        if (isInitialScreen) {
            startActivity(new Intent(PassCodeActivity.this, HomeActivity.class));
        }
        finish();
    }

    /**
     * Starting {@link LoginActivity} by clearing all other activities earlier opened.
     */
    private void startLoginActivity() {
        Intent i = new Intent(PassCodeActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    /**
     * Enables Backpress only when {@code isInitialScreen} is true
     */
    @Override
    public void onBackPressed() {
        if (isInitialScreen) {
            super.onBackPressed();
        }
    }
}
