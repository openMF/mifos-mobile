package org.mifos.selfserviceapp.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.BaseApiManager;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.data.Client;
import org.mifos.selfserviceapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 16/06/16
 */
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private DataManager mDataManager;
    private Intent clientActivityIntent;
    private List<Client> clientList = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BaseApiManager baseApiManager = new BaseApiManager(this);
        mDataManager = new DataManager(baseApiManager);
        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getString(R.string.home));
        }

        loadClientDetails();
        //replaceFragment(new ClientListFragment(), false, R.id.container);
        // setup navigation drawer
        setupNavigationBar();
    }

    //TODO For testing purpose only, convert it to MVP
    private void loadClientDetails() {
        Call<Client> call = mDataManager.getClients();
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Response<Client> response) {
                if(response.body().getPageItems().size()==1) {
                    /*clientList = response.body().getPageItems();*/
                    clientActivityIntent = new Intent(getParent(), ClientAccountsActivity.class);
                    clientActivityIntent.putExtra(Constants.CLIENT_ID, response.body().getId());
                    startActivity(clientActivityIntent);
                }
                else {
                    replaceFragment(new ClientListFragment(), false, R.id.container);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
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
            case R.id.item_clients:
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
        mNavigationView.setCheckedItem(R.id.item_clients);
        return true;
    }

    protected void setupNavigationBar() {

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

    public void replaceFragment(Fragment fragment, boolean addToBackStack, int containerId) {
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