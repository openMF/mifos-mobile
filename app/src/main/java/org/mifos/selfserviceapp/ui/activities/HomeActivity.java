package org.mifos.selfserviceapp.ui.activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.api.local.PreferencesHelper;
import org.mifos.selfserviceapp.presenters.HomePresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.enums.AccountType;
import org.mifos.selfserviceapp.ui.fragments.ClientAccountsFragment;
import org.mifos.selfserviceapp.ui.fragments.ClientChargeFragment;
import org.mifos.selfserviceapp.ui.fragments.HelpFragment;
import org.mifos.selfserviceapp.ui.fragments.HomeFragment;
import org.mifos.selfserviceapp.ui.fragments.RecentTransactionsFragment;
import org.mifos.selfserviceapp.ui.views.HomeView;
import org.mifos.selfserviceapp.utils.CircularImageView;
import org.mifos.selfserviceapp.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 14/07/2016
 */

public class HomeActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, HomeView {


    @Inject
    HomePresenter homePresenter;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    DataManager dataManager;
    HeaderView header;
    private long clientId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        getActivityComponent().inject(this);

        ButterKnife.bind(this);
        homePresenter.attachView(this);

        View navigationViewHeader = navigationView.getHeaderView(0);
        header = new HeaderView(navigationViewHeader);
        try {
            homePresenter.getImage(); //set user details.

        } catch (Exception e) {
            Log.d("Error setting image", e.getMessage());
        }

        setToolbarTitle(getString(R.string.home));

        clientId = getIntent().getExtras().getLong(Constants.CLIENT_ID);
        replaceFragment(HomeFragment.newInstance(clientId), true,  R.id.container);

        setupNavigationBar();

        header.profileTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDetails.class));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // select which item to open
        clearFragmentBackStack();
        switch (item.getItemId()) {
            case R.id.item_home:
                replaceFragment(HomeFragment.newInstance(clientId), true, R.id.container);
                break;
            case R.id.item_accounts:
                replaceFragment(ClientAccountsFragment.newInstance(clientId, AccountType.SAVINGS),
                        true, R.id.container);
                break;
            case R.id.item_recent_transactions:
                replaceFragment(RecentTransactionsFragment.newInstance(clientId),
                        true, R.id.container);
                break;
            case R.id.item_charges:
                replaceFragment(ClientChargeFragment.newInstance(clientId), true,  R.id.container);
                break;
            case R.id.item_about_us:
                break;
            case R.id.item_help:
                replaceFragment(HelpFragment.getInstance(), true, R.id.container);
                break;
        }

        // close the drawer
        drawerLayout.closeDrawer(GravityCompat.START);
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
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferencesHelper.clear();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showMessage(String errorMessage) {
        showToast(errorMessage, Toast.LENGTH_LONG);
    }

    @Override
    public void setUserName(String userName) {
        header.profileTextView.setText(userName);
    }

    @Override
    public void setUserImage(Bitmap userImage) {
        header.profileImageView.setImageBitmap(userImage); //set User image
        homePresenter.getUserName(); //set user name
    }

    @Override
    public void showProgress() {
        showProgressDialog("Loading....");
    }

    static class HeaderView {

        @BindView(R.id.ProfileTab)
        RelativeLayout profileTab;


        @BindView(R.id.tv_user_name)
        TextView profileTextView;

        @BindView(R.id.iv_user_image)
        CircularImageView profileImageView;

        public HeaderView(View view) {
            ButterKnife.bind(this, view);

        }
    }

}
