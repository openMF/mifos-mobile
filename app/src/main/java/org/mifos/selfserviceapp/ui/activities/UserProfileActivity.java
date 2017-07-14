package org.mifos.selfserviceapp.ui.activities;

import android.os.Bundle;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.UserProfileFragment;

public class UserProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        replaceFragment(UserProfileFragment.newInstance(), false , R.id.container);
    }
}
