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
import org.mifos.mobilebanking.models.accounts.loan.LoanWithAssociations;
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

import java.util.Calendar;
import java.util.List;

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


    private LoanWithAssociations loanWithAssociations;
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

        rootView = inflater.inflate(R.layout.fragment_loan_account_details, container,
                false);
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
        outState.putParcelable(Constants.LOAN_ACCOUNT, loanWithAssociations);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            showLoanAccountsDetail((LoanWithAssociations) savedInstanceState.
                    getParcelable(Constants.LOAN_ACCOUNT));
        }
    }


    /**
     * Shows details about loan account fetched from server is status is Active else shows and
     * error layout i.e. {@code layoutError} with a msg related to the status.
     *
     * @param loanWithAssociations object containing details of each loan account,
     */
    @Override
    public void showLoanAccountsDetail(LoanWithAssociations loanWithAssociations) {
        llAccountDetail.setVisibility(View.VISIBLE);
        this.loanWithAssociations = loanWithAssociations;

        Calendar currentDate = Calendar.getInstance();
        List<Integer> dueDate = loanWithAssociations.getTimeline().getActualDisbursementDate();
        int m = currentDate.get(Calendar.MONTH) + 1;
        dueDate.set(1, m);
        if (dueDate.get(2) < currentDate.get(Calendar.DAY_OF_MONTH)) {
            m = (m == 12) ? 1 : m + 1;
            dueDate.set(1, m);
            dueDate.set(0, currentDate.get(Calendar.YEAR) + 1);
        }
        dueDate.set(0, currentDate.get(Calendar.YEAR));

        if (loanWithAssociations.getStatus().getActive()) {
            tvDueDateName.setText(DateHelper.getDateAsString(dueDate));
            showDetails(loanWithAssociations);
        } else if (loanWithAssociations.getStatus().getPendingApproval()) {
            tv_status.setText(R.string.approval_pending);
            llAccountDetail.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
            showLoanUpdateOption = true;
        } else if (loanWithAssociations.getStatus().getWaitingForDisbursal()) {
            tv_status.setText(R.string.waiting_for_disburse);
            llAccountDetail.setVisibility(View.GONE);
            layoutError.setVisibility(View.VISIBLE);
        } else {
            btMakePayment.setVisibility(View.GONE);
            showDetails(loanWithAssociations);
        }

        getActivity().invalidateOptionsMenu();
    }

    /**
     * Sets basic information about a loan
     *
     * @param loanWithAssociations object containing details of each loan account,
     */
    public void showDetails(LoanWithAssociations loanWithAssociations) {
        tvOutstandingBalanceName.setText(getResources().getString(R.string.string_and_string,
                loanWithAssociations.getSummary().getCurrency().getDisplaySymbol(), CurrencyUtil.
                        formatCurrency(getActivity(),
                                loanWithAssociations.getSummary().getTotalOutstanding())));

        int numPayments = loanWithAssociations.getNumberOfRepayments();
        double interestRate = loanWithAssociations.getInterestRatePerPeriod();
        interestRate /= 100;
        if (loanWithAssociations.getInterestRateFrequencyType().getValue().equals("3")) {
            interestRate /= 12;
        }

        double installment = (loanWithAssociations.getSummary().getTotalOutstanding()
                * interestRate * Math.pow(1 + interestRate, numPayments)
                / (Math.pow(1 + interestRate, numPayments) - 1));

        tvNextInstallmentName.setText(getResources().getString(R.string.string_and_string,
                loanWithAssociations.getSummary().getCurrency().getDisplaySymbol(), CurrencyUtil.
                        formatCurrency(getActivity(), installment)));
        tvAccountNumberName.setText(loanWithAssociations.getAccountNo());
        tvLoanTypeName.setText(loanWithAssociations.getLoanType().getValue());
        tvCurrencyName.setText(loanWithAssociations.getSummary().getCurrency().getCode());
    }

    /**
     * Opens {@link SavingsMakeTransferFragment} to Make a payment for loan account with given
     * {@code loanId}
     */
    @OnClick(R.id.btn_make_payment)
    public void onMakePaymentClicked() {
        ((BaseActivity) getActivity()).replaceFragment(SavingsMakeTransferFragment
                        .newInstance(loanId, Constants.TRANSFER_PAY_TO),
                true, R.id.container);
    }

    /**
     * Opens {@link LoanAccountSummaryFragment}
     */
    @OnClick(R.id.ll_summary)
    public void onLoanSummaryClicked() {
        ((BaseActivity) getActivity()).replaceFragment(LoanAccountSummaryFragment
                .newInstance(loanWithAssociations), true, R.id.container);
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
                        .newInstance(loanWithAssociations.getId(), ChargeType.LOAN),
                true, R.id.container);
    }

    @OnClick(R.id.ll_loan_qr_code)
    public void onQrCodeClicked() {
        String accountDetailsInJson = QrCodeGenerator.getAccountDetailsInString(
                loanWithAssociations.getAccountNo(), preferencesHelper.getOfficeName(),
                AccountType.LOAN);
        ((BaseActivity) getActivity()).replaceFragment(QrCodeDisplayFragment.
                newInstance(accountDetailsInJson), true, R.id.container);
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
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
                    .newInstance(LoanState.UPDATE), true, R.id.container);
            return true;
        } else if (id == R.id.menu_withdraw_loan) {
            ((BaseActivity) getActivity()).replaceFragment(LoanAccountWithdrawFragment
                    .newInstance(loanWithAssociations), true, R.id.container);
        }
        return super.onOptionsItemSelected(item);
    }
}
