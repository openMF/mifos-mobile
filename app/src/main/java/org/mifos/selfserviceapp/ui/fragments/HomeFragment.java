package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccountsMetaData;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccountsMetaData;
import org.mifos.selfserviceapp.models.accounts.share.ShareAccountsMetaData;
import org.mifos.selfserviceapp.presenters.HomePresenter;
import org.mifos.selfserviceapp.ui.activities.HomeActivity;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.enums.AccountType;
import org.mifos.selfserviceapp.ui.enums.LoanState;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.HomeView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.PieChartUtils;
import org.mifos.selfserviceapp.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by michaelsosnick on 1/1/17.
 */

public class HomeFragment extends BaseFragment implements HomeView {

    public static final String LOG_TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.pc_loan_chart)
    PieChart pcLoansChart;

    @BindView(R.id.pc_savings_chart)
    PieChart pcSavingsChart;

    @BindView(R.id.pc_shares_chart)
    PieChart pcSharesChart;

    @BindView(R.id.tv_saving_total_amount)
    TextView tvSavingTotalAmount;

    @BindView(R.id.tv_loan_total_amount)
    TextView tvLoanTotalAmount;

    @BindView(R.id.tv_shares_approved)
    TextView tvSharesTotal;

    @Inject
    HomePresenter presenter;

    View rootView;
    private PieChartUtils loanChartUtils;
    private PieChartUtils savingChartUtils;
    private PieChartUtils shareChartUtils;
    private long clientId;

    public static HomeFragment newInstance(Long clientId) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientId = getArguments().getLong(Constants.CLIENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ((HomeActivity) getActivity()).getActivityComponent().inject(this);

        setToolbarTitle(getString(R.string.home));
        ButterKnife.bind(this, rootView);

        presenter.attachView(this);
        presenter.loadClientAccountDetails();

        showUserInterface();

        return rootView;
    }
    @Override
    public void showUserInterface() {
        pcLoansChart.setDrawHoleEnabled(false);
        pcLoansChart.setDrawEntryLabels(false);
        pcLoansChart.setTouchEnabled(false);
        pcLoansChart.getDescription().setEnabled(false);
        pcLoansChart.setExtraRightOffset(20);
        loanChartUtils = new PieChartUtils(getActivity(), pcLoansChart.getLegend());

        pcSavingsChart.setDrawHoleEnabled(false);
        pcSavingsChart.setDrawEntryLabels(false);
        pcSavingsChart.setTouchEnabled(false);
        pcSavingsChart.getDescription().setEnabled(false);
        pcSavingsChart.setExtraRightOffset(20);
        savingChartUtils = new PieChartUtils(getActivity(), pcSavingsChart.getLegend());

        pcSharesChart.setDrawHoleEnabled(false);
        pcSharesChart.setDrawEntryLabels(false);
        pcSharesChart.setTouchEnabled(false);
        pcSharesChart.getDescription().setEnabled(false);
        pcSharesChart.setExtraRightOffset(20);
        shareChartUtils = new PieChartUtils(getActivity(), pcSharesChart.getLegend());

    }

    @OnClick(R.id.btn_apply_for_loan)
    public void onApplicationClicked() {
        ((BaseActivity) getActivity()).replaceFragment(
                LoanApplicationFragment.newInstance(LoanState.CREATE), true, R.id.container);
    }

    @OnClick(R.id.btn_loan_repayment)
    public void onTransactionClicked() {

    }

    @OnClick(R.id.btn_quick_transfer)
    public void onQuickTransferClicked() {
        ((BaseActivity) getActivity()).replaceFragment(SavingsMakeTransferFragment.newInstance(1,
                Constants.TRANSFER_QUICK), true, R.id.container);
    }

    @OnClick(R.id.cv_saving_account)
    public void onSavingAccountClicked() {
        openAccount(AccountType.SAVINGS);
    }

    @OnClick(R.id.cv_loan_account)
    public void onLoanAccountClicked() {
        openAccount(AccountType.LOAN);
    }

    @OnClick(R.id.cv_share_account)
    public void onShareAccountClicked() {
        openAccount(AccountType.SHARE);
    }

    public void openAccount(AccountType accountType) {
        ((BaseActivity) getActivity()).replaceFragment(
                ClientAccountsFragment.newInstance(clientId, accountType), true, R.id.container);
    }

    @Override
    public void showLoanAccountDetails(LoanAccountsMetaData accountsMetaData) {

        loanChartUtils.addDataSet(accountsMetaData.getWaitingForDisbursal(),
                getString(R.string.waiting), ContextCompat.getColor(getActivity(), R.color.blue));
        loanChartUtils.addDataSet(accountsMetaData.getPendingApproval(), getString(R.string.pending)
                , ContextCompat.getColor(getActivity(), R.color.light_yellow));
        loanChartUtils.addDataSet(accountsMetaData.getActive(), getString(R.string.active),
                ContextCompat.getColor(getActivity(), R.color.deposit_green));
        loanChartUtils.addDataSet(accountsMetaData.getOverpaid(), getString(R.string.overpaid),
                ContextCompat.getColor(getActivity(), R.color.purple));
        loanChartUtils.addDataSet(accountsMetaData.getClosed(), getString(R.string.closed),
                ContextCompat.getColor(getActivity(), R.color.black));

        pcLoansChart.setData(loanChartUtils.getPieData());
        pcLoansChart.invalidate();

        tvLoanTotalAmount.setText(getString(R.string.string_and_double, getString(R.string.amount),
                accountsMetaData.getAmount()));
    }

    @Override
    public void showSavingAccountDetails(SavingAccountsMetaData accountsMetaData) {
        savingChartUtils.addDataSet(accountsMetaData.getPendingApproval(),
                getString(R.string.pending), ContextCompat.getColor(getActivity(),
                        R.color.light_yellow));
        savingChartUtils.addDataSet(accountsMetaData.getActive(), getString(R.string.active),
                ContextCompat.getColor(getActivity(), R.color.deposit_green));
        savingChartUtils.addDataSet(accountsMetaData.getApproved(), getString(R.string.approved),
                ContextCompat.getColor(getActivity(), R.color.light_green));
        savingChartUtils.addDataSet(accountsMetaData.getMatured(), getString(R.string.matured),
                ContextCompat.getColor(getActivity(), R.color.red));
        savingChartUtils.addDataSet(accountsMetaData.getClosed(), getString(R.string.closed),
                ContextCompat.getColor(getActivity(), R.color.black));

        pcSavingsChart.setData(savingChartUtils.getPieData());
        pcSavingsChart.invalidate();

        tvSavingTotalAmount.setText(getString(R.string.string_and_double, getString(R.string.amount)
                , accountsMetaData.getAmount()));
    }

    @Override
    public void showShareAccountDetails(ShareAccountsMetaData accountsMetaData) {
        shareChartUtils.addDataSet(accountsMetaData.getPendingApproval(),
                getString(R.string.pending), ContextCompat.getColor(getActivity(),
                        R.color.light_yellow));
        shareChartUtils.addDataSet(accountsMetaData.getActive(), getString(R.string.active),
                ContextCompat.getColor(getActivity(), R.color.deposit_green));
        shareChartUtils.addDataSet(accountsMetaData.getApproved(), getString(R.string.approved),
                ContextCompat.getColor(getActivity(), R.color.light_green));
        shareChartUtils.addDataSet(accountsMetaData.getRejected(), getString(R.string.rejected),
                ContextCompat.getColor(getActivity(), R.color.red));
        shareChartUtils.addDataSet(accountsMetaData.getClosed(), getString(R.string.closed),
                ContextCompat.getColor(getActivity(), R.color.black));

        pcSharesChart.setData(shareChartUtils.getPieData());
        pcSharesChart.invalidate();

        tvSharesTotal.setText(getString(R.string.string_and_int, getString(R.string.approved),
                accountsMetaData.getApprovedShares()));
    }

    @Override
    public void showError(String errorMessage) {
        Toaster.show(rootView, errorMessage);
    }

    @Override
    public void showProgress() {
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        hideProgressBar();
    }
}


