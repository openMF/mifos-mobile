package org.mifos.selfserviceapp.login;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.widget.EditText;
import android.widget.Toast;

import org.mifos.selfserviceapp.data.User;
import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.BaseApiManager;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.utils.PrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 05/06/16
 */

public class LoginActivity extends AppCompatActivity implements LoginView{

    String TAG= "LoginActivity";
    private LoginPresenter mLoginPresenter;
    private DataManager mDataManager;
    private BaseApiManager mBaseApiManager;
    private ProgressDialog progress;
    private PrefManager prefManager;

    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.et_email)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mBaseApiManager = new BaseApiManager();
        mDataManager = new DataManager(mBaseApiManager);
        mLoginPresenter = new LoginPresenter(mDataManager);
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
    public void onLoginSuccessful(Response<User> response) {

        Toast.makeText(this, getString(R.string.toast_welcome) + " "
                + response.body().getUsername(), Toast.LENGTH_LONG);
        prefManager = PrefManager.getInstance(this);
        mLoginPresenter.setUserInfo(prefManager, response);
        finish();
    }

    @Override
    public void onLoginError(Throwable throwable) {

    //TODO: Handle this error properly
    Toast.makeText(this, R.string.unable_to_connect, Toast.LENGTH_LONG);

    }

    @Override
    public void showProgress() {
        if (progress == null) {
            progress = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
        }
        progress.setMessage("Logging In");
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
        super.onDestroy();
        mLoginPresenter.detachView();
    }
}
