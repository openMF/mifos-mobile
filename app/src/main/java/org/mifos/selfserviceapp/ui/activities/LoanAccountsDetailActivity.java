package org.mifos.selfserviceapp.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.LoanAccount;
import org.mifos.selfserviceapp.presenters.LoanAccountsDetailPresenter;
import org.mifos.selfserviceapp.ui.views.LoanAccountsDetailView;
import org.mifos.selfserviceapp.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 19/08/16
 */

public class LoanAccountsDetailActivity extends BaseActivity implements LoanAccountsDetailView {

    @Inject
    LoanAccountsDetailPresenter mLoanAccountDetailsPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_clientName)
    TextView tvClientName;
    @BindView(R.id.tv_client_name)
    TextView tvClientNameValue;
    @BindView(R.id.tv_accountNumber)
    TextView tvAccountNumber;
    @BindView(R.id.tv_account_number)
    TextView tvAccountNumberValue;
    @BindView(R.id.tv_principal)
    TextView tvPrincipal;
    @BindView(R.id.tv_principal_value)
    TextView tvPrincipalValue;
    @BindView(R.id.tv_loanProductName)
    TextView tvLoanProductName;
    @BindView(R.id.tv_loan_product_name)
    TextView tvLoanProductNameValue;
    @BindView(R.id.tv_principalDisbursed)
    TextView tvPrincipalDisbursed;
    @BindView(R.id.tv_principal_disbursed)
    TextView tvPrincipalDisbursedValue;
    @BindView(R.id.tv_annualInterestRate)
    TextView tvAnnualInterestRate;
    @BindView(R.id.tv_annual_interest_rate)
    TextView tvAnnualInterestRateValue;
    @BindView(R.id.tv_interestCharged)
    TextView tvInterestCharged;
    @BindView(R.id.tv_interest_charged)
    TextView tvInterestChargedValue;
    @BindView(R.id.tv_interestPaid)
    TextView tvInterestPaid;
    @BindView(R.id.tv_interest_paid)
    TextView tvInterestPaidValue;
    @BindView(R.id.tv_loanProductDescription)
    TextView tvLoanProductDescription;
    @BindView(R.id.tv_loan_product_description)
    TextView tvLoanProductDescriptionValue;
    private long loanId;
    private ProgressDialog progressDialog;

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
        tvAccountNumberValue.setText(String.valueOf(loanAccount.getAccountNo()));
        tvLoanProductNameValue.setText(loanAccount.getLoanProductName());
        tvLoanProductDescriptionValue.setText(loanAccount.getLoanProductDescription());
        tvAnnualInterestRateValue.setText(String.valueOf(loanAccount.getAnnualInterestRate()));
        tvClientNameValue.setText(loanAccount.getClientName());
        tvPrincipalValue.setText(String.valueOf(loanAccount.getPrincipal()));
        tvPrincipalDisbursedValue.setText(
                String.valueOf(loanAccount.getSummary().getPrincipalDisbursed()));
        tvInterestChargedValue.setText(
                String.valueOf(loanAccount.getSummary().getInterestCharged()));
        tvInterestPaidValue.setText(String.valueOf(loanAccount.getSummary().getInterestPaid()));
    }

    @Override
    public void showErrorFetchingLoanAccountsDetail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(getResources().getText(R.string.progress_message_loading));
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        mLoanAccountDetailsPresenter.detachView();
        super.onDestroy();
    }
}
