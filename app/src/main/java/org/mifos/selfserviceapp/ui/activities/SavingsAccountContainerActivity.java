package org.mifos.selfserviceapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.SavingAccountsDetailFragment;
import org.mifos.selfserviceapp.utils.Constants;

/**
 * Created by Rajan Maurya on 05/03/17.
 */

public class SavingsAccountContainerActivity extends BaseActivity {

    private long savingsId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        savingsId = getIntent().getExtras().getLong(Constants.SAVINGS_ID);

        replaceFragment(SavingAccountsDetailFragment.newInstance(savingsId), false, R.id.container);
        showBackButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_saving_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.item_transactions:
                intent = new Intent(this, SavingsAccountTransactionContainerActivity.class);
                intent.putExtra(Constants.SAVINGS_ID, savingsId);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
