package org.mifos.selfserviceapp.ui.activities;

import android.app.ProgressDialog;
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
    LoginPresenter mLoginPresenter;

    private ProgressDialog progress;

    private String username;
    private String password;

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

        mLoginPresenter.attachView(this);
    }

    public boolean validateUserInputs() {
        username = etUsername.getEditableText().toString();
        if (username.length() < 5) {
            Toast.makeText(this, R.string.invalid_username, Toast.LENGTH_LONG);
            return false;
        }
        password = etPassword.getEditableText().toString();
        if (password.length() < 6) {
            Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }
    @Override
    public void onLoginSuccessful(String userName) {

        Toast.makeText(this, getString(R.string.toast_welcome) + " "
                +userName, Toast.LENGTH_LONG).show();

        //startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void onLoginError(Throwable throwable) {

    //TODO: Handle this error properly
    Toast.makeText(this, R.string.unable_to_connect, Toast.LENGTH_LONG).show();

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

    @OnClick(R.id.btn_login)
    public void loginClick(){
        if (!validateUserInputs())
            return;

        mLoginPresenter.login(username, password);
    }

    @Override
    protected void onDestroy() {
        mLoginPresenter.detachView();
        super.onDestroy();
    }
}
