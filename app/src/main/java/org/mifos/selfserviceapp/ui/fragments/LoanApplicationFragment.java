package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.payload.LoansPayload;
import org.mifos.selfserviceapp.models.templates.loans.LoanPurposeOptions;
import org.mifos.selfserviceapp.models.templates.loans.LoanTemplate;
import org.mifos.selfserviceapp.models.templates.loans.ProductOptions;
import org.mifos.selfserviceapp.presenters.LoanApplicationPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.LoanApplicationMvpView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.DateHelper;
import org.mifos.selfserviceapp.utils.MFDatePicker;
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

    @Inject
    LoanApplicationPresenter loanApplicationPresenter;

    View rootView;

    private List<String> listLoanProducts = new ArrayList<>();
    private List<String> listLoanPurpose = new ArrayList<>();

    private ArrayAdapter<String> loanProductAdapter;
    private ArrayAdapter<String> loanPurposeAdapter;

    private LoanTemplate loanTemplate;
    private DialogFragment mfDatePicker;
    private int productId;
    private int purposeId;
    private String disbursementDate;
    private String submittedDate;
    private boolean isDisbursebemntDate = false;
    private boolean isSubmissionDate = false;

    public static LoanApplicationFragment newInstance() {
        LoanApplicationFragment fragment = new LoanApplicationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        setToolbarTitle(getString(R.string.apply_for_loan));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_loan_application, container, false);

        ButterKnife.bind(this, rootView);
        loanApplicationPresenter.attachView(this);

        showUserInterface();
        loanApplicationPresenter.loadLoanApplicationTemplate();

        return rootView;
    }

    @OnClick(R.id.btn_loan_submit)
    void onSubmitLoanApplication() {
        LoansPayload loansPayload = new LoansPayload();
        loansPayload.setClientId(loanTemplate.getClientId());
        loansPayload.setLoanPurposeId(purposeId);
        loansPayload.setProductId(productId);
        loansPayload.setPrincipal(loanTemplate.getPrincipal());
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
        loansPayload.setTransactionProcessingStrategyId(
                loanTemplate.getTransactionProcessingStrategyId());
        loansPayload.setInterestCalculationPeriodType(
                loanTemplate.getInterestCalculationPeriodType().getId());
        loansPayload.setInterestType(loanTemplate.getInterestType().getId());

        loanApplicationPresenter.createLoansAccount(loansPayload);
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

    @OnClick(R.id.tv_expected_disbursement_date)
    public void setTvDisbursementOnDate() {
        isDisbursebemntDate = true;
        mfDatePicker.show(getActivity().getSupportFragmentManager(), Constants
                .DFRAG_DATE_PICKER);
    }

    @OnClick(R.id.tv_submission_date)
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
    public void showLoanAccountCreatedSuccessfully() {
        Toast.makeText(getActivity(), R.string.loan_application_submitted_successfully,
                Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showError(String message) {
        Toaster.show(rootView, message);
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
                loanApplicationPresenter.loadLoanApplicationTemplateByProduct(productId);
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
