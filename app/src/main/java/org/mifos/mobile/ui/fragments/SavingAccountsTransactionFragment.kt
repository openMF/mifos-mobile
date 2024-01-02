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
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentSavingAccountTransactionsBinding
import org.mifos.mobile.databinding.LayoutFilterDialogBinding
import org.mifos.mobile.models.CheckboxStatus
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.accounts.savings.Transactions
import org.mifos.mobile.ui.adapters.CheckBoxAdapter
import org.mifos.mobile.ui.adapters.SavingAccountsTransactionListAdapter
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.CheckBoxStatusUtil
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.DatePick
import org.mifos.mobile.utils.DividerItemDecoration
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.SavingsAccountUiState
import org.mifos.mobile.utils.StatusUtils
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.utils.getDatePickerDialog
import org.mifos.mobile.viewModels.SavingAccountsTransactionViewModel
import java.time.Instant
import javax.inject.Inject

/**
 * Created by dilpreet on 6/3/17.
 */
@AndroidEntryPoint
class SavingAccountsTransactionFragment : BaseFragment() {

    private var _binding: FragmentSavingAccountTransactionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SavingAccountsTransactionViewModel by viewModels()

    @JvmField
    @Inject
    var transactionListAdapter: SavingAccountsTransactionListAdapter? = null

    @JvmField
    @Inject
    var checkBoxAdapter: CheckBoxAdapter? = null
    private var tvStartDate: Button? = null
    private var tvEndDate: Button? = null
    private var sweetUIErrorHandler: SweetUIErrorHandler? = null
    private var savingsId: Long = 0
    private var transactionsList: List<Transactions?>? = null
    private var savingsWithAssociations: SavingsWithAssociations? = null
    private var datePick: DatePick? = null
    private var startDate: Long = Instant.now().toEpochMilli()
    private var endDate: Long = Instant.now().toEpochMilli()
    private var isReady = false
    private var statusList: List<CheckboxStatus>? = null
    private var isCheckBoxPeriod = false
    private var selectedRadioButtonId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setToolbarTitle(getString(R.string.saving_account_transactions_details))
        if (arguments != null) savingsId = arguments?.getLong(Constants.SAVINGS_ID)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSavingAccountTransactionsBinding.inflate(inflater, container, false)
        sweetUIErrorHandler = SweetUIErrorHandler(context, binding.root)
        showUserInterface()
        if (savedInstanceState == null) {
            viewModel.loadSavingsWithAssociations(savingsId)
        }
        initializeFilterVariables()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvHelpLineNumber.setOnClickListener {
                dialHelpLineNumber()
            }
            layoutError.btnTryAgain.setOnClickListener {
                retryClicked()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.savingAccountsTransactionUiState.collect { state ->
                    when (state) {
                        SavingsAccountUiState.Loading -> showProgress()

                        SavingsAccountUiState.Error -> {
                            hideProgress()
                            showErrorFetchingSavingAccountsDetail(context?.getString(R.string.saving_account_details))
                        }

                        is SavingsAccountUiState.SuccessLoadingSavingsWithAssociations -> {
                            hideProgress()
                            showSavingAccountsDetail(state.savingAccount)
                        }

                        is SavingsAccountUiState.ShowFilteredTransactionsList -> {
                            showFilteredList(state.savingAccountsTransactionList)
                        }

                        is SavingsAccountUiState.Initial -> {}

                        else -> throw IllegalStateException("Unexpected State : $state")
                    }
                }
            }
        }
    }

    private fun initializeFilterVariables() {
        statusList = StatusUtils.getSavingsAccountTransactionList(activity)
        isCheckBoxPeriod = false
        isReady = false
        selectedRadioButtonId = -1
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
     * Setting up basic components
     */
    fun showUserInterface() {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSavingAccountsTransaction.setHasFixedSize(true)
        binding.rvSavingAccountsTransaction.layoutManager = layoutManager
        binding.rvSavingAccountsTransaction.adapter = transactionListAdapter
        binding.rvSavingAccountsTransaction.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                layoutManager.orientation,
            ),
        )
    }

    /**
     * Provides with `savingsWithAssociations` fetched from server which is used to update the
     * `transactionListAdapter`
     *
     * @param savingsWithAssociations Contains [Transactions] for given Savings account.
     */
    private fun showSavingAccountsDetail(savingsWithAssociations: SavingsWithAssociations?) {
        binding.llAccount.visibility = View.VISIBLE
        this.savingsWithAssociations = savingsWithAssociations
        transactionsList = savingsWithAssociations?.transactions
        if (transactionsList != null && transactionsList?.isNotEmpty() == true) {
            transactionListAdapter?.setContext(context)
            transactionListAdapter?.setSavingAccountsTransactionList(transactionsList)
        } else {
            showEmptyTransactions()
        }
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param message Error message that tells the user about the problem.
     */
    private fun showErrorFetchingSavingAccountsDetail(message: String?) {
        if (!Network.isConnected(activity)) {
            sweetUIErrorHandler?.showSweetNoInternetUI(
                binding.rvSavingAccountsTransaction,
                binding.layoutError.root,
            )
        } else {
            sweetUIErrorHandler?.showSweetErrorUI(
                message,
                binding.rvSavingAccountsTransaction,
                binding.layoutError.root,
            )
        }
    }

    private fun retryClicked() {
        if (Network.isConnected(context)) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.rvSavingAccountsTransaction,
                binding.layoutError.root,
            )
            viewModel.loadSavingsWithAssociations(savingsId)
        } else {
            Toast.makeText(
                context,
                getString(R.string.internet_not_connected),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    /**
     * Provides with a filtered list according to the constraints used in `filter()` function
     */
    private fun showFilteredList(list: List<Transactions?>?) {
        if (!list.isNullOrEmpty()) {
            Toaster.show(binding.root, getString(R.string.filtered))
            transactionListAdapter?.setSavingAccountsTransactionList(list)
        } else {
            showEmptyTransactions()
        }
    }

    private fun showEmptyTransactions() {
        sweetUIErrorHandler?.showSweetEmptyUI(
            getString(R.string.transactions),
            R.drawable.ic_compare_arrows_black_24dp,
            binding.rvSavingAccountsTransaction,
            binding.layoutError.root,
        )
    }

    /**
     * Opens up Phone Dialer
     */
    private fun dialHelpLineNumber() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + getString(R.string.help_line_number))
        startActivity(intent)
    }

    private fun startDatePick() {
        datePick = DatePick.START
        getDatePickerDialog(Instant.ofEpochMilli(startDate)) {
            tvEndDate?.isEnabled = true
            tvStartDate?.text = DateHelper.getDateAsStringFromLong(it)
            startDate = it
        }.show(requireActivity().supportFragmentManager, Constants.DFRAG_DATE_PICKER)
    }

    private fun endDatePick() {
        datePick = DatePick.END
        getDatePickerDialog(Instant.ofEpochMilli(startDate)) {
            endDate = it
            tvEndDate?.text = DateHelper.getDateAsStringFromLong(it)
            isReady = true
        }.show(requireActivity().supportFragmentManager, Constants.DFRAG_DATE_PICKER)
    }

    /**
     * Checks if `startDate` is less than `endDate`
     *
     * @return Returns true if `startDate` is less than `endDate`
     */
    private fun isEndDateLargeThanStartDate(): Boolean {
        return startDate <= endDate
    }

    fun showProgress() {
        showProgressBar()
    }

    fun hideProgress() {
        hideProgressBar()
    }

    /**
     * Shows a filter dialog
     */
     private lateinit var filterBinding: LayoutFilterDialogBinding
    private fun showFilterDialog() {
        filterBinding = LayoutFilterDialogBinding.inflate(layoutInflater)
        val dialogView = filterBinding.root

        val checkBoxPeriod: AppCompatCheckBox? = filterBinding.cbSelect
        val radioGroupFilter = filterBinding.rgDateFilter
        tvStartDate = filterBinding.tvStartDate
        tvEndDate = filterBinding.tvEndDate
        tvStartDate?.isEnabled = false
        tvEndDate?.isEnabled = false

        tvStartDate?.text = DateHelper.getDateAsStringFromLong(startDate)
        tvEndDate?.text = DateHelper.getDateAsStringFromLong(endDate)

        tvStartDate?.setOnClickListener { startDatePick() }
        tvEndDate?.setOnClickListener { endDatePick() }

        checkBoxPeriod?.setOnCheckedChangeListener { _, isChecked ->
            isCheckBoxPeriod = isChecked
            tvStartDate?.isEnabled = isChecked
            tvEndDate?.isEnabled = false

            if (!isChecked) {
                isReady = false
                filterBinding.rgDateFilter.clearCheck()
                selectedRadioButtonId = -1
            } else {
                if (selectedRadioButtonId == -1) {
                    val btn = filterBinding.rbDate
                    btn.isChecked = true
                }
            }
        }
        radioGroupFilter?.setOnCheckedChangeListener { _, checkedId ->
            isCheckBoxPeriod = true
            selectedRadioButtonId = checkedId
            when (checkedId) {
                R.id.rb_four_weeks, R.id.rb_three_months, R.id.rb_six_months -> {
                    tvStartDate?.isEnabled = false
                    tvEndDate?.isEnabled = false
                    startDate = when (checkedId) {
                        R.id.rb_four_weeks -> DateHelper.subtractWeeks(4)
                        R.id.rb_three_months -> DateHelper.subtractMonths(3)
                        R.id.rb_six_months -> DateHelper.subtractMonths(6)
                        else -> startDate
                    }
                    endDate = System.currentTimeMillis()
                    isReady = true
                }
                R.id.rb_date -> {
                    tvStartDate?.isEnabled = true
                    tvEndDate?.isEnabled = false
                }
            }
        }

        val checkBoxRecyclerView: RecyclerView? = filterBinding.recyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        checkBoxRecyclerView?.layoutManager = layoutManager
        checkBoxRecyclerView?.adapter = checkBoxAdapter
        checkBoxAdapter?.statusList = statusList

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.select_you_want)
            .setView(dialogView)
            .setPositiveButton(getString(R.string.filter)) { _, _ ->
                if (checkBoxPeriod?.isChecked == true) {
                    if (!isReady) {
                        Toaster.show(binding.root, getString(R.string.select_date))
                        return@setPositiveButton
                    } else if (!isEndDateLargeThanStartDate()) {
                        Toaster.show(
                            binding.root,
                            getString(R.string.end_date_must_be_greater),
                        )
                        return@setPositiveButton
                    }
                    filter(startDate, endDate, checkBoxAdapter?.statusList)
                } else {
                    filter(checkBoxAdapter?.statusList)
                }
                filterSavingsAccountTransactionsByType(checkBoxAdapter?.statusList)
            }
            .setNeutralButton(getString(R.string.clear_filters)) { _, _ ->
                transactionListAdapter?.setSavingAccountsTransactionList(transactionsList)
                initializeFilterVariables()
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .create()
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_saving_accounts_transaction, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Will filter `transactionsList` according to `startDate` and `endDate`
     *
     * @param startDate Starting date
     * @param endDate   Ending date
     */
    private fun filter(startDate: Long?, endDate: Long?, statusModelList: List<CheckboxStatus?>?) {
        val dummyTransactionList = filterSavingsAccountTransactionsByType(statusModelList)
        viewModel.filterTransactionList(
            dummyTransactionList,
            startDate,
            endDate,
        )
    }

    /**
     * Will filter `transactionsList` according to `startDate` and `endDate`
     *
     * @param statusModelList Status Model List
     */
    private fun filter(statusModelList: List<CheckboxStatus?>?) {
        showFilteredList(filterSavingsAccountTransactionsByType(statusModelList))
    }

    /**
     * Will filter `transactionsList` according to `startDate` and `endDate`
     * @param statusModelList Status Model List
     */
    private fun filterSavingsAccountTransactionsByType(statusModelList: List<CheckboxStatus?>?): List<Transactions?> {
        val filteredSavingsTransactions: MutableList<Transactions?> = ArrayList()
        for (status in viewModel
            .getCheckedStatus(statusModelList)!!) {
            viewModel
                .filterTransactionListByType(transactionsList, status, getCheckBoxStatusStrings())
                ?.let { filteredSavingsTransactions.addAll(it) }
        }
        return filteredSavingsTransactions
    }

    private fun getCheckBoxStatusStrings(): CheckBoxStatusUtil {
        return CheckBoxStatusUtil().apply {
            this.depositString = context?.getString(R.string.deposit)
            this.dividendPayoutString = context?.getString(R.string.dividend_payout)
            this.withdrawalString = context?.getString(R.string.withdrawal)
            this.interestPostingString = context?.getString(R.string.interest_posting)
            this.feeDeductionString = context?.getString(R.string.fee_deduction)
            this.withdrawalTransferString = context?.getString(R.string.withdrawal_transfer)
            this.rejectedTransferString = context?.getString(R.string.rejected_transfer)
            this.overdraftFeeString = context?.getString(R.string.overdraft_fee)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter_savings_transactions -> showFilterDialog()
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgress()
        _binding = null
    }

    companion object {
        fun newInstance(savingsId: Long?): SavingAccountsTransactionFragment {
            val fragment = SavingAccountsTransactionFragment()
            val args = Bundle()
            if (savingsId != null) args.putLong(Constants.SAVINGS_ID, savingsId)
            fragment.arguments = args
            return fragment
        }
    }
}
