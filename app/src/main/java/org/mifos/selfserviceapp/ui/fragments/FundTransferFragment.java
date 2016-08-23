package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferRequest;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferResponse;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferTemplate;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferTemplateResponse;
import org.mifos.selfserviceapp.presenters.FundTransferPresenter;
import org.mifos.selfserviceapp.ui.activities.BaseActivity;
import org.mifos.selfserviceapp.ui.views.FundTransferView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vishwajeet
 * @since 22/8/16.
 */

public class FundTransferFragment extends Fragment implements FundTransferView {

    @Inject
    FundTransferPresenter mFundTransferPresenter;

    private View rootView;
    private long fromClientId;
    private long fromOfficeId;
    private long fromAccountId;
    private long fromAccountType;
    private long toClientId;
    private long toOfficeId;
    private long toAccountId;
    private long toAccountType;

    @BindView(R.id.tv_transfer_date)
    TextView tvTransactionDate;
    @BindView(R.id.et_transfer_date)
    EditText etTransferDate;
    @BindView(R.id.tv_transfer_description)
    TextView tvTransferDescription;
    @BindView(R.id.et_transfer_description)
    EditText etTransferDescription;
    @BindView(R.id.tv_amount_transfer)
    TextView tvAmountTransfer;
    @BindView(R.id.et_amount_transfer)
    EditText etAmountTransfer;
    @BindView(R.id.tv_from_account)
    TextView tvFromAccount;
    @BindView(R.id.sp_from_account)
    Spinner spFromAccount;
    @BindView(R.id.tv_to_account)
    TextView tvToAccount;
    @BindView(R.id.sp_to_account)
    Spinner spToAccount;
    @BindView(R.id.bt_transfer_amount)
    Button btTransferAmount;
    @BindView(R.id.bt_cancel_payment)
    Button btCancelPayment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fund_transfer, container, false);
        ButterKnife.bind(this, rootView);

        mFundTransferPresenter.attachView(this);

        mFundTransferPresenter.loadAccountTransferTemplate();

        return rootView;
    }

    @Override
    public void showFundTransferTemplate(final FundTransferTemplateResponse fundTransferTemplate) {
        List<FundTransferTemplate> fromAccountNumber = fundTransferTemplate.getFromAccountOptions();
        List<FundTransferTemplate> toAccountNumber = fundTransferTemplate.getToAccountOptions();

        String[] fromAccountItems = new String[fromAccountNumber.size()];
        String[] toAccountItems = new String[toAccountNumber.size()];

        for (int i = 0; i < fromAccountNumber.size(); i++) {
            fromAccountItems[i] = fromAccountNumber.get(i).getAccountNo();
        }
        ArrayAdapter<String> fromAccountAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, fromAccountItems);
        spFromAccount.setAdapter(fromAccountAdapter);
        spFromAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long
                    id) {
                fromClientId = fundTransferTemplate.getFromAccountOptions().
                        get(position).getClientId();
                fromAccountId = fundTransferTemplate.getFromAccountOptions().
                        get(position).getAccountId();
                fromAccountType = fundTransferTemplate.getFromAccountOptions().
                        get(position).getAccountType().getId();
                fromOfficeId = fundTransferTemplate.getFromAccountOptions().
                        get(position).getOfficeId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        for (int i = 0; i < toAccountNumber.size(); i++) {
            toAccountItems[i] = toAccountNumber.get(i).getAccountNo();
        }
        ArrayAdapter<String> toAccountAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, toAccountItems);
        spToAccount.setAdapter(toAccountAdapter);
        spToAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long
                    id) {
                toClientId = fundTransferTemplate.getToAccountOptions().
                        get(position).getClientId();
                toAccountId = fundTransferTemplate.getToAccountOptions().
                        get(position).getAccountId();
                toAccountType = fundTransferTemplate.getToAccountOptions().
                        get(position).getAccountType().getId();
                toOfficeId = fundTransferTemplate.getToAccountOptions().
                        get(position).getOfficeId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void submitPayment(FundTransferResponse fundTransferResponse) {
        Toast.makeText(getActivity(), "You have transferred fund successfully with resource id " +
                fundTransferResponse.getResourceId() + " savings Id " + fundTransferResponse.getSavingsId(), Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.bt_transfer_amount)
    public void fundTransfer() {
        FundTransferRequest fundTransferRequest = new FundTransferRequest();
        fundTransferRequest.setFromClientId(fromClientId);
        fundTransferRequest.setDateFormat("dd MMMM yyyy");
        fundTransferRequest.setFromOfficeId(fromOfficeId);
        fundTransferRequest.setFromAccountId(fromAccountId);
        fundTransferRequest.setFromAccountType(fromAccountType);
        fundTransferRequest.setToClientId(toClientId);
        fundTransferRequest.setToOfficeId(toOfficeId);
        fundTransferRequest.setToAccountType(toAccountType);
        fundTransferRequest.setToAccountId(toAccountId);
        fundTransferRequest.setLocale("en");
        fundTransferRequest.setTransferAmount(etAmountTransfer.getText().toString());
        fundTransferRequest.setTransferDescription(etTransferDescription.getText().toString());
        fundTransferRequest.setTransferDate(etTransferDate.getText().toString());

        mFundTransferPresenter.submitTransfer(fundTransferRequest);
    }

    @OnClick(R.id.bt_cancel_payment)
    public void onCancelPaymentButtonClicked() {
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void showErrorFetchingFundTransfers(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onDestroyView() {
        mFundTransferPresenter.detachView();
        super.onDestroyView();
    }
}
