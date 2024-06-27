package org.mifos.mobile.ui.activities

import android.os.Bundle
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityContainerBinding
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.savings_account.SavingAccountsDetailFragment
import org.mifos.mobile.core.common.Constants

/**
 * Created by Rajan Maurya on 05/03/17.
 */
class SavingsAccountContainerActivity : BaseActivity() {

    private lateinit var binding: ActivityContainerBinding

    private var savingsId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        savingsId = intent?.extras?.getLong(Constants.SAVINGS_ID)!!
        replaceFragment(SavingAccountsDetailFragment.newInstance(savingsId), false, R.id.container)
        showBackButton()
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 2 && transferSuccess) {
            supportFragmentManager.popBackStack()
            supportFragmentManager.popBackStack()
            transferSuccess = false
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        var transferSuccess = false
    }
}
