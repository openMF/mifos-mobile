package org.mifos.mobile.ui.savings_account

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
import org.mifos.mobile.feature.savings.savings_account_application.SavingsAccountApplicationScreen
import org.mifos.mobile.feature.savings.savings_account_application.SavingsAccountApplicationViewModel

/*
* Created by saksham on 30/June/2018
*/
@AndroidEntryPoint
class SavingsAccountApplicationFragment : BaseFragment() {

    private val viewModel: SavingsAccountApplicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
