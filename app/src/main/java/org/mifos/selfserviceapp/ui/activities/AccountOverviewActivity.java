package org.mifos.selfserviceapp.ui.activities;

import android.os.Bundle;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.AccountOverviewFragment;

/**
 * @author Rajan Maurya
 *         On 16/10/17.
 */
public class AccountOverviewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        replaceFragment(AccountOverviewFragment.newInstance(), false, R.id.container);
        showBackButton();
        hideToolbarElevation();
    }
}
