package org.mifos.selfserviceapp.ui.activities;

import android.os.Bundle;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.SavingAccountsTransactionFragment;
import org.mifos.selfserviceapp.utils.Constants;

/**
 * Created by AMIT on 22-Mar-17.
 */

public class SavingsAccountTransactionContainerActivity extends BaseActivity {

    private long savingsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        savingsId = getIntent().getExtras().getLong(Constants.SAVINGS_ID);
        replaceFragment(SavingAccountsTransactionFragment.newInstance(savingsId), false,
                R.id.container);
        showBackButton();
    }
}

