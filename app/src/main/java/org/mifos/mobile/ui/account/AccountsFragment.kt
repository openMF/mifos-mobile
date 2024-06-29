package org.mifos.mobile.ui.account

/*
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentAccountsBinding
import org.mifos.mobile.models.CheckboxStatus
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.models.accounts.share.ShareAccount
import org.mifos.mobile.ui.activities.LoanAccountContainerActivity
import org.mifos.mobile.ui.activities.SavingsAccountContainerActivity
import org.mifos.mobile.ui.adapters.LoanAccountsListAdapter
import org.mifos.mobile.ui.adapters.SavingAccountsListAdapter
import org.mifos.mobile.ui.adapters.ShareAccountsListAdapter
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.*
import org.mifos.mobile.core.common.utils.ParcelableAndSerializableUtils.getCheckedArrayListFromParcelable
import java.util.*

/**
 * Created by Rajan Maurya on 23/10/16.
 */
@AndroidEntryPoint
class AccountsFragment : BaseFragment(), OnRefreshListener {
    private var _binding: FragmentAccountsBinding? = null
    private val binding get() = _binding!!
    private var loanAccountsListAdapter: LoanAccountsListAdapter? = null
    private var savingAccountsListAdapter: SavingAccountsListAdapter? = null
    private var shareAccountsListAdapter: ShareAccountsListAdapter? = null
    private var accountType: String? = null
    private var loanAccounts: List<LoanAccount?>? = null
    private var savingAccounts: List<SavingAccount?>? = null
    private var shareAccounts: List<ShareAccount?>? = null
    private var currentFilterList: List<CheckboxStatus?>? = null

    private val viewModel: AccountsViewModel by viewModels()

    /**
     * Method to get the current filter list for the fragment
     *
     * @return currentFilterList
     */
    fun getCurrentFilterList(): List<CheckboxStatus?>? {
        return currentFilterList
    }

    /**
     * Method to set current filter list value
     */
    fun setCurrentFilterList(currentFilterList: List<CheckboxStatus?>?) {
        this.currentFilterList = currentFilterList
    }

    private var sweetUIErrorHandler: SweetUIErrorHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loanAccounts = ArrayList()
        savingAccounts = ArrayList()
        shareAccounts = ArrayList()
        if (arguments != null) {
            accountType = arguments?.getString(Constants.ACCOUNT_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAccountsBinding.inflate(inflater, container, false)
        val rootView = binding.root
        loanAccountsListAdapter = LoanAccountsListAdapter(::onItemClick)
        savingAccountsListAdapter = SavingAccountsListAdapter(::onItemClick)
        shareAccountsListAdapter = ShareAccountsListAdapter(::onItemClick)

        sweetUIErrorHandler = SweetUIErrorHandler(activity, rootView)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.rvAccounts.layoutManager = layoutManager
        binding.rvAccounts.setHasFixedSize(true)
        binding.rvAccounts.addItemDecoration(
            DividerItemDecoration(
                activity,
                layoutManager.orientation,
            ),
        )
        binding.swipeContainer.setColorSchemeColors(
            *requireActivity()
                .resources.getIntArray(R.array.swipeRefreshColors),
        )
        binding.swipeContainer.setOnRefreshListener(this)
        if (savedInstanceState == null) {
            showProgress()
        }
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.ACCOUNT_TYPE, accountType)
        when (accountType) {
            Constants.SAVINGS_ACCOUNTS -> outState.putParcelableArrayList(
                Constants.SAVINGS_ACCOUNTS,
                ArrayList<Parcelable?>(savingAccounts),
            )

            Constants.LOAN_ACCOUNTS -> outState.putParcelableArrayList(
                Constants.LOAN_ACCOUNTS,
                ArrayList<Parcelable?>(loanAccounts),
            )

            Constants.SHARE_ACCOUNTS -> outState.putParcelableArrayList(
                Constants.SHARE_ACCOUNTS,
                ArrayList<Parcelable?>(shareAccounts),
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            accountType = savedInstanceState.getString(Constants.ACCOUNT_TYPE)
            when (accountType) {
                Constants.SAVINGS_ACCOUNTS -> {
                    val savingAccountList: List<SavingAccount?> =
                        savedInstanceState.getCheckedArrayListFromParcelable(
                            SavingAccount::class.java,
                            Constants.SAVINGS_ACCOUNTS
                        )
                            ?: listOf()
                    showSavingsAccounts(savingAccountList)
                }

                Constants.LOAN_ACCOUNTS -> {
                    val loanAccountList: List<LoanAccount?> =
                        savedInstanceState.getCheckedArrayListFromParcelable(
                            LoanAccount::class.java,
                            Constants.LOAN_ACCOUNTS,
                        ) ?: listOf()
                    showLoanAccounts(loanAccountList)
                }

                Constants.SHARE_ACCOUNTS -> {
                    val shareAccountList: List<ShareAccount?> =
                        savedInstanceState.getCheckedArrayListFromParcelable(
                            ShareAccount::class.java,
                            Constants.SHARE_ACCOUNTS,
                        ) ?: listOf()
                    showShareAccounts(shareAccountList)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutError.btnTryAgain.setOnClickListener {
            onRetry()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.accountsUiState.collect {
                when (it) {
                    AccountsUiState.Loading -> showProgress()

                    AccountsUiState.Error -> {
                        hideProgress()
                        showError(context?.getString(R.string.error_fetching_accounts))
                    }

                    is AccountsUiState.ShowSavingsAccounts -> {
                        hideProgress()
                        showSavingsAccounts(it.savingAccounts)
                    }

                    is AccountsUiState.ShowLoanAccounts -> {
                        hideProgress()
                        showLoanAccounts(it.loanAccounts)

                    }

                    is AccountsUiState.ShowShareAccounts -> {
                        hideProgress()
                        showShareAccounts(it.shareAccounts)
                    }
                }

            }
        }
    }

    /**
     * Used for reloading account of a particular `accountType` in case of a network error.
     */
    private fun onRetry() {
        if (Network.isConnected(context)) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.rvAccounts,
                binding.layoutError.root,
            )
            viewModel.loadAccounts(accountType)
        } else {
            Toast.makeText(
                context,
                getString(R.string.internet_not_connected),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    /**
     * This method is called when we swipe to refresh the view and is used for reloading account of
     * a particular `accountType`
     */
    override fun onRefresh() {
        if (Network.isConnected(context)) {
            clearFilter()
            viewModel.loadAccounts(accountType)
        } else {
            hideProgress()
            sweetUIErrorHandler?.showSweetNoInternetUI(binding.rvAccounts, binding.layoutError.root)
            Toast.makeText(
                context,
                getString(R.string.internet_not_connected),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    /**
     * Method to clear the current filters and set
     * currentFilterList = null
     */
    fun clearFilter() {
        sweetUIErrorHandler?.hideSweetErrorLayoutUI(binding.rvAccounts, binding.layoutError.root)
        currentFilterList = null
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
    }

    /**
     * Shows [List] of [LoanAccount] fetched from server using
     * [LoanAccountsListAdapter]
     *
     * @param loanAccounts [List] of [LoanAccount]
     */
    private fun showLoanAccounts(loanAccounts: List<LoanAccount?>?) {
        Collections.sort(loanAccounts, ComparatorBasedOnId())
        this.loanAccounts = loanAccounts
        if (loanAccounts?.isNotEmpty() == true) {
            loanAccountsListAdapter?.setLoanAccountsList(loanAccounts)
            binding.rvAccounts.adapter = loanAccountsListAdapter
        } else {
            showEmptyAccounts(getString(R.string.loan_account))
        }
    }

    /**
     * Shows [List] of [SavingAccount] fetched from server using
     * [SavingAccountsListAdapter]
     *
     * @param savingAccounts [List] of [SavingAccount]
     */
    private fun showSavingsAccounts(savingAccounts: List<SavingAccount?>?) {
        Collections.sort(savingAccounts, ComparatorBasedOnId())
        this.savingAccounts = savingAccounts
        if (savingAccounts?.isNotEmpty() == true) {
            savingAccountsListAdapter?.setSavingAccountsList(savingAccounts)
            binding.rvAccounts.adapter = savingAccountsListAdapter
        } else {
            showEmptyAccounts(getString(R.string.savings_account))
        }
    }

    /**
     * Shows [List] of [ShareAccount] fetched from server using
     * [ShareAccountsListAdapter]
     *
     * @param shareAccounts [List] of [ShareAccount]
     */
    private fun showShareAccounts(shareAccounts: List<ShareAccount?>?) {
        Collections.sort(shareAccounts, ComparatorBasedOnId())
        this.shareAccounts = shareAccounts
        if (shareAccounts?.isNotEmpty() == true) {
            shareAccountsListAdapter?.setShareAccountsList(shareAccounts)
            binding.rvAccounts.adapter = shareAccountsListAdapter
        } else {
            showEmptyAccounts(getString(R.string.share_account))
        }
    }

    /**
     * Shows an error layout when this function is called.
     *
     * @param emptyAccounts Text to show in `noAccountText`
     */
    private fun showEmptyAccounts(emptyAccounts: String?) {
        sweetUIErrorHandler?.showSweetEmptyUI(
            emptyAccounts,
            R.drawable.ic_account_balance_wallet_black_background_24dp,
            binding.rvAccounts,
            binding.layoutError.root,
        )
    }

    /**
     * Used for searching an `input` String in `savingAccounts` and displaying it in the
     * recyclerview.
     *
     * @param input String which is needs to be searched in list
     */
    fun searchSavingsAccount(input: String?) {
        val searchResult = viewModel.searchInSavingsList(savingAccounts, input)
        if (searchResult?.size == 0) {
            showEmptyAccounts(getString(R.string.no_saving_account))
        } else {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.rvAccounts,
                binding.layoutError.root,
            )
            savingAccountsListAdapter?.setSavingAccountsList(searchResult)
        }
    }

    /**
     * Used for searching an `input` String in `loanAccounts` and displaying it in the
     * recyclerview.
     *
     * @param input String which is needs to be searched in list
     */
    fun searchLoanAccount(input: String?) {
        val searchResult: List<LoanAccount?>? =
            viewModel.searchInLoanList(loanAccounts, input)
        if (searchResult?.size == 0) {
            showEmptyAccounts(getString(R.string.no_loan_account))
        } else {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.rvAccounts,
                binding.layoutError.root,
            )
            loanAccountsListAdapter?.setLoanAccountsList(searchResult)
        }
    }

    /**
     * Used for searching an `input` String in `savingAccounts` and displaying it in the
     * recyclerview.
     *
     * @param input String which is needs to be searched in list
     */
    fun searchSharesAccount(input: String?) {
        val searchResult: List<ShareAccount?>? =
            viewModel.searchInSharesList(shareAccounts, input)
        if (searchResult?.size == 0) {
            showEmptyAccounts(getString(R.string.no_sharing_account))
        } else {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.rvAccounts,
                binding.layoutError.root,
            )
            shareAccountsListAdapter?.setShareAccountsList(searchResult)
        }
    }

    /**
     * Used for filtering `savingAccounts` depending upon [List] of
     * [CheckboxStatus]
     *
     * @param statusModelList [List] of [CheckboxStatus]
     */
    fun filterSavingsAccount(statusModelList: List<CheckboxStatus?>?) {
        val filteredSavings: MutableList<SavingAccount?> = ArrayList()
        if (viewModel.getCheckedStatus(statusModelList) != null) {
            for (status in viewModel.getCheckedStatus(statusModelList)!!) {
                viewModel.getFilteredSavingsAccount(
                    savingAccounts,
                    status,
                    getFilterStrings()
                )?.let { filteredSavings.addAll(it) }
            }
        }
        if (filteredSavings.size != 0) {
            savingAccountsListAdapter?.setSavingAccountsList(filteredSavings)
        }
    }

    /**
     * Used for filtering `loanAccounts` depending upon [List] of
     * [CheckboxStatus]
     *
     * @param statusModelList [List] of [CheckboxStatus]
     */
    fun filterLoanAccount(statusModelList: List<CheckboxStatus?>?) {
        val filteredSavings: MutableList<LoanAccount?> = ArrayList()
        if (viewModel.getCheckedStatus(statusModelList) != null) {
            for (status in viewModel.getCheckedStatus(statusModelList)!!) {
                viewModel.getFilteredLoanAccount(
                    loanAccounts,
                    status,
                    getFilterStrings()
                )?.let { filteredSavings.addAll(it) }
            }
        }
        if (filteredSavings.size != 0) {
            loanAccountsListAdapter?.setLoanAccountsList(filteredSavings)
        }
    }

    /**
     * Used for filtering `shareAccounts` depending upon [List] of
     * [CheckboxStatus]
     *
     * @param statusModelList [List] of [CheckboxStatus]
     */
    fun filterShareAccount(statusModelList: List<CheckboxStatus?>?) {
        val filteredSavings: MutableList<ShareAccount?> = ArrayList()
        if (viewModel.getCheckedStatus(statusModelList) != null) {
            for (status in viewModel.getCheckedStatus(statusModelList)!!) {
                viewModel.getFilteredShareAccount(
                    shareAccounts,
                    status,
                    getFilterStrings()
                )?.let { filteredSavings.addAll(it) }
            }
        }
        if (filteredSavings.size != 0) {
            shareAccountsListAdapter?.setShareAccountsList(filteredSavings)
        }
    }

    private fun getFilterStrings(): AccountsFilterUtil {
        return AccountsFilterUtil().apply {
            this.activeString = context?.getString(R.string.active)
            this.approvedString = context?.getString(R.string.approved)
            this.approvalPendingString = context?.getString(R.string.approval_pending)
            this.maturedString = context?.getString(R.string.matured)
            this.waitingForDisburseString = context?.getString(R.string.waiting_for_disburse)
            this.overpaidString = context?.getString(R.string.overpaid)
            this.closedString = context?.getString(R.string.closed)
            this.withdrawnString = context?.getString(R.string.withdrawn)
            this.inArrearsString = context?.getString(R.string.in_arrears)
        }
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param errorMessage Error message that tells the user about the problem.
     */
    fun showError(errorMessage: String?) {
        if (!Network.isConnected(context)) {
            sweetUIErrorHandler?.showSweetNoInternetUI(binding.rvAccounts, binding.layoutError.root)
        } else {
            sweetUIErrorHandler?.showSweetErrorUI(
                errorMessage,
                binding.rvAccounts,
                binding.layoutError.root,
            )
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
        hideProgress()
    }

    /**
     * Shows [SwipeRefreshLayout]
     */
    fun showProgress() {
        showSwipeRefreshLayout(true)
    }

    /**
     * Hides [SwipeRefreshLayout]
     */
    fun hideProgress() {
        showSwipeRefreshLayout(false)
    }

    private fun showSwipeRefreshLayout(show: Boolean) {
        binding.swipeContainer.isRefreshing = show
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemClick(position: Int) {
        var intent: Intent? = null
        when (accountType) {
            Constants.SAVINGS_ACCOUNTS -> {
                intent = Intent(activity, SavingsAccountContainerActivity::class.java)
                intent.putExtra(
                    Constants.SAVINGS_ID,
                    savingAccountsListAdapter
                        ?.getSavingAccountsList()?.get(position)?.id,
                )
            }

            Constants.LOAN_ACCOUNTS -> {
                intent = Intent(activity, LoanAccountContainerActivity::class.java)
                intent.putExtra(
                    Constants.LOAN_ID,
                    loanAccountsListAdapter?.getLoanAccountsList()
                        ?.get(position)?.id,
                )
            }
        }
        openActivity(intent)
    }

    /**
     * This function opens up an activity only if the intent
     * is not null.
     *
     * This will prevent the application from crashing if the
     * intent is null.
     */
    private fun openActivity(intent: Intent?) {
        intent?.let { startActivity(it) }
    }

    companion object {
        val LOG_TAG: String? = AccountsFragment::class.java.simpleName
        fun newInstance(accountType: String?): AccountsFragment {
            val fragment = AccountsFragment()
            val args = Bundle()
            args.putString(Constants.ACCOUNT_TYPE, accountType)
            fragment.arguments = args
            return fragment
        }
    }
}
*/