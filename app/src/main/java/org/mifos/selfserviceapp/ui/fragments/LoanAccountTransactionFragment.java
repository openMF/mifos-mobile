package org.mifos.selfserviceapp.ui.fragments;
/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanWithAssociations;
import org.mifos.selfserviceapp.presenters.LoanAccountsTransactionPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.RecentTransactionListAdapter;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.LoanAccountsTransactionView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 4/3/17.
 */

public class LoanAccountTransactionFragment extends BaseFragment
        implements LoanAccountsTransactionView {

    @BindView(R.id.ll_error)
    View layoutError;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.ll_loan_account_trans)
    LinearLayout llLoanAccountTrans;

    @BindView(R.id.tv_loan_product_name)
    TextView tvLoanProductName;

    @BindView(R.id.rv_loan_transactions)
    RecyclerView rvLoanTransactions;

    @Inject
    RecentTransactionListAdapter transactionsListAdapter;

    @Inject
    LoanAccountsTransactionPresenter loanAccountsTransactionPresenter;

    private long loanId;
    private View rootView;
    private LoanWithAssociations loanWithAssociations;

    public static LoanAccountTransactionFragment newInstance(long loanId) {
        LoanAccountTransactionFragment fragment = new LoanAccountTransactionFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.LOAN_ID, loanId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            loanId = getArguments().getLong(Constants.LOAN_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_loan_account_transactions, container, false);
        setToolbarTitle(getString(R.string.transactions));

        ButterKnife.bind(this, rootView);
        loanAccountsTransactionPresenter.attachView(this);

        showUserInterface();
        if (savedInstanceState == null) {
            loanAccountsTransactionPresenter.loadLoanAccountDetails(loanId);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.LOAN_ACCOUNT, loanWithAssociations);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            showLoanTransactions((LoanWithAssociations) savedInstanceState.
                    getParcelable(Constants.LOAN_ACCOUNT));
        }
    }

    /**
     * Initialized {@link RecyclerView} {@code rvLoanTransactions}
     */
    @Override
    public void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvLoanTransactions.setHasFixedSize(true);
        rvLoanTransactions.setLayoutManager(layoutManager);
        rvLoanTransactions.setAdapter(transactionsListAdapter);
    }

    /**
     * Fetches {@code loanWithAssociations} from server and intializes it in
     * {@code transactionsListAdapter}
     * @param loanWithAssociations object containing details about a Loan Account with Associations
     */
    @Override
    public void showLoanTransactions(LoanWithAssociations loanWithAssociations) {
        this.loanWithAssociations = loanWithAssociations;
        llLoanAccountTrans.setVisibility(View.VISIBLE);
        tvLoanProductName.setText(loanWithAssociations.getLoanProductName());
        transactionsListAdapter.setTransactions(loanWithAssociations.getTransactions());
    }

    /**
     * Sets a {@link TextView} with a msg if Transactions list is empty
     * @param loanWithAssociations
     */
    @Override
    public void showEmptyTransactions(LoanWithAssociations loanWithAssociations) {
        layoutError.setVisibility(View.VISIBLE);
        tvStatus.setText(R.string.empty_transactions);
    }

    /**
     * It is called whenever any error occurs while executing a request
     * @param message Error message that tells the user about the problem.
     */
    @Override
    public void showErrorFetchingLoanAccountsDetail(String message) {
        Toaster.show(rootView, message, Toast.LENGTH_SHORT);
        layoutError.setVisibility(View.VISIBLE);
        tvStatus.setText(message);
    }

    @Override
    public void showProgress() {
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        hideProgressBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgressBar();
        loanAccountsTransactionPresenter.detachView();
    }
}
