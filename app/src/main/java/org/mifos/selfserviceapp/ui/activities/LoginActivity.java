package org.mifos.selfserviceapp.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
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

    static LoginActivity loginActivity ;

    @BindView(R.id.et_username)
    EditText etUsername;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.ll_login)
    LinearLayout llLogin;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private boolean loginStatus;

    int passcodeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter.attachView(this);

        sp = getApplicationContext().getSharedPreferences(Constants.SHAREDPREF_NAME, MODE_PRIVATE);
        editor = sp.edit();

        loginActivity = this;

        boolean loginrequest =  getIntent().getBooleanExtra("requestlogin" , false);
        if(loginrequest){
            String username = sp.getString(Constants.USERNAME , " ");
            String password = sp.getString(Constants.PASSWORD , " ");
            loginRequest(username , password);
        }


        passcodeStatus = sp.getInt(Constants.SP_KEY_PASSCODESTATUS, Constants.PASSCODE_NOT_SET);
        Log.i("abcde", passcodeStatus + "");

        if (passcodeStatus == Constants.PASSCODE_SET && loginrequest==false) {
            startActivity(new Intent(LoginActivity.this, Passcode.class));
            finish();
        }
    }


    public static LoginActivity getInstance(){
        return loginActivity;
    }

    public  void  loginRequest(String username , String Password){
        loginPresenter.login(username , Password);
    }


    @Override
    public  void onLoginSuccess(String userName) {
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

        editor.putString(Constants.USERNAME , username );
        editor.putString(Constants.PASSWORD , password);
        editor.commit();
        Toast.makeText(getApplicationContext() , sp.getString(Constants.USERNAME , "not set") , Toast.LENGTH_LONG).show();

        startActivity(new Intent(LoginActivity.this , Passcode.class));

//     loginPresenter.login(username, password);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
    }
}
