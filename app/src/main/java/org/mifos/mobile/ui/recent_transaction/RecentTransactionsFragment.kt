package org.mifos.mobile.ui.recent_transaction

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.models.Transaction
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.ParcelableAndSerializableUtils.getCheckedArrayListFromParcelable

/**
 * @author Vishwwajeet
 * @since 09/08/16
 */
@AndroidEntryPoint
class RecentTransactionsFragment : BaseFragment() {

    private val recentTransactionViewModel: RecentTransactionViewModel by viewModels()

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
        setToolbarTitle(getString(R.string.recent_transactions))
        if (savedInstanceState == null) {
            recentTransactionViewModel.loadRecentTransactions(false, 0)
        }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                RecentTransactionScreen(recentTransactionUiState = recentTransactionViewModel.recentTransactionUiState.collectAsStateWithLifecycle().value)
            }
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
                savedInstanceState.getCheckedArrayListFromParcelable(
                    Transaction::class.java,
                    Constants.RECENT_TRANSACTIONS,
                ) ?: listOf()
        }
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
