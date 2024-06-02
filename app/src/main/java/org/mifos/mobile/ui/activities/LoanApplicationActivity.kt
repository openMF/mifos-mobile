package org.mifos.mobile.ui.activities

import android.os.Bundle
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityLoanApplicationBinding
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.ui.loan_account_application.LoanApplicationFragment

class LoanApplicationActivity : BaseActivity() {

    private lateinit var binding: ActivityLoanApplicationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            replaceFragment(
                LoanApplicationFragment.newInstance(LoanState.CREATE),
                false,
                R.id.container,
            )
        }
        showBackButton()
    }
}
