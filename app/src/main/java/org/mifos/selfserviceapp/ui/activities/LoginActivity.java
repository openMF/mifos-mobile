package org.mifos.selfserviceapp.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.widget.EditText;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.presenters.LoginPresenter;
import org.mifos.selfserviceapp.ui.views.LoginView;

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

    private ProgressDialog progress;

    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;

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

        final String toastMessage =
                getString(R.string.toast_welcome, userName);
        showToast(toastMessage);

        startActivity(new Intent(this, ClientListActivity.class));
    }

    @Override
    public void onLoginError(String errorMessage) {
        showToast(errorMessage);
    }

    @Override
    public void showProgress() {
        if (progress == null) {
            progress = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
        }
        progress.setMessage(getResources().getText(R.string.progress_message_login));
        progress.show();
    }

    @Override
    public void hideProgress() {
        if (progress != null && progress.isShowing())
            progress.dismiss();
    }

    @Override
    public void showInputValidationError(String errorMessage) {
        showToast(errorMessage, Toast.LENGTH_LONG);
    }

    @OnClick(R.id.btn_login)
    public void onLoginClicked() {

        final String username = etUsername.getEditableText().toString();
        final String password = etPassword.getEditableText().toString();

        loginPresenter.login(username, password);
    }

    @Override
    protected void onDestroy() {
        loginPresenter.detachView();
        super.onDestroy();
    }


}
