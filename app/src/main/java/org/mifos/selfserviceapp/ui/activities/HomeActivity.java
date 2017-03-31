package org.mifos.selfserviceapp.ui.activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import org.mifos.selfserviceapp.ui.enums.ChargeType;
import org.mifos.selfserviceapp.ui.fragments.AboutUsFragment;
import org.mifos.selfserviceapp.ui.fragments.BeneficiaryListFragment;
import org.mifos.selfserviceapp.ui.fragments.ClientAccountsFragment;
import org.mifos.selfserviceapp.ui.fragments.ClientChargeFragment;
import org.mifos.selfserviceapp.ui.fragments.HelpUCFragment;
import org.mifos.selfserviceapp.ui.fragments.HomeFragment;
import org.mifos.selfserviceapp.ui.fragments.RecentTransactionsFragment;
import org.mifos.selfserviceapp.ui.fragments.ThirdPartyTransferFragment;
import org.mifos.selfserviceapp.ui.views.UserDetailsView;
import org.mifos.selfserviceapp.utils.CircularImageView;
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
    private final int CAMERA_PERMISSION = 10;
    private long clientId;

    boolean  doubleBackToExitPressedOnce = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);

        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        setToolbarTitle(getString(R.string.home));

        clientId = preferencesHelper.getClientId();
        replaceFragment(HomeFragment.newInstance(), false ,  R.id.container);

        setupNavigationBar();
    }

    /**
     * Called whenever any item is selected in {@link NavigationView}
     * @param item {@link MenuItem} which is selected by the user
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // select which item to open
        clearFragmentBackStack();
        switch (item.getItemId()) {
            case R.id.item_home:
                replaceFragment(HomeFragment.newInstance(), true, R.id.container);
                break;
            case R.id.item_accounts:
                replaceFragment(ClientAccountsFragment.newInstance(AccountType.SAVINGS),
                        true, R.id.container);
                break;
            case R.id.item_recent_transactions:
                replaceFragment(RecentTransactionsFragment.newInstance(), true, R.id.container);
                break;
            case R.id.item_charges:
                replaceFragment(ClientChargeFragment.newInstance(clientId, ChargeType.CLIENT), true,
                        R.id.container);
                break;
            case R.id.item_third_party_transfer:
                replaceFragment(ThirdPartyTransferFragment.newInstance(), true,  R.id.container);
                break;
            case R.id.item_beneficiaries:
                replaceFragment(BeneficiaryListFragment.newInstance(), true,  R.id.container);
                break;
            case R.id.item_about_us:
                replaceFragment(AboutUsFragment.getInstance(), true, R.id.container);
                break;
            case R.id.item_help:
                replaceFragment(HelpUCFragment.getInstance(), true, R.id.container);
                break;
        }

        // close the drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        setNavigationViewSelectedItem(R.id.item_home);
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
        setUpBackStackListener();
    }

    /**
     * Used for initializing values for HeaderView of NavigationView
     * @param headerView Header view of NavigationView
     */
    private void setupHeaderView(View headerView) {
        tvUsername = (TextView) headerView.findViewById(R.id.tv_user_name);
        ivUserProfilePicture = (CircularImageView) headerView.findViewById(R.id.iv_user_image);
        ivUserProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
            }
        });

        detailsPresenter.attachView(this);
        detailsPresenter.getUserDetails();
        detailsPresenter.getUserImage();

    }

    /**
     * Shows Client username in HeaderView of NavigationView
     * @param client Contains details about the client
     */
    @Override
    public void showUserDetails(Client client) {
        tvUsername.setText(client.getDisplayName());
    }

    /**
     * Displays UserProfile Picture in HeaderView in NavigationView
     * @param bitmap UserProfile Picture
     */
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

    /**
     * It is called whenever any error occurs while executing a request
     * @param message contains information about error occurred
     */
    @Override
    public void showError(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailsPresenter.detachView();
    }

    /**
     * Handling back press
     */
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof HomeFragment) {
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
        }

        if (stackCount() != 0) {
            super.onBackPressed();
        }
    }

    private void setUpBackStackListener() {
        getSupportFragmentManager().
                addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                        @Override
                        public void onBackStackChanged() {
                        Fragment fragment = getSupportFragmentManager().
                                findFragmentById(R.id.container);
                        if (fragment instanceof HomeFragment) {
                            setNavigationViewSelectedItem(R.id.item_home);
                        } else if (fragment instanceof ClientAccountsFragment) {
                            setNavigationViewSelectedItem(R.id.item_accounts);
                        } else if (fragment instanceof RecentTransactionsFragment) {
                            setNavigationViewSelectedItem(R.id.item_recent_transactions);
                        } else if (fragment instanceof ClientChargeFragment) {
                            setNavigationViewSelectedItem(R.id.item_charges);
                        } else if (fragment instanceof ThirdPartyTransferFragment) {
                            setNavigationViewSelectedItem(R.id.item_third_party_transfer);
                        } else if (fragment instanceof BeneficiaryListFragment) {
                            setNavigationViewSelectedItem(R.id.item_beneficiaries);
                        } else if (fragment instanceof HelpUCFragment) {
                            setNavigationViewSelectedItem(R.id.item_help);
                        } else if (fragment instanceof AboutUsFragment) {
                            setNavigationViewSelectedItem(R.id.item_help);
                        }
                    }
                });
    }

    public void setNavigationViewSelectedItem(int id) {
        navigationView.setCheckedItem(id);
    }
}
