package org.mifos.mobilebanking.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.accounts.loan.LoanAccount;
import org.mifos.mobilebanking.models.accounts.loan.LoanWithdraw;
import org.mifos.mobilebanking.presenters.LoanAccountWithdrawPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.LoanAccountWithdrawView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.DateHelper;
import org.mifos.mobilebanking.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilpreet on 7/6/17.
 */

public class LoanAccountWithdrawFragment extends BaseFragment implements LoanAccountWithdrawView {

    @BindView(R.id.tv_client_name)
    TextView tvClientName;

    @BindView(R.id.tv_account_number)
    TextView tvAccountNumber;

    @BindView(R.id.et_withdraw_reason)
    EditText etWithdrawReason;

    @Inject
    LoanAccountWithdrawPresenter loanAccountWithdrawPresenter;

    View rootView;
    private LoanAccount loanAccount;


    public static LoanAccountWithdrawFragment newInstance(LoanAccount loanAccount) {
        LoanAccountWithdrawFragment fragment = new LoanAccountWithdrawFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.LOAN_ACCOUNT, loanAccount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            loanAccount = getArguments().getParcelable(Constants.LOAN_ACCOUNT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_loan_withdraw, container, false);
        setToolbarTitle(getString(R.string.withdraw_loan));

        ButterKnife.bind(this, rootView);
        showUserInterface();

        loanAccountWithdrawPresenter.attachView(this);

        return rootView;
    }

    /**
     * Sets Basic Information about that Loan Application
     */
    private void showUserInterface() {
        tvClientName.setText(loanAccount.getClientName());
        tvAccountNumber.setText(loanAccount.getAccountNo());
    }

    /**
     * Sends a request to server to withdraw that Loan Account
     */
    @OnClick(R.id.btn_withdraw_loan)
    public void onLoanWithdraw() {
        if (isFieldsValid()) {
            LoanWithdraw loanWithdraw = new LoanWithdraw();
            loanWithdraw.setNote(etWithdrawReason.getText().toString().trim());
            loanWithdraw.setWithdrawnOnDate(DateHelper
                    .getDateAsStringFromLong(System.currentTimeMillis()));
            loanAccountWithdrawPresenter.withdrawLoanAccount(loanAccount.getId(), loanWithdraw);
        }
    }

    /**
     * Receives A confirmation after successfull withdrawing of Loan Application.
     */
    @Override
    public void showLoanAccountWithdrawSuccess() {
        Toaster.show(rootView, R.string.loan_application_withdrawn_successfully);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    /**
     * Shows an error in {@link android.support.design.widget.Snackbar} if any error occurs during
     * withdrawing of Loan
     * @param message Error Message displayed
     */
    @Override
    public void showLoanAccountWithdrawError(String message) {
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

    public boolean isFieldsValid() {
        if (etWithdrawReason.getText().toString().trim().length() == 0) {
            Toaster.show(rootView, getResources().getString(R.string.error_validation_blank,
                    getResources().getString(R.string.reason_of_withdrawal)));
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgress();
        loanAccountWithdrawPresenter.detachView();
    }
}
