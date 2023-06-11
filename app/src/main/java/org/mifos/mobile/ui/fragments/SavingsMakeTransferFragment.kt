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
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentSavingsMakeTransferBinding
import org.mifos.mobile.models.payload.TransferPayload
import org.mifos.mobile.models.templates.account.AccountOption
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.presenters.SavingsMakeTransferPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.adapters.AccountsSpinnerAdapter
import org.mifos.mobile.ui.enums.TransferType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.SavingsMakeTransferMvpView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.utils.Utils
import org.mifos.mobile.utils.getTodayFormatted
import javax.inject.Inject

/**
 * Created by Rajan Maurya on 10/03/17.
 */
class SavingsMakeTransferFragment : BaseFragment(), SavingsMakeTransferMvpView {

    private var _binding: FragmentSavingsMakeTransferBinding? = null
    private val binding get() = _binding!!

    @JvmField
    @Inject
    var savingsMakeTransferPresenter: SavingsMakeTransferPresenter? = null
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
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        setToolbarTitle(getString(R.string.transfer))
        savingsMakeTransferPresenter?.attachView(this)
        sweetUIErrorHandler = SweetUIErrorHandler(activity, binding.root)
        showUserInterface()
        if (savedInstanceState == null) {
            savingsMakeTransferPresenter?.loanAccountTransferTemplate()
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
    fun reviewTransfer() {
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
     * Cancels the transfer by poping current Fragment
     */
    fun cancelTransfer() {
        activity?.supportFragmentManager?.popBackStack()
    }

    fun onRetry() {
        if (Network.isConnected(context)) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.llMakeTransfer,
                binding.layoutError.root,
            )
            savingsMakeTransferPresenter?.loanAccountTransferTemplate()
        } else {
            Toaster.show(binding.root, getString(R.string.internet_not_connected))
        }
    }

    /**
     * Setting up basic components
     */
    override fun showUserInterface() {
        binding.processOne.setCurrentActive()
        binding.payFromField.setOnItemClickListener { parent, view, position, id ->
            toAccountOption = accountOptionsTemplate?.toAccountOptions?.get(position)
            payTo = toAccountOption?.accountNo
            println("paytofield item selected payTo:$payTo")
            updateDetails()
        }
        binding.payToField.setOnItemClickListener { parent, view, position, id ->
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
                toAccountOption = savingsMakeTransferPresenter
                    ?.searchAccount(accountOptionsTemplate?.toAccountOptions, accountId)
                binding.payToFieldWrapper.isEnabled = false
                binding.processOne.setCurrentCompleted()
            }

            Constants.TRANSFER_PAY_FROM -> {
                setToolbarTitle(getString(R.string.transfer))
                fromAccountOption = savingsMakeTransferPresenter
                    ?.searchAccount(accountOptionsTemplate?.fromAccountOptions, accountId)
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
    override fun showSavingsAccountTemplate(accountOptionsTemplate: AccountOptionsTemplate?) {
        this.accountOptionsTemplate = accountOptionsTemplate
        binding.payToField.setAdapter(
            AccountsSpinnerAdapter(
                requireContext(),
                savingsMakeTransferPresenter?.getAccountNumbers(
                    accountOptionsTemplate?.toAccountOptions,
                    false,
                )!!,
            ),
        )
        binding.payFromField.setAdapter(
            AccountsSpinnerAdapter(
                requireContext(),
                savingsMakeTransferPresenter?.getAccountNumbers(
                    accountOptionsTemplate?.toAccountOptions,
                    true,
                )!!,
            ),
        )
    }

    /**
     * Shows a {@link Snackbar} with `message`
     *
     * @param message String to be shown
     */
    override fun showToaster(message: String?) {
        Toaster.show(binding.root, message)
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param message Error message that tells the user about the problem.
     */
    override fun showError(message: String?) {
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

    override fun showProgressDialog() {
        showMifosProgressDialog(getString(R.string.making_transfer))
    }

    override fun hideProgressDialog() {
        hideMifosProgressDialog()
    }

    override fun showProgress() {
        binding.llMakeTransfer.visibility = View.GONE
        showProgressBar()
    }

    override fun hideProgress() {
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
    fun payToSelected() {
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
    fun payFromSelected() {
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
    fun amountSet() {
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
        savingsMakeTransferPresenter?.detachView()
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
