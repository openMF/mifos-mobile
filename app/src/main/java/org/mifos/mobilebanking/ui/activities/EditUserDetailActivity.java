package org.mifos.mobilebanking.ui.activities;

/*
 * Created by saksham on 14/July/2018
 */

import android.os.Bundle;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.fragments.UpdatePasswordFragment;

import butterknife.ButterKnife;

public class EditUserDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_detail);

        ButterKnife.bind(this);
        setToolbarTitle(getString(R.string.string_and_string, getString(R.string.edit),
                getString(R.string.user_details)));
        showBackButton();

        replaceFragment(UpdatePasswordFragment.newInstance(), false, R.id.container);
    }
}
