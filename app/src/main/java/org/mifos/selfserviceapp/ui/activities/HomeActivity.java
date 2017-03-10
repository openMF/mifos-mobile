package org.mifos.selfserviceapp.ui.activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.api.local.PreferencesHelper;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.ClientAccountsFragment;
import org.mifos.selfserviceapp.ui.fragments.ClientChargeFragment;
import org.mifos.selfserviceapp.ui.fragments.HelpFragment;
import org.mifos.selfserviceapp.ui.fragments.RecentTransactionsFragment;
import org.mifos.selfserviceapp.utils.Base64toBitmap;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.Toaster;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 14/07/2016
 */

public class HomeActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    View navigationViewHeader;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    DataManager dataManager;
    Bitmap userImage;
    private View.OnClickListener showProfileClickListener;
    private RelativeLayout profilePageTab;
    private ImageView profileImageView;
    private TextView profileTextView;
    private long clientId;
    private Client current_client;
    private boolean userImageRetrieved;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Get current Client
        current_client = (Client) getIntent().getExtras().get(Constants.CURRENT_CLIENT);
        clientId = current_client.getId();

        setContentView(R.layout.activity_home);
        setToolbarTitle(getString(R.string.accounts));
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        setupNavigationBar();
        setUserDetails();
        profilePageTab = (RelativeLayout) navigationViewHeader.findViewById(R.id.ProfileTab);
        profilePageTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userImageRetrieved) {
                    openUserDetails(userImage);
                } else {
                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openUserDetails(null);
                        }
                    };
                    Toaster.show(v, getString(R.string.user_details_alert),
                            Toaster.LONG,
                            listener);

                }
            }
        });
        replaceFragment(ClientAccountsFragment.newInstance(clientId), false, R.id.container);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // ignore the current selected item
        if (item.isChecked()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return false;
        }

        // select which item to open
        clearFragmentBackStack();
        switch (item.getItemId()) {
            case R.id.item_accounts:
                replaceFragment(ClientAccountsFragment.newInstance(clientId),
                        true, R.id.container);
                break;
            case R.id.item_recent_transactions:
                replaceFragment(RecentTransactionsFragment.newInstance(clientId),
                        true, R.id.container);
                break;
            case R.id.item_charges:
                replaceFragment(ClientChargeFragment.newInstance(clientId),
                        false,
                        R.id.container);
                replaceFragment(ClientChargeFragment.newInstance(clientId), true,  R.id.container);
                replaceFragment(ClientChargeFragment.newInstance(clientId),
                        false,
                        R.id.container);
                break;
            case R.id.item_about_us:
                break;
            case R.id.item_help:
                replaceFragment(HelpFragment.getInstance(), true, R.id.container);
                break;

        }

        // close the drawer
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        navigationView.setCheckedItem(R.id.item_accounts);
        setTitle(item.getTitle());
        return true;
    }

    /**
     * This method is used to set up the navigation drawer for
     * self-service application
     */
    private void setupNavigationBar() {

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
        }
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferencesHelper.clear();
    }

    void setUserDetails() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        navigationViewHeader = layoutInflater.inflate(R.layout.nav_drawer_header, navigationView);

        {   //Set User name.
            profileTextView = (TextView) navigationViewHeader.findViewById(R.id.tv_user_name);
            profileTextView.setText(current_client.getDisplayName());
            profileTextView.setVisibility(View.VISIBLE);
        }
        {   //Set User Image
            profileImageView = (ImageView) navigationViewHeader.findViewById(R.id.Iv_user_image);
            profileImageView.setImageResource(R.drawable.ic_material_user_icon_black_24dp);
            setUserImage();
        }
    }


    private void openUserDetails(Bitmap image) {
        Intent profilePage = new Intent(HomeActivity.this, UserDetails.class);
        profilePage.putExtra(Constants.CURRENT_CLIENT, current_client);
        profilePage.putExtra(Constants.CLIENT_IMAGE, image);
        startActivity(profilePage);
    }

    void setUserImage() {
        {
            final Call<ResponseBody> imageResponse = dataManager.getClientImage(clientId);
            imageResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Bitmap recievedUserImage;
                        String imageString = response.body().string();
                        Base64toBitmap base64toBitmap = new Base64toBitmap();
                        recievedUserImage = base64toBitmap.toBitmap(imageString);
                        if (recievedUserImage == null) {
                            Log.e("Error in base 64 image", base64toBitmap.getError().toString());
                        } else {
                            //Reduce Image size to 50 kb
                            double compressionRatio = 1.0 * (1024 * 50) /
                                    (recievedUserImage.getRowBytes() *
                                            recievedUserImage.getHeight());
                            compressionRatio = compressionRatio <= 0.05 ? 0.05
                                    : compressionRatio > 0.5 ? 0.5
                                    : compressionRatio;
                            recievedUserImage.compress(Bitmap.CompressFormat.JPEG,
                                    (int) compressionRatio * 100,
                                    new ByteArrayOutputStream());
                            recievedUserImage = Bitmap.createScaledBitmap(recievedUserImage,
                                    100, 100,
                                    false);
                            profileImageView.setImageBitmap(recievedUserImage);
                            userImage = recievedUserImage;
                            userImageRetrieved = true;
                        }

                    } catch (IOException e) {
                        userImage = null;
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    userImage = null;
                }
            });

        }
    }
}


