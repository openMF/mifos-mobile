package org.mifos.mobile.ui.activities

import android.os.Bundle

import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityContainerBinding
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.AccountOverviewFragment


/**
 * @author Rajan Maurya
 * On 16/10/17.
 */
class AccountOverviewActivity : BaseActivity() {

    private lateinit var binding: ActivityContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(AccountOverviewFragment.newInstance(), false, R.id.container)
        showBackButton()
        hideToolbarElevation()
    }
}