package org.mifos.selfserviceapp.ui.activities;
/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/

import android.os.Bundle;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.LoanAccountsDetailFragment;
import org.mifos.selfserviceapp.utils.Constants;

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
