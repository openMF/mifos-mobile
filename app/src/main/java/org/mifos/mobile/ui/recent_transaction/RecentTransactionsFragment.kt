package org.mifos.mobile.ui.recent_transaction

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.Transaction
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.CurrencyUtil
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.Network.isConnected
import org.mifos.mobile.utils.RecentTransactionUiState
import org.mifos.mobile.utils.Utils.formatTransactionType

/**
 * @author Vishwwajeet
 * @since 09/08/16
 */
@AndroidEntryPoint
class RecentTransactionsFragment : BaseFragment(), OnRefreshListener {

    private val recentTransactionViewModel: RecentTransactionViewModel by viewModels()

    private var recentTransactionList: MutableList<Transaction?>? = null
    private var list by mutableStateOf<MutableList<Transaction?>?>(null)
    private var recentList by mutableStateOf(mutableListOf(RecentTransaction()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recentTransactionList = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setToolbarTitle(getString(R.string.recent_transactions))

        if (savedInstanceState == null) {
            recentTransactionViewModel.loadRecentTransactions(false, 0)
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    RecentTransactionScreen(
                        list = recentTransaction(list),
                        isOnline = isConnected(activity)
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recentTransactionViewModel.recentTransactionUiState.collect {
                    when (it) {
                        is RecentTransactionUiState.Loading -> showProgress()
                        is RecentTransactionUiState.RecentTransactions -> {
                            hideProgress()
                            list = showRecentTransactions(it.transactions)
                        }

                        is RecentTransactionUiState.Error -> {
                            hideProgress()
                            showMessage(getString(it.message))
                        }

                        is RecentTransactionUiState.EmptyTransaction -> {
                            hideProgress()
                            showEmptyTransaction()
                        }

                        is RecentTransactionUiState.LoadMoreRecentTransactions -> {
                            hideProgress()
                            showLoadMoreRecentTransactions(it.transactions)
                        }

                        RecentTransactionUiState.Initial -> {}
                    }
                }
            }
        }

//        binding.layoutError.btnTryAgain.setOnClickListener {
//            retryClicked()
//        }
    }

    private fun recentTransaction(list: MutableList<Transaction?>?): List<RecentTransaction> {
        if (list != null) {
            for (i in list) {
                context?.getString(
                    R.string.string_and_string,
                    i?.currency?.displaySymbol,
                    CurrencyUtil.formatCurrency(
                        activity,
                        i?.amount!!,
                    )
                )?.let {
                    RecentTransaction(
                        formatTransactionType(i.type.value),
                        it,
                        DateHelper.getDateAsString(i.date),
                    )
                }?.let {
                    recentList.add(
                        it
                    )
                }
            }
        }

        return recentList
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(
            Constants.RECENT_TRANSACTIONS,
            ArrayList<Parcelable?>(
                recentTransactionList,
            ),
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            val transactions: List<Transaction?> =
                savedInstanceState.getParcelableArrayList(Constants.RECENT_TRANSACTIONS) ?: listOf()
            showRecentTransactions(transactions)
        }
    }

    /**
     * Setting up `rvRecentTransactions`
     */
    fun showUserInterface() {
//        val layoutManager = LinearLayoutManager(activity)
//        layoutManager.orientation = LinearLayoutManager.VERTICAL
//        binding.rvRecentTransactions.layoutManager = layoutManager
//        binding.rvRecentTransactions.setHasFixedSize(true)
//        binding.rvRecentTransactions.addItemDecoration(
//            DividerItemDecoration(
//                requireActivity(),
//                layoutManager.orientation,
//            ),
//        )
//        recentTransactionsListAdapter?.setTransactions(recentTransactionList)
//        binding.rvRecentTransactions.adapter = recentTransactionsListAdapter
//        binding.rvRecentTransactions.addOnScrollListener(
//            object : EndlessRecyclerViewScrollListener(layoutManager) {
//                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                    recentTransactionViewModel.loadRecentTransactions(true, totalItemsCount)
//                }
//
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
//                    if (!recyclerView.canScrollVertically(1) &&
//                        newState == RecyclerView.SCROLL_STATE_IDLE
//                    ) {
//                        Toaster.show(binding.root, R.string.no_more_transactions_available)
//                    }
//                }
//            }
//        )
//        binding.swipeTransactionContainer.setColorSchemeColors(
//            *requireActivity()
//                .resources.getIntArray(R.array.swipeRefreshColors),
//        )
//        binding.swipeTransactionContainer.setOnRefreshListener(this)
    }

    /**
     * Refreshes the List of [Transaction]
     */
    override fun onRefresh() {
//        if (binding.layoutError.root.visibility == View.VISIBLE) {
//            resetUI()
//        }
        recentTransactionViewModel.loadRecentTransactions(false, 0)
    }

    /**
     * Shows a Toast
     */
    private fun showMessage(message: String?) {
        (activity as BaseActivity?)?.showToast(message!!)
    }

    /**
     * Updates `recentTransactionsListAdapter` with `recentTransactionList` fetched from
     * server
     *
     * @param recentTransactionList List of [Transaction]
     */
    private fun showRecentTransactions(recentTransactionList: List<Transaction?>?): MutableList<Transaction?>? {
        this.recentTransactionList = recentTransactionList as MutableList<Transaction?>?
        return this.recentTransactionList
    }

    /**
     * Appends more Transactions in `recentTransactionList`
     *
     * @param transactions List of [Transaction]
     */
    private fun showLoadMoreRecentTransactions(transactions: List<Transaction?>?) {
        this.recentTransactionList?.addAll(recentTransactionList!!)
    }

    private fun resetUI() {
//        sweetUIErrorHandler?.hideSweetErrorLayoutUI(
//            binding.rvRecentTransactions,
//            binding.layoutError.root,
//        )
    }

    /**
     * Hides `rvRecentTransactions` and shows a textview prompting no transactions
     */
    private fun showEmptyTransaction() {
//        sweetUIErrorHandler?.showSweetEmptyUI(
//            getString(R.string.recent_transactions),
//            R.drawable.ic_error_black_24dp,
//            binding.rvRecentTransactions,
//            binding.layoutError.root,
//        )
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param message Error message that tells the user about the problem.
     */
    fun showErrorFetchingRecentTransactions(message: String?) {
//        if (!isConnected(requireActivity())) {
//            sweetUIErrorHandler?.showSweetNoInternetUI(
//                binding.rvRecentTransactions,
//                binding.layoutError.root,
//            )
//        } else {
//            sweetUIErrorHandler?.showSweetErrorUI(
//                message,
//                binding.rvRecentTransactions,
//                binding.layoutError.root,
//            )
//        }
    }

    private fun retryClicked() {
//        if (isConnected(requireContext())) {
//            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
//                binding.rvRecentTransactions,
//                binding.layoutError.root,
//            )
//            recentTransactionViewModel.loadRecentTransactions(false, 0)
//        } else {
//            Toast.makeText(
//                context,
//                getString(R.string.internet_not_connected),
//                Toast.LENGTH_SHORT,
//            ).show()
//        }
    }

    fun showProgress() {
        showSwipeRefreshLayout(true)
    }

    fun hideProgress() {
        showSwipeRefreshLayout(false)
    }

    private fun showSwipeRefreshLayout(show: Boolean) {
//        binding.swipeTransactionContainer.post {
//            binding.swipeTransactionContainer.isRefreshing = show
//        }
    }

    companion object {
        fun newInstance(): RecentTransactionsFragment {
            val fragment = RecentTransactionsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
