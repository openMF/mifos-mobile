package org.mifos.mobilebanking.ui.fragments;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.models.accounts.loan.LoanAccount;
import org.mifos.mobilebanking.presenters.LoanAccountsDetailPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.enums.AccountType;
import org.mifos.mobilebanking.ui.enums.ChargeType;
import org.mifos.mobilebanking.ui.enums.LoanState;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.LoanAccountsDetailView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.CurrencyUtil;
import org.mifos.mobilebanking.utils.DateHelper;
import org.mifos.mobilebanking.utils.QrCodeGenerator;
import org.mifos.mobilebanking.utils.Toaster;

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
    LoanAccountsDetailPresenter loanAccountDetailsPresenter;

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
        loanAccountDetailsPresenter.attachView(this);
        if (savedInstanceState == null) {
            loanAccountDetailsPresenter.loadLoanAccountDetails(loanId);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.LOAN_ACCOUNT, loanAccount);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            showLoanAccountsDetail((LoanAccount) savedInstanceState.
                    getParcelable(Constants.LOAN_ACCOUNT));
        }
    }


    /**
     * Shows details about loan account fetched from server is status is Active else shows and
     * error layout i.e. {@code layoutError} with a msg related to the status.
     * @param loanAccount object containing details of each loan account,
     */
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
            tvDueDateName.setText(R.string.not_available);
            showDetails(loanAccount);
        }

        getActivity().invalidateOptionsMenu();
    }

    /**
     * Sets basic information about a loan
     * @param loanAccount object containing details of each loan account,
     */
    public void showDetails(LoanAccount loanAccount) {
        //TODO: Calculate nextInstallment value
        tvOutstandingBalanceName.setText(getResources().getString(R.string.string_and_string,
                loanAccount.getSummary().getCurrency().getDisplaySymbol(), CurrencyUtil.
                formatCurrency(getActivity(), loanAccount.getSummary().getTotalOutstanding())));
        tvNextInstallmentName.setText(String.valueOf(
                loanAccount.getSummary().getTotalOutstanding()));
        tvAccountNumberName.setText(loanAccount.getAccountNo());
        tvLoanTypeName.setText(loanAccount.getLoanType().getValue());
        tvCurrencyName.setText(loanAccount.getSummary().getCurrency().getCode());
    }

    /**
     * Opens {@link SavingsMakeTransferFragment} to Make a payment for loan account with given
     * {@code loanId}
     */
    @OnClick(R.id.btn_make_payment)
    public void onMakePaymentClicked() {
        ((BaseActivity) getActivity()).replaceFragment(SavingsMakeTransferFragment
                .newInstance(loanId, Constants.TRANSFER_PAY_TO), true, R.id.container);
    }

    /**
     * Opens {@link LoanAccountSummaryFragment}
     */
    @OnClick(R.id.ll_summary)
    public void onLoanSummaryClicked() {
        ((BaseActivity) getActivity()).replaceFragment(LoanAccountSummaryFragment
                .newInstance(loanAccount), true, R.id.container);
    }

    /**
     * Opens {@link LoanRepaymentScheduleFragment}
     */
    @OnClick(R.id.ll_repayment)
    public void onRepaymentScheduleClicked() {
        ((BaseActivity) getActivity()).replaceFragment(LoanRepaymentScheduleFragment
                .newInstance(loanId), true, R.id.container);
    }

    /**
     * Opens {@link LoanAccountTransactionFragment}
     */
    @OnClick(R.id.ll_loan_transactions)
    public void onTransactionsClicked() {
        ((BaseActivity) getActivity()).replaceFragment(LoanAccountTransactionFragment
                .newInstance(loanId), true, R.id.container);
    }

    @OnClick(R.id.ll_loan_charges)
    public void chargesClicked() {
        ((BaseActivity) getActivity()).replaceFragment(ClientChargeFragment
                .newInstance(loanAccount.getId(), ChargeType.LOAN), true, R.id.container);
    }

    @OnClick(R.id.ll_loan_qr_code)
    public void onQrCodeClicked() {
        String accountDetailsInJson = QrCodeGenerator.getAccountDetailsInString(loanAccount.
                getAccountNo(), preferencesHelper.getOfficeName(), AccountType.LOAN);
        ((BaseActivity) getActivity()).replaceFragment(QrCodeDisplayFragment.
                newInstance(accountDetailsInJson), true, R.id.container);
    }

    /**
     * It is called whenever any error occurs while executing a request
     * @param message Error message that tells the user about the problem.
     */
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
        loanAccountDetailsPresenter.detachView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_loan_details, menu);
        if (showLoanUpdateOption) {
            menu.findItem(R.id.menu_update_loan).setVisible(true);
            menu.findItem(R.id.menu_withdraw_loan).setVisible(true);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
