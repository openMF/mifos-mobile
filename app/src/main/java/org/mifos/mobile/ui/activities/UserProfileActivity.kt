package org.mifos.mobile.ui.activities

import android.os.Bundle

import org.mifos.mobile.R
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.UserProfileFragment

class UserProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        replaceFragment(UserProfileFragment.newInstance(), false, R.id.container)
    }
}