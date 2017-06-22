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
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.payload.SavingsTransferPayload;
import org.mifos.selfserviceapp.models.templates.account.AccountOption;
import org.mifos.selfserviceapp.models.templates.account.AccountOptionsTemplate;
import org.mifos.selfserviceapp.presenters.SavingsMakeTransferPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.SavingsMakeTransferMvpView;
import org.mifos.selfserviceapp.utils.Constants;
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
 * Created by Rajan Maurya on 10/03/17.
 */
public class SavingsMakeTransferFragment extends BaseFragment implements
        SavingsMakeTransferMvpView, AdapterView.OnItemSelectedListener {

    @BindView(R.id.sp_pay_to)
    Spinner spPayTo;

    @BindView(R.id.sp_pay_from)
    Spinner spPayFrom;

    @NotEmpty
    @BindView(R.id.et_amount)
    EditText etAmount;


    @BindView(R.id.tv_transfer_date)
    TextView tvTransferDate;

    @NotEmpty
    @BindView(R.id.et_remark)
    EditText etRemark;

    @BindView(R.id.ll_make_transfer)
    LinearLayout layoutMakeTransfer;

    @Inject
    SavingsMakeTransferPresenter savingsMakeTransferPresenter;

    View rootView;

    private List<String> listPayTo = new ArrayList<>();
    private List<String> listPayFrom = new ArrayList<>();

    private ArrayAdapter<String> payToAdapter;
    private ArrayAdapter<String> payFromAdapter;

    private String transferDate;
    private AccountOption toAccountOption, fromAccountOption;
    private AccountOptionsTemplate accountOptionsTemplate;
    private String transferType;
    private long accountId;
    private Validator validator;

    public static SavingsMakeTransferFragment newInstance(long accountId, String transferType) {
        SavingsMakeTransferFragment transferFragment = new SavingsMakeTransferFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.ACCOUNT_ID, accountId);
        args.putString(Constants.TRANSFER_TYPE, transferType);
        transferFragment.setArguments(args);
        return transferFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            accountId = getArguments().getLong(Constants.ACCOUNT_ID);
            transferType = getArguments().getString(Constants.TRANSFER_TYPE);
        }

        validator = setValidator();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_savings_make_transfer, container, false);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        setToolbarTitle(getString(R.string.transfer));
        ButterKnife.bind(this, rootView);
        savingsMakeTransferPresenter.attachView(this);

        showUserInterface();
        savingsMakeTransferPresenter.loanAccountTransferTemplate();

        return rootView;
    }

    @OnClick(R.id.btn_review_transfer)
    void reviewTransfer() {


        if (etAmount.getText().toString().equals(".")
                || etAmount.getText().toString().equals("0") ) {
            showToaster(getString(R.string.enter_amount));
            return;
        }

        if (spPayTo.getSelectedItem().toString().equals(spPayFrom.getSelectedItem().toString())) {
            showToaster(getString(R.string.error_same_account_transfer));
            return;
        }


        validator.validate(true);
    }

    @OnClick(R.id.btn_cancel_transfer)
    void cancelTransfer() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showUserInterface() {
        payFromAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                listPayFrom);
        payFromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPayFrom.setAdapter(payFromAdapter);
        spPayFrom.setOnItemSelectedListener(this);

        payToAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                listPayTo);
        payToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPayTo.setAdapter(payToAdapter);
        spPayTo.setOnItemSelectedListener(this);

        tvTransferDate.setText(MFDatePicker.getDatePickedAsString());
        transferDate = DateHelper.getSpecificFormat(DateHelper.FORMAT_dd_MMMM_yyyy,
                tvTransferDate.getText().toString());
    }

    @Override
    public void makeTransfer() {
        SavingsTransferPayload savingsTransferPayload = new SavingsTransferPayload();
        savingsTransferPayload.setFromAccountId(fromAccountOption.getAccountId());
        savingsTransferPayload.setFromClientId(fromAccountOption.getClientId());
        savingsTransferPayload.setFromAccountType(fromAccountOption.getAccountType().getId());
        savingsTransferPayload.setFromOfficeId(fromAccountOption.getOfficeId());
        savingsTransferPayload.setToOfficeId(toAccountOption.getOfficeId());
        savingsTransferPayload.setToAccountId(toAccountOption.getAccountId());
        savingsTransferPayload.setToClientId(toAccountOption.getClientId());
        savingsTransferPayload.setToAccountType(toAccountOption.getAccountType().getId());
        savingsTransferPayload.setTransferDate(transferDate);
        savingsTransferPayload.setTransferAmount(Double.parseDouble(etAmount.getText().toString()));
        savingsTransferPayload.setTransferDescription(etRemark.getText().toString());

        savingsMakeTransferPresenter.makeTransfer(savingsTransferPayload);
    }

    @Override
    public void showSavingsAccountTemplate(AccountOptionsTemplate accountOptionsTemplate) {
        this.accountOptionsTemplate = accountOptionsTemplate;
        listPayFrom.addAll(savingsMakeTransferPresenter.getAccountNumbers(
                accountOptionsTemplate.getFromAccountOptions()));
        listPayTo.addAll(savingsMakeTransferPresenter.getAccountNumbers(
                accountOptionsTemplate.getToAccountOptions()));
        payToAdapter.notifyDataSetChanged();
        payFromAdapter.notifyDataSetChanged();
    }

    @Override
    public void showTransferredSuccessfully() {
        Toast.makeText(getActivity(), getString(R.string.transferred_Successfully),
                Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showToaster(String message) {
        Toaster.show(rootView, message);
    }

    @Override
    public void showError(String message) {
        Toaster.show(rootView, message);
    }

    @Override
    public void showProgressDialog() {
        showMifosProgressDialog(getString(R.string.making_transfer));
    }

    @Override
    public void hideProgressDialog() {
        hideMifosProgressDialog();
    }

    @Override
    public void showProgress() {
        layoutMakeTransfer.setVisibility(View.GONE);
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        layoutMakeTransfer.setVisibility(View.VISIBLE);
        hideProgressBar();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_pay_to:
                toAccountOption = accountOptionsTemplate.getToAccountOptions().get(position);
                break;
            case R.id.sp_pay_from:
                fromAccountOption = accountOptionsTemplate.getFromAccountOptions().get(position);
                break;
        }

        switch (transferType) {
            case Constants.TRANSFER_PAY_TO:
                setToolbarTitle(getString(R.string.deposit));
                toAccountOption = savingsMakeTransferPresenter
                        .searchAccount(accountOptionsTemplate.getToAccountOptions(), accountId);
                spPayTo.setSelection(accountOptionsTemplate.getToAccountOptions()
                        .indexOf(toAccountOption));
                spPayTo.setEnabled(false);
                break;
            case Constants.TRANSFER_PAY_FROM:
                setToolbarTitle(getString(R.string.transfer));
                fromAccountOption = savingsMakeTransferPresenter
                        .searchAccount(accountOptionsTemplate.getFromAccountOptions(), accountId);
                spPayFrom.setSelection(accountOptionsTemplate.getFromAccountOptions()
                        .indexOf(fromAccountOption));
                spPayFrom.setEnabled(false);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgress();
        hideMifosProgressDialog();
        savingsMakeTransferPresenter.detachView();
    }
    private Validator setValidator() {
        Validator returnValidator = new Validator(this);
        returnValidator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                String[][] data = {
                        {getString(R.string.transfer_to), ""},
                        {getString(R.string.account_number), spPayTo.getSelectedItem().toString()},
                        {getString(R.string.transfer_from), ""},
                        {getString(R.string.account_number),
                                spPayFrom.getSelectedItem().toString()},
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
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(getContext());


                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {

                        showToaster(message);

                    }
                }
            }


        });

        return returnValidator;

    }
}



