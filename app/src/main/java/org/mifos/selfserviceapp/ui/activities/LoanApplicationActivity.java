package org.mifos.selfserviceapp.ui.activities;

import android.os.Bundle;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.enums.LoanState;
import org.mifos.selfserviceapp.ui.fragments.LoanApplicationFragment;

public class LoanApplicationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);

        replaceFragment(LoanApplicationFragment.newInstance(LoanState.CREATE), false,
                R.id.container);
        showBackButton();
    }
}
