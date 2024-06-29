package org.mifos.mobile.ui.loan_review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.core.common.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.core.common.utils.ParcelableAndSerializableUtils.getCheckedSerializable

@AndroidEntryPoint
class ReviewLoanApplicationFragment : BaseFragment() {

    private val viewModel by viewModels<ReviewLoanApplicationViewModel>()

    companion object {
        const val LOANS_PAYLOAD = "loans_payload"
        const val LOAN_NAME = "loan_name"
        const val ACCOUNT_NO = "account_no"
        const val LOAN_STATE = "loan_state"
        const val LOAN_ID = "loan_id"

        fun newInstance(
            loanState: LoanState,
            loansPayload: LoansPayload,
            loanName: String,
            accountNo: String,
        ): ReviewLoanApplicationFragment {
            val fragment = ReviewLoanApplicationFragment()
            val args = Bundle().apply {
                putSerializable(LOAN_STATE, loanState)
                putParcelable(LOANS_PAYLOAD, loansPayload)
                putString(LOAN_NAME, loanName)
                putString(ACCOUNT_NO, accountNo)
            }
            fragment.arguments = args
            return fragment
        }

        fun newInstance(
            loanState: LoanState?,
            loansPayload: LoansPayload?,
            loanId: Long?,
            loanName: String?,
            accountNo: String?,
        ): ReviewLoanApplicationFragment {
            val fragment = ReviewLoanApplicationFragment()
            val args = Bundle().apply {
                putSerializable(LOAN_STATE, loanState)
                if (loanId != null) putLong(LOAN_ID, loanId)
                putParcelable(LOANS_PAYLOAD, loansPayload)
                putString(LOAN_NAME, loanName)
                putString(ACCOUNT_NO, accountNo)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (activity as? BaseActivity)?.hideToolbar()
        val loanState =
            arguments?.getCheckedSerializable(LoanState::class.java, LOAN_STATE) as LoanState
        viewModel.insertData(
            loanState = loanState,
            loansPayload = arguments?.getCheckedParcelable(
                LoansPayload::class.java,
                LOANS_PAYLOAD
            )!!,
            loanName = arguments?.getString(LOAN_NAME),
            accountNo = arguments?.getString(ACCOUNT_NO),
            loanId = arguments?.getLong(LOAN_ID),
        )
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    ReviewLoanApplicationScreen(
                        submit = { viewModel.submitLoan() },
                        navigateBack = { reviewSuccess ->
                            if(reviewSuccess)activity?.finish()
                            else activity?.supportFragmentManager?.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
