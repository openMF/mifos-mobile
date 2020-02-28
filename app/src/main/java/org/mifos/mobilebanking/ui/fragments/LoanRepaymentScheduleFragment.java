package org.mifos.mobilebanking.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.accounts.loan.LoanWithAssociations;
import org.mifos.mobilebanking.presenters.LoanRepaymentSchedulePresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.adapters.LoanRepaymentScheduleAdapter;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.LoanRepaymentScheduleMvpView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.DateHelper;
import org.mifos.mobilebanking.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rajan Maurya on 03/03/17.
 */
public class LoanRepaymentScheduleFragment extends BaseFragment implements
        LoanRepaymentScheduleMvpView {

    @BindView(R.id.rv_repayment_schedule)
    RecyclerView rvRepaymentSchedule;

    @BindView(R.id.tv_account_number)
    TextView tvAccountNumber;

    @BindView(R.id.tv_disbursement_date)
    TextView tvDisbursementDate;

    @BindView(R.id.tv_number_of_payments)
    TextView tvNumberOfPayments;

    @BindView(R.id.iv_status)
    ImageView ivStatus;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.ll_error)
    View layoutError;

    @Inject
    LoanRepaymentSchedulePresenter loanRepaymentSchedulePresenter;

    @Inject
    LoanRepaymentScheduleAdapter loanRepaymentScheduleAdapter;

    View rootView;
    private long loanId;
    private LoanWithAssociations loanWithAssociations;

    public static LoanRepaymentScheduleFragment newInstance(long loanId) {
        LoanRepaymentScheduleFragment loanRepaymentScheduleFragment =
                new LoanRepaymentScheduleFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.LOAN_ID, loanId);
        loanRepaymentScheduleFragment.setArguments(args);
        return loanRepaymentScheduleFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        setToolbarTitle(getString(R.string.loan_repayment_schedule));
        if (getArguments() != null) {
            loanId = getArguments().getLong(Constants.LOAN_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_loan_repayment_schedule, container, false);
        ButterKnife.bind(this, rootView);
        loanRepaymentSchedulePresenter.attachView(this);

        showUserInterface();
        if (savedInstanceState == null) {
            loanRepaymentSchedulePresenter.loanLoanWithAssociations(loanId);
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
            showLoanRepaymentSchedule((LoanWithAssociations) savedInstanceState.
                    getParcelable(Constants.LOAN_ACCOUNT));
        }
    }

    /**
     * Initializes the layout
     */
    @Override
    public void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRepaymentSchedule.setLayoutManager(layoutManager);
        rvRepaymentSchedule.setHasFixedSize(true);
        loanRepaymentScheduleAdapter.setContext(getActivity());
        rvRepaymentSchedule.setAdapter(loanRepaymentScheduleAdapter);
    }

    @Override
    public void showProgress() {
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        hideProgressBar();
    }

    /**
     * Fetches {@link LoanWithAssociations} for a loan with {@code loanId}
     * @param loanWithAssociations Contains details about Repayment Schedule
     */
    @Override
    public void showLoanRepaymentSchedule(LoanWithAssociations loanWithAssociations) {
        this.loanWithAssociations = loanWithAssociations;
        loanRepaymentScheduleAdapter
                .setCurrency(loanWithAssociations.getCurrency().getDisplaySymbol());
        loanRepaymentScheduleAdapter
                .setPeriods(loanWithAssociations.getRepaymentSchedule().getPeriods());

        tvAccountNumber.setText(loanWithAssociations.getAccountNo());
        tvDisbursementDate.setText(DateHelper.getDateAsString(loanWithAssociations.
                getTimeline().getExpectedDisbursementDate()));
        tvNumberOfPayments.setText(String.
                valueOf(loanWithAssociations.getNumberOfRepayments()));
    }

    /**
     * Shows an empty layout for a loan with {@code loanId} which has no Repayment Schedule
     * @param loanWithAssociations Contains details about Repayment Schedule
     */
    @Override
    public void showEmptyRepaymentsSchedule(LoanWithAssociations loanWithAssociations) {
        tvAccountNumber.setText(loanWithAssociations.getAccountNo());
        tvDisbursementDate.setText(DateHelper.getDateAsString(loanWithAssociations.
                getTimeline().getExpectedDisbursementDate()));
        tvNumberOfPayments.setText(String.
                valueOf(loanWithAssociations.getNumberOfRepayments()));
        layoutError.setVisibility(View.VISIBLE);
        tvStatus.setText(R.string.empty_repayment_schedule);
    }

    /**
     * It is called whenever any error occurs while executing a request
     * @param message Error message that tells the user about the problem.
     */
    @Override
    public void showError(String message) {
        Toaster.show(rootView, message);
        layoutError.setVisibility(View.VISIBLE);
        tvStatus.setText(message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgressBar();
        loanRepaymentSchedulePresenter.detachView();
    }
}
