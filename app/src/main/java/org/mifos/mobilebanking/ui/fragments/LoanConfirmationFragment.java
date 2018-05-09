package org.mifos.mobilebanking.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.accounts.loan.LoanAccount;
import org.mifos.mobilebanking.models.payload.LoansPayload;
import org.mifos.mobilebanking.presenters.LoanConfirmationPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.enums.LoanState;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.LoanConfirmationView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * Created by saksham on 08/May/2018
 */

public class LoanConfirmationFragment extends BaseFragment implements LoanConfirmationView {

    @BindView(R.id.tv_loan_amount)
    TextView tvLoanAmount;

    @BindView(R.id.tv_loan_purpose)
    TextView tvLoanPurpose;

    @BindView(R.id.tv_loan_product_name)
    TextView tvLoanProduct;

    @BindView(R.id.iv_success)
    ImageView ivSuccess;

    @BindView(R.id.btn_close_loan)
    AppCompatButton btnCloseLoan;

    @BindView(R.id.btn_cancel_loan)
    AppCompatButton btnCancelLoan;

    @BindView(R.id.btn_apply_loan)
    AppCompatButton btnApplyLoan;

    @Inject
    LoanConfirmationPresenter presenter;

    private View rootView;
    private LoanState loanState;
    private LoansPayload loansPayload;
    private LoanAccount loanAccount;


    public static LoanConfirmationFragment newInstance(LoansPayload loansPayload,
                         LoanState loanState, LoanAccount loanAccount) {

        LoanConfirmationFragment loanConfirmationFragment = new LoanConfirmationFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.PAYLOAD, loansPayload);
        bundle.putSerializable(Constants.LOAN_STATE, loanState);

        if (loanAccount != null) {
            bundle.putParcelable(Constants.LOAN_ACCOUNT, loanAccount);
        }
        loanConfirmationFragment.setArguments(bundle);
        return loanConfirmationFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            loanState = (LoanState) getArguments().getSerializable(Constants.LOAN_STATE);
            loansPayload = getArguments().getParcelable(Constants.PAYLOAD);
            if (getArguments().getParcelable(Constants.LOAN_ACCOUNT) != null)
                loanAccount = getArguments().getParcelable(Constants.LOAN_ACCOUNT);
        }
        setToolbarTitle(getResources().getString(R.string.loan_account_details));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_loan_confirmation, container, false);
        ButterKnife.bind(this, rootView);

        presenter.attachView(this);

        tvLoanAmount.setText(String.valueOf(loansPayload.getPrincipal()));
        tvLoanProduct.setText(String.valueOf(loansPayload.getProductId()));
        tvLoanPurpose.setText(String.valueOf(loansPayload.getLoanPurposeId()));
        return rootView;
    }

    @OnClick(R.id.btn_apply_loan)
    public void applyLoan() {
        if (loanState == LoanState.UPDATE) {
            presenter.updateLoanAccount(loanAccount.getId(), loansPayload);
        } else if (loanState == LoanState.CREATE) {
            presenter.createLoansAccount(loansPayload);
        }
    }


    @OnClick(R.id.btn_cancel_loan)
    public void cancelLoan() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @OnClick(R.id.btn_close_loan)
    public void closeLoan() {
        getActivity().finish();
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void showError(String message) {
        Toaster.show(rootView, message);
    }

    @Override
    public void showLoanAccountCreatedSuccessfully() {
        ivSuccess.setVisibility(View.VISIBLE);
        btnApplyLoan.setVisibility(View.GONE);
        btnCancelLoan.setVisibility(View.GONE);
        btnCloseLoan.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoanAccountUpdatedSuccessfully() {
        ivSuccess.setVisibility(View.VISIBLE);
        btnApplyLoan.setVisibility(View.GONE);
        btnCancelLoan.setVisibility(View.GONE);
        btnCloseLoan.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
