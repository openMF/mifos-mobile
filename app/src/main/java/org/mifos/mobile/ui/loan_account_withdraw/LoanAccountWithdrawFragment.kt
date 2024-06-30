package org.mifos.mobile.ui.loan_account_withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.common.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.feature.loan.loan_account_withdraw.LoanAccountWithdrawScreen
import org.mifos.mobile.feature.loan.loan_account_withdraw.LoanAccountWithdrawViewModel

/**
 * Created by dilpreet on 7/6/17.
 */
@AndroidEntryPoint
class LoanAccountWithdrawFragment : BaseFragment() {

    private val viewModel: LoanAccountWithdrawViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? BaseActivity)?.hideToolbar()
        if (arguments != null) {
            viewModel.setLoanContent(
                loanWithAssociations = arguments?.getCheckedParcelable(LoanWithAssociations::class.java, Constants.LOAN_ACCOUNT)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    LoanAccountWithdrawScreen(
                        navigateBack = { activity?.supportFragmentManager?.popBackStack() }
                    )
                }
            }
        }
    }

    companion object {
        fun newInstance(
            loanWithAssociations: LoanWithAssociations?,
        ): LoanAccountWithdrawFragment {
            val fragment = LoanAccountWithdrawFragment()
            val args = Bundle()
            args.putParcelable(Constants.LOAN_ACCOUNT, loanWithAssociations)
            fragment.arguments = args
            return fragment
        }
    }
}
