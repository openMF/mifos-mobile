package org.mifos.selfserviceapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.presenters.LoginPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.views.LoginView;
import org.mifos.selfserviceapp.utils.Constants;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginPresenter.attachView(this);
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
        Intent accountsActivityIntent = new Intent(this, HomeActivity.class);
        accountsActivityIntent.putExtra(Constants.CLIENT_ID, clientId);
        accountsActivityIntent.putExtra(Constants.CLIENT_ID, clientId);
        startActivity(accountsActivityIntent);
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

        loginPresenter.login(username, password);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
    }
}
