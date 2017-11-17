package org.mifos.mobilebanking.ui.activities;

import android.os.Bundle;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.fragments.SavingAccountsDetailFragment;
import org.mifos.mobilebanking.utils.Constants;

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

}
