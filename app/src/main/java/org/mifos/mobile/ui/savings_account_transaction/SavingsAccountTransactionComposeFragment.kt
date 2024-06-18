package org.mifos.mobile.ui.savings_account_transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.SavingsAccountContainerActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants


@AndroidEntryPoint
class SavingAccountsTransactionComposeFragment : BaseFragment() {

    private val viewModel: SavingAccountsTransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? SavingsAccountContainerActivity)?.hideToolbar()
        if (arguments != null) {
            arguments?.getLong(Constants.SAVINGS_ID)?.let {
                viewModel.setSavingsId(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            SavingsAccountTransactionScreen(
                navigateBack = { activity?.supportFragmentManager?.popBackStack() },
            )
        }
    }

    companion object {
        fun newInstance(savingsId: Long?): SavingAccountsTransactionComposeFragment {
            val fragment = SavingAccountsTransactionComposeFragment()
            val args = Bundle()
            if (savingsId != null) args.putLong(Constants.SAVINGS_ID, savingsId)
            fragment.arguments = args
            return fragment
        }
    }
}
