package org.mifos.selfserviceapp.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.presenters.SavingAccountsDetailPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.views.SavingAccountsDetailView;
import org.mifos.selfserviceapp.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 18/8/16.
 */

public class SavingAccountsDetailActivity extends BaseActivity implements SavingAccountsDetailView {

    @Inject
    SavingAccountsDetailPresenter mSavingAccountsDetailPresenter;
    @BindView(R.id.tv_clientName)
    TextView tvClientName;
    @BindView(R.id.tv_client_name)
    TextView tvClientNameValue;
    @BindView(R.id.tv_savingProductName)
    TextView tvSavingProductName;
    @BindView(R.id.tv_savings_product_name)
    TextView tvSavingProductNameValue;
    @BindView(R.id.tv_minRequiredBalance)
    TextView tvMiniRequiredBalance;
    @BindView(R.id.tv_min_req_bal)
    TextView tvMiniRequiredBalanceValue;
    @BindView(R.id.tv_accountBalance)
    TextView tvAccountBalance;
    @BindView(R.id.tv_account_balance)
    TextView tvAccountBalanceValue;
    @BindView(R.id.tv_savingAccountNumber)
    TextView tvSavingAccountNumber;
    @BindView(R.id.tv_saving_account_number)
    TextView tvSavingAccountNumberValue;
    @BindView(R.id.tv_nominalInterestRate)
    TextView tvNominalInterestRate;
    @BindView(R.id.tv_nominal_interest_rate)
    TextView tvNominalInterestRateValue;
    @BindView(R.id.tv_totalDeposits)
    TextView tvTotalDeposits;
    @BindView(R.id.tv_total_deposits)
    TextView tvTotalDepositsValue;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private long accountId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.activity_saving_account_details);
        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getString(R.string.saving_account_details));
        }

        mSavingAccountsDetailPresenter.attachView(this);

        accountId = getIntent().getExtras().getLong(Constants.ACCOUNT_ID);

        mSavingAccountsDetailPresenter.loadSavingAccountDetails(accountId);
        showBackButton();
    }

    @Override
    public void showSavingAccountsDetail(SavingAccount savingAccount) {
        tvClientNameValue.setText(savingAccount.getClientName());
        tvMiniRequiredBalanceValue.setText(String.valueOf(savingAccount.getMinRequiredBalance()));
        tvSavingProductNameValue.setText(savingAccount.getSavingsProductName());
        tvAccountBalanceValue.setText(String.valueOf(savingAccount.getAccountBalance()));
        tvNominalInterestRateValue.setText(
                String.valueOf(savingAccount.getNominalAnnualInterestRate()));
        tvSavingAccountNumberValue.setText(String.valueOf(savingAccount.getAccountNo()));
        tvTotalDepositsValue.setText(String.valueOf(savingAccount.getTotalDeposits()));
    }

    @Override
    public void showErrorFetchingSavingAccountsDetail(String message) {

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
        mSavingAccountsDetailPresenter.detachView();
        super.onDestroy();
    }
}
