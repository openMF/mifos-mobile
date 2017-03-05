package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanWithAssociations;
import org.mifos.selfserviceapp.presenters.LoanRepaymentSchedulePresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.LoanRepaymentScheduleAdapter;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.LoanRepaymentScheduleMvpView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.Toaster;

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

    @BindView(R.id.tv_loan_name)
    TextView tvLoanName;

    @BindView(R.id.tv_loan_type)
    TextView tvLoanType;

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
        loanRepaymentSchedulePresenter.loanLoanWithAssociations(loanId);

        return rootView;
    }

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

    @Override
    public void showLoanRepaymentSchedule(LoanWithAssociations loanWithAssociations) {
        loanRepaymentScheduleAdapter
                .setCurrency(loanWithAssociations.getCurrency().getDisplaySymbol());
        loanRepaymentScheduleAdapter
                .setPeriods(loanWithAssociations.getRepaymentSchedule().getPeriods());

        tvLoanName.setText(loanWithAssociations.getLoanProductName());
        tvLoanType.setText(loanWithAssociations.getLoanType().getValue());
    }

    @Override
    public void showEmptyRepaymentsSchedule(LoanWithAssociations loanWithAssociations) {
        tvLoanName.setText(loanWithAssociations.getLoanProductName());
        tvLoanType.setText(loanWithAssociations.getLoanType().getValue());
        layoutError.setVisibility(View.VISIBLE);
        tvStatus.setText(R.string.empty_repayment_schedule);
    }

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
