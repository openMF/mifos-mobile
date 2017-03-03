package org.mifos.selfserviceapp.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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



public class SavingsAccountFragment extends Fragment implements SavingAccountsDetailView {
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
    @Inject
    SavingAccountsDetailPresenter mSavingAccountsDetailPresenter;
    View view;
    long accountno;
    private ProgressDialog progressDialog;
    public static SavingsAccountFragment newinstance(long accno){
        SavingsAccountFragment ob=new SavingsAccountFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.ACCOUNT_ID, accno);
        ob.setArguments(args);
        return ob;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mSavingAccountsDetailPresenter.attachView(this);

        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            accountno = getArguments().getLong(Constants.ACCOUNT_ID);
        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_savingsaccount,container,false);
        ButterKnife.bind(this,view);
        mSavingAccountsDetailPresenter.attachView(this);
        mSavingAccountsDetailPresenter.loadSavingAccountDetails(accountno);
        return view;
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
    public  SavingsAccountFragment newInstance(long accountid)
    {
        SavingsAccountFragment ob=new SavingsAccountFragment();
        ob. mSavingAccountsDetailPresenter.attachView(this);
        ob.mSavingAccountsDetailPresenter.loadSavingAccountDetails(accountid);
        return ob;

    }

    @Override
    public void showErrorFetchingSavingAccountsDetail(String message) {

    }

    @Override
    public void showProgress() {


    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSavingAccountsDetailPresenter.detachView();
    }

}
