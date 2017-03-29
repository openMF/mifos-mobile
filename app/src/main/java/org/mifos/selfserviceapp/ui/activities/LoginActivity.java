package org.mifos.selfserviceapp.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.presenters.LoginPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.views.LoginView;
import org.mifos.selfserviceapp.utils.Network;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.Toaster;

import java.util.HashSet;
import java.util.Set;

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
    AutoCompleteTextView etUsername;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.ll_login)
    LinearLayout llLogin;

    private boolean loginStatus;
    public static final String PREFS_NAME = "PingBusPrefs";
    public static final String PREFS_SEARCH_HISTORY = "SearchHistory";
    private SharedPreferences settings;
    private Set<String> usernames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter.attachView(this);
        settings = getSharedPreferences(PREFS_NAME, 0);
        usernames = settings.getStringSet(PREFS_SEARCH_HISTORY, new HashSet<String>());
        setAutoCompleteSource(etUsername.toString());
    }

    @Override
    public void onLoginSuccess(String userName) {
        loginStatus = true;
        showToast(getString(R.string.toast_welcome, userName));
        llLogin.setVisibility(View.GONE);
        loginPresenter.loadClient();
        if (!usernames.contains(userName)) {
            usernames.add(userName);
            setAutoCompleteSource(userName);

        }
    }
    private void saveprfs(String userName) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(PREFS_SEARCH_HISTORY , usernames);
        editor.commit();
    }
    private void setAutoCompleteSource(String userName) {
        int size = usernames.size();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this ,
                android.R.layout.simple_list_item_1 , usernames.toArray(new String[size]));
        etUsername.setAdapter(adapter);
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
        if (Network.isConnected(this)) {
            loginPresenter.login(username, password);
        } else {
            Toaster.show(llLogin, getString(R.string.no_internet_connection));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveprfs(etUsername.toString());
    }
}
