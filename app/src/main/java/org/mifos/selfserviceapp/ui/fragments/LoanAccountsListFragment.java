package org.mifos.selfserviceapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.LoanAccount;
import org.mifos.selfserviceapp.presenters.LoanAccountsListPresenter;
import org.mifos.selfserviceapp.ui.activities.BaseActivity;
import org.mifos.selfserviceapp.ui.activities.LoanAccountsDetailActivity;
import org.mifos.selfserviceapp.ui.adapters.LoanAccountsListAdapter;
import org.mifos.selfserviceapp.ui.views.LoanAccountsListView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 30/07/16.
 */

public class LoanAccountsListFragment extends Fragment implements
        RecyclerItemClickListener.OnItemClickListener, LoanAccountsListView {

    @Inject
    LoanAccountsListPresenter mLoanAccountsListPresenter;
    @BindView(R.id.rv_loan_accounts_list)
    RecyclerView rvLoanAccountsList;
    @BindView(R.id.swipe_loan_container)
    SwipeRefreshLayout swipeLoanContainer;
    private View rootView;
    private LinearLayoutManager layoutManager;
    private long clientId;
    private List<LoanAccount> loanAccountsList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private LoanAccountsListAdapter loanAccountsListAdapter;

    public static LoanAccountsListFragment newInstance(long clientId) {
        LoanAccountsListFragment fragment = new LoanAccountsListFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            clientId = getArguments().getLong(Constants.CLIENT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_loan_accounts_list, container, false);
        ButterKnife.bind(this, rootView);

        mLoanAccountsListPresenter.attachView(this);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvLoanAccountsList.setLayoutManager(layoutManager);
        rvLoanAccountsList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), this));
        //  rvLoanAccountsList.setHasFixedSize(true);

        swipeLoanContainer.setColorSchemeResources(R.color.blue_light, R.color.green_light, R
                .color.orange_light, R.color.red_light);
        swipeLoanContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoanAccountsListPresenter.loadLoanAccountsList(clientId);
            }
        });

        mLoanAccountsListPresenter.loadLoanAccountsList(clientId);
        return rootView;
    }

    @Override
    public void onItemClick(View childView, int position) {
        Intent intent = new Intent(getActivity(), LoanAccountsDetailActivity.class);
        intent.putExtra(Constants.LOAN_ID, loanAccountsList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

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
    public void showErrorFetchingLoanAccounts(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoanAccounts(List<LoanAccount> loanAccountsList) {
        this.loanAccountsList = loanAccountsList;
        inflateLoanAccountsList();
        if (swipeLoanContainer.isRefreshing()) {
            swipeLoanContainer.setRefreshing(false);
        }
    }

    private void inflateLoanAccountsList() {
        loanAccountsListAdapter = new LoanAccountsListAdapter(getContext(), loanAccountsList);
        rvLoanAccountsList.setAdapter(loanAccountsListAdapter);
    }

    @Override
    public void onDestroyView() {
        mLoanAccountsListPresenter.detachView();
        super.onDestroyView();
    }
}