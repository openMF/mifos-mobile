package org.mifos.mobile.ui.loan_repayment_schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.feature.loan.loan_repayment_schedule.LoanRepaymentScheduleScreen
import org.mifos.mobile.feature.loan.loan_repayment_schedule.LoanRepaymentScheduleViewModel

/**
 * Created by Rajan Maurya on 03/03/17.
 */
@AndroidEntryPoint
class LoanRepaymentScheduleFragment : BaseFragment() {

    val viewModel: LoanRepaymentScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                MifosMobileTheme {
                    LoanRepaymentScheduleScreen(navigateBack = { activity?.supportFragmentManager?.popBackStack() })
                }
            }
        }
    }

    companion object {
        fun newInstance(loanId: Long?): LoanRepaymentScheduleFragment {
            val loanRepaymentScheduleFragment = LoanRepaymentScheduleFragment()
            val args = Bundle()
            if (loanId != null) args.putLong(Constants.LOAN_ID, loanId)
            loanRepaymentScheduleFragment.arguments = args
            return loanRepaymentScheduleFragment
        }
    }
}
