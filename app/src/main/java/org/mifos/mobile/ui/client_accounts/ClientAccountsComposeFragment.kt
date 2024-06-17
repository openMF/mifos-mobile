package org.mifos.mobile.ui.client_accounts


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.AccountType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/
@AndroidEntryPoint
class ClientAccountsComposeFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (activity as? BaseActivity)?.hideToolbar()
        return mifosComposeView(requireContext()) {
            ClientAccountsScreen(
                navigateBack = { activity?.onBackPressed() }
            )
        }
    }

    companion object {
        fun newInstance(accountType: AccountType?): ClientAccountsComposeFragment {
            val clientAccountsComposeFragment = ClientAccountsComposeFragment()
            val args = Bundle()
            args.putSerializable(Constants.ACCOUNT_TYPE, accountType)
            clientAccountsComposeFragment.arguments = args
            return clientAccountsComposeFragment
        }
    }
}
