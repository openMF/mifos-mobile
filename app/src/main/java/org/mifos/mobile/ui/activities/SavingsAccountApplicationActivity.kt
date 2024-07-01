package org.mifos.mobile.ui.activities

import android.os.Bundle
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivitySavingsAccountApplicationBinding
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.core.model.enums.SavingsAccountState
import org.mifos.mobile.ui.savings_account.SavingsAccountApplicationFragment.Companion.newInstance

/*
* Created by saksham on 30/June/2018
*/
class SavingsAccountApplicationActivity : BaseActivity() {

    private lateinit var binding: ActivitySavingsAccountApplicationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavingsAccountApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarTitle(getString(R.string.apply_savings_account))
        showBackButton()
        replaceFragment(
            newInstance(
                SavingsAccountState.CREATE,
                null,
            ),
            false,
            R.id.container,
        )
    }
}
