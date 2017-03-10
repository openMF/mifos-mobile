package org.mifos.selfserviceapp.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.utils.CircularImageView;
import org.mifos.selfserviceapp.utils.Constants;

import java.util.List;

public class UserDetails extends AppCompatActivity {

    LinearLayout userDetailsContainer;
    TextView username;
    TextView fullname;
    TextView email;
    TextView activeSince;
    TextView accountNo;
    TextView dateOfBirth;
    CircularImageView userImage;

    Client currentClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentClient = (Client) getIntent().getExtras().get(Constants.CURRENT_CLIENT);
        if (currentClient == null) {
            gotoLoginPage();
        } else {
            setupProfilePage((Bitmap) getIntent().getExtras().get(Constants.CLIENT_IMAGE));
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

    void gotoLoginPage() {
        Intent loginPageIntent = new Intent(UserDetails.this, LoginActivity.class);
        startActivity(loginPageIntent);
    }

    void setupProfilePage(Bitmap image) {
        username.setText(currentClient.getDisplayName());
        fullname.setText(currentClient.getFullname());
        if (image != null) {
            userImage.setImageBitmap(image);
        } else {
            userImage.setImageResource(R.drawable.ic_material_user_icon_black_24dp);
            userImage.setBackgroundColor(getResources().getColor(R.color.white));
        }

        activeSince.setText(getDateFromList(currentClient.getActivationDate()));
        accountNo.setText(currentClient.getAccountNo());
        dateOfBirth.setText(getDateFromList(currentClient.getDobDate()));

    }

    void initializeViews() {
        userDetailsContainer = (LinearLayout) findViewById(R.id.UserDetailsContainer);
        username = (TextView) userDetailsContainer.findViewById(R.id.TV_User_name);
        fullname = (TextView) userDetailsContainer.findViewById(R.id.tv_fullName);
        email = (TextView) userDetailsContainer.findViewById(R.id.tv_Email);
        activeSince = (TextView) userDetailsContainer.findViewById(R.id.tv_Active_since);
        accountNo = (TextView) userDetailsContainer.findViewById(R.id.tv_accountNo);
        userImage = (CircularImageView) userDetailsContainer.findViewById(R.id.IV_UserImageView);
        dateOfBirth = (TextView) userDetailsContainer.findViewById(R.id.dateOfBirth);

    }

    String getDateFromList(List<Integer> date) {
        try {
            String dateString = date.get(2) +
                    "/" + date.get(1) +
                    "/" + date.get(0);
            return dateString;
        } catch (Exception e) {
            return "N/A";
        }
    }
}
