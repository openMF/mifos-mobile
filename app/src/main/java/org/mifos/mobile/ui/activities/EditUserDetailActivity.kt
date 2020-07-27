package org.mifos.mobile.ui.activities

import android.os.Bundle

import butterknife.ButterKnife

import org.mifos.mobile.R
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.UpdatePasswordFragment

/*
* Created by saksham on 14/July/2018
*/
class EditUserDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_detail)
        ButterKnife.bind(this)
        setToolbarTitle(getString(R.string.string_and_string, getString(R.string.edit),
                getString(R.string.user_details)))
        showBackButton()
        replaceFragment(UpdatePasswordFragment.newInstance(), false, R.id.container)
    }
}