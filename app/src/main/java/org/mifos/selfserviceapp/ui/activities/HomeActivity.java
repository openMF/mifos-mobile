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
import org.mifos.selfserviceapp.ui.fragments.ClientListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 14/07/2016
 */

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);

        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getString(R.string.home));
        }

        replaceFragment(new ClientListFragment(), false, R.id.container);

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
            case R.id.item_client_list:
              replaceFragment(new ClientListFragment(), false, R.id.container);
                break;
            case R.id.item_accounts:
                break;
            case R.id.item_funds_transfer:
                break;
            case R.id.item_recent_transactions:
                break;
            case R.id.item_questionnaire:
                break;
            case R.id.item_about_us:
                break;
        }

        // close the drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mNavigationView.setCheckedItem(R.id.item_client_list);
        return true;
    }

    /**
     *This method is used to set up the navigation drawer for
     * self-service application
     */
    private void setupNavigationBar() {

        // setup navigation view
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        // setup drawer layout and sync to toolbar
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
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
     *Replace the Activity with desired fragment depending upon what user is
     * selecting in navigation drawer.
     *
     * @param fragment Name of the fragment which is to be replaced
     * @param addToBackStack boolean value to decide weather to store fragment in backstack
     * @param containerId id of container where fragment has to be hold
     */
    private void replaceFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        invalidateOptionsMenu();
        String backStateName = fragment.getClass().getName();
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(backStateName,
                0);

        if (!fragmentPopped && getSupportFragmentManager().findFragmentByTag(backStateName) ==
                null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(containerId, fragment, backStateName);
            if (addToBackStack) {
                transaction.addToBackStack(backStateName);
            }
            transaction.commit();
        }
    }
}
