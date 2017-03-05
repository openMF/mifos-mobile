package org.mifos.selfserviceapp.ui.activities;

import android.os.Bundle;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.SavingAccountsDetailFragment;
import org.mifos.selfserviceapp.utils.Constants;

/**
 * Created by Rajan Maurya on 05/03/17.
 */

public class SavingsAccountContainerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        long savingsId = getIntent().getExtras().getLong(Constants.SAVINGS_ID);

        replaceFragment(SavingAccountsDetailFragment.newInstance(savingsId), false, R.id.container);
        showBackButton();
    }
}
