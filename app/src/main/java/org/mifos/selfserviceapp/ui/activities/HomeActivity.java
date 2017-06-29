package org.mifos.selfserviceapp.ui.activities;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.local.PreferencesHelper;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.presenters.UserDetailsPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.enums.AccountType;
import org.mifos.selfserviceapp.ui.fragments.BeneficiaryListFragment;
import org.mifos.selfserviceapp.ui.fragments.HomeFragment;
import org.mifos.selfserviceapp.ui.fragments.ClientAccountsFragment;
import org.mifos.selfserviceapp.ui.fragments.ClientChargeFragment;
import org.mifos.selfserviceapp.ui.fragments.HelpFragment;
import org.mifos.selfserviceapp.ui.fragments.RecentTransactionsFragment;
import org.mifos.selfserviceapp.ui.fragments.ThirdPartyTransferFragment;
import org.mifos.selfserviceapp.ui.views.UserDetailsView;
import org.mifos.selfserviceapp.utils.CircularImageView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 14/07/2016
 */

public class HomeActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, UserDetailsView {

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    UserDetailsPresenter detailsPresenter;

    private TextView tvUsername;
    private CircularImageView ivUserProfilePicture;

    private long clientId;

    boolean  doubleBackToExitPressedOnce = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);

        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        setToolbarTitle(getString(R.string.home));

        clientId = getIntent().getExtras().getLong(Constants.CLIENT_ID);
        replaceFragment(HomeFragment.newInstance(clientId), false ,  R.id.container);

        setupNavigationBar();
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
            case R.id.item_third_party_transfer:
                replaceFragment(ThirdPartyTransferFragment.newInstance(), true,  R.id.container);
                break;
            case R.id.item_beneficiaries:
                replaceFragment(BeneficiaryListFragment.newInstance(), true,  R.id.container);
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

        setupHeaderView(navigationView.getHeaderView(0));
    }

    private void setupHeaderView(View headerView) {
        tvUsername = (TextView) headerView.findViewById(R.id.tv_user_name);
        ivUserProfilePicture = (CircularImageView) headerView.findViewById(R.id.iv_user_image);

        detailsPresenter.attachView(this);
        detailsPresenter.getUserDetails();
        detailsPresenter.getUserImage();

    }

    @Override
    public void showUserDetails(Client client) {
        tvUsername.setText(client.getDisplayName());
    }

    @Override
    public void showUserImage(final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivUserProfilePicture.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public void showProgress() {
        //empty, no need to show/hide progress in headerview
    }

    @Override
    public void hideProgress() {
        //empty
    }

    @Override
    public void showError(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferencesHelper.clear();
        detailsPresenter.detachView();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce && stackCount() == 0) {
            HomeActivity.this.finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toaster.show(findViewById(android.R.id.content), getString(R.string.exit_message));
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

        if (stackCount() != 0) {
            super.onBackPressed();
        }
    }
}
