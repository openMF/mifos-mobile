package org.mifos.selfserviceapp.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.presenters.UserDetailsPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.views.UserDetailsView;
import org.mifos.selfserviceapp.utils.CircularImageView;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetails extends BaseActivity implements UserDetailsView {

    @BindView(R.id.UserDetailsContainer)
    LinearLayout userDetailsContainer;

    @BindView(R.id.TV_User_name)
    TextView username;

    @BindView(R.id.tv_fullName)
    TextView fullname;

    @BindView(R.id.tv_Email)
    TextView email;

    @BindView(R.id.tv_Active_since)
    TextView activeSince;

    @BindView(R.id.tv_accountNo)
    TextView accountNo;

    @BindView(R.id.dateOfBirth)
    TextView dateOfBirth;

    @BindView(R.id.IV_UserImageView)
    CircularImageView userImage;

    @BindView(R.id.toolbar)
    Toolbar toolbar_user_details;

    @Inject
    UserDetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar_user_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter.getUserDetails();
        try {
            presenter.getImage();
        } catch (IOException e) {
            Log.d("Error setting image", e.getMessage());
        }
    }

    //Setup on back button pressed event.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void gotoLoginPage() {
        Intent loginPageIntent = new Intent(UserDetails.this, LoginActivity.class);
        startActivity(loginPageIntent);
    }

    @Override
    public void setupProfilePage(Client currentClient) {
        username.setText(currentClient.getDisplayName());
        fullname.setText(currentClient.getFullname());
        activeSince.setText(presenter.getDateFromList(currentClient.getActivationDate()));
        accountNo.setText(currentClient.getAccountNo());
        dateOfBirth.setText(presenter.getDateFromList(currentClient.getDobDate()));
    }

    @Override
    public void setUserImage(Bitmap image) {
        userImage.setImageBitmap(image);
    }


    /**
     * Should be called when a time taking process starts and we want the user
     * to wait for the process to finish. The UI should gracefully display some
     * sort of progress bar or animation so that the user knows that the app is
     * doing some work and has not stalled.
     * <p>
     * For example: a network request to the API is made for authenticating
     * the user.
     */
    @Override
    public void showProgress() {
        showProgressDialog(getString(R.string.fetching_client));
    }

    /**
     * Should be called when a time taking process ends and we have some result
     * for the user.
     */
    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showMessage(String message) {
        showToast(message);
    }

}
