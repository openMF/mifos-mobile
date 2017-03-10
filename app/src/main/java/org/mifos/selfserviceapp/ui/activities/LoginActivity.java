package org.mifos.selfserviceapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.local.PreferencesHelper;
import org.mifos.selfserviceapp.presenters.LoginPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.views.LoginView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.Network;
import org.mifos.selfserviceapp.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vishwajeet
 * @since 05/06/16
 */

public class LoginActivity extends BaseActivity implements LoginView {

    @Inject
    LoginPresenter loginPresenter;

    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;

    @BindView(R.id.et_username)
    EditText etUsername;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.ll_login)
    LinearLayout llLogin;

    private boolean loginStatus;

    int passcodeStatus;

    PreferencesHelper pHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter.attachView(this);
        pHelper = new PreferencesHelper(LoginActivity.this);
        boolean loginrequest = getIntent().getBooleanExtra(Constants.REQUESTLOGIN_KEY, false);
        if (loginrequest) {
            String username = pHelper.getString(Constants.USERNAME, " ");
            String password = pHelper.getString(Constants.PASSWORD, " ");
            loginRequest(username, password);
        }

        String passcodeStatusKey = Constants.SP_KEY_PASSCODESTATUS;
        int keyPasscodeNotSet = Constants.PASSCODE_NOT_SET;
        passcodeStatus = pHelper.getInt(passcodeStatusKey, keyPasscodeNotSet);

        if (passcodeStatus == Constants.PASSCODE_SET && !loginrequest) {
            startActivity(new Intent(LoginActivity.this, Passcode.class));
            finish();
        }
    }

    public void loginRequest(String username, String password) {
        loginPresenter.login(username, password);
    }

    @Override
    public void onLoginSuccess(String userName) {
        loginStatus = true;
        showToast(getString(R.string.toast_welcome, userName));
        llLogin.setVisibility(View.GONE);
        loginPresenter.loadClient();
    }

    @Override
    public void showProgress() {
        if (!loginStatus) {
            showProgressDialog(getString(R.string.progress_message_login));
        } else {
            showProgressDialog(getString(R.string.fetching_client));
        }
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showClient(long clientId) {
        Intent homeActivityIntent = new Intent(this, HomeActivity.class);
        homeActivityIntent.putExtra(Constants.CLIENT_ID, clientId);
        startActivity(homeActivityIntent);
        finish();
    }

    @Override
    public void showMessage(String errorMessage) {
        showToast(errorMessage, Toast.LENGTH_LONG);
        llLogin.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_login)
    public void onLoginClicked() {

        final String username = etUsername.getEditableText().toString();
        final String password = etPassword.getEditableText().toString();

        if (Network.isConnected(this)) {
//            loginPresenter.login(username, password);

            pHelper.putString(Constants.USERNAME, username);
            pHelper.putString(Constants.PASSWORD, password);

            Context c = getApplicationContext();
            Toast.makeText(c, "PLEASE WAIT ... ", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(LoginActivity.this, Passcode.class));

        } else {
            Toaster.show(llLogin, getString(R.string.no_internet_connection));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
    }
}
