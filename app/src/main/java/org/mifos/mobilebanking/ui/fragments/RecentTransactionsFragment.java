package org.mifos.mobilebanking.ui.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.Transaction;
import org.mifos.mobilebanking.presenters.RecentTransactionsPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.adapters.RecentTransactionListAdapter;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.RecentTransactionsView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.DividerItemDecoration;
import org.mifos.mobilebanking.utils.EndlessRecyclerViewScrollListener;
import org.mifos.mobilebanking.utils.Network;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vishwwajeet
 * @since 09/08/16
 */
public class RecentTransactionsFragment extends BaseFragment implements RecentTransactionsView,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_recent_transactions)
    RecyclerView rvRecentTransactions;

    @BindView(R.id.swipe_transaction_container)
    SwipeRefreshLayout swipeTransactionContainer;

    @BindView(R.id.layout_error)
    View layoutError;

    @Inject
    RecentTransactionsPresenter recentTransactionsPresenter;

    @Inject
    RecentTransactionListAdapter recentTransactionsListAdapter;

    private SweetUIErrorHandler sweetUIErrorHandler;

    private View rootView;

    private List<Transaction> recentTransactionList;

    public static RecentTransactionsFragment newInstance() {
        RecentTransactionsFragment fragment = new RecentTransactionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        recentTransactionList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recent_transactions, container, false);

        ButterKnife.bind(this, rootView);
        recentTransactionsPresenter.attachView(this);
        sweetUIErrorHandler = new SweetUIErrorHandler(getActivity(), rootView);

        showUserInterface();
        setToolbarTitle(getString(R.string.recent_transactions));
        if (savedInstanceState == null) {
            recentTransactionsPresenter.loadRecentTransactions(false, 0);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.RECENT_TRANSACTIONS, new ArrayList<Parcelable>(
                recentTransactionList));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            List<Transaction> transactions = savedInstanceState.getParcelableArrayList(Constants.
                    RECENT_TRANSACTIONS);
            showRecentTransactions(transactions);
        }
    }

    /**
     * Setting up {@code rvRecentTransactions}
     */
    @Override
    public void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecentTransactions.setLayoutManager(layoutManager);
        rvRecentTransactions.setHasFixedSize(true);
        rvRecentTransactions.addItemDecoration(new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation()));
        recentTransactionsListAdapter.setTransactions(recentTransactionList);
        rvRecentTransactions.setAdapter(recentTransactionsListAdapter);
        rvRecentTransactions.addOnScrollListener(
                new EndlessRecyclerViewScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        recentTransactionsPresenter.loadRecentTransactions(true, totalItemsCount);
                    }
                });
        swipeTransactionContainer.setColorSchemeColors(getActivity()
                .getResources().getIntArray(R.array.swipeRefreshColors));
        swipeTransactionContainer.setOnRefreshListener(this);
    }

    /**
     * Refreshes the List of {@link Transaction}
     */
    @Override
    public void onRefresh() {
        if (layoutError.getVisibility() == View.VISIBLE) {
            resetUI();
        }
        recentTransactionsPresenter.loadRecentTransactions(false, 0);
    }

    /**
     * Shows a Toast
     * @param message
     */
    @Override
    public void showMessage(String message) {
        ((BaseActivity) getActivity()).showToast(message);
    }

    /**
     * Updates {@code recentTransactionsListAdapter} with {@code recentTransactionList} fetched from
     * server
     * @param recentTransactionList List of {@link Transaction}
     */
    @Override
    public void showRecentTransactions(List<Transaction> recentTransactionList) {
        this.recentTransactionList = recentTransactionList;
        recentTransactionsListAdapter.setTransactions(recentTransactionList);
    }

    /**
     * Appends more Transactions in {@code recentTransactionList}
     * @param transactions List of {@link Transaction}
     */
    @Override
    public void showLoadMoreRecentTransactions(List<Transaction> transactions) {
        this.recentTransactionList.addAll(recentTransactionList);
        recentTransactionsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void resetUI() {
        sweetUIErrorHandler.hideSweetErrorLayoutUI(rvRecentTransactions, layoutError);
    }

    /**
     * Hides {@code rvRecentTransactions} and shows a textview prompting no transactions
     */
    @Override
    public void showEmptyTransaction() {
        sweetUIErrorHandler.showSweetEmptyUI(getString(R.string.recent_transactions),
                R.drawable.ic_label_black_24dp, rvRecentTransactions, layoutError);
    }

    /**
     * It is called whenever any error occurs while executing a request
     * @param message Error message that tells the user about the problem.
     */
    @Override
    public void showErrorFetchingRecentTransactions(String message) {
        if (!Network.isConnected(getActivity())) {
            sweetUIErrorHandler.showSweetNoInternetUI(rvRecentTransactions, layoutError);
        } else {
            sweetUIErrorHandler.showSweetErrorUI(message, rvRecentTransactions, layoutError);
        }
    }

    @OnClick(R.id.btn_try_again)
    public void retryClicked() {
        if (Network.isConnected(getContext())) {
            sweetUIErrorHandler.hideSweetErrorLayoutUI(rvRecentTransactions, layoutError);
            recentTransactionsPresenter.loadRecentTransactions(false, 0);
        } else {
            Toast.makeText(getContext(), getString(R.string.internet_not_connected),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showProgress() {
        showSwipeRefreshLayout(true);
    }

    @Override
    public void hideProgress() {
        showSwipeRefreshLayout(false);
    }

    @Override
    public void showSwipeRefreshLayout(final boolean show) {
        swipeTransactionContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeTransactionContainer.setRefreshing(show);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recentTransactionsPresenter.detachView();
    }
}