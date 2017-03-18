package org.mifos.selfserviceapp.ui.fragments;
/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.presenters.LoanAccountsDetailPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.LoanAccountsDetailView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.DateHelper;
import org.mifos.selfserviceapp.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vishwajeet
 * @since 19/08/16
 */

public class LoanAccountsDetailFragment extends BaseFragment implements LoanAccountsDetailView {

    @Inject
    LoanAccountsDetailPresenter mLoanAccountDetailsPresenter;

    @BindView(R.id.tv_outstanding_balance)
    TextView tvOutstandingBalanceName;

    @BindView(R.id.tv_next_installment)
    TextView tvNextInstallmentName;

    @BindView(R.id.tv_due_date)
    TextView tvDueDateName;

    @BindView(R.id.tv_account_number)
    TextView tvAccountNumberName;

    @BindView(R.id.tv_loan_type)
    TextView tvLoanTypeName;

    @BindView(R.id.tv_currency)
    TextView tvCurrencyName;

    @BindView(R.id.ll_account_detail)
    LinearLayout llAccountDetail;

    @BindView(R.id.ll_error)
    View layoutError;

    @BindView(R.id.tv_status)
    TextView tv_status;

    private long loanId;

    View rootView;

    public static LoanAccountsDetailFragment newInstance(long loanId) {
        LoanAccountsDetailFragment fragment = new LoanAccountsDetailFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.LOAN_ID, loanId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loanId = getArguments().getLong(Constants.LOAN_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);

        rootView = inflater.inflate(R.layout.fragment_loan_account_details, container, false);
        setToolbarTitle(getString(R.string.loan_account_details));

        ButterKnife.bind(this, rootView);
        mLoanAccountDetailsPresenter.attachView(this);
        mLoanAccountDetailsPresenter.loadLoanAccountDetails(loanId);

        return rootView;
    }

    @Override
    public void showLoanAccountsDetail(LoanAccount loanAccount) {
        llAccountDetail.setVisibility(View.VISIBLE);

        if (loanAccount.getStatus().getActive()) {
            tvDueDateName.setText(DateHelper.getDateAsString(loanAccount.getTimeline()
                    .getActualDisbursementDate()));
            showDetails(loanAccount);
        }

        if (loanAccount.getStatus().getPendingApproval()) {
            tv_status.setText(R.string.approval_pending);
            llAccountDetail.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
        } else if (loanAccount.getStatus().getWaitingForDisbursal()) {
            tv_status.setText(R.string.waiting_for_disburse);
            llAccountDetail.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
        } else if (loanAccount.getStatus().getClosedObligationsMet()) {
            showDetails(loanAccount);
        } else {
            showDetails(loanAccount);
        }
    }

    public void showDetails(LoanAccount loanAccount) {
        //TODO: Calculate nextInstallment value
        tvOutstandingBalanceName.setText(getResources()
                .getString(R.string.outstanding_balance_str,
                        loanAccount.getSummary().getCurrency().getDisplaySymbol(),
                        String.valueOf(loanAccount.getSummary().getTotalOutstanding())));
        tvNextInstallmentName.setText(String.valueOf(
                loanAccount.getSummary().getTotalOutstanding()));
        tvAccountNumberName.setText(loanAccount.getAccountNo());
        tvLoanTypeName.setText(loanAccount.getLoanType().getValue());
        tvCurrencyName.setText(loanAccount.getSummary().getCurrency().getCode());
    }

    @OnClick(R.id.btn_make_payment)
    public void onMakePaymentClicked() {
        Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_loan_summary)
    public void onLoanSummaryClicked() {
        ((BaseActivity) getActivity()).replaceFragment(LoanAccountSummaryFragment
                .newInstance(loanId), true, R.id.container);
    }

    @OnClick(R.id.btn_repayment_schedule)
    public void onRepaymentScheduleClicked() {
        ((BaseActivity) getActivity()).replaceFragment(LoanRepaymentScheduleFragment
                .newInstance(loanId), true, R.id.container);
    }

    @OnClick(R.id.btn_transactions)
    public void onTransactionsClicked() {
        ((BaseActivity) getActivity()).replaceFragment(LoanAccountTransactionFragment
                .newInstance(loanId), true, R.id.container);
    }

    @Override
    public void showErrorFetchingLoanAccountsDetail(String message) {
        llAccountDetail.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        tv_status.setText(message);
        Toaster.show(rootView, message);
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
        mLoanAccountDetailsPresenter.detachView();
    }
}
