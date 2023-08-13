package org.mifos.mobile.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.databinding.FragmentSavingAccountDetailsBinding
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.accounts.savings.Status
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.AccountType
import org.mifos.mobile.ui.enums.ChargeType
import org.mifos.mobile.ui.enums.SavingsAccountState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.CurrencyUtil
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.QrCodeGenerator
import org.mifos.mobile.utils.SavingsAccountUiState
import org.mifos.mobile.utils.SymbolsUtils
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.utils.Utils
import org.mifos.mobile.viewModels.SavingAccountsDetailViewModel
import java.lang.IllegalStateException
import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 18/8/16.
 */
@AndroidEntryPoint
class SavingAccountsDetailFragment : BaseFragment() {

    private var _binding: FragmentSavingAccountDetailsBinding? = null
    private val binding get() = _binding!!

    @JvmField
    @Inject
    var preferencesHelper: PreferencesHelper? = null
    private lateinit var viewModel: SavingAccountsDetailViewModel
    private var savingsId: Long? = 0
    private var status: Status? = null
    private var savingsWithAssociations: SavingsWithAssociations? = null
    private var sweetUIErrorHandler: SweetUIErrorHandler? = null
    private var isMenuVisible: Boolean? = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            savingsId = arguments?.getLong(Constants.SAVINGS_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSavingAccountDetailsBinding.inflate(inflater, container, false)
        setToolbarTitle(getString(R.string.saving_account_details))
        viewModel = ViewModelProvider(this)[SavingAccountsDetailViewModel::class.java]
        sweetUIErrorHandler = SweetUIErrorHandler(context, binding.root)
        if (savedInstanceState == null && this.savingsWithAssociations == null) {
            viewModel.loadSavingsWithAssociations(savingsId)
        } else {
            showSavingAccountsDetail(this.savingsWithAssociations)
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvHelpLineNumber.setOnClickListener {
            dialHelpLineNumber()
        }

        binding.tvDeposit.setOnClickListener {
            deposit()
        }

        binding.tvMakeATransfer.setOnClickListener {
            transfer()
        }
        binding.layoutError.btnTryAgain.setOnClickListener {
            onRetry()
        }

        binding.llSavingsTransactions.setOnClickListener {
            transactionsClicked()
        }

        binding.llSavingsCharges.setOnClickListener {
            chargeClicked()
        }

        binding.llSavingsQrCode.setOnClickListener {
            qrCodeClicked()
        }

        viewModel.savingAccountsDetailUiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SavingsAccountUiState.Loading -> showProgress()

                SavingsAccountUiState.Error -> {
                    hideProgress()
                    showErrorFetchingSavingAccountsDetail(
                        context?.getString(R.string.error_saving_account_details_loading),
                    )
                }

                is SavingsAccountUiState.SuccessLoadingSavingsWithAssociations -> {
                    hideProgress()
                    showSavingAccountsDetail(state.savingAccount)
                }

                else -> throw IllegalStateException("Unexpected State: $state")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(Constants.SAVINGS_ACCOUNTS, savingsWithAssociations)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            showSavingAccountsDetail(savedInstanceState.getParcelable<Parcelable>(Constants.SAVINGS_ACCOUNTS) as SavingsWithAssociations)
        }
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
    fun deposit() {
        if (status?.active == true) {
            (activity as BaseActivity?)?.replaceFragment(
                SavingsMakeTransferFragment.newInstance(
                    savingsId,
                    Constants.TRANSFER_PAY_TO,
                ),
                true,
                R.id.container,
            )
        } else {
            Toaster.show(binding.root, getString(R.string.account_not_active_to_perform_deposit))
        }
    }

    /**
     * Opens [SavingsMakeTransferFragment] if status is ACTIVE else shows a
     * {@link Snackbar} that Account should be Active
     */
    fun transfer() {
        if (status?.active == true) {
            (activity as BaseActivity?)?.replaceFragment(
                SavingsMakeTransferFragment.newInstance(
                    savingsId,
                    Constants.TRANSFER_PAY_FROM,
                ),
                true,
                R.id.container,
            )
        } else {
            Toaster.show(binding.root, getString(R.string.account_not_active_to_perform_transfer))
        }
    }

    /**
     * Sets Saving account basic info fetched from the server
     *
     * @param savingsWithAssociations object containing details of a saving account
     */
    private fun showSavingAccountsDetail(savingsWithAssociations: SavingsWithAssociations?) {
        if (savingsWithAssociations?.status?.submittedAndPendingApproval == true) {
            sweetUIErrorHandler?.showSweetCustomErrorUI(
                getString(R.string.approval_pending),
                R.drawable.ic_assignment_turned_in_black_24dp,
                binding.llAccount,
                binding.layoutError.root,
            )
            isMenuVisible = (savingsWithAssociations.status?.submittedAndPendingApproval == true)
        } else {
            binding.llAccount.visibility = View.VISIBLE
            val currencySymbol = savingsWithAssociations?.currency?.displaySymbol
            val accountBalance = savingsWithAssociations?.summary?.accountBalance
            binding.tvAccountStatus.text = savingsWithAssociations?.clientName
            if (savingsWithAssociations?.minRequiredOpeningBalance != null) {
                binding.tvMinReqBal.text = getString(
                    R.string.string_and_string,
                    currencySymbol,
                    CurrencyUtil.formatCurrency(
                        activity,
                        savingsWithAssociations.minRequiredOpeningBalance!!,
                    ),
                )
            } else {
                binding.tvMinRequiredBalance.visibility = View.GONE
                binding.tvMinReqBal.visibility = View.GONE
            }
            if (savingsWithAssociations?.summary?.totalWithdrawals != null) {
                binding.tvTotalWithdrawals.text = getString(
                    R.string.string_and_string,
                    currencySymbol,
                    CurrencyUtil.formatCurrency(
                        activity,
                        savingsWithAssociations.summary?.totalWithdrawals!!,
                    ),
                )
            } else {
                binding.tvTotalWithdrawals.setText(R.string.no_withdrawals)
            }
            binding.tvAccBalance.text = getString(
                R.string.string_and_string,
                currencySymbol,
                CurrencyUtil.formatCurrency(activity, accountBalance!!),
            )
            binding.tvNominalInterestRate.text = getString(
                R.string.double_and_string,
                savingsWithAssociations.getNominalAnnualInterestRate(),
                SymbolsUtils.PERCENT,
            )
            binding.tvSavingAccountNumber.text = savingsWithAssociations.accountNo.toString()
            if (savingsWithAssociations.summary?.totalDeposits != null) {
                binding.tvTotalDeposits.text = getString(
                    R.string.string_and_string,
                    currencySymbol,
                    CurrencyUtil.formatCurrency(
                        activity,
                        savingsWithAssociations.summary?.totalDeposits!!,
                    ),
                )
            } else {
                binding.tvTotalDeposits.text = getString(R.string.not_available)
            }
            if (savingsWithAssociations.transactions.isNotEmpty()) {
                binding.tvLastTransaction.text = getString(
                    R.string.string_and_double,
                    currencySymbol,
                    savingsWithAssociations.transactions[0].amount,
                )
                binding.tvMadeOn.text = DateHelper.getDateAsString(
                    savingsWithAssociations.lastActiveTransactionDate,
                )
            } else {
                binding.tvLastTransaction.setText(R.string.no_transaction)
                binding.tvMadeOn.visibility = View.GONE
                binding.madeOn.visibility = View.GONE
            }
            showAccountStatus(savingsWithAssociations)
        }
        this.savingsWithAssociations = savingsWithAssociations
        activity?.invalidateOptionsMenu()
        showAccountStatus(savingsWithAssociations)
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param message Error message that tells the user about the problem.
     */
    private fun showErrorFetchingSavingAccountsDetail(message: String?) {
        if (!Network.isConnected(context)) {
            sweetUIErrorHandler?.showSweetNoInternetUI(binding.llAccount, binding.layoutError.root)
            Toast.makeText(
                context,
                getString(R.string.internet_not_connected),
                Toast.LENGTH_SHORT,
            ).show()
        } else {
            sweetUIErrorHandler?.showSweetErrorUI(
                message,
                binding.llAccount,
                binding.layoutError.root,
            )
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(binding.llAccount, binding.layoutError.root)
            viewModel.loadSavingsWithAssociations(savingsId)
        }
    }

    /**
     * Sets the status of account i.e. `tvAccountStatus` and `ivCircularStatus` color
     * according to `savingsWithAssociations`
     *
     * @param savingsWithAssociations object containing details of a saving account
     */
    private fun showAccountStatus(savingsWithAssociations: SavingsWithAssociations?) {
        status = savingsWithAssociations?.status
        when {
            status?.active == true -> {
                binding.ivCircleStatus.setImageDrawable(
                    Utils.setCircularBackground(R.color.deposit_green, activity),
                )
                binding.tvAccountStatus.setText(R.string.active)
            }

            status?.approved == true -> {
                binding.ivCircleStatus.setImageDrawable(
                    Utils.setCircularBackground(R.color.blue, activity),
                )
                binding.tvAccountStatus.setText(R.string.need_approval)
            }

            status?.submittedAndPendingApproval == true -> {
                binding.ivCircleStatus.setImageDrawable(
                    Utils.setCircularBackground(R.color.light_yellow, activity),
                )
                binding.tvAccountStatus.setText(R.string.pending)
            }

            status?.matured == true -> {
                binding.ivCircleStatus.setImageDrawable(
                    Utils.setCircularBackground(R.color.red_light, activity),
                )
                binding.tvAccountStatus.setText(R.string.matured)
            }

            else -> {
                binding.ivCircleStatus.setImageDrawable(
                    Utils.setCircularBackground(R.color.black, activity),
                )
                binding.tvAccountStatus.setText(R.string.closed)
            }
        }
        activity?.invalidateOptionsMenu()
    }

    fun showProgress() {
        binding.llAccount.visibility = View.GONE
        showProgressBar()
    }

    fun hideProgress() {
        binding.llAccount.visibility = View.VISIBLE
        hideProgressBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgressBar()
        _binding = null
    }

    private fun transactionsClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            SavingAccountsTransactionFragment.newInstance(
                savingsId,
            ),
            true,
            R.id.container,
        )
    }

    private fun chargeClicked() {
        (activity as BaseActivity?)?.replaceFragment(
            ClientChargeFragment.newInstance(
                savingsId,
                ChargeType.SAVINGS,
            ),
            true,
            R.id.container,
        )
    }

    private fun qrCodeClicked() {
        val accountDetailsInJson = QrCodeGenerator.getAccountDetailsInString(
            savingsWithAssociations?.accountNo,
            preferencesHelper?.officeName,
            AccountType.SAVINGS,
        )
        (activity as BaseActivity?)?.replaceFragment(
            QrCodeDisplayFragment.newInstance(
                accountDetailsInJson,
            ),
            true,
            R.id.container,
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_savings_account_detail, menu)
        if (isMenuVisible == true) {
            menu.findItem(R.id.menu_withdraw_savings_account).isVisible = true
            menu.findItem(R.id.menu_update_savings_account).isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_withdraw_savings_account -> (activity as BaseActivity?)?.replaceFragment(
                SavingsAccountWithdrawFragment.newInstance(savingsWithAssociations),
                true,
                R.id.container,
            )

            R.id.menu_update_savings_account -> (activity as BaseActivity?)?.replaceFragment(
                SavingsAccountApplicationFragment.newInstance(
                    SavingsAccountState.UPDATE,
                    savingsWithAssociations,
                ),
                true,
                R.id.container,
            )
        }
        return super.onOptionsItemSelected(item)
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
