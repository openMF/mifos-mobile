package org.mifos.selfserviceapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.presenters.AccountsPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.activities.LoanAccountsDetailActivity;
import org.mifos.selfserviceapp.ui.activities.SavingAccountsDetailActivity;
import org.mifos.selfserviceapp.ui.adapters.LoanAccountsListAdapter;
import org.mifos.selfserviceapp.ui.adapters.SavingAccountsListAdapter;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.AccountsView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rajan Maurya on 23/10/16.
 */

public class AccountsFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener, AccountsView,
        RecyclerItemClickListener.OnItemClickListener {

    public static final String LOG_TAG = AccountsFragment.class.getSimpleName();

    @BindView(R.id.rv_accounts)
    RecyclerView rvAccounts;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.noAccountText)
    TextView noAccountText;

    @BindView(R.id.ll_error)
    LinearLayout ll_error;

    @BindView(R.id.noAccountIcon)
    ImageView noAccountIcon;

    @Inject
    AccountsPresenter accountsPresenter;

    View rootView;

    private String accountType;
    private List<LoanAccount> loanAccounts;
    private List<SavingAccount> savingAccounts;

    public static AccountsFragment newInstance(String accountType) {
        AccountsFragment fragment = new AccountsFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ACCOUNT_TYPE, accountType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        loanAccounts = new ArrayList<>();
        savingAccounts = new ArrayList<>();
        if (getArguments() != null) {
            accountType = getArguments().getString(Constants.ACCOUNT_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_accounts, container, false);

        ButterKnife.bind(this, rootView);
        accountsPresenter.attachView(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAccounts.setLayoutManager(layoutManager);
        rvAccounts.setHasFixedSize(true);
        rvAccounts.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
        swipeRefreshLayout.setColorSchemeColors(getActivity()
                .getResources().getIntArray(R.array.swipeRefreshColors));
        swipeRefreshLayout.setOnRefreshListener(this);
        showProgress();

        return rootView;
    }

    @OnClick(R.id.noAccountIcon)
    void reloadOnError() {
        ll_error.setVisibility(View.GONE);
        rvAccounts.setVisibility(View.VISIBLE);
        accountsPresenter.loadAccounts(accountType);
    }

    @Override
    public void onRefresh() {
        ll_error.setVisibility(View.GONE);
        rvAccounts.setVisibility(View.VISIBLE);
        accountsPresenter.loadAccounts(accountType);
    }

    @Override
    public void showLoanAccounts(List<LoanAccount> loanAccounts) {
        this.loanAccounts = loanAccounts;
        if (loanAccounts.size() != 0) {
            LoanAccountsListAdapter loanAccountsListAdapter =
                    new LoanAccountsListAdapter(getContext(), loanAccounts);
            rvAccounts.setAdapter(loanAccountsListAdapter);
        } else {
            showEmptyAccounts(getString(R.string.empty_loan_accounts));
        }
    }

    @Override
    public void showSavingsAccounts(List<SavingAccount> savingAccounts) {
        this.savingAccounts = savingAccounts;
        if (savingAccounts.size() != 0) {
            SavingAccountsListAdapter savingAccountsListAdapter =
                    new SavingAccountsListAdapter(getContext(), savingAccounts);
            rvAccounts.setAdapter(savingAccountsListAdapter);
        } else {
            showEmptyAccounts(getString(R.string.empty_savings_accounts));
        }
    }

    public void showEmptyAccounts(String emptyAccounts) {
        ll_error.setVisibility(View.VISIBLE);
        noAccountText.setText(emptyAccounts);
        noAccountIcon.setImageResource(R.drawable.ic_assignment_turned_in_black_24dp);
        rvAccounts.setVisibility(View.GONE);
    }

    @Override
    public void showError(String errorMessage) {
        ll_error.setVisibility(View.VISIBLE);
        rvAccounts.setVisibility(View.GONE);
        noAccountText.setText(errorMessage);
        noAccountIcon.setImageResource(R.drawable.ic_error_black_24dp);
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        showSwipeRefreshLayout(true);
    }

    @Override
    public void hideProgress() {
        showSwipeRefreshLayout(false);
    }

    public void showSwipeRefreshLayout(final boolean show) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(show);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        accountsPresenter.detachView();
    }

    @Override
    public void onItemClick(View childView, int position) {
        Intent intent = null;
        switch (accountType) {
            case Constants.SAVINGS_ACCOUNTS:
                intent = new Intent(getActivity(), SavingAccountsDetailActivity.class);
                intent.putExtra(Constants.ACCOUNT_ID, savingAccounts.get(position).getId());
                break;
            case Constants.LOAN_ACCOUNTS:
                intent = new Intent(getActivity(), LoanAccountsDetailActivity.class);
                intent.putExtra(Constants.LOAN_ID, loanAccounts.get(position).getId());
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}
