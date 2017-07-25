package org.mifos.selfserviceapp.ui.fragments;
/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.local.PreferencesHelper;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.presenters.LoanAccountsDetailPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.enums.AccountType;
import org.mifos.selfserviceapp.ui.enums.ChargeType;
import org.mifos.selfserviceapp.ui.enums.LoanState;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.LoanAccountsDetailView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.DateHelper;
import org.mifos.selfserviceapp.utils.QrCodeGenerator;
import org.mifos.selfserviceapp.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vishwajeet
 * @since 19/08/16
 */

public class LoanAccountsDetailFragment extends BaseFragment implements LoanAccountsDetailView {

    @Inject
    LoanAccountsDetailPresenter mLoanAccountDetailsPresenter;

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

    @BindView(R.id.ll_account_detail)
    LinearLayout llAccountDetail;

    @BindView(R.id.ll_error)
    View layoutError;

    @BindView(R.id.tv_status)
    TextView tv_status;

    @BindView(R.id.btn_make_payment)
    Button btMakePayment;

    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;

    @Inject
    PreferencesHelper preferencesHelper;


    private LoanAccount loanAccount;
    private boolean showLoanUpdateOption = false;
    private long loanId;

    View rootView;

    public static LoanAccountsDetailFragment newInstance(long loanId) {
        LoanAccountsDetailFragment fragment = new LoanAccountsDetailFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.LOAN_ID, loanId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loanId = getArguments().getLong(Constants.LOAN_ID);
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);

        rootView = inflater.inflate(R.layout.fragment_loan_account_details, container, false);
        setToolbarTitle(getString(R.string.loan_account_details));

        ButterKnife.bind(this, rootView);
        mLoanAccountDetailsPresenter.attachView(this);
        mLoanAccountDetailsPresenter.loadLoanAccountDetails(loanId);

        return rootView;
    }

    @Override
    public void showLoanAccountsDetail(LoanAccount loanAccount) {
        llAccountDetail.setVisibility(View.VISIBLE);
        this.loanAccount = loanAccount;

        if (loanAccount.getStatus().getActive()) {
            tvDueDateName.setText(DateHelper.getDateAsString(loanAccount.getTimeline()
                    .getActualDisbursementDate()));
            showDetails(loanAccount);
        } else if (loanAccount.getStatus().getPendingApproval()) {
            tv_status.setText(R.string.approval_pending);
            llAccountDetail.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
            showLoanUpdateOption = true;
        } else if (loanAccount.getStatus().getWaitingForDisbursal()) {
            tv_status.setText(R.string.waiting_for_disburse);
            llAccountDetail.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
        } else {
            btMakePayment.setVisibility(View.GONE);
            showDetails(loanAccount);
        }

        String accountDetails = QrCodeGenerator.getAccountDetailsInString(loanAccount.
                getAccountNo(), preferencesHelper.getOfficeName(), AccountType.LOAN);
        ivQrCode.setImageBitmap(QrCodeGenerator.encodeAsBitmap(accountDetails));

        getActivity().invalidateOptionsMenu();
    }

    public void showDetails(LoanAccount loanAccount) {
        //TODO: Calculate nextInstallment value
        tvOutstandingBalanceName.setText(getResources()
                .getString(R.string.outstanding_balance_str,
                        loanAccount.getSummary().getCurrency().getDisplaySymbol(),
                        String.valueOf(loanAccount.getSummary().getTotalOutstanding())));
        tvNextInstallmentName.setText(String.valueOf(
                loanAccount.getSummary().getTotalOutstanding()));
        tvAccountNumberName.setText(loanAccount.getAccountNo());
        tvLoanTypeName.setText(loanAccount.getLoanType().getValue());
        tvCurrencyName.setText(loanAccount.getSummary().getCurrency().getCode());
    }

    @OnClick(R.id.btn_make_payment)
    public void onMakePaymentClicked() {
        ((BaseActivity) getActivity()).replaceFragment(SavingsMakeTransferFragment
                .newInstance(loanId, Constants.TRANSFER_PAY_TO), true, R.id.container);
    }

    @OnClick(R.id.btn_loan_summary)
    public void onLoanSummaryClicked() {
        ((BaseActivity) getActivity()).replaceFragment(LoanAccountSummaryFragment
                .newInstance(loanId), true, R.id.container);
    }

    @OnClick(R.id.btn_repayment_schedule)
    public void onRepaymentScheduleClicked() {
        ((BaseActivity) getActivity()).replaceFragment(LoanRepaymentScheduleFragment
                .newInstance(loanId), true, R.id.container);
    }

    @OnClick(R.id.btn_transactions)
    public void onTransactionsClicked() {
        ((BaseActivity) getActivity()).replaceFragment(LoanAccountTransactionFragment
                .newInstance(loanId), true, R.id.container);
    }

    @Override
    public void showErrorFetchingLoanAccountsDetail(String message) {
        llAccountDetail.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        tv_status.setText(message);
        Toaster.show(rootView, message);
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
        mLoanAccountDetailsPresenter.detachView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_loan_details, menu);
        if (showLoanUpdateOption) {
            menu.findItem(R.id.menu_update_loan).setVisible(true);
            menu.findItem(R.id.menu_withdraw_loan).setVisible(true);
        } else {
            menu.findItem(R.id.menu_loan_charges).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_update_loan) {
            ((BaseActivity) getActivity()).replaceFragment(LoanApplicationFragment
                    .newInstance(LoanState.UPDATE, loanAccount), true, R.id.container);
            return true;
        } else if (id == R.id.menu_withdraw_loan) {
            ((BaseActivity) getActivity()).replaceFragment(LoanAccountWithdrawFragment
                    .newInstance(loanAccount), true, R.id.container);
        } else if (id == R.id.menu_loan_charges) {
            ((BaseActivity) getActivity()).replaceFragment(ClientChargeFragment
                    .newInstance(loanAccount.getId(), ChargeType.LOAN), true, R.id.container);
        }
        return super.onOptionsItemSelected(item);
    }
}
