package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.beneficary.Beneficiary;
import org.mifos.selfserviceapp.models.beneficary.BeneficiaryPayload;
import org.mifos.selfserviceapp.models.beneficary.BeneficiaryUpdatePayload;
import org.mifos.selfserviceapp.models.templates.beneficiary.AccountTypeOption;
import org.mifos.selfserviceapp.models.templates.beneficiary.BeneficiaryTemplate;
import org.mifos.selfserviceapp.presenters.BeneficiaryApplicationPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.enums.BeneficiaryState;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.BeneficiaryApplicationView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.Toaster;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilpreet on 16/6/17.
 */

public class BeneficiaryApplicationFragment extends BaseFragment implements
        BeneficiaryApplicationView, AdapterView.OnItemSelectedListener {

    @BindView(R.id.sp_account_type)
    Spinner spAccountType;

    @BindView(R.id.ll_application_beneficiary)
    LinearLayout llApplicationBeneficiary;

    @BindView(R.id.et_account_number)
    EditText etAccountNumber;

    @BindView(R.id.et_office_name)
    EditText etOfficeName;

    @BindView(R.id.et_transfer_limit)
    EditText etTransferLimit;

    @BindView(R.id.et_beneficiary_name)
    EditText etBeneficiaryName;

    @Inject
    BeneficiaryApplicationPresenter presenter;

    private List<String> listAccountType = new ArrayList<>();
    private ArrayAdapter<String> accountTypeAdapter;

    private BeneficiaryState beneficiaryState;
    private Beneficiary beneficiary;
    private BeneficiaryTemplate beneficiaryTemplate;
    private int accountTypeId = -1;
    private View rootView;

    public static BeneficiaryApplicationFragment newInstance(BeneficiaryState beneficiaryState,
                                                             @Nullable Beneficiary beneficiary) {
        BeneficiaryApplicationFragment fragment = new BeneficiaryApplicationFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.BENEFICIARY_STATE, beneficiaryState);
        if (beneficiary != null) {
            args.putParcelable(Constants.BENEFICIARY, beneficiary);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(getString(R.string.add_beneficiary));
        if (getArguments() != null) {
            beneficiaryState = (BeneficiaryState) getArguments()
                    .getSerializable(Constants.BENEFICIARY_STATE);
            if (beneficiaryState == BeneficiaryState.UPDATE) {
                beneficiary = getArguments().getParcelable(Constants.BENEFICIARY);
                setToolbarTitle(getString(R.string.update_beneficiary));
            } else if (beneficiaryState == BeneficiaryState.CREATE_QR) {
                beneficiary = getArguments().getParcelable(Constants.BENEFICIARY);
                setToolbarTitle(getString(R.string.add_beneficiary));
            } else {
                setToolbarTitle(getString(R.string.add_beneficiary));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_beneficiary_application, container, false);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        showUserInterface();

        presenter.attachView(this);
        presenter.loadBeneficiaryTemplate();

        return rootView;
    }

    @Override
    public void showUserInterface() {
        accountTypeAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, listAccountType);
        accountTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAccountType.setOnItemSelectedListener(this);
        spAccountType.setAdapter(accountTypeAdapter);
    }

    @Override
    public void showBeneficiaryTemplate(BeneficiaryTemplate beneficiaryTemplate) {
        this.beneficiaryTemplate = beneficiaryTemplate;
        for (AccountTypeOption accountTypeOption : beneficiaryTemplate.getAccountTypeOptions()) {
            listAccountType.add(accountTypeOption.getValue());
        }
        accountTypeAdapter.notifyDataSetChanged();

        if (beneficiaryState == BeneficiaryState.UPDATE) {
            spAccountType.setSelection(accountTypeAdapter.getPosition(beneficiary
                    .getAccountType().getValue()));
            spAccountType.setEnabled(false);
            etAccountNumber.setText(beneficiary.getAccountNumber());
            etAccountNumber.setEnabled(false);
            etOfficeName.setText(beneficiary.getOfficeName());
            etOfficeName.setEnabled(false);

            etBeneficiaryName.setText(beneficiary.getName());
            etTransferLimit.setText(String.valueOf(beneficiary.getTransferLimit()));
        } else if (beneficiaryState == BeneficiaryState.CREATE_QR) {
            spAccountType.setSelection(beneficiary.getAccountType().getId());
            etAccountNumber.setText(beneficiary.getAccountNumber());
            etOfficeName.setText(beneficiary.getOfficeName());
        }
    }

    @OnClick(R.id.btn_beneficiary_submit)
    public void submitBeneficiary() {
        if (accountTypeId == -1) {
            Toaster.show(rootView, getString(R.string.choose_account_type));
            return;
        } else if (etAccountNumber.getText().toString().equals("")) {
            Toaster.show(rootView, getString(R.string.enter_account_number));
            return;
        } else if (etOfficeName.getText().toString().equals("")) {
            Toaster.show(rootView, getString(R.string.enter_office_name));
            return;
        } else if (etTransferLimit.getText().toString().equals("")) {
            Toaster.show(rootView, getString(R.string.enter_transfer_limit));
            return;
        } else if (etBeneficiaryName.getText().toString().equals("")) {
            Toaster.show(rootView, getString(R.string.enter_beneficiary_name));
            return;
        } else if (beneficiaryState == BeneficiaryState.CREATE_MANUAL ||
                beneficiaryState == BeneficiaryState.CREATE_QR ) {
            submitNewBeneficiaryApplication();
        } else if (beneficiaryState == BeneficiaryState.UPDATE ) {
            submitUpdateBeneficiaryApplication();
        }

    }

    private void submitNewBeneficiaryApplication() {
        BeneficiaryPayload beneficiaryPayload = new BeneficiaryPayload();
        beneficiaryPayload.setAccountNumber(etAccountNumber.getText().toString());
        beneficiaryPayload.setOfficeName(etOfficeName.getText().toString());
        beneficiaryPayload.setAccountType(accountTypeId);
        beneficiaryPayload.setName(etBeneficiaryName.getText().toString());
        beneficiaryPayload.setTransferLimit(Integer.parseInt(etTransferLimit.getText().toString()));
        presenter.createBeneficiary(beneficiaryPayload);
    }

    private void submitUpdateBeneficiaryApplication() {
        BeneficiaryUpdatePayload payload = new BeneficiaryUpdatePayload();
        payload.setName(etBeneficiaryName.getText().toString());
        payload.setTransferLimit(Integer.parseInt(etTransferLimit.getText().toString()));
        presenter.updateBeneficiary(beneficiary.getId(), payload);
    }

    @Override
    public void showBeneficiaryCreatedSuccessfully() {
        Toaster.show(rootView, getString(R.string.beneficiary_created_successfully));
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showBeneficiaryUpdatedSuccessfully() {
        Toaster.show(rootView, getString(R.string.beneficiary_updated_successfully));
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().popBackStack();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        accountTypeId = beneficiaryTemplate.getAccountTypeOptions().get(position).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void showError(String msg) {
        Toaster.show(rootView, msg);
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
