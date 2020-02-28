package org.mifos.mobilebanking.ui.activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.models.client.Client;
import org.mifos.mobilebanking.presenters.UserDetailsPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.enums.AccountType;
import org.mifos.mobilebanking.ui.enums.ChargeType;
import org.mifos.mobilebanking.ui.fragments.AboutUsFragment;
import org.mifos.mobilebanking.ui.fragments.BeneficiaryListFragment;
import org.mifos.mobilebanking.ui.fragments.ClientAccountsFragment;
import org.mifos.mobilebanking.ui.fragments.ClientChargeFragment;
import org.mifos.mobilebanking.ui.fragments.HelpFragment;
import org.mifos.mobilebanking.ui.fragments.HomeOldFragment;
import org.mifos.mobilebanking.ui.fragments.NotificationFragment;
import org.mifos.mobilebanking.ui.fragments.RecentTransactionsFragment;
import org.mifos.mobilebanking.ui.fragments.ThirdPartyTransferFragment;
import org.mifos.mobilebanking.ui.views.UserDetailsView;
import org.mifos.mobilebanking.utils.CircularImageView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.MaterialDialog;
import org.mifos.mobilebanking.utils.TextDrawable;
import org.mifos.mobilebanking.utils.Toaster;
import org.mifos.mobilebanking.utils.gcm.RegistrationIntentService;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 14/07/2016
 */
public class HomeActivity extends BaseActivity implements UserDetailsView, NavigationView.
        OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    UserDetailsPresenter detailsPresenter;

    private TextView tvUsername;
    private CircularImageView ivCircularUserProfilePicture;
    private ImageView ivTextDrawableUserProfilePicture;
    private long clientId;
    private Bitmap userProfileBitmap;
    private Client client;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean isReceiverRegistered;
    private int menuItem;
    boolean  doubleBackToExitPressedOnce = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        clientId = preferencesHelper.getClientId();

        setupNavigationBar();
        setToolbarElevation();
        setToolbarTitle(getString(R.string.home));
        replaceFragment(HomeOldFragment.newInstance(), false, R.id.container);

        if (getIntent() != null && getIntent().getBooleanExtra(getString(R.string.notification),
                false)) {
            replaceFragment(NotificationFragment.newInstance(), true, R.id.container);
        }

        if (savedInstanceState == null) {
            detailsPresenter.attachView(this);
            detailsPresenter.getUserDetails();
            detailsPresenter.getUserImage();
            showUserImage(null);
        } else {
            client = savedInstanceState.getParcelable(Constants.USER_DETAILS);
            detailsPresenter.setUserProfile(preferencesHelper.getUserProfileImage());
            showUserDetails(client);
        }

        if (checkPlayServices() && !preferencesHelper.sentTokenToServerState()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.USER_DETAILS, client);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(registerReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(registerReceiver,
                    new IntentFilter(Constants.REGISTER_ON_SERVER));
            isReceiverRegistered = true;
        }
    }

    /**
     * Called whenever any item is selected in {@link NavigationView}
     *
     * @param item {@link MenuItem} which is selected by the user
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // select which item to open
        clearFragmentBackStack();
        setToolbarElevation();
        menuItem = item.getItemId();
        switch (item.getItemId()) {
            case R.id.item_home:
                hideToolbarElevation();
                replaceFragment(HomeOldFragment.newInstance(), true, R.id.container);
                break;
            case R.id.item_accounts:
                hideToolbarElevation();
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
                replaceFragment(ThirdPartyTransferFragment.newInstance(), true, R.id.container);
                break;
            case R.id.item_beneficiaries:
                replaceFragment(BeneficiaryListFragment.newInstance(), true, R.id.container);
                break;
            case R.id.item_settings:
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                break;
            case R.id.item_about_us:
                replaceFragment(AboutUsFragment.newInstance(), true, R.id.container);
                break;
            case R.id.item_help:
                replaceFragment(HelpFragment.getInstance(), true, R.id.container);
                break;
            case R.id.item_share:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getString(R.string.string_and_string,
                        getString(R.string.share_msg), getApplication().getPackageName()));
                startActivity(Intent.createChooser(i, getString(R.string.choose)));
                break;
            case R.id.item_logout:
                showLogoutDialog();
                break;
        }

        // close the drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        setNavigationViewSelectedItem(R.id.item_home);
        return true;
    }

    /**
     * Asks users to confirm whether he want to logout or not
     */
    private void showLogoutDialog() {
        new MaterialDialog.Builder().init(HomeActivity.this)
                .setMessage(R.string.dialog_logout)
                .setPositiveButton(getString(R.string.logout),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                preferencesHelper.clear();
                                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.
                                        FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setNavigationViewSelectedItem(R.id.item_home);
                            }
                        })
                .createMaterialDialog()
                .show();
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
                hideKeyboard(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setupHeaderView(navigationView.getHeaderView(0));
        setUpBackStackListener();
    }

    /**
     * Used for initializing values for HeaderView of NavigationView
     *
     * @param headerView Header view of NavigationView
     */
    private void setupHeaderView(View headerView) {
        tvUsername = ButterKnife.findById(headerView, R.id.tv_user_name);
        ivCircularUserProfilePicture = ButterKnife.findById(headerView,
                R.id.iv_circular_user_image);
        ivTextDrawableUserProfilePicture = ButterKnife.findById(headerView, R.id.iv_user_image);

        ivTextDrawableUserProfilePicture.setOnClickListener(this);
        ivCircularUserProfilePicture.setOnClickListener(this);
    }

    /**
     * Shows Client username in HeaderView of NavigationView
     *
     * @param client Contains details about the client
     */
    @Override
    public void showUserDetails(Client client) {
        this.client = client;
        preferencesHelper.setUserName(client.getDisplayName());
        tvUsername.setText(client.getDisplayName());
    }

    /**
     * Displays UserProfile Picture in HeaderView in NavigationView
     *
     * @param bitmap UserProfile Picture
     */
    @Override
    public void showUserImage(final Bitmap bitmap) {
        if (bitmap != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    userProfileBitmap = bitmap;
                    ivCircularUserProfilePicture.setImageBitmap(bitmap);
                    ivCircularUserProfilePicture.setVisibility(View.VISIBLE);
                    ivTextDrawableUserProfilePicture.setVisibility(View.GONE);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String userName;
                    if (!preferencesHelper.getUserName().isEmpty()) {
                        userName = preferencesHelper.getUserName();
                    } else {
                        userName = getString(R.string.app_name);
                    }
                    ivCircularUserProfilePicture.setVisibility(View.GONE);
                    ivTextDrawableUserProfilePicture.setVisibility(View.VISIBLE);
                    TextDrawable drawable = TextDrawable.builder()
                            .beginConfig()
                            .toUpperCase()
                            .endConfig()
                            .buildRound(userName.substring(0, 1),
                                    ContextCompat.getColor(
                                            HomeActivity.this, R.color.primary_dark));
                    ivTextDrawableUserProfilePicture.setImageDrawable(drawable);
                }
            });
        }
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
     *
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
        if (fragment instanceof HomeOldFragment) {
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
                        setToolbarElevation();
                        if (fragment instanceof HomeOldFragment) {
                            setNavigationViewSelectedItem(R.id.item_home);
                        } else if (fragment instanceof ClientAccountsFragment) {
                            hideToolbarElevation();
                            setNavigationViewSelectedItem(R.id.item_accounts);
                        } else if (fragment instanceof RecentTransactionsFragment) {
                            setNavigationViewSelectedItem(R.id.item_recent_transactions);
                        } else if (fragment instanceof ClientChargeFragment) {
                            setNavigationViewSelectedItem(R.id.item_charges);
                        } else if (fragment instanceof ThirdPartyTransferFragment) {
                            setNavigationViewSelectedItem(R.id.item_third_party_transfer);
                        } else if (fragment instanceof BeneficiaryListFragment) {
                            setNavigationViewSelectedItem(R.id.item_beneficiaries);
                        } else if (fragment instanceof HelpFragment) {
                            setNavigationViewSelectedItem(R.id.item_help);
                        } else if (fragment instanceof AboutUsFragment) {
                            setNavigationViewSelectedItem(R.id.item_about_us);
                        }
                    }
                });
    }

    public void setNavigationViewSelectedItem(int id) {
        navigationView.setCheckedItem(id);
    }

    @Override
    public void onClick(View v) {
        // Click Header to view full profile of User
        startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(HomeActivity.class.getName(), "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String token = intent.getStringExtra(Constants.TOKEN);
            detailsPresenter.registerNotification(token);
        }
    };

    public int getCheckedItem() {
        return menuItem;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager
                .RESULT_UNCHANGED_SHOWN);
    }

}
