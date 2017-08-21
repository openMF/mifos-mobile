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
import android.widget.ImageView;
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
import org.mifos.selfserviceapp.utils.CurrencyUtil;
import org.mifos.selfserviceapp.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 25/2/17.
 */

public class LoanAccountSummaryFragment extends BaseFragment implements LoanAccountsDetailView {

    @BindView(R.id.tv_loan_product_name)
    TextView tvLoanProductName;

    @BindView(R.id.tv_principal)
    TextView tvPrincipalName;

    @BindView(R.id.tv_interest_charged)
    TextView tvInterestChargedName;

    @BindView(R.id.tv_fees)
    TextView tvFeesName;

    @BindView(R.id.tv_penalties)
    TextView tvPenaltiesName;

    @BindView(R.id.tv_total_repayment)
    TextView tvTotalRepaymentName;

    @BindView(R.id.tv_total_paid)
    TextView tvTotalPaidName;

    @BindView(R.id.tv_interest_waived)
    TextView tvInterestWaivedName;

    @BindView(R.id.tv_penalties_waived)
    TextView tvPenaltiesWaivedName;

    @BindView(R.id.tv_fees_waived)
    TextView tvFeesWaivedName;

    @BindView(R.id.tv_outstanding_balance)
    TextView tvOutstandingBalanceName;

    @BindView(R.id.ll_error)
    View layoutError;

    @BindView(R.id.tv_account_status)
    TextView tvAccountStatus;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.ll_loan_summary)
    LinearLayout llLoanSummary;

    @BindView(R.id.ll_loan_purpose)
    LinearLayout llLoanPurpose;

    @BindView(R.id.tv_loan_account_number)
    TextView tvLoanAccountNumber;

    @BindView(R.id.tv_loan_purpose)
    TextView tvLoanPurpose;

    @BindView(R.id.iv_account_status)
    ImageView ivAccountStatus;


    @Inject
    LoanAccountsDetailPresenter loanAccountsDetailPresenter;

    private long loanId;

    private View rootView;

    public static LoanAccountSummaryFragment newInstance(long loanId) {
        LoanAccountSummaryFragment loanAccountSummaryFragment = new LoanAccountSummaryFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.LOAN_ID, loanId);
        loanAccountSummaryFragment.setArguments(args);
        return loanAccountSummaryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        rootView = inflater.inflate(R.layout.fragment_loan_account_summary, container, false);
        setToolbarTitle(getString(R.string.loan_summary));

        ButterKnife.bind(this, rootView);
        loanAccountsDetailPresenter.attachView(this);

        loanAccountsDetailPresenter.loadLoanAccountDetails(loanId);

        return rootView;
    }

    /**
     * Sets basic information about a Loan Account
     * @param loanAccount object containing details of each loan account,
     */
    @Override
    public void showLoanAccountsDetail(LoanAccount loanAccount) {
        llLoanSummary.setVisibility(View.VISIBLE);
        tvLoanProductName.setText(loanAccount.getLoanProductName());
        tvPrincipalName.setText(CurrencyUtil.formatCurrency(getActivity(),
                loanAccount.getPrincipal()));
        tvInterestChargedName.setText(CurrencyUtil.formatCurrency(getActivity(), loanAccount.
                getSummary().getInterestCharged()));
        tvFeesName.setText(CurrencyUtil.formatCurrency(getActivity(), loanAccount.getSummary().
                getFeeChargesCharged()));
        tvPenaltiesName.setText(CurrencyUtil.formatCurrency(getActivity(), loanAccount.getSummary().
                getPenaltyChargesCharged()));
        tvTotalRepaymentName.setText(CurrencyUtil.formatCurrency(getActivity(), loanAccount.
                getSummary().getTotalExpectedRepayment()));
        tvTotalPaidName.setText(CurrencyUtil.formatCurrency(getActivity(), loanAccount.getSummary().
                getTotalRepayment()));
        tvInterestWaivedName.setText(CurrencyUtil.formatCurrency(getActivity(), loanAccount.
                getSummary().getInterestWaived()));
        tvPenaltiesWaivedName.setText(CurrencyUtil.formatCurrency(getActivity(), loanAccount.
                getSummary().getPenaltyChargesWaived()));
        tvFeesWaivedName.setText(CurrencyUtil.formatCurrency(getActivity(), loanAccount.getSummary()
                .getFeeChargesWaived()));
        tvOutstandingBalanceName.setText(getResources().getString(R.string.string_and_string,
                loanAccount.getSummary().getCurrency().getDisplaySymbol(), CurrencyUtil.
                formatCurrency(getActivity(), loanAccount.getSummary().getTotalOutstanding())));
        tvLoanAccountNumber.setText(loanAccount.getAccountNo());
        if (loanAccount.getLoanPurposeName() != null) {
            llLoanPurpose.setVisibility(View.VISIBLE);
            tvLoanPurpose.setText(loanAccount.getLoanPurposeName());
        }
        if (loanAccount.getStatus().getActive()) {
            tvAccountStatus.setText(R.string.active_uc);
            ivAccountStatus.setImageResource(R.drawable.ic_check_circle_green_24px
            );
        } else {
            tvAccountStatus.setText(R.string.inactive_uc);
            ivAccountStatus.setImageResource(R.drawable.ic_report_problem_red_24px);
        }

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
        loanAccountsDetailPresenter.detachView();
    }


}
