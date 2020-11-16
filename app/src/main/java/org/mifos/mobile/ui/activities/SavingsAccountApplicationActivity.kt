package org.mifos.mobile.ui.activities

import android.os.Bundle

import org.mifos.mobile.R
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.SavingsAccountState
import org.mifos.mobile.ui.fragments.SavingsAccountApplicationFragment.Companion.newInstance

/*
* Created by saksham on 30/June/2018
*/
class SavingsAccountApplicationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savings_account_application)
        setToolbarTitle(getString(R.string.apply_savings_account))
        showBackButton()
        replaceFragment(newInstance(SavingsAccountState.CREATE,
                null), false, R.id.container)
    }
}