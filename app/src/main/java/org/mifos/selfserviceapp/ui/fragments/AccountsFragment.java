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
import org.mifos.selfserviceapp.models.CheckboxStatus;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.models.accounts.share.ShareAccount;
import org.mifos.selfserviceapp.presenters.AccountsPresenter;
import org.mifos.selfserviceapp.ui.activities.LoanAccountContainerActivity;
import org.mifos.selfserviceapp.ui.activities.SavingsAccountContainerActivity;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.LoanAccountsListAdapter;
import org.mifos.selfserviceapp.ui.adapters.SavingAccountsListAdapter;
import org.mifos.selfserviceapp.ui.adapters.ShareAccountsListAdapter;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.AccountsView;
import org.mifos.selfserviceapp.utils.ComparatorBasedOnId;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.DividerItemDecoration;
import org.mifos.selfserviceapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
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

    @Inject
    LoanAccountsListAdapter loanAccountsListAdapter;

    @Inject
    SavingAccountsListAdapter savingAccountsListAdapter;

    @Inject
    ShareAccountsListAdapter shareAccountsListAdapter;

    View rootView;
    private String accountType;
    private List<LoanAccount> loanAccounts;
    private List<SavingAccount> savingAccounts;
    private List<ShareAccount> shareAccounts;

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
        shareAccounts = new ArrayList<>();
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
        rvAccounts.addItemDecoration(new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation()));
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
        Collections.sort(loanAccounts, new ComparatorBasedOnId());
        this.loanAccounts = loanAccounts;
        if (loanAccounts.size() != 0) {
            loanAccountsListAdapter.setLoanAccountsList(loanAccounts);
            rvAccounts.setAdapter(loanAccountsListAdapter);
        } else {
            showEmptyAccounts(getString(R.string.empty_loan_accounts));
        }
    }

    @Override
    public void showSavingsAccounts(List<SavingAccount> savingAccounts) {
        Collections.sort(savingAccounts, new ComparatorBasedOnId());
        this.savingAccounts = savingAccounts;
        if (savingAccounts.size() != 0) {
            savingAccountsListAdapter.setSavingAccountsList(savingAccounts);
            rvAccounts.setAdapter(savingAccountsListAdapter);
        } else {
            showEmptyAccounts(getString(R.string.empty_savings_accounts));
        }
    }

    @Override
    public void showShareAccounts(List<ShareAccount> shareAccounts) {
        Collections.sort(shareAccounts, new ComparatorBasedOnId());
        this.shareAccounts = shareAccounts;
        if (shareAccounts.size() != 0) {
            shareAccountsListAdapter.setShareAccountsList(shareAccounts);
            rvAccounts.setAdapter(shareAccountsListAdapter);
        } else {
            showEmptyAccounts(getString(R.string.empty_share_accounts));
        }
    }


    public void showEmptyAccounts(String emptyAccounts) {
        ll_error.setVisibility(View.VISIBLE);
        noAccountText.setText(emptyAccounts);
        noAccountIcon.setImageResource(R.drawable.ic_assignment_turned_in_black_24dp);
        rvAccounts.setVisibility(View.GONE);
    }

    public void searchSavingsAccount(String input) {
        savingAccountsListAdapter.setSavingAccountsList(accountsPresenter.
                searchInSavingsList(savingAccounts, input));
    }

    public void searchLoanAccount(String input) {
        loanAccountsListAdapter.setLoanAccountsList(accountsPresenter.
                searchInLoanList(loanAccounts, input));
    }

    public void searchSharesAccount(String input) {
        shareAccountsListAdapter.setShareAccountsList(accountsPresenter.
                searchInSharesList(shareAccounts, input));
    }

    public void filterSavingsAccount(List<CheckboxStatus> statusModelList) {
        List<SavingAccount> filteredSavings = new ArrayList<>();
        for (CheckboxStatus status : accountsPresenter.getCheckedStatus(statusModelList)) {
            filteredSavings.addAll(accountsPresenter.getFilteredSavingsAccount(savingAccounts,
                    status));
        }
        savingAccountsListAdapter.setSavingAccountsList(filteredSavings);
    }

    public void filterLoanAccount(List<CheckboxStatus> statusModelList) {
        List<LoanAccount> filteredSavings = new ArrayList<>();
        for (CheckboxStatus status : accountsPresenter.getCheckedStatus(statusModelList)) {
            filteredSavings.addAll(accountsPresenter.getFilteredLoanAccount(loanAccounts,
                    status));
        }
        loanAccountsListAdapter.setLoanAccountsList(filteredSavings);
    }

    public void filterShareAccount(List<CheckboxStatus> statusModelList) {
        List<ShareAccount> filteredSavings = new ArrayList<>();
        for (CheckboxStatus status : accountsPresenter.getCheckedStatus(statusModelList)) {
            filteredSavings.addAll(accountsPresenter.getFilteredShareAccount(shareAccounts,
                    status));
        }
        shareAccountsListAdapter.setShareAccountsList(filteredSavings);
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
                intent = new Intent(getActivity(), SavingsAccountContainerActivity.class);
                intent.putExtra(Constants.SAVINGS_ID, savingAccountsListAdapter.
                        getSavingAccountsList().get(position).getId());
                break;
            case Constants.LOAN_ACCOUNTS:
                intent = new Intent(getActivity(), LoanAccountContainerActivity.class);
                intent.putExtra(Constants.LOAN_ID, loanAccountsListAdapter.getLoanAccountsList().
                        get(position).getId());
                break;
        }
        openActivity(intent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    /**
     * This function opens up an activity only if the intent
     * is not null.
     *
     * This will prevent the application from crashing if the
     * intent is null.
     * @param intent
     */
    private void openActivity(Intent intent) {
        if (intent != null) {
            startActivity(intent);
        }
    }
}
