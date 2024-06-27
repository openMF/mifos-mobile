package org.mifos.mobile.ui.activities

import android.os.Bundle
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityContainerBinding
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.loan_account.LoanAccountsDetailFragment
import org.mifos.mobile.core.common.Constants

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/
class LoanAccountContainerActivity : BaseActivity() {

    private lateinit var binding: ActivityContainerBinding

    private var loanId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loanId = intent?.extras?.getLong(org.mifos.mobile.core.common.Constants.LOAN_ID)!!
        replaceFragment(LoanAccountsDetailFragment.newInstance(loanId), false, R.id.container)
        showBackButton()
    }
}
