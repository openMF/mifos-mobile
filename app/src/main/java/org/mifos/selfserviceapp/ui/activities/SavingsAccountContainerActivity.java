package org.mifos.selfserviceapp.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.SavingAccountsDetailFragment;
import org.mifos.selfserviceapp.ui.fragments.SavingAccountsTransactionFragment;
import org.mifos.selfserviceapp.utils.Constants;

/**
 * Created by Rajan Maurya on 05/03/17.
 */

public class SavingsAccountContainerActivity extends BaseActivity {

    private long savingsId;
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        savingsId = getIntent().getExtras().getLong(Constants.SAVINGS_ID);

        addOnBackStackChangedListener();
        replaceFragment(SavingAccountsDetailFragment.newInstance(savingsId), false, R.id.container);
        showBackButton();
    }

    private void addOnBackStackChangedListener() {
        if (getSupportFragmentManager() == null) {
            return;
        }
        getSupportFragmentManager()
                .addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
                        if (fragment instanceof SavingAccountsTransactionFragment) {
                            setMenuTransactionButton(false);
                        } else {
                            setMenuTransactionButton(true);
                        }
                    }
                });
    }

    private void setMenuTransactionButton(boolean isEnabled) {
        menu.getItem(0).setEnabled(isEnabled);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_saving_account, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_transactions:
                replaceFragment(SavingAccountsTransactionFragment.newInstance(savingsId),
                        true, R.id.container);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
