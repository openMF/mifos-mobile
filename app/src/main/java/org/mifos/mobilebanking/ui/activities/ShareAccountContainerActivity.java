package org.mifos.mobilebanking.ui.activities;

/*
 * Created by saksham on 04/July/2018
 */

import android.os.Bundle;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.accounts.share.ShareAccount;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.fragments.ShareAccountDetailFragment;
import org.mifos.mobilebanking.utils.Constants;

public class ShareAccountContainerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_account_container);

        replaceFragment(ShareAccountDetailFragment.newInstance(
                (ShareAccount) getIntent().getParcelableExtra(Constants.SHARE_ACCOUNTS)),
                false, R.id.container);
        showBackButton();
    }
}
