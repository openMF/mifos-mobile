package org.mifos.mobile.ui.activities;
/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/

import android.os.Bundle;

import org.mifos.mobile.R;
import org.mifos.mobile.ui.activities.base.BaseActivity;
import org.mifos.mobile.ui.fragments.LoanAccountsDetailFragment;
import org.mifos.mobile.utils.Constants;

public class LoanAccountContainerActivity extends BaseActivity {

    private long loanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        loanId = getIntent().getExtras().getLong(Constants.LOAN_ID);

        replaceFragment(LoanAccountsDetailFragment.newInstance(loanId), false, R.id.container);
        showBackButton();
    }
}
