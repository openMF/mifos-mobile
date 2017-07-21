package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.payload.LoansPayload;
import org.mifos.selfserviceapp.models.templates.loans.LoanPurposeOptions;
import org.mifos.selfserviceapp.models.templates.loans.LoanTemplate;
import org.mifos.selfserviceapp.models.templates.loans.ProductOptions;
import org.mifos.selfserviceapp.presenters.LoanApplicationPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.enums.LoanState;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.LoanApplicationMvpView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.DateHelper;
import org.mifos.selfserviceapp.utils.MFDatePicker;
import org.mifos.selfserviceapp.utils.Network;
import org.mifos.selfserviceapp.utils.Toaster;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rajan Maurya on 06/03/17.
 */
public class LoanApplicationFragment extends BaseFragment implements LoanApplicationMvpView,
        MFDatePicker.OnDatePickListener, AdapterView.OnItemSelectedListener {

    @BindView(R.id.tv_new_loan_application)
    TextView tvNewLoanApplication;

    @BindView(R.id.tv_account_number)
    TextView tvAccountNumber;

    @BindView(R.id.tv_submission_date)
    TextView tvSubmissionDate;

    @BindView(R.id.sp_loan_products)
    Spinner spLoanProducts;

    @BindView(R.id.sp_loan_purpose)
    Spinner spLoanPurpose;

    @BindView(R.id.et_principal_amount)
    EditText etPrincipalAmount;

    @BindView(R.id.tv_currency)
    TextView tvCurrency;

    @BindView(R.id.tv_expected_disbursement_date)
    TextView tvExpectedDisbursementDate;

    @BindView(R.id.ll_add_loan)
    LinearLayout llAddLoan;

    @BindView(R.id.ll_error)
    RelativeLayout llError;

    @BindView(R.id.tv_status)
    TextView tvErrorStatus;

    @BindView(R.id.iv_status)
    ImageView ivReload;

    @Inject
    LoanApplicationPresenter loanApplicationPresenter;

    View rootView;

    private List<String> listLoanProducts = new ArrayList<>();
    private List<String> listLoanPurpose = new ArrayList<>();

    private ArrayAdapter<String> loanProductAdapter;
    private ArrayAdapter<String> loanPurposeAdapter;

    private LoanTemplate loanTemplate;
    private DialogFragment mfDatePicker;
    private LoanState loanState;
    private LoanAccount loanAccountToModify;
    private int productId;
    private int purposeId;
    private String disbursementDate;
    private String submittedDate;
    private boolean isDisbursebemntDate = false;
    private boolean isSubmissionDate = false;
    private boolean isLoanUpdatePurposesInitialization = true;



    public static LoanApplicationFragment newInstance(LoanState loanState) {
        LoanApplicationFragment fragment = new LoanApplicationFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.LOAN_STATE, loanState);
        fragment.setArguments(args);
        return fragment;
    }

    public static LoanApplicationFragment newInstance(LoanState loanState,
                                                      LoanAccount loanAccountToModify) {
        LoanApplicationFragment fragment = new LoanApplicationFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.LOAN_STATE, loanState);
        args.putParcelable(Constants.LOAN_ACCOUNT, loanAccountToModify);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            loanState = (LoanState) getArguments().getSerializable(Constants.LOAN_STATE);

            if (loanState == LoanState.CREATE) {
                setToolbarTitle(getString(R.string.apply_for_loan));
            } else {
                setToolbarTitle(getString(R.string.update_loan));
                loanAccountToModify = getArguments().getParcelable(Constants.LOAN_ACCOUNT);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_loan_application, container, false);

        ButterKnife.bind(this, rootView);
        loanApplicationPresenter.attachView(this);

        showUserInterface();
        loadLoanTemplate();

        return rootView;
    }

    private void loadLoanTemplate() {
        if (loanState == LoanState.CREATE) {
            loanApplicationPresenter.loadLoanApplicationTemplate(LoanState.CREATE);
        } else {
            loanApplicationPresenter.loadLoanApplicationTemplate(LoanState.UPDATE);
        }
    }

    @OnClick(R.id.btn_loan_submit)
    void onSubmitLoanApplication() {
        if (loanState == LoanState.CREATE) {
            submitNewLoanApplication();
        } else {
            submitUpdateLoanApplication();
        }
    }

    private void submitNewLoanApplication() {
        LoansPayload loansPayload = new LoansPayload();
        loansPayload.setClientId(loanTemplate.getClientId());
        loansPayload.setLoanPurposeId(purposeId);
        loansPayload.setProductId(productId);
        loansPayload.setPrincipal(Double.
                parseDouble(etPrincipalAmount.getText().toString()));
        loansPayload.setLoanTermFrequency(loanTemplate.getTermFrequency());
        loansPayload.setLoanTermFrequencyType(loanTemplate.getInterestRateFrequencyType().getId());
        loansPayload.setLoanType("individual");
        loansPayload.setNumberOfRepayments(loanTemplate.getNumberOfRepayments());
        loansPayload.setRepaymentEvery(loanTemplate.getRepaymentEvery());
        loansPayload.setRepaymentFrequencyType(loanTemplate.getInterestRateFrequencyType().getId());
        loansPayload.setInterestRatePerPeriod(loanTemplate.getInterestRatePerPeriod());
        loansPayload.setExpectedDisbursementDate(disbursementDate);
        loansPayload.setSubmittedOnDate(submittedDate);

        loansPayload.setTransactionProcessingStrategyId(
                loanTemplate.getTransactionProcessingStrategyId());
        loansPayload.setAmortizationType(loanTemplate.getAmortizationType().getId());
        loansPayload.setInterestCalculationPeriodType(
                loanTemplate.getInterestCalculationPeriodType().getId());
        loansPayload.setInterestType(loanTemplate.getInterestType().getId());

        loanApplicationPresenter.createLoansAccount(loansPayload);
    }

    private void submitUpdateLoanApplication() {
        LoansPayload loansPayload = new LoansPayload();
        loansPayload.setPrincipal(Double.
                parseDouble(etPrincipalAmount.getText().toString()));
        loansPayload.setProductId(productId);
        loansPayload.setLoanPurposeId(purposeId);
        loansPayload.setLoanTermFrequency(loanTemplate.getTermFrequency());
        loansPayload.setLoanTermFrequencyType(loanTemplate.getInterestRateFrequencyType().getId());
        loansPayload.setNumberOfRepayments(loanTemplate.getNumberOfRepayments());
        loansPayload.setRepaymentEvery(loanTemplate.getRepaymentEvery());
        loansPayload.setRepaymentFrequencyType(loanTemplate.getInterestRateFrequencyType().getId());
        loansPayload.setInterestRatePerPeriod(loanTemplate.getInterestRatePerPeriod());
        loansPayload.setInterestType(loanTemplate.getInterestType().getId());
        loansPayload.setInterestCalculationPeriodType(
                loanTemplate.getInterestCalculationPeriodType().getId());
        loansPayload.setAmortizationType(loanTemplate.getAmortizationType().getId());
        loansPayload.setTransactionProcessingStrategyId(
                loanTemplate.getTransactionProcessingStrategyId());
        loansPayload.setExpectedDisbursementDate(disbursementDate);

        loanApplicationPresenter.updateLoanAccount(loanAccountToModify.getId(), loansPayload);
    }

    @OnClick(R.id.iv_status)
    void onRetry() {
        llError.setVisibility(View.GONE);
        llAddLoan.setVisibility(View.VISIBLE);
        loadLoanTemplate();
    }

    public void inflateSubmissionDate() {
        mfDatePicker = MFDatePicker.newInsance(this);
        tvSubmissionDate.setText(MFDatePicker.getDatePickedAsString());
    }

    public void inflateDisbursementDate() {
        mfDatePicker = MFDatePicker.newInsance(this);
        tvExpectedDisbursementDate.setText(MFDatePicker.getDatePickedAsString());
    }

    public void setSubmissionDisburseDate() {
        disbursementDate = tvExpectedDisbursementDate.getText().toString();
        submittedDate = tvSubmissionDate.getText().toString();
        submittedDate = DateHelper.getSpecificFormat(DateHelper.FORMAT_dd_MMMM_yyyy, submittedDate);
        disbursementDate = DateHelper.getSpecificFormat(
                DateHelper.FORMAT_dd_MMMM_yyyy, disbursementDate);
    }

    @OnClick(R.id.ll_expected_disbursement_date_edit)
    public void setTvDisbursementOnDate() {
        isDisbursebemntDate = true;
        mfDatePicker.show(getActivity().getSupportFragmentManager(), Constants
                .DFRAG_DATE_PICKER);
    }

    @OnClick(R.id.ll_submission_date_edit)
    public void setTvSubmittedOnDate() {
        isSubmissionDate = true;
        mfDatePicker.show(getActivity().getSupportFragmentManager(), Constants
                .DFRAG_DATE_PICKER);
    }

    @Override
    public void onDatePicked(String date) {
        if (isSubmissionDate) {
            tvSubmissionDate.setText(date);
            submittedDate = date;
            isSubmissionDate = false;
        }

        if (isDisbursebemntDate) {
            tvExpectedDisbursementDate.setText(date);
            disbursementDate = date;
            isDisbursebemntDate = false;
        }
        setSubmissionDisburseDate();
    }


    @Override
    public void showUserInterface() {
        loanProductAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                listLoanProducts);
        loanProductAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLoanProducts.setAdapter(loanProductAdapter);
        spLoanProducts.setOnItemSelectedListener(this);

        loanPurposeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                listLoanPurpose);
        loanPurposeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLoanPurpose.setAdapter(loanPurposeAdapter);
        spLoanPurpose.setOnItemSelectedListener(this);

        inflateSubmissionDate();
        inflateDisbursementDate();
        setSubmissionDisburseDate();
    }

    @Override
    public void showLoanTemplate(LoanTemplate loanTemplate) {
        this.loanTemplate = loanTemplate;
        for (ProductOptions productOption : loanTemplate.getProductOptions()) {
            listLoanProducts.add(productOption.getName());
        }
        loanProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void showUpdateLoanTemplate(LoanTemplate loanTemplate) {
        this.loanTemplate = loanTemplate;

        for (ProductOptions productOption : loanTemplate.getProductOptions()) {
            listLoanProducts.add(productOption.getName());
        }
        loanProductAdapter.notifyDataSetChanged();

        spLoanProducts.setSelection(loanProductAdapter
                .getPosition(loanAccountToModify.getLoanProductName()));
        tvAccountNumber.setText(getString(R.string.string_and_string,
                getString(R.string.account_number) + " ", loanAccountToModify.getAccountNo()));
        tvNewLoanApplication.setText(getString(R.string.string_and_string,
                getString(R.string.update_loan_application) + " ",
                loanAccountToModify.getClientName()));
        etPrincipalAmount.setText(String.valueOf(loanAccountToModify.getPrincipal()));
        tvCurrency.setText(loanAccountToModify.getCurrency().getDisplayLabel());

        tvSubmissionDate.setText(DateHelper.getDateAsString(loanAccountToModify.
                getTimeline().getSubmittedOnDate(), "dd-MM-YYYY"));
        tvExpectedDisbursementDate.setText(DateHelper.getDateAsString(loanAccountToModify.
                getTimeline().getExpectedDisbursementDate(), "dd-MM-YYYY"));
        setSubmissionDisburseDate();
    }

    @Override
    public void showLoanTemplateByProduct(LoanTemplate loanTemplate) {
        this.loanTemplate = loanTemplate;
        tvAccountNumber.setText(getString(R.string.string_and_string,
                getString(R.string.account_number) + " ", loanTemplate.getClientAccountNo()));
        tvNewLoanApplication.setText(getString(R.string.string_and_string,
                getString(R.string.new_loan_application) + " ", loanTemplate.getClientName()));
        etPrincipalAmount.setText(String.valueOf(loanTemplate.getPrincipal()));
        tvCurrency.setText(loanTemplate.getCurrency().getDisplayLabel());

        listLoanPurpose.clear();
        for (LoanPurposeOptions loanPurposeOptions : loanTemplate.getLoanPurposeOptions()) {
            listLoanPurpose.add(loanPurposeOptions.getName());
        }
        loanPurposeAdapter.notifyDataSetChanged();
    }

    @Override
    public void showUpdateLoanTemplateByProduct(LoanTemplate loanTemplate) {
        this.loanTemplate = loanTemplate;
        listLoanPurpose.clear();
        for (LoanPurposeOptions loanPurposeOptions : loanTemplate.getLoanPurposeOptions()) {
            listLoanPurpose.add(loanPurposeOptions.getName());
        }
        loanPurposeAdapter.notifyDataSetChanged();

        if (isLoanUpdatePurposesInitialization &&
                loanAccountToModify.getLoanPurposeName() != null) {
            spLoanPurpose.setSelection(loanPurposeAdapter
                    .getPosition(loanAccountToModify.getLoanPurposeName()));
            isLoanUpdatePurposesInitialization = false;
        } else {
            tvAccountNumber.setText(getString(R.string.string_and_string,
                    getString(R.string.account_number) + " ", loanTemplate.getClientAccountNo()));
            tvNewLoanApplication.setText(getString(R.string.string_and_string,
                    getString(R.string.new_loan_application) + " ", loanTemplate.getClientName()));
            etPrincipalAmount.setText(String.valueOf(loanTemplate.getPrincipal()));
            tvCurrency.setText(loanTemplate.getCurrency().getDisplayLabel());
        }
    }

    @Override
    public void showLoanAccountCreatedSuccessfully() {
        Toaster.show(rootView, R.string.loan_application_submitted_successfully);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showLoanAccountUpdatedSuccessfully() {
        Toaster.show(rootView, R.string.loan_application_updated_successfully);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showError(String message) {
        if (!Network.isConnected(getActivity())) {
            ivReload.setImageResource(R.drawable.ic_error_black_24dp);
            tvErrorStatus.setText(getString(R.string.internet_not_connected));
            llAddLoan.setVisibility(View.GONE);
            llError.setVisibility(View.VISIBLE);
        } else {
            Toaster.show(rootView, message);
        }
    }

    @Override
    public void showProgress() {
        llAddLoan.setVisibility(View.GONE);
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        llAddLoan.setVisibility(View.VISIBLE);
        hideProgressBar();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_loan_products:
                productId = loanTemplate.getProductOptions().get(position).getId();

                if (loanState == LoanState.CREATE) {
                    loanApplicationPresenter.loadLoanApplicationTemplateByProduct(productId,
                            LoanState.CREATE);
                } else {
                    loanApplicationPresenter.loadLoanApplicationTemplateByProduct(productId,
                            LoanState.UPDATE);
                }
                break;

            case R.id.sp_loan_purpose:
                purposeId = loanTemplate.getLoanPurposeOptions().get(position).getId();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgressBar();
        loanApplicationPresenter.detachView();
    }
}
