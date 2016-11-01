package org.mifos.selfserviceapp.ui.fragments;

import android.app.ProgressDialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.Transaction;
import org.mifos.selfserviceapp.presenters.RecentTransactionsPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.RecentTransactionListAdapter;
import org.mifos.selfserviceapp.ui.views.RecentTransactionsView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.DividerItemDecoration;
import org.mifos.selfserviceapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwwajeet
 * @since 09/08/16
 */
public class RecentTransactionsFragment extends Fragment implements
        RecyclerItemClickListener.OnItemClickListener, RecentTransactionsView {

    @Inject
    RecentTransactionsPresenter mRecentTransactionsPresenter;
    RecentTransactionListAdapter recentTransactionsListAdapter;
    @BindView(R.id.rv_recent_transactions)
    RecyclerView rvRecentTransactions;
    @BindView(R.id.swipe_transaction_container)
    SwipeRefreshLayout swipeTransactionContainer;
    @BindView(R.id.ll_error)
    RelativeLayout ll_error;
    @BindView(R.id.tv_error_msg)
    TextView tv_error_msg;
    @BindView(R.id.iv_error)
    ImageView iv_error;
    private long clientId;
    private View rootView;
    private LinearLayoutManager layoutManager;
    private ProgressDialog progressDialog;
    private List<Transaction> recentTransactionList = new ArrayList<Transaction>();

    public static RecentTransactionsFragment newInstance(long clientId) {
        RecentTransactionsFragment recentTransactionsFragment = new RecentTransactionsFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.CLIENT_ID, clientId);
        recentTransactionsFragment.setArguments(args);
        return recentTransactionsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            clientId = getArguments().getLong(Constants.CLIENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recent_transactions, container, false);
        ButterKnife.bind(this, rootView);

        mRecentTransactionsPresenter.attachView(this);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvRecentTransactions.setLayoutManager(layoutManager);
        rvRecentTransactions.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvRecentTransactions.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), this));
        recentTransactionsListAdapter = new RecentTransactionListAdapter(getContext(),
                recentTransactionList);
        rvRecentTransactions.setAdapter(recentTransactionsListAdapter);

        swipeTransactionContainer.setColorSchemeResources(R.color.blue_light, R.color.green_light, R
                .color.orange_light, R.color.red_light);
        swipeTransactionContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecentTransactionsPresenter.loadRecentTransactions(clientId);
                setErrorView(false, null, 0);
            }
        });

        mRecentTransactionsPresenter.loadRecentTransactions(clientId);
        return rootView;
    }

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(getResources().getText(R.string.progress_message_loading));
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onItemClick(View childView, int position) {

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void showErrorFetchingRecentTransactions(String message) {
        setErrorView(true, message, R.drawable.ic_error_black_24dp);
        if (swipeTransactionContainer.isRefreshing()) {
            swipeTransactionContainer.setRefreshing(false);
        }
    }

    @Override
    public void showRecentTransactions(List<Transaction> recentTransactionList) {
        if (recentTransactionList.size() > 0) {
            this.recentTransactionList.clear();
            this.recentTransactionList.addAll(recentTransactionList);
            recentTransactionsListAdapter.notifyDataSetChanged();
        } else {
            setErrorView(true, getString(R.string.msg_no_transaction), R.drawable
                    .ic_assignment_turned_in_black_24dp);
        }

        if (swipeTransactionContainer.isRefreshing()) {
            swipeTransactionContainer.setRefreshing(false);
        }
    }

    @Override
    public void onDestroyView() {
        mRecentTransactionsPresenter.detachView();
        super.onDestroyView();
    }

    private void setErrorView(boolean flag, String message, int imageId) {
        if (flag) {
            ll_error.setVisibility(View.VISIBLE);
            tv_error_msg.setText(message);
            iv_error.setImageResource(imageId);
            recentTransactionList.clear();
            recentTransactionsListAdapter.notifyDataSetChanged();
        } else {
            ll_error.setVisibility(View.GONE);
        }
    }
}