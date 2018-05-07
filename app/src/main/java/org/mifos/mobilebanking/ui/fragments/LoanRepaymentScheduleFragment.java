package org.mifos.mobilebanking.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.accounts.loan.LoanWithAssociations;
import org.mifos.mobilebanking.presenters.LoanRepaymentSchedulePresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.adapters.LoanRepaymentScheduleAdapter;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.LoanRepaymentScheduleMvpView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.DateHelper;
import org.mifos.mobilebanking.utils.Network;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @BindView(R.id.layout_error)
    View layoutError;

    @Inject
    LoanRepaymentSchedulePresenter loanRepaymentSchedulePresenter;

    @Inject
    LoanRepaymentScheduleAdapter loanRepaymentScheduleAdapter;

    SweetUIErrorHandler sweetUIErrorHandler;

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

        sweetUIErrorHandler = new SweetUIErrorHandler(getContext(), rootView);

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
        sweetUIErrorHandler.showSweetEmptyUI(getString(R.string.repayment_schedule),
                R.drawable.ic_charges, rvRepaymentSchedule, layoutError);
    }

    /**
     * It is called whenever any error occurs while executing a request
     * @param message Error message that tells the user about the problem.
     */
    @Override
    public void showError(String message) {
        if (!Network.isConnected(getActivity())) {
            sweetUIErrorHandler.showSweetNoInternetUI(rvRepaymentSchedule, layoutError);
        } else {
            sweetUIErrorHandler.showSweetErrorUI(message,
                    rvRepaymentSchedule, layoutError);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_try_again)
    public void retryClicked() {
        if (Network.isConnected(getContext())) {
            sweetUIErrorHandler.hideSweetErrorLayoutUI(rvRepaymentSchedule, layoutError);
            loanRepaymentSchedulePresenter.loanLoanWithAssociations(loanId);
        } else {
            Toast.makeText(getContext(), getString(R.string.internet_not_connected),
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgressBar();
        loanRepaymentSchedulePresenter.detachView();
    }
}
