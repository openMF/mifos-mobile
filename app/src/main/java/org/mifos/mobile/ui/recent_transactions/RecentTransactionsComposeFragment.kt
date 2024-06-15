package org.mifos.mobile.ui.recent_transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment

/**
 * @author Vishwwajeet
 * @since 09/08/16
 */
@AndroidEntryPoint
class RecentTransactionsComposeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (activity as? BaseActivity)?.hideToolbar()
        return mifosComposeView(requireContext()) {
            RecentTransactionScreen(
                navigateBack = { activity?.onBackPressedDispatcher?.onBackPressed() }
            )
        }
    }

    companion object {
        fun newInstance(): RecentTransactionsComposeFragment {
            val fragment = RecentTransactionsComposeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
