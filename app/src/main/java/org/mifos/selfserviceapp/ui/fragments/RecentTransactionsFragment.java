package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.Transaction;
import org.mifos.selfserviceapp.presenters.RecentTransactionsPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.RecentTransactionListAdapter;
import org.mifos.selfserviceapp.ui.views.RecentTransactionsView;
import org.mifos.selfserviceapp.utils.EndlessRecyclerViewScrollListener;
import org.mifos.selfserviceapp.utils.Toaster;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwwajeet
 * @since 09/08/16
 */
public class RecentTransactionsFragment extends Fragment implements RecentTransactionsView,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_recent_transactions)
    RecyclerView rvRecentTransactions;

    @BindView(R.id.swipe_transaction_container)
    SwipeRefreshLayout swipeTransactionContainer;

    @BindView(R.id.iv_status)
    ImageView ivStatus;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.ll_error)
    View layoutError;

    @Inject
    RecentTransactionsPresenter recentTransactionsPresenter;

    @Inject
    RecentTransactionListAdapter recentTransactionsListAdapter;

    private View rootView;

    private List<Transaction> recentTransactionList;


    public static RecentTransactionsFragment newInstance() {
        RecentTransactionsFragment recentTransactionsFragment = new RecentTransactionsFragment();
        return recentTransactionsFragment;
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

        showUserInterface();
        recentTransactionsPresenter.loadRecentTransactions(false, 0);

        return rootView;
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
        resetUI();
        recentTransactionsPresenter.loadRecentTransactions(false, 0);
    }

    @Override
    public void showMessage(String message) {
        Toaster.show(rootView, message, Toaster.LONG);
    }

    /**
     * Updates {@code recentTransactionsListAdapter} with {@code recentTransactionList} fetched from
     * server
     * @param recentTransactionList List of {@link Transaction}
     */
    @Override
    public void showRecentTransactions(List<Transaction> recentTransactionList) {
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
        rvRecentTransactions.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
    }

    /**
     * Hides {@code rvRecentTransactions} and shows a textview prompting no transactions
     */
    @Override
    public void showEmptyTransaction() {
        rvRecentTransactions.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        tvStatus.setText(getString(R.string.empty_transactions));
    }

    /**
     * It is called whenever any error occurs while executing a request
     * @param message Error message that tells the user about the problem.
     */
    @Override
    public void showErrorFetchingRecentTransactions(String message) {
        showMessage(message);
        rvRecentTransactions.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        tvStatus.setText(message);
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