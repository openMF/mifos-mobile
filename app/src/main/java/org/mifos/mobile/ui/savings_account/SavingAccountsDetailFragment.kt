package org.mifos.mobile.ui.savings_account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.ui.activities.SavingsAccountContainerActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.client_charge.ClientChargeComposeFragment
import org.mifos.mobile.ui.enums.AccountType
import org.mifos.mobile.ui.enums.ChargeType
import org.mifos.mobile.ui.enums.SavingsAccountState
import org.mifos.mobile.ui.savings_account_transaction.SavingAccountsTransactionComposeFragment
import org.mifos.mobile.ui.savings_account_application.SavingsAccountApplicationFragment
import org.mifos.mobile.ui.savings_account_withdraw.SavingsAccountWithdrawFragment
import org.mifos.mobile.ui.savings_make_transfer.SavingsMakeTransferFragment
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.qr_code_display.QrCodeDisplayComposeFragment
import org.mifos.mobile.ui.savings_account_transaction.SavingAccountsTransactionFragment
import org.mifos.mobile.ui.savings_make_transfer.SavingsMakeTransferComposeFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.QrCodeGenerator
import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 18/8/16.
 */
@AndroidEntryPoint
class SavingAccountsDetailFragment : BaseFragment() {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper
    private val viewModel: SavingAccountsDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            viewModel.setSavingsId(arguments?.getLong(Constants.SAVINGS_ID))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel.loadSavingsWithAssociations(viewModel.savingsId)
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    SavingsAccountDetailScreen(
                        uiState = viewModel.savingAccountsDetailUiState.value,
                        navigateBack = { activity?.finish() },
                        updateSavingsAccount = { updateSavingsAccount(it) },
                        withdrawSavingsAccount = { withdrawSavingsAccount(it) },
                        makeTransfer = { transfer(it) },
                        viewTransaction = { transactionsClicked() },
                        viewCharges = { chargeClicked() },
                        viewQrCode = { qrCodeClicked(it) },
                        callUs = { dialHelpLineNumber() },
                        deposit = { deposit(it) },
                        retryConnection = { onRetry() }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? SavingsAccountContainerActivity)?.hideToolbar()
    }

    /**
     * Opens up Phone Dialer
     */
    private fun dialHelpLineNumber() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + getString(R.string.help_line_number))
        startActivity(intent)
    }

    /**
     * Opens [SavingsMakeTransferFragment] if status is ACTIVE else shows a
     * {@link Snackbar} that Account should be Active
     */
    fun deposit(isStatusActive: Boolean) {
        if (isStatusActive) {
            (activity as BaseActivity?)?.replaceFragment(
                SavingsMakeTransferComposeFragment.newInstance(
                    viewModel.savingsId,
                    Constants.TRANSFER_PAY_TO,
                ),
                true,
                R.id.container,
            )
        } else {
            Toast.makeText(
                context,
                getString(R.string.account_not_active_to_perform_deposit),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Opens [SavingsMakeTransferFragment] if status is ACTIVE else shows a
     * {@link Snackbar} that Account should be Active
     */
    fun transfer(isStatusActive: Boolean) {
        if (isStatusActive) {
            (activity as BaseActivity?)?.replaceFragment(
                SavingsMakeTransferComposeFragment.newInstance(
                    viewModel.savingsId,
                    Constants.TRANSFER_PAY_FROM,
                ),
                true,
                R.id.container,
            )
        } else {
            Toast.makeText(
                context,
                getString(R.string.account_not_active_to_perform_transfer),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onRetry() {
        if (!Network.isConnected(context)) {
            Toast.makeText(
                context,
                getString(R.string.internet_not_connected),
                Toast.LENGTH_SHORT,
            ).show()
        } else {
            viewModel.loadSavingsWithAssociations(viewModel.savingsId)
        }
    }

    private fun transactionsClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            SavingAccountsTransactionComposeFragment.newInstance(viewModel.savingsId),
            true,
            R.id.container,
        )
    }

    private fun chargeClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            ClientChargeComposeFragment.newInstance(viewModel.savingsId, ChargeType.SAVINGS),
            true,
            R.id.container,
        )
    }

    private fun qrCodeClicked(savingsWithAssociations: SavingsWithAssociations) {
        val accountDetailsInJson = QrCodeGenerator.getAccountDetailsInString(
            savingsWithAssociations.accountNo,
            preferencesHelper.officeName,
            AccountType.SAVINGS,
        )
        (activity as BaseActivity?)?.replaceFragment(
            QrCodeDisplayComposeFragment.newInstance(
                accountDetailsInJson,
            ),
            true,
            R.id.container,
        )
    }

    private fun withdrawSavingsAccount(savingsWithAssociations: SavingsWithAssociations?) {
        (activity as BaseActivity?)?.replaceFragment(
            SavingsAccountWithdrawFragment.newInstance(savingsWithAssociations),
            true,
            R.id.container,
        )
    }

    private fun updateSavingsAccount(savingsWithAssociations: SavingsWithAssociations?) {
        (activity as BaseActivity?)?.replaceFragment(
            SavingsAccountApplicationFragment.newInstance(
                SavingsAccountState.UPDATE,
                savingsWithAssociations,
            ),
            true,
            R.id.container,
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(savingsId: Long): SavingAccountsDetailFragment {
            val fragment = SavingAccountsDetailFragment()
            val args = Bundle()
            args.putLong(Constants.SAVINGS_ID, savingsId)
            fragment.arguments = args
            return fragment
        }
    }
}
