package org.mifos.selfserviceapp.ui.activities;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.fragments.ClientAccountsFragment;
import org.mifos.selfserviceapp.ui.fragments.ClientChargeFragment;
import org.mifos.selfserviceapp.ui.fragments.RecentTransactionsFragment;
import org.mifos.selfserviceapp.utils.Constants;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 14/07/2016
 */

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int clientId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);

        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getString(R.string.accounts));
        }

        clientId = getIntent().getExtras().getInt(Constants.CLIENT_ID);
        ClientAccountsFragment.newInstance(clientId);
        replaceFragment(ClientAccountsFragment.newInstance(clientId), R.id.container);

        setupNavigationBar();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // ignore the current selected item
        if (item.isChecked()) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        // select which item to open
        switch (item.getItemId()) {
            case R.id.item_accounts:
                replaceFragment(ClientAccountsFragment.newInstance(clientId), R.id.container);
                break;
            case R.id.item_funds_transfer:
                break;
            case R.id.item_recent_transactions:
                RecentTransactionsFragment.newInstance(clientId);
                replaceFragment(RecentTransactionsFragment.newInstance(clientId), R.id.container);
                break;
            case R.id.item_charges:
                ClientChargeFragment.newInstance(clientId);
                replaceFragment(ClientChargeFragment.newInstance(clientId), R.id.container);
                break;
            case R.id.item_questionnaire:
                break;
            case R.id.item_about_us:
                break;
        }

        // close the drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mNavigationView.setCheckedItem(R.id.item_accounts);
        setTitle(item.getTitle());
        return true;
    }

    /**
     * This method is used to set up the navigation drawer for
     * self-service application
     */
    private void setupNavigationBar() {

        mNavigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    /**
     * Replace the Activity with desired fragment depending upon what user is
     * selecting in navigation drawer.
     *
     * @param fragment    Name of the fragment which is to be replaced
     * @param containerId id of container where fragment has to be hold
     */
    private void replaceFragment(Fragment fragment, int containerId) {
        invalidateOptionsMenu();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();

    }


}
