package org.mifos.selfserviceapp.ui.activities;
/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.presenters.LoanAccountsDetailPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.views.LoanAccountsDetailView;
import org.mifos.selfserviceapp.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vishwajeet
 * @since 19/08/16
 */

public class LoanAccountsDetailActivity extends BaseActivity implements LoanAccountsDetailView {

    @Inject
    LoanAccountsDetailPresenter mLoanAccountDetailsPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

    private long loanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.activity_loan_account_details);
        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getString(R.string.loan_account_details));
        }

        mLoanAccountDetailsPresenter.attachView(this);

        loanId = getIntent().getExtras().getLong(Constants.LOAN_ID);

        mLoanAccountDetailsPresenter.loadLoanAccountDetails(loanId);
        showBackButton();
    }

    @Override
    public void showLoanAccountsDetail(LoanAccount loanAccount) {
        //TODO: Calculate nextInstallment value
        tvOutstandingBalanceName.setText(getResources().getString(R.string.outstanding_balance_str,
                    loanAccount.getSummary().getCurrency().getDisplaySymbol(),
                    String.valueOf(loanAccount.getSummary().getTotalOutstanding())));
        tvNextInstallmentName.setText(String.valueOf(
                    loanAccount.getSummary().getTotalOutstanding()));
        tvDueDateName.setText(loanAccount.getSummary().getOverdueSinceDate());
        tvAccountNumberName.setText(loanAccount.getAccountNo());
        tvLoanTypeName.setText(loanAccount.getLoanType().getValue());
        tvCurrencyName.setText(loanAccount.getSummary().getCurrency().getCode());
    }

    @OnClick(R.id.btn_make_payment)
    public void onMakePaymentClicked() {
        Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_loan_summary)
    public void onLoanSummaryClicked() {
        Intent loanAccountIntent = new Intent(getApplicationContext(),
                LoanAccountContainerActivity.class);
        loanAccountIntent.putExtra(Constants.LOAN_ID , loanId);
        startActivity(loanAccountIntent);
    }

    @OnClick(R.id.btn_repayment_schedule)
    public void onRepaymentScheduleClicked() {
        Toast.makeText(getApplicationContext(), "clicked" , Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_transactions)
    public void onTransactionsClicked() {
        Toast.makeText(getApplicationContext(), "clicked" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorFetchingLoanAccountsDetail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        showProgressDialog(getResources().getString(R.string.progress_message_loading));
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoanAccountDetailsPresenter.detachView();
    }
}
