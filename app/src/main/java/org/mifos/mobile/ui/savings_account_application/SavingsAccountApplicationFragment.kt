package org.mifos.mobile.ui.savings_account_application

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
import org.mifos.mobile.core.model.enums.SavingsAccountState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.common.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.core.common.utils.ParcelableAndSerializableUtils.getCheckedSerializable

/*
* Created by saksham on 30/June/2018
*/
@AndroidEntryPoint
class SavingsAccountApplicationFragment : BaseFragment() {

    private val viewModel: SavingsAccountApplicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? BaseActivity)?.hideToolbar()
        if (arguments != null) {
            viewModel.setSavingsAccountState(arguments?.getCheckedSerializable(SavingsAccountState::class.java, Constants.SAVINGS_ACCOUNT_STATE) as SavingsAccountState)
            viewModel.setSavingsWithAssociations(arguments?.getCheckedParcelable(
                SavingsWithAssociations::class.java, Constants.SAVINGS_ACCOUNTS))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel.loadSavingsAccountApplicationTemplate()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    SavingsAccountApplicationScreen(
                        uiState = viewModel.savingsAccountApplicationUiState.value,
                        savingsWithAssociations = viewModel.savingsWithAssociations,
                        navigateBack = { activity?.finish() },
                        retryConnection = { viewModel.onRetry() },
                        submit = { productId, clientId, showToast ->
                            viewModel.onSubmit(productId = productId, clientId = clientId, showToast = showToast)
                        }
                    )
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(
            state: SavingsAccountState?,
            savingsWithAssociations: SavingsWithAssociations?,
        ): SavingsAccountApplicationFragment {
            val fragment = SavingsAccountApplicationFragment()
            val bundle = Bundle()
            bundle.putSerializable(Constants.SAVINGS_ACCOUNT_STATE, state)
            bundle.putParcelable(Constants.SAVINGS_ACCOUNTS, savingsWithAssociations)
            fragment.arguments = bundle
            return fragment
        }
    }
}
