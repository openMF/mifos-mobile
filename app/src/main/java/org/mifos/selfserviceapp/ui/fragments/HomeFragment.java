package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingsWithAssociations;
import org.mifos.selfserviceapp.models.accounts.share.ShareAccount;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.presenters.HomePresenter;
import org.mifos.selfserviceapp.presenters.SavingAccountsDetailPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.HomeView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.widget.Button;

/**
 * Created by michaelsosnick on 1/1/17.
 */

public class HomeFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener, HomeView {

    public static final String LOG_TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.noAccountText)
    TextView noAccountText;

    @BindView(R.id.ll_error)
    LinearLayout ll_error;

    @BindView(R.id.noAccountIcon)
    ImageView noAccountIcon;

    @BindView(R.id.iv_profile_pic)
    ImageView iv_profile_pic;

    @Inject
    HomePresenter homePresenter;

    @Inject
    SavingAccountsDetailPresenter savingPresenter;

    View rootView;

    @BindView(R.id.tv_client_name)
    TextView tv_client_name;

    @BindView(R.id.tv_savings)
    TextView tv_savings;

    @BindView(R.id.tv_loans)
    TextView tv_loans;

    @BindView(R.id.tv_shares)
    TextView tv_shares;

    @BindView(R.id.btn_application)
    Button btn_application;

    @BindView(R.id.btn_savings)
    Button btn_savings;

    @BindView(R.id.btn_loans)
    Button btn_loans;

    @BindView(R.id.btn_shares)
    Button btn_shares;

    @BindView(R.id.btn_transaction)
    Button btn_transaction;

    @BindView(R.id.btn_transfer)
    Button btn_transfer;

    @BindView(R.id.btn_payment)
    Button btn_payment;

    private List<LoanAccount> loanAccounts;
    private List<SavingAccount> savingAccounts;
    private List<ShareAccount> shareAccounts;
    private double totalLoans;
    private long clientId;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        loanAccounts = new ArrayList<>();
        savingAccounts = new ArrayList<>();
        shareAccounts = new ArrayList<>();
        totalLoans = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, rootView);
        homePresenter.attachView(this);
        homePresenter.loadClient();
        homePresenter.loadInfo();
        this.clientId = homePresenter.getClientId();
        showProgress();

        return rootView;
    }

    @OnClick(R.id.noAccountIcon)
    void reloadOnError() {
        ll_error.setVisibility(View.GONE);
        homePresenter.loadInfo();
        homePresenter.loadClient();
    }

    @OnClick(R.id.btn_application)
    public void onApplicationClicked() {
        ((BaseActivity) getActivity()).replaceFragment(
                LoanApplicationFragment.newInstance(), false, R.id.container);
    }

    @OnClick(R.id.btn_transaction)
    public void onTransactionClicked() {
        ((BaseActivity) getActivity()).replaceFragment(
                RecentTransactionsFragment.newInstance(clientId), false, R.id.container);
    }

    @OnClick(R.id.btn_savings)
    public void onSavingsClicked() {
        ((BaseActivity) getActivity()).replaceFragment(
                ClientAccountsFragment.newInstance(clientId), false, R.id.container);
    }

    @OnClick(R.id.btn_loans)
    public void onLoansClicked() {
        ((BaseActivity) getActivity()).replaceFragment(
                ClientAccountsFragment.newInstance(clientId), false, R.id.container);
    }

    @OnClick(R.id.btn_shares)
    public void onSharesClicked() {
        ((BaseActivity) getActivity()).replaceFragment(
                ClientAccountsFragment.newInstance(clientId), false, R.id.container);
    }

    @Override
    public void onRefresh() {
        ll_error.setVisibility(View.GONE);
        homePresenter.loadInfo();
        homePresenter.loadClient();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void showClientInfo(Client client) {
        tv_client_name.setText("Hello, " + client.getFirstname());
        iv_profile_pic.setImageResource(R.drawable.ic_clients);
    }

    @Override
    public void showInfo(List<LoanAccount> loanAccounts,
                         List<SavingAccount> savingAccounts,
                         List<ShareAccount> shareAccounts) {

        this.savingAccounts = savingAccounts;
        this.loanAccounts = loanAccounts;
        this.shareAccounts = shareAccounts;

        showSavingTotal(savingAccounts);
        showShareTotal(shareAccounts);
        showLoanTotal(loanAccounts);

    }


    @Override
    public void showSavingTotal(List<SavingAccount> savingAccounts) {
        double total = 0;
        for (SavingAccount s: savingAccounts) {
            total += s.getAccountBalance();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        tv_savings.setText(getContext().getString(R.string.total)
                            + Double.parseDouble(df.format(total)));
    }

    @Override
    public void showShareTotal(List<ShareAccount> shareAccounts) {
        double total = 0;
        for (ShareAccount sh: shareAccounts) {
            total += sh.getTotalApprovedShares();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        tv_shares.setText(getContext().getString(R.string.total)
                            + Double.parseDouble(df.format(total)));
    }

    @Override
    public void showLoanTotal(final List<LoanAccount> loanAccounts) {
        for (final LoanAccount l : loanAccounts) {
            Thread t = new Thread () {
                public void run() {
                    homePresenter.loadLoanAccountDetails(l.getId());
                }
            };
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                continue;
            }
        }
    }

    @Override
    public void showLoanAccountsDetail(LoanAccount loanAccount) {
        if (loanAccount.getStatus().getActive()) {
            this.totalLoans += loanAccount.getSummary().getTotalOutstanding();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        tv_loans.setText(getContext().getString(R.string.total)
                            + Double.parseDouble(df.format(this.totalLoans)));
    }

    @Override
    public void showErrorFetchingLoanAccountsDetail(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSavingAccountsDetail(SavingsWithAssociations savingAccount) {
    }

    @Override
    public void showErrorFetchingSavingAccountsDetail(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homePresenter.detachView();
    }

}


