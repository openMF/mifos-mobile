package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentRecentTransactionsBinding
import org.mifos.mobile.models.Transaction
import org.mifos.mobile.presenters.RecentTransactionsPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.adapters.RecentTransactionListAdapter
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.RecentTransactionsView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DividerItemDecoration
import org.mifos.mobile.utils.EndlessRecyclerViewScrollListener
import org.mifos.mobile.utils.Network.isConnected
import org.mifos.mobile.utils.Toaster
import javax.inject.Inject

/**
 * @author Vishwwajeet
 * @since 09/08/16
 */
@AndroidEntryPoint
class RecentTransactionsFragment : BaseFragment(), RecentTransactionsView, OnRefreshListener {

    private var _binding: FragmentRecentTransactionsBinding? = null
    private val binding get() = _binding!!

    @JvmField
    @Inject
    var recentTransactionsPresenter: RecentTransactionsPresenter? = null

    @JvmField
    @Inject
    var recentTransactionsListAdapter: RecentTransactionListAdapter? = null
    private var sweetUIErrorHandler: SweetUIErrorHandler? = null
    private var recentTransactionList: MutableList<Transaction?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recentTransactionList = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecentTransactionsBinding.inflate(inflater, container, false)
        recentTransactionsPresenter?.attachView(this)
        sweetUIErrorHandler = SweetUIErrorHandler(activity, binding.root)
        showUserInterface()
        setToolbarTitle(getString(R.string.recent_transactions))
        if (savedInstanceState == null) {
            recentTransactionsPresenter?.loadRecentTransactions(false, 0)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutError.btnTryAgain.setOnClickListener {
            retryClicked()
        }
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
    override fun showUserInterface() {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvRecentTransactions.layoutManager = layoutManager
        binding.rvRecentTransactions.setHasFixedSize(true)
        binding.rvRecentTransactions.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                layoutManager.orientation,
            ),
        )
        recentTransactionsListAdapter?.setTransactions(recentTransactionList)
        binding.rvRecentTransactions.adapter = recentTransactionsListAdapter
        binding.rvRecentTransactions.addOnScrollListener(
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    recentTransactionsPresenter?.loadRecentTransactions(true, totalItemsCount)
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) &&
                        newState == RecyclerView.SCROLL_STATE_IDLE
                    ) {
                        Toaster.show(binding.root, R.string.no_more_transactions_available)
                    }
                }
            }
        )
        binding.swipeTransactionContainer.setColorSchemeColors(
            *requireActivity()
                .resources.getIntArray(R.array.swipeRefreshColors),
        )
        binding.swipeTransactionContainer.setOnRefreshListener(this)
    }

    /**
     * Refreshes the List of [Transaction]
     */
    override fun onRefresh() {
        if (binding.layoutError.root.visibility == View.VISIBLE) {
            resetUI()
        }
        recentTransactionsPresenter?.loadRecentTransactions(false, 0)
    }

    /**
     * Shows a Toast
     */
    override fun showMessage(message: String?) {
        (activity as BaseActivity?)?.showToast(message!!)
    }

    /**
     * Updates `recentTransactionsListAdapter` with `recentTransactionList` fetched from
     * server
     *
     * @param recentTransactionList List of [Transaction]
     */
    override fun showRecentTransactions(recentTransactionList: List<Transaction?>?) {
        this.recentTransactionList = recentTransactionList as MutableList<Transaction?>?
        recentTransactionsListAdapter?.setTransactions(recentTransactionList)
    }

    /**
     * Appends more Transactions in `recentTransactionList`
     *
     * @param transactions List of [Transaction]
     */
    override fun showLoadMoreRecentTransactions(transactions: List<Transaction?>?) {
        this.recentTransactionList?.addAll(recentTransactionList!!)
        recentTransactionsListAdapter?.notifyDataSetChanged()
    }

    override fun resetUI() {
        sweetUIErrorHandler?.hideSweetErrorLayoutUI(
            binding.rvRecentTransactions,
            binding.layoutError.root,
        )
    }

    /**
     * Hides `rvRecentTransactions` and shows a textview prompting no transactions
     */
    override fun showEmptyTransaction() {
        sweetUIErrorHandler?.showSweetEmptyUI(
            getString(R.string.recent_transactions),
            R.drawable.ic_error_black_24dp,
            binding.rvRecentTransactions,
            binding.layoutError.root,
        )
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param message Error message that tells the user about the problem.
     */
    override fun showErrorFetchingRecentTransactions(message: String?) {
        if (!isConnected(requireActivity())) {
            sweetUIErrorHandler?.showSweetNoInternetUI(
                binding.rvRecentTransactions,
                binding.layoutError.root,
            )
        } else {
            sweetUIErrorHandler?.showSweetErrorUI(
                message,
                binding.rvRecentTransactions,
                binding.layoutError.root,
            )
        }
    }

    fun retryClicked() {
        if (isConnected(requireContext())) {
            sweetUIErrorHandler?.hideSweetErrorLayoutUI(
                binding.rvRecentTransactions,
                binding.layoutError.root,
            )
            recentTransactionsPresenter?.loadRecentTransactions(false, 0)
        } else {
            Toast.makeText(
                context,
                getString(R.string.internet_not_connected),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    override fun showProgress() {
        showSwipeRefreshLayout(true)
    }

    override fun hideProgress() {
        showSwipeRefreshLayout(false)
    }

    override fun showSwipeRefreshLayout(show: Boolean) {
        binding.swipeTransactionContainer.post {
            binding.swipeTransactionContainer.isRefreshing = show
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recentTransactionsPresenter?.detachView()
        _binding = null
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
