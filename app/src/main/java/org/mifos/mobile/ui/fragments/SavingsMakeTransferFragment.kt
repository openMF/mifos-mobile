package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentSavingsMakeTransferBinding
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.models.templates.account.AccountOption
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.adapters.AccountsSpinnerAdapter
import org.mifos.mobile.ui.enums.TransferType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.SavingsAccountUiState
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.utils.Utils
import org.mifos.mobile.utils.getTodayFormatted
import org.mifos.mobile.viewModels.SavingsMakeTransferViewModel
import java.lang.IllegalStateException

/**
 * Created by Rajan Maurya on 10/03/17.
 */
@AndroidEntryPoint
class SavingsMakeTransferFragment : BaseFragment() {

    private var _binding: FragmentSavingsMakeTransferBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : SavingsMakeTransferViewModel
    private var transferPayload: TransferPayload? = null
    private var transferDate: String? = null
    private var toAccountOption: AccountOption? = null
    private var fromAccountOption: AccountOption? = null
    private var accountOptionsTemplate: AccountOptionsTemplate? = null
    private var transferType: String? = null
    private var payTo: String? = null
    private var payFrom: String? = null
    private var accountId: Long? = 0
    private var outStandingBalance: Double? = 0.0
    private var isLoanRepayment = false
    private var sweetUIErrorHandler: SweetUIErrorHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            accountId = arguments?.getLong(Constants.ACCOUNT_ID)
            transferType = arguments?.getString(Constants.TRANSFER_TYPE)
            if (arguments?.getBoolean(Constants.LOAN_REPAYMENT, false) == true) {
                isLoanRepayment = true
                outStandingBalance = arguments?.getDouble(Constants.OUTSTANDING_BALANCE)
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSavingsMakeTransferBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SavingsMakeTransferViewModel::class.java]
        setToolbarTitle(getString(R.string.transfer))
        sweetUIErrorHandler = SweetUIErrorHandler(activity, binding.root)
        showUserInterface()
        if (savedInstanceState == null) {
            viewModel.loanAccountTransferTemplate()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCancelTransfer.setOnClickListener {
            cancelTransfer()
        }

        binding.btnReviewTransfer.setOnClickListener {
            reviewTransfer()
        }

        binding.btnPayFrom.setOnClickListener {
            payFromSelected()
        }

        binding.btnPayTo.setOnClickListener {
            payToSelected()
        }

        binding.btnAmount.setOnClickListener {
            amountSet()
        }

        binding.layoutError.btnTryAgain.setOnClickListener {
            onRetry()
        }

        viewModel.savingsMakeTransferUiState.observe(viewLifecycleOwner) { state ->
            when(state) {
                SavingsAccountUiState.Loading -> showProgress()

                is SavingsAccountUiState.ErrorMessage -> {
                    hideProgress()
                    showError(context?.getString(R.string.error_fetching_account_transfer_template))
                }

                is SavingsAccountUiState.ShowSavingsAccountTemplate -> {
                    hideProgress()
                    showSavingsAccountTemplate(state.accountOptionsTemplate)
                }

                else -> throw IllegalStateException("Unexpected state : $state")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(Constants.TEMPLATE, accountOptionsTemplate)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            showSavingsAccountTemplate(savedInstanceState.getParcelable<Parcelable>(Constants.TEMPLATE) as AccountOptionsTemplate)
        }
    }

    /**
     * Checks validation of `etRemark` and then opens [TransferProcessFragment] for
     * initiating the transfer
     */
    private fun reviewTransfer() {
        if (binding.etRemark.text.toString().trim { it <= ' ' } == "") {
            showToaster(getString(R.string.remark_is_mandatory))
            return
        }
        transferPayload = TransferPayload()
        transferPayload?.fromAccountId = fromAccountOption?.accountId
        transferPayload?.fromClientId = fromAccountOption?.clientId
        transferPayload?.fromAccountType = fromAccountOption?.accountType?.id
        transferPayload?.fromOfficeId = fromAccountOption?.officeId
        transferPayload?.toOfficeId = toAccountOption?.officeId
        transferPayload?.toAccountId = toAccountOption?.accountId
        transferPayload?.toClientId = toAccountOption?.clientId
        transferPayload?.toAccountType = toAccountOption?.accountType?.id
        transferPayload?.transferDate = transferDate
        transferPayload?.transferAmount = binding.amountField.text.toString().toDouble()
        transferPayload?.transferDescription = binding.etRemark.text.toString()
        transferPayload?.fromAccountNumber = fromAccountOption?.accountNo
        transferPayload?.toAccountNumber = toAccountOption?.accountNo
        (activity as BaseActivity?)?.replaceFragment(
            TransferProcessFragment.newInstance(
                transferPayload,
                TransferType.SELF,
            ),
            true,
            R.id.container,
        )
    }

    /**
     * Cancels the transfer by popping current Fragment
     */
    private fun cancelTransfer() {
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun onRetry() {
        if (Network.isConnected(context)) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.llMakeTransfer,
                binding.layoutError.root,
            )
            viewModel.loanAccountTransferTemplate()
        } else {
            Toaster.show(binding.root, getString(R.string.internet_not_connected))
        }
    }

    /**
     * Setting up basic components
     */
    fun showUserInterface() {
        binding.processOne.setCurrentActive()
        binding.payFromField.setOnItemClickListener { _, _, position, _ ->
            toAccountOption = accountOptionsTemplate?.toAccountOptions?.get(position)
            payTo = toAccountOption?.accountNo
            println("paytofield item selected payTo:$payTo")
            updateDetails()
        }
        binding.payToField.setOnItemClickListener { _, _, position, _ ->
            fromAccountOption = accountOptionsTemplate?.fromAccountOptions?.get(position)
            payFrom = fromAccountOption?.accountNo
            println("payfrom item selected payFrom:$payFrom")
            updateDetails()
        }
        transferDate = DateHelper.getSpecificFormat(
            DateHelper.FORMAT_dd_MMMM_yyyy,
            getTodayFormatted(),
        )
        if (isLoanRepayment) {
            binding.amountField.setText(outStandingBalance.toString())
            binding.amountFieldWrapper.isFocusable = false
        }
    }

    private fun updateDetails() {
        when (transferType) {
            Constants.TRANSFER_PAY_TO -> {
                setToolbarTitle(getString(R.string.deposit))
                toAccountOption = viewModel.searchAccount(accountOptionsTemplate?.toAccountOptions, accountId)
                binding.payToFieldWrapper.isEnabled = false
                binding.processOne.setCurrentCompleted()
            }

            Constants.TRANSFER_PAY_FROM -> {
                setToolbarTitle(getString(R.string.transfer))
                fromAccountOption = viewModel.searchAccount(accountOptionsTemplate?.fromAccountOptions, accountId)
                binding.payFromFieldWrapper.isEnabled = false
                binding.payFromFieldWrapper.visibility = View.VISIBLE
                binding.processTwo.setCurrentCompleted()
            }
        }
    }

    /**
     * Provides with `accountOptionsTemplate` fetched from server which is used to update
     * `listPayFrom` and `listPayTo`
     *
     * @param accountOptionsTemplate Template for account transfer
     */
    private fun showSavingsAccountTemplate(accountOptionsTemplate: AccountOptionsTemplate?) {
        this.accountOptionsTemplate = accountOptionsTemplate
        binding.payToField.setAdapter(
            AccountsSpinnerAdapter(
                requireContext(),
                viewModel.getAccountNumbers(
                    accountOptionsTemplate?.toAccountOptions,
                    false,
                    context?.getString(R.string.account_type_loan)
                ),
            ),
        )
        binding.payFromField.setAdapter(
            AccountsSpinnerAdapter(
                requireContext(),
                viewModel.getAccountNumbers(
                    accountOptionsTemplate?.toAccountOptions,
                    true,
                    context?.getString(R.string.account_type_loan)
                ),
            ),
        )
    }

    /**
     * Shows a {@link Snackbar} with `message`
     *
     * @param message String to be shown
     */
    private fun showToaster(message: String?) {
        Toaster.show(binding.root, message)
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param message Error message that tells the user about the problem.
     */
    fun showError(message: String?) {
        if (!Network.isConnected(context)) {
            sweetUIErrorHandler?.showSweetNoInternetUI(
                binding.llMakeTransfer,
                binding.layoutError.root,
            )
        } else {
            sweetUIErrorHandler?.showSweetErrorUI(
                message,
                binding.llMakeTransfer,
                binding.layoutError.root,
            )
            Toaster.show(binding.root, message)
        }
    }

    fun showProgress() {
        binding.llMakeTransfer.visibility = View.GONE
        showProgressBar()
    }

    fun hideProgress() {
        binding.llMakeTransfer.visibility = View.VISIBLE
        hideProgressBar()
    }

    /**
     * Callback for `spPayFrom` and `spPayTo`
     */

    /**
     * Disables `spPayTo` [Spinner] and sets `pvOne` to completed and make
     * `pvTwo` active
     */
    private fun payToSelected() {
        binding.processOne.setCurrentCompleted()
        binding.processTwo.setCurrentActive()
        binding.btnPayTo.visibility = View.GONE
        binding.btnPayFrom.visibility = View.VISIBLE
        binding.payFromFieldWrapper.visibility = View.VISIBLE
        binding.payToFieldWrapper.isEnabled = false
    }

    /**
     * Checks validation of `spPayTo` [Spinner].<br></br>
     * Disables `spPayFrom` [Spinner] and sets `pvTwo` to completed and make
     * `pvThree` active
     */
    private fun payFromSelected() {
        if (payTo == payFrom) {
            showToaster(getString(R.string.error_same_account_transfer))
            return
        }
        binding.processTwo.setCurrentCompleted()
        binding.processThree.setCurrentActive()
        binding.btnPayFrom.visibility = View.GONE
        binding.amountFieldWrapper.visibility = View.VISIBLE
        binding.btnAmount.visibility = View.VISIBLE
        binding.payFromFieldWrapper.isEnabled = false
    }

    /**
     * Checks validation of `etAmount` [EditText].<br></br>
     * Disables `etAmount` and sets `pvThree` to completed and make
     * `pvFour` active
     */
    private fun amountSet() {
        if (binding.amountField.text.toString() == "") {
            showToaster(getString(R.string.enter_amount))
            return
        }
        if (binding.amountField.text.toString() == ".") {
            showToaster(getString(R.string.invalid_amount))
            return
        }
        if (binding.amountField.text.toString().toDouble() == 0.0) {
            showToaster(getString(R.string.amount_greater_than_zero))
            return
        }
        binding.processThree.setCurrentCompleted()
        binding.processFour.setCurrentActive()
        binding.btnAmount.visibility = View.GONE
        binding.tvEnterRemark.visibility = View.GONE
        binding.etRemark.visibility = View.VISIBLE
        binding.llReview.visibility = View.VISIBLE
        binding.amountFieldWrapper.isEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_transfer, menu)
        Utils.setToolbarIconColor(activity, menu, R.color.white)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh_transfer) {
            val transaction = fragmentManager?.beginTransaction()
            val currFragment = activity?.supportFragmentManager
                ?.findFragmentById(R.id.container)
            if (currFragment != null) {
                transaction?.detach(currFragment)
                transaction?.attach(currFragment)
            }
            transaction?.commit()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgress()
        hideMifosProgressDialog()
        _binding = null
    }

    companion object {
        /**
         * Provides an instance of [SavingsMakeTransferFragment], use `transferType` as
         * `Constants.TRANSFER_PAY_TO` when we want to deposit and
         * `Constants.TRANSFER_PAY_FROM` when we want to make a transfer
         *
         * @param accountId    Saving account Id
         * @param transferType Type of transfer i.e. `Constants.TRANSFER_PAY_TO` or
         * `Constants.TRANSFER_PAY_FROM`
         * @return Instance of [SavingsMakeTransferFragment]
         */
        fun newInstance(accountId: Long?, transferType: String?): SavingsMakeTransferFragment {
            val transferFragment = SavingsMakeTransferFragment()
            val args = Bundle()
            if (accountId != null) args.putLong(Constants.ACCOUNT_ID, accountId)
            args.putString(Constants.TRANSFER_TYPE, transferType)
            transferFragment.arguments = args
            return transferFragment
        }

        fun newInstance(
            accountId: Long?,
            outstandingBalance: Double?,
            transferType: String?,
        ): SavingsMakeTransferFragment {
            val transferFragment = SavingsMakeTransferFragment()
            val args = Bundle()
            if (accountId != null) args.putLong(Constants.ACCOUNT_ID, accountId)
            args.putString(Constants.TRANSFER_TYPE, transferType)
            if (outstandingBalance != null) {
                args.putDouble(
                    Constants.OUTSTANDING_BALANCE,
                    outstandingBalance,
                )
            }
            args.putBoolean(Constants.LOAN_REPAYMENT, true)
            transferFragment.arguments = args
            return transferFragment
        }
    }
}
