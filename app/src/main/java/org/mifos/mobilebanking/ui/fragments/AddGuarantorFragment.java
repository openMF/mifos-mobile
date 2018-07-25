package org.mifos.mobilebanking.ui.fragments;

/*
 * Created by saksham on 23/July/2018
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.guarantor.GuarantorApplicationPayload;
import org.mifos.mobilebanking.models.guarantor.GuarantorPayload;
import org.mifos.mobilebanking.models.guarantor.GuarantorTemplatePayload;
import org.mifos.mobilebanking.models.guarantor.GuarantorType;
import org.mifos.mobilebanking.presenters.AddGuarantorPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.enums.GuarantorState;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.AddGuarantorView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.RxBus;
import org.mifos.mobilebanking.utils.RxEvent;
import org.mifos.mobilebanking.utils.Toaster;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddGuarantorFragment extends BaseFragment implements AddGuarantorView {

    @BindView(R.id.sp_guarantor_type)
    Spinner spGuarantorType;

    @BindView(R.id.til_first_name)
    TextInputLayout tilFirstName;

    @BindView(R.id.til_last_name)
    TextInputLayout tilLastName;

    @BindView(R.id.til_office_name)
    TextInputLayout tilOfficeName;

    @Inject
    AddGuarantorPresenter presenter;

    ArrayAdapter<String> guarantorTypeAdapter;
    GuarantorTemplatePayload template;
    GuarantorPayload payload;
    GuarantorState guarantorState;
    GuarantorApplicationPayload guarantorApplicationPayload;
    View rootView;
    long loanId;
    int index;

    public static AddGuarantorFragment newInstance(int index, GuarantorState guarantorState,
                                                   GuarantorPayload payload, long loanId) {
        AddGuarantorFragment fragment = new AddGuarantorFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.LOAN_ID, loanId);
        bundle.putSerializable(Constants.GUARANTOR_STATE, guarantorState);
        bundle.putParcelable(Constants.PAYLOAD, payload);
        bundle.putInt(Constants.INDEX, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loanId = getArguments().getLong(Constants.LOAN_ID);
            guarantorState = (GuarantorState) getArguments()
                    .getSerializable(Constants.GUARANTOR_STATE);
            payload = getArguments().getParcelable(Constants.PAYLOAD);
            index = getArguments().getInt(Constants.INDEX);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_guarantor, container, false);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        presenter.attachView(this);
        if (guarantorState == GuarantorState.CREATE) {
            setToolbarTitle(getString(R.string.add_guarantor));
        } else if (guarantorState == GuarantorState.UPDATE) {
            setToolbarTitle(getString(R.string.update_guarantor));
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.getGuarantorTemplate(guarantorState, loanId);
    }

    @OnClick(R.id.btn_submit_guarantor)
    void onSubmit() {
        tilFirstName.setErrorEnabled(false);
        tilLastName.setErrorEnabled(false);
        tilOfficeName.setErrorEnabled(false);
        if (isFieldsCompleted()) {
            guarantorApplicationPayload = generatePayload();
            if (guarantorState == GuarantorState.CREATE) {
                presenter.createGuarantor(loanId, guarantorApplicationPayload);
            } else if (guarantorState == GuarantorState.UPDATE) {
                presenter.updateGuarantor(guarantorApplicationPayload, loanId, payload.getId());
            }
        }
    }

    private GuarantorApplicationPayload generatePayload() {
        return new GuarantorApplicationPayload(
                template.getGuarantorTypeOptions()
                        .get(getGuarantorTypeIndex(spGuarantorType.getSelectedItem().toString())),
                tilFirstName.getEditText().getText().toString().trim(),
                tilLastName.getEditText().getText().toString().trim(),
                tilOfficeName.getEditText().getText().toString().trim()
        );
    }

    private boolean isFieldsCompleted() {
        boolean rv = true;
        if (tilFirstName.getEditText().getText().toString().trim().length() == 0) {
            tilFirstName.setError(getString(R.string.error_validation_blank,
                    getString(R.string.first_name)));
            rv = false;
        }
        if (tilLastName.getEditText().getText().toString().trim().length() == 0) {
            tilLastName.setError(getString(R.string.error_validation_blank,
                    getString(R.string.last_name)));
            rv = false;
        }
        if (tilOfficeName.getEditText().getText().toString().trim().length() == 0) {
            tilOfficeName.setError(getString(R.string.error_validation_blank,
                    getString(R.string.office_name)));
            rv = false;
        }
        return rv;
    }

    private int getGuarantorTypeIndex(String optionSelected) {
        int rv = 0;
        for (GuarantorType option : template.getGuarantorTypeOptions()) {
            if (option.getValue().equals(optionSelected)) {
                return rv;
            }
            rv++;
        }
        return rv;
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
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        hideProgressBar();
    }

    @Override
    public void showGuarantorApplication(GuarantorTemplatePayload template) {
        this.template = template;
        setUpSpinner();
    }


    @Override
    public void showGuarantorUpdation(GuarantorTemplatePayload template) {
        this.template = template;
        setUpSpinner();
        tilFirstName.getEditText().setText(payload.getFirstname());
        tilLastName.getEditText().setText(payload.getLastname());
        tilOfficeName.getEditText().setText(payload.getOfficeName());
        spGuarantorType.setSelection(findPreviouslySelectedGuarantorType(payload.getGuarantorType()
                .getValue()));
    }

    private int findPreviouslySelectedGuarantorType(String value) {
        int rv = 0;
        int counter = 0;
        for (GuarantorType option : template.getGuarantorTypeOptions()) {
            if (option.getValue().equals(value)) {
                rv = counter;
            }
            counter++;
        }
        return rv;
    }

    private void setUpSpinner() {
        List<String> options = new ArrayList<>();
        for (GuarantorType option : template.getGuarantorTypeOptions()) {
            options.add(option.getValue());
        }
        guarantorTypeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, options);
        spGuarantorType.setAdapter(guarantorTypeAdapter);
    }

    @Override
    public void updatedSuccessfully(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        RxBus.publish(new RxEvent.UpdateGuarantorEvent(guarantorApplicationPayload, index));
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void submittedSuccessfully(String message, GuarantorApplicationPayload payload) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        RxBus.publish(new RxEvent.AddGuarantorEvent(payload, index));
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showError(String message) {
        Toaster.show(rootView, message);
    }

}