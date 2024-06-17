package org.mifos.mobile.ui.loan_account

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.client_charge.ClientChargeComposeFragment
import org.mifos.mobile.ui.enums.AccountType
import org.mifos.mobile.ui.enums.ChargeType
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.ui.loan_account_transaction.LoanAccountTransactionFragment
import org.mifos.mobile.ui.loan_account_withdraw.LoanAccountWithdrawFragment
import org.mifos.mobile.ui.loan_account_application.LoanApplicationFragment
import org.mifos.mobile.ui.loan_repayment_schedule.LoanRepaymentScheduleFragment
import org.mifos.mobile.ui.qr_code_display.QrCodeDisplayFragment
import org.mifos.mobile.ui.savings_make_transfer.SavingsMakeTransferFragment
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.guarantor.GuarantorActivity
import org.mifos.mobile.ui.loan_account_summary.LoanAccountSummaryFragment
import org.mifos.mobile.ui.qr_code_display.QrCodeDisplayComposeFragment
import org.mifos.mobile.ui.savings_make_transfer.SavingsMakeTransferComposeFragment
import org.mifos.mobile.utils.*
import javax.inject.Inject

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/ /**
 * @author Vishwajeet
 * @since 19/08/16
 */
@AndroidEntryPoint
class LoanAccountsDetailFragment : BaseFragment() {
    @Inject
    lateinit var preferencesHelper: PreferencesHelper
    private val viewModel: LoanAccountsDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            viewModel.setLoanId(arguments?.getLong(Constants.LOAN_ID))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel.loadLoanAccountDetails(viewModel.loanId)
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    LoanAccountDetailScreen(
                        uiState = viewModel.loanUiState.value,
                        navigateBack = { activity?.finish() },
                        viewGuarantor = { viewGuarantor() },
                        updateLoan = { updateLoan() },
                        withdrawLoan = { withdrawLoan() },
                        viewLoanSummary = {  onLoanSummaryClicked() },
                        viewCharges = { chargesClicked() },
                        viewRepaymentSchedule = { onRepaymentScheduleClicked() },
                        viewTransactions = { onTransactionsClicked() },
                        viewQr = {  onQrCodeClicked() },
                        makePayment = { onMakePaymentClicked() },
                        retryConnection = { retryClicked() }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.hideToolbar()
    }

    /**
     * Opens [SavingsMakeTransferFragment] to Make a payment for loan account with given
     * `loanId`
     */
    private fun onMakePaymentClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            SavingsMakeTransferComposeFragment.newInstance(
                viewModel.loanId,
                viewModel.loanWithAssociations?.summary?.totalOutstanding,
                Constants.TRANSFER_PAY_TO,
            ),
            true,
            R.id.container,
        )
    }

    /**
     * Opens [LoanAccountSummaryFragment]
     */
    private fun onLoanSummaryClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            LoanAccountSummaryFragment.newInstance(
                viewModel.loanWithAssociations,
            ),
            true,
            R.id.container,
        )
    }

    /**
     * Opens [LoanRepaymentScheduleFragment]
     */
    private fun onRepaymentScheduleClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            LoanRepaymentScheduleFragment.newInstance(
                viewModel.loanId,
            ),
            true,
            R.id.container,
        )
    }

    /**
     * Opens [LoanAccountTransactionFragment]
     */
    private fun onTransactionsClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            LoanAccountTransactionFragment.newInstance(
                viewModel.loanId,
            ),
            true,
            R.id.container,
        )
    }

    private fun chargesClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            ClientChargeComposeFragment.newInstance(
                viewModel.loanWithAssociations?.id?.toLong(),
                ChargeType.LOAN,
            ),
            true,
            R.id.container,
        )
    }

    private fun onQrCodeClicked() {
        val accountDetailsInJson = QrCodeGenerator.getAccountDetailsInString(
            viewModel.loanWithAssociations?.accountNo,
            preferencesHelper.officeName,
            AccountType.LOAN,
        )
        (activity as BaseActivity?)?.replaceFragment(
            QrCodeDisplayComposeFragment.newInstance(
                accountDetailsInJson,
            ),
            true,
            R.id.container,
        )
    }

    private fun retryClicked() {
        if (Network.isConnected(context)) {
            viewModel.loadLoanAccountDetails(viewModel.loanId)
        } else {
            Toast.makeText(
                context,
                getString(R.string.internet_not_connected),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    private fun updateLoan() {
        (activity as BaseActivity?)?.replaceFragment(
            LoanApplicationFragment.newInstance(
                LoanState.UPDATE,
                viewModel.loanWithAssociations,
            ),
            true,
            R.id.container,
        )
    }

    private fun withdrawLoan() {
        (activity as BaseActivity?)?.replaceFragment(
            LoanAccountWithdrawFragment.newInstance(
                viewModel.loanWithAssociations,
            ),
            true,
            R.id.container,
        )
    }

    private fun viewGuarantor() {
        val intent = Intent(requireContext(), GuarantorActivity::class.java)
        intent.putExtra(Constants.LOAN_ID, viewModel.loanId)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(loanId: Long): LoanAccountsDetailFragment {
            val fragment = LoanAccountsDetailFragment()
            val args = Bundle()
            args.putLong(Constants.LOAN_ID, loanId)
            fragment.arguments = args
            return fragment
        }
    }
}
