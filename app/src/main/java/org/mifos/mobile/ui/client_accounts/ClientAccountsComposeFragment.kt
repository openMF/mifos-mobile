package org.mifos.mobile.ui.client_accounts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.LoanAccountContainerActivity
import org.mifos.mobile.ui.activities.LoanApplicationActivity
import org.mifos.mobile.ui.activities.SavingsAccountApplicationActivity
import org.mifos.mobile.ui.activities.SavingsAccountContainerActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.feature.account.client_account.screens.ClientAccountsScreen

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/
@AndroidEntryPoint
class ClientAccountsComposeFragment : BaseFragment() {
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View {
//        (activity as? BaseActivity)?.hideToolbar()
////        return mifosComposeView(requireContext()) {
////            ClientAccountsScreen(
////                navigateBack = { activity?.onBackPressed() },
////                openNextActivity = { currentPage -> openActivity(currentPage) },
////                onItemClick = { accountType, accountId -> onItemClick(accountType, accountId) }
////            )
////        }
//    }

    private fun openActivity( currentPage : Int) {
        when (currentPage) {
            0 -> startActivity(Intent(activity, SavingsAccountApplicationActivity::class.java))
            1 -> startActivity(Intent(activity, LoanApplicationActivity::class.java))
        }
    }

    private fun onItemClick(accountType: String, accountId: Long
    ) {
        var intent: Intent? = null
        when (accountType) {
            Constants.SAVINGS_ACCOUNTS -> {
                intent = Intent(activity, SavingsAccountContainerActivity::class.java)
                intent.putExtra(Constants.SAVINGS_ID, accountId)
            }
            Constants.LOAN_ACCOUNTS -> {
                intent = Intent(activity, LoanAccountContainerActivity::class.java)
                intent.putExtra(Constants.LOAN_ID,accountId)
            }
        }
        openActivity(intent)
    }

    private fun openActivity(intent: Intent?) {
        intent?.let { startActivity(it) }
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
