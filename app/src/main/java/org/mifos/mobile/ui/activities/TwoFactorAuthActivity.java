package org.mifos.mobile.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import org.mifos.mobile.R;
import org.mifos.mobile.ui.activities.base.BaseActivity;
import org.mifos.mobile.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Prashant Khandelwal
 * On 11/04/19.
 */
public class TwoFactorAuthActivity extends BaseActivity {

    @BindView(R.id.et_code)
    EditText etCode;

    private GoogleAuthenticator gAuth;
    private String sharedKey;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_factor_auth);
        showBackButton();

        ButterKnife.bind(this);
        setToolbarTitle(getString(R.string.title_verify_code));

        sharedPreferences = getSharedPreferences(Constants.TWO_FACTOR_AUTHENTICATION,
                Context.MODE_PRIVATE);
        sharedKey = sharedPreferences.getString(Constants.TWO_FACTOR_AUTHENTICATION_KEY, "");
        System.setProperty("com.warrenstrange.googleauth.rng.algorithmProvider", "AndroidOpenSSL");
        gAuth = new GoogleAuthenticator();

    }

    @OnClick(R.id.btn_submit)
    void verify() {
        String stCode = etCode.getText().toString();
        if (!stCode.isEmpty()) {
            try {
                Integer code = Integer.parseInt(stCode);
                boolean isCodeValid = gAuth.authorize(sharedKey, code);
                if (isCodeValid) {
                    startPassCodeActivity();
                } else {
                    Toast.makeText(this, getString(R.string.incorrect_code),
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.invalid_code),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.enter_verification_code),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Starts {@link PassCodeActivity} with {@code Constans.INTIAL_LOGIN} as true
     */
    private void startPassCodeActivity() {
        Intent intent = new Intent(this, PassCodeActivity.class);
        intent.putExtra(Constants.INTIAL_LOGIN, true);
        startActivity(intent);
        finish();
    }
}
