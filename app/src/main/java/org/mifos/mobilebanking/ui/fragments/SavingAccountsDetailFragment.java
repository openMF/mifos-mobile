package org.mifos.mobilebanking.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.models.accounts.savings.SavingsWithAssociations;
import org.mifos.mobilebanking.models.accounts.savings.Status;
import org.mifos.mobilebanking.presenters.SavingAccountsDetailPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.enums.AccountType;
import org.mifos.mobilebanking.ui.enums.ChargeType;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.SavingAccountsDetailView;
import org.mifos.mobilebanking.utils.CircularImageView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.CurrencyUtil;
import org.mifos.mobilebanking.utils.DateHelper;
import org.mifos.mobilebanking.utils.Network;
import org.mifos.mobilebanking.utils.QrCodeGenerator;
import org.mifos.mobilebanking.utils.SymbolsUtils;
import org.mifos.mobilebanking.utils.Toaster;
import org.mifos.mobilebanking.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vishwajeet
 * @since 18/8/16.
 */

public class SavingAccountsDetailFragment extends BaseFragment implements SavingAccountsDetailView {

    @BindView(R.id.tv_account_status)
    TextView tvAccountStatus;

    @BindView(R.id.iv_circle_status)
    CircularImageView ivCircularStatus;

    @BindView(R.id.tv_total_withdrawals)
    TextView tvTotalWithdrawals;

    @BindView(R.id.tv_min_req_bal)
    TextView tvMiniRequiredBalance;

    @BindView(R.id.tv_saving_account_number)
    TextView tvSavingAccountNumber;

    @BindView(R.id.tv_nominal_interest_rate)
    TextView tvNominalInterestRate;

    @BindView(R.id.tv_total_deposits)
    TextView tvTotalDeposits;

    @BindView(R.id.tv_acc_balance)
    TextView tvAccountBalanceMain;

    @BindView(R.id.tv_last_transaction)
    TextView tvLastTransaction;

    @BindView(R.id.made_on)
    TextView tvMadeOnTextView;

    @BindView(R.id.tv_made_on)
    TextView tvMadeOnTransaction;

    @BindView(R.id.ll_account)
    LinearLayout layoutAccount;

    @BindView(R.id.layout_error)
    View layoutError;

    @BindView(R.id.tv_minRequiredBalance)
    TextView tvMinRequiredBalanceLabel;

    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    SavingAccountsDetailPresenter savingAccountsDetailPresenter;

    private View rootView;
    private long savingsId;
    private Status status;
    private SavingsWithAssociations savingsWithAssociations;
    private SweetUIErrorHandler sweetUIErrorHandler;

    public static SavingAccountsDetailFragment newInstance(long savingsId) {
        SavingAccountsDetailFragment fragment = new SavingAccountsDetailFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.SAVINGS_ID, savingsId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            savingsId = getArguments().getLong(Constants.SAVINGS_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_saving_account_details, container, false);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        setToolbarTitle(getString(R.string.saving_account_details));
        ButterKnife.bind(this, rootView);
        savingAccountsDetailPresenter.attachView(this);
        sweetUIErrorHandler = new SweetUIErrorHandler(getContext(), rootView);

        if (savedInstanceState == null) {
            savingAccountsDetailPresenter.loadSavingsWithAssociations(savingsId);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.SAVINGS_ACCOUNTS, savingsWithAssociations);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            showSavingAccountsDetail((SavingsWithAssociations) savedInstanceState.
                    getParcelable(Constants.SAVINGS_ACCOUNTS));
        }
    }

    /**
     * Opens up Phone Dialer
     */
    @OnClick(R.id.tv_help_line_number)
    void dialHelpLineNumber() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + getString(R.string.help_line_number)));
        startActivity(intent);
    }

    /**
     * Opens {@link SavingsMakeTransferFragment} if status is ACTIVE else shows a
     * {@link android.support.design.widget.Snackbar} that Account should be Active
     */
    @OnClick(R.id.tv_deposit)
    void deposit() {
        if (status.getActive()) {
            ((BaseActivity) getActivity()).replaceFragment(SavingsMakeTransferFragment
                    .newInstance(savingsId, Constants.TRANSFER_PAY_TO), true, R.id.container);
        } else {
            Toaster.show(rootView, getString(R.string.account_not_active_to_perform_deposit));
        }
    }

    /**
     * Opens {@link SavingsMakeTransferFragment} if status is ACTIVE else shows a
     * {@link android.support.design.widget.Snackbar} that Account should be Active
     */
    @OnClick(R.id.tv_make_a_transfer)
    void transfer() {
        if (status.getActive()) {
            ((BaseActivity) getActivity()).replaceFragment(SavingsMakeTransferFragment
                    .newInstance(savingsId, Constants.TRANSFER_PAY_FROM), true, R.id.container);
        } else {
            Toaster.show(rootView, getString(R.string.account_not_active_to_perform_transfer));
        }
    }

    /**
     * Sets Saving account basic info fetched from the server
     * @param savingsWithAssociations object containing details of a saving account
     */
    @Override
    public void showSavingAccountsDetail(SavingsWithAssociations savingsWithAssociations) {
        if (savingsWithAssociations.getStatus().getSubmittedAndPendingApproval()) {
            sweetUIErrorHandler.showSweetCustomErrorUI(getString(R.string.approval_pending),
                    R.drawable.ic_assignment_turned_in_black_24dp, layoutAccount, layoutError);
        } else {
            layoutAccount.setVisibility(View.VISIBLE);
            String currencySymbol = savingsWithAssociations.getCurrency().getDisplaySymbol();
            Double accountBalance = savingsWithAssociations.getSummary().getAccountBalance();

            tvAccountStatus.setText(savingsWithAssociations.getClientName());
            if (savingsWithAssociations.getMinRequiredOpeningBalance() != null) {
                tvMiniRequiredBalance.setText(getString(R.string.string_and_string, currencySymbol,
                        CurrencyUtil.formatCurrency(getActivity(), savingsWithAssociations.
                                getMinRequiredOpeningBalance())));
            } else {
                tvMinRequiredBalanceLabel.setVisibility(View.GONE);
                tvMiniRequiredBalance.setVisibility(View.GONE);
            }

            if (savingsWithAssociations.getSummary().getTotalWithdrawals() != null) {
                tvTotalWithdrawals.setText(getString(R.string.string_and_string, currencySymbol,
                        CurrencyUtil.formatCurrency(getActivity(), savingsWithAssociations.
                                getSummary().getTotalWithdrawals())));
            } else {
                tvTotalWithdrawals.setText(R.string.no_withdrawals);
            }

            tvAccountBalanceMain.setText(getString(R.string.string_and_string,
                    currencySymbol, CurrencyUtil.formatCurrency(getActivity(), accountBalance)));
            tvNominalInterestRate.setText(getString(R.string.double_and_string,
                    savingsWithAssociations.getNominalAnnualInterestRate(), SymbolsUtils.PERCENT));
            tvSavingAccountNumber.setText(String.valueOf(savingsWithAssociations.getAccountNo()));
            if (savingsWithAssociations.getSummary().getTotalDeposits() != null) {
                tvTotalDeposits.setText(getString(R.string.string_and_string, currencySymbol,
                        CurrencyUtil.formatCurrency(getActivity(), savingsWithAssociations.
                                getSummary().getTotalDeposits())));
            } else {
                tvTotalDeposits.setText(getString(R.string.not_available));
            }

            if (savingsWithAssociations.getTransactions() != null &&
                    !savingsWithAssociations.getTransactions().isEmpty()) {
                tvLastTransaction.setText(getString(R.string.double_and_string,
                        savingsWithAssociations.getTransactions().get(0).getAmount(),
                        currencySymbol));
                tvMadeOnTransaction.setText(DateHelper.getDateAsString(
                        savingsWithAssociations.getLastActiveTransactionDate()));
            } else {
                tvLastTransaction.setText(R.string.no_transaction);
                tvMadeOnTransaction.setVisibility(View.GONE);
                tvMadeOnTextView.setVisibility(View.GONE);
            }
            showAccountStatus(savingsWithAssociations);
        }
        this.savingsWithAssociations = savingsWithAssociations;
    }

    /**
     * It is called whenever any error occurs while executing a request
     * @param message Error message that tells the user about the problem.
     */
    @Override
    public void showErrorFetchingSavingAccountsDetail(String message) {
        if (!Network.isConnected(getContext())) {
            sweetUIErrorHandler.showSweetNoInternetUI(layoutAccount, layoutError);
            Toast.makeText(getContext(), getString(R.string.internet_not_connected),
                    Toast.LENGTH_SHORT).show();
        } else {
            sweetUIErrorHandler.showSweetErrorUI(message, layoutAccount, layoutError);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_try_again)
    void onRetry() {
        if (!Network.isConnected(getContext())) {
            Toast.makeText(getContext(), getString(R.string.internet_not_connected),
                    Toast.LENGTH_SHORT).show();
        } else {
            sweetUIErrorHandler.hideSweetErrorLayoutUI(layoutAccount, layoutError);
            savingAccountsDetailPresenter.loadSavingsWithAssociations(savingsId);
        }
    }

    /**
     * Sets the status of account i.e. {@code tvAccountStatus} and {@code ivCircularStatus} color
     * according to {@code savingsWithAssociations}
     * @param savingsWithAssociations object containing details of a saving account
     */
    @Override
    public void showAccountStatus(SavingsWithAssociations savingsWithAssociations) {
        status = savingsWithAssociations.getStatus();
        if (status.getActive()) {
            ivCircularStatus.setImageDrawable(
                    Utils.setCircularBackground(R.color.deposit_green, getActivity()));
            tvAccountStatus.setText(R.string.active);
        } else if (status.getApproved()) {
            ivCircularStatus.setImageDrawable(
                    Utils.setCircularBackground(R.color.blue, getActivity()));
            tvAccountStatus.setText(R.string.need_approval);
        } else if (status.getSubmittedAndPendingApproval()) {
            ivCircularStatus.setImageDrawable(
                    Utils.setCircularBackground(R.color.light_yellow, getActivity()));
            tvAccountStatus.setText(R.string.pending);
        } else if (status.getMatured()) {
            ivCircularStatus.setImageDrawable(
                    Utils.setCircularBackground(R.color.red_light, getActivity()));
            tvAccountStatus.setText(R.string.matured);
        } else {
            ivCircularStatus.setImageDrawable(
                    Utils.setCircularBackground(R.color.black, getActivity()));
            tvAccountStatus.setText(R.string.closed);
        }
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showProgress() {
        layoutAccount.setVisibility(View.GONE);
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        layoutAccount.setVisibility(View.VISIBLE);
        hideProgressBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgressBar();
        savingAccountsDetailPresenter.detachView();
    }

    @OnClick(R.id.ll_savings_transactions)
    public void transactionsClicked() {
        ((BaseActivity) getActivity()).replaceFragment(SavingAccountsTransactionFragment.
                newInstance(savingsId), true, R.id.container);
    }

    @OnClick(R.id.ll_savings_charges)
    public void chargeClicked() {
        ((BaseActivity) getActivity()).replaceFragment(ClientChargeFragment.
                newInstance(savingsId, ChargeType.SAVINGS), true, R.id.container);
    }

    @OnClick(R.id.ll_savings_qr_code)
    public void qrCodeClicked() {
        String accountDetailsInJson = QrCodeGenerator.getAccountDetailsInString(
                savingsWithAssociations.getAccountNo(), preferencesHelper.getOfficeName(),
                AccountType.SAVINGS);
        ((BaseActivity) getActivity()).replaceFragment(QrCodeDisplayFragment.
                newInstance(accountDetailsInJson), true, R.id.container);
    }

}
