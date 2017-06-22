package org.mifos.selfserviceapp.ui.fragments;

import android.content.DialogInterface;
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
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.beneficary.Beneficiary;
import org.mifos.selfserviceapp.models.payload.TransferPayload;
import org.mifos.selfserviceapp.models.templates.account.AccountOption;
import org.mifos.selfserviceapp.models.templates.account.AccountOptionsTemplate;
import org.mifos.selfserviceapp.presenters.ThirdPartyTransferPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.ThirdPartyTransferView;
import org.mifos.selfserviceapp.utils.DateHelper;
import org.mifos.selfserviceapp.utils.MFDatePicker;
import org.mifos.selfserviceapp.utils.MaterialDialog;
import org.mifos.selfserviceapp.utils.Toaster;
import org.mifos.selfserviceapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilpreet on 21/6/17.
 */

public class ThirdPartyTransferFragment extends BaseFragment implements ThirdPartyTransferView,
        AdapterView.OnItemSelectedListener {

    @BindView(R.id.sp_beneficiary)
    Spinner spBeneficiary;

    @BindView(R.id.sp_pay_from)
    Spinner spPayFrom;

    @BindView(R.id.et_amount)
    EditText etAmount;

    @BindView(R.id.tv_transfer_date)
    TextView tvTransferDate;

    @BindView(R.id.et_remark)
    EditText etRemark;

    @BindView(R.id.ll_make_transfer)
    LinearLayout layoutMakeTransfer;

    @Inject
    ThirdPartyTransferPresenter presenter;

    private List<String> listBeneficiary = new ArrayList<>();
    private List<String> listPayFrom = new ArrayList<>();

    private ArrayAdapter<String> beneficiaryAdapter;
    private ArrayAdapter<String> payFromAdapter;
    private AccountOption fromAccountOption;
    private AccountOption beneficiaryAccountOption;
    private AccountOptionsTemplate accountOptionsTemplate;
    private String transferDate;
    private View rootView;

    public static ThirdPartyTransferFragment newInstance() {
        ThirdPartyTransferFragment fragment = new ThirdPartyTransferFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        rootView = inflater.inflate(R.layout.fragment_third_party_transfer, container, false);
        setToolbarTitle(getString(R.string.third_party_transfer));
        ButterKnife.bind(this, rootView);

        showUserInterface();

        presenter.attachView(this);
        presenter.loadTransferTemplate();


        return rootView;
    }

    @Override
    public void showUserInterface() {
        payFromAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                listPayFrom);
        payFromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPayFrom.setAdapter(payFromAdapter);
        spPayFrom.setOnItemSelectedListener(this);

        beneficiaryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                listBeneficiary);
        beneficiaryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBeneficiary.setAdapter(beneficiaryAdapter);
        spBeneficiary.setOnItemSelectedListener(this);

        tvTransferDate.setText(MFDatePicker.getDatePickedAsString());
        transferDate = DateHelper.getSpecificFormat(DateHelper.FORMAT_dd_MMMM_yyyy,
                tvTransferDate.getText().toString());
    }

    @OnClick(R.id.btn_review_transfer)
    public void reviewTransfer() {
        if (etAmount.getText().toString().equals("")) {
            Toaster.show(rootView, getString(R.string.enter_amount));
            return;
        }

        if (etAmount.getText().toString().equals(".")) {
            Toaster.show(rootView, getString(R.string.invalid_amount));
            return;
        }

        if (etRemark.getText().toString().equals("")) {
            Toaster.show(rootView, getString(R.string.remark_is_mandatory));
            return;
        }

        if (spBeneficiary.getSelectedItem().toString().
                equals(spPayFrom.getSelectedItem().toString())) {
            Toaster.show(rootView, getString(R.string.error_same_account_transfer));
            return;
        }

        final String[][] data = {
                {getString(R.string.transfer_to), ""},
                {getString(R.string.account_number), spBeneficiary.getSelectedItem().toString()},
                {getString(R.string.transfer_from), ""},
                {getString(R.string.account_number), spPayFrom.getSelectedItem().toString()},
                {getString(R.string.transfer_date), transferDate},
                {getString(R.string.amount), etAmount.getText().toString()},
                {getString(R.string.remark), etRemark.getText().toString()}
        };
        new MaterialDialog.Builder().init(getActivity())
                .setTitle(R.string.review_transfer)
                .setMessage(Utils.generateFormString(data))
                .setPositiveButton(R.string.transfer,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                makeTransfer();
                            }
                        })
                .setNegativeButton(R.string.dialog_action_back,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .createMaterialDialog()
                .show();
    }

    @Override
    public void makeTransfer() {
        TransferPayload transferPayload = new TransferPayload();
        transferPayload.setFromAccountId(fromAccountOption.getAccountId());
        transferPayload.setFromClientId(fromAccountOption.getClientId());
        transferPayload.setFromAccountType(fromAccountOption.getAccountType().getId());
        transferPayload.setFromOfficeId(fromAccountOption.getOfficeId());
        transferPayload.setToOfficeId(beneficiaryAccountOption.getOfficeId());
        transferPayload.setToAccountId(beneficiaryAccountOption.getAccountId());
        transferPayload.setToClientId(beneficiaryAccountOption.getClientId());
        transferPayload.setToAccountType(beneficiaryAccountOption.getAccountType().getId());
        transferPayload.setTransferDate(transferDate);
        transferPayload.setTransferAmount(Double.parseDouble(etAmount.getText().toString()));
        transferPayload.setTransferDescription(etRemark.getText().toString());

        presenter.makeTransfer(transferPayload);
    }

    @Override
    public void showThirdPartyTransferTemplate(AccountOptionsTemplate accountOptionsTemplate) {
        this.accountOptionsTemplate = accountOptionsTemplate;
        listPayFrom.addAll(presenter.getAccountNumbersFromAccountOptions(accountOptionsTemplate.
                getFromAccountOptions()));
        payFromAdapter.notifyDataSetChanged();

    }

    @Override
    public void showBeneficiaryList(List<Beneficiary> beneficiaries) {
        listBeneficiary.addAll(presenter.getAccountNumbersFromBeneficiaries(beneficiaries));
        beneficiaryAdapter.notifyDataSetChanged();
    }

    @Override
    public void showTransferredSuccessfully() {
        Toaster.show(rootView, getString(R.string.transferred_Successfully));
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showError(String msg) {
        Toaster.show(rootView, msg);
    }

    @Override
    public void showProgress() {
        layoutMakeTransfer.setVisibility(View.GONE);
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        hideProgressBar();
        layoutMakeTransfer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_beneficiary:
                beneficiaryAccountOption = presenter.searchAccount(accountOptionsTemplate.
                        getFromAccountOptions(), beneficiaryAdapter.getItem(position));
                break;
            case R.id.sp_pay_from:
                fromAccountOption = accountOptionsTemplate.getFromAccountOptions().get(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
