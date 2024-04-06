package org.mifos.mobile.ui.savings_account_withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.ui.activities.SavingsAccountContainerActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedParcelable

/*
* Created by saksham on 02/July/2018
*/
@AndroidEntryPoint
class SavingsAccountWithdrawFragment : BaseFragment() {

    private val viewModel: SavingsAccountWithdrawViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? SavingsAccountContainerActivity)?.hideToolbar()
        arguments?.getCheckedParcelable(SavingsWithAssociations::class.java, Constants.SAVINGS_ACCOUNTS)?.let {
            viewModel.setContent(it)
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
                    SavingsAccountWithdrawScreen(
                        withdraw = { viewModel.submitWithdrawSavingsAccount(it) },
                        navigateBack = { withdrawSuccess ->
                            if (withdrawSuccess) {
                                activity?.finish()
                            } else {
                                activity?.supportFragmentManager?.popBackStack()
                            }
                        },
                    )
                }
            }
        }
    }

    companion object {
        fun newInstance(
            savingsWithAssociations: SavingsWithAssociations?,
        ): SavingsAccountWithdrawFragment {
            val fragment = SavingsAccountWithdrawFragment()
            val bundle = Bundle()
            bundle.putParcelable(Constants.SAVINGS_ACCOUNTS, savingsWithAssociations)
            fragment.arguments = bundle
            return fragment
        }
    }
}
