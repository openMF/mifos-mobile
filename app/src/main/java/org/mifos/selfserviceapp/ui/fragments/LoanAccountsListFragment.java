package org.mifos.selfserviceapp.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.adapters.LoanAccountsListAdapter;
import org.mifos.selfserviceapp.api.BaseApiManager;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.data.accounts.LoanAccount;
import org.mifos.selfserviceapp.presenters.LoanAccountsListPresenter;
import org.mifos.selfserviceapp.ui.views.LoanAccountsListView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 21/06/16
 */
public class LoanAccountsListFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener, LoanAccountsListView {

    @BindView(R.id.rv_loan_accounts_list)
    RecyclerView rvLoanAccounts;
    @BindView(R.id.swipe_loan_container)
    SwipeRefreshLayout swipeLoanContainer;

    public int clientId;
    private View rootView;
    LoanAccountsListPresenter mLoanAccountsListPresenter;
    private LinearLayoutManager linearLayoutManager;
    private ProgressDialog progress;
    private List<LoanAccount> loanAccountList = new ArrayList<>();
    private LoanAccountsListAdapter loanAccountsListAdapter;
    private BaseApiManager mBaseApiManager;
    private DataManager mDataManager;

    public static LoanAccountsListFragment newInstance(int clientId) {
        LoanAccountsListFragment fragment = new LoanAccountsListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            clientId = getArguments().getInt(Constants.CLIENT_ID);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_loan_accounts_list, container, false);
        ButterKnife.bind(this, rootView);
        mBaseApiManager = new BaseApiManager(getActivity());
        mDataManager = new DataManager(mBaseApiManager);
        mLoanAccountsListPresenter = new LoanAccountsListPresenter(mDataManager);
        mLoanAccountsListPresenter.attachView(this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvLoanAccounts.setLayoutManager(linearLayoutManager);
        rvLoanAccounts.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
        rvLoanAccounts.setHasFixedSize(true);

        swipeLoanContainer.setColorSchemeResources(R.color.blue_light, R.color.green_light, R
                .color.orange_light, R.color.red_light);
        swipeLoanContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchLoanAccountsList();
            }
        });
        fetchLoanAccountsList();
        return rootView;
    }

    private void fetchLoanAccountsList() {
        mLoanAccountsListPresenter.fetchLoanAccounts(clientId);
    }

    @Override
    public void onItemClick(View childView, int position) {

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void showLoanAccounts(Response<LoanAccount> response) {
        loanAccountList = response.body().getLoanAccounts();
        inflateLoanAccountsList();
        if (swipeLoanContainer.isRefreshing())
            swipeLoanContainer.setRefreshing(false);
    }

    @Override
    public void showErrorFetchingLoanAccounts(String string) {

    }

    private void inflateLoanAccountsList() {
        loanAccountsListAdapter = new LoanAccountsListAdapter(getContext(), loanAccountList);
        rvLoanAccounts.setAdapter(loanAccountsListAdapter);
    }

    @Override
    public void showProgress() {
        if (progress == null) {
            progress = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
        }
        progress.setMessage(getResources().getText(R.string.progress_message_loading));
        progress.show();
    }

    @Override
    public void hideProgress() {
        if (progress != null && progress.isShowing())
            progress.dismiss();
    }

}
