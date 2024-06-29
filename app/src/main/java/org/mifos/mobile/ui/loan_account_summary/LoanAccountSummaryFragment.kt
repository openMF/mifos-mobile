package org.mifos.mobile.ui.loan_account_summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.common.utils.ParcelableAndSerializableUtils.getCheckedParcelable

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/ /**
 * Created by dilpreet on 25/2/17.
 */
@AndroidEntryPoint
class LoanAccountSummaryFragment : BaseFragment() {

    private var loanWithAssociations: LoanWithAssociations? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? BaseActivity)?.hideToolbar()
        if (arguments != null) {
            loanWithAssociations = arguments?.getCheckedParcelable(LoanWithAssociations::class.java, Constants.LOAN_ACCOUNT)
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
                    LoanAccountSummaryScreen(
                        navigateBack = { },
                        loanWithAssociations = loanWithAssociations
                    )
                }
            }
        }
    }

    companion object {
        fun newInstance(
            loanWithAssociations: LoanWithAssociations?,
        ): LoanAccountSummaryFragment {
            val loanAccountSummaryFragment = LoanAccountSummaryFragment()
            val args = Bundle()
            args.putParcelable(Constants.LOAN_ACCOUNT, loanWithAssociations)
            loanAccountSummaryFragment.arguments = args
            return loanAccountSummaryFragment
        }
    }
}
