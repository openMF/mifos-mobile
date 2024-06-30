package org.mifos.mobile.ui.loan_account_transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.feature.loan.loan_account_transaction.LoanAccountTransactionScreen
import org.mifos.mobile.feature.loan.loan_account_transaction.LoanAccountTransactionViewModel

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/ /**
 * Created by dilpreet on 4/3/17.
 */
@AndroidEntryPoint
class LoanAccountTransactionFragment : BaseFragment() {

    private val viewModel: LoanAccountTransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).hideToolbar()
        if (arguments != null) {
            viewModel.setLoanId(arguments?.getLong(Constants.LOAN_ID))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            LoanAccountTransactionScreen(
                navigateBack = { activity?.supportFragmentManager?.popBackStack() }
            )
        }
    }

    companion object {
        fun newInstance(loanId: Long?): LoanAccountTransactionFragment {
            val fragment = LoanAccountTransactionFragment()
            val args = Bundle()
            if (loanId != null) args.putLong(Constants.LOAN_ID, loanId)
            fragment.arguments = args
            return fragment
        }
    }
}
