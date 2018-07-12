package org.mifos.mobilebanking.ui.fragments;

/*
 * Created by saksham on 30/June/2018
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.models.accounts.savings.SavingsAccountApplicationPayload;
import org.mifos.mobilebanking.models.accounts.savings.SavingsAccountUpdatePayload;
import org.mifos.mobilebanking.models.accounts.savings.SavingsWithAssociations;
import org.mifos.mobilebanking.models.templates.savings.ProductOptions;
import org.mifos.mobilebanking.models.templates.savings.SavingsAccountTemplate;
import org.mifos.mobilebanking.presenters.SavingsAccountApplicationPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.enums.SavingsAccountState;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.SavingsAccountApplicationView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.DateHelper;
import org.mifos.mobilebanking.utils.MFDatePicker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SavingsAccountApplicationFragment extends BaseFragment
        implements SavingsAccountApplicationView {

    @BindView(R.id.sp_product_id)
    Spinner spProductId;

    @BindView(R.id.tv_submission_date)
    TextView tvSubmissionDate;

    @BindView(R.id.tv_client_name)
    TextView tvClientName;

    @Inject
    SavingsAccountApplicationPresenter presenter;

    @Inject
    PreferencesHelper preferencesHelper;

    private View rootView;
    private SavingsAccountState state;
    private SavingsWithAssociations savingsWithAssociations;
    private ArrayAdapter<String> productIdAdapter;
    private SavingsAccountTemplate template;
    private List<ProductOptions> productOptions;
    private List<String> productIdList = new ArrayList<>();

    public static SavingsAccountApplicationFragment newInstance(
            SavingsAccountState state, SavingsWithAssociations savingsWithAssociations) {
        SavingsAccountApplicationFragment fragment = new SavingsAccountApplicationFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.SAVINGS_ACCOUNT_STATE, state);
        bundle.putParcelable(Constants.SAVINGS_ACCOUNTS, savingsWithAssociations);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            state = (SavingsAccountState) getArguments()
                    .getSerializable(Constants.SAVINGS_ACCOUNT_STATE);
            savingsWithAssociations = getArguments().getParcelable(Constants.SAVINGS_ACCOUNTS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_savings_account_application, container,
                false);
        ButterKnife.bind(this, rootView);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);

        presenter.attachView(this);
        presenter.loadSavingsAccountApplicationTemplate(preferencesHelper.getClientId(), state);
        return rootView;
    }

    @Override
    public void showUserInterfaceSavingAccountApplication(SavingsAccountTemplate template) {
        showUserInterface(template);
    }

    @Override
    public void showSavingsAccountApplicationSuccessfully() {
        showMessage(getString(R.string.new_saving_account_created_successfully));
        getActivity().finish();
    }

    @Override
    public void showUserInterfaceSavingAccountUpdate(SavingsAccountTemplate template) {
        showUserInterface(template);
        getActivity().setTitle(getString(R.string.string_savings_account,
                getString(R.string.update)));
        spProductId.setSelection(productIdAdapter.getPosition(savingsWithAssociations
                .getSavingsProductName()));
    }

    @Override
    public void showSavingsAccountUpdateSuccessfully() {
        showMessage(getString(R.string.saving_account_updated_successfully));
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public void showUserInterface(SavingsAccountTemplate template) {
        this.template = template;
        productOptions = template.getProductOptions();
        for (ProductOptions item : productOptions) {
            productIdList.add(item.getName());
        }

        tvClientName.setText(template.getClientName());

        productIdAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                productIdList);
        productIdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProductId.setAdapter(productIdAdapter);

        tvSubmissionDate.setText(MFDatePicker.getDatePickedAsString());
    }

    public void submitSavingsAccountApplication() {
        SavingsAccountApplicationPayload payload = new SavingsAccountApplicationPayload();
        payload.setClientId(template.getClientId());
        payload.setProductId(productOptions.get(spProductId.getSelectedItemPosition()).getId());
        payload.setSubmittedOnDate(DateHelper.getSpecificFormat(DateHelper.FORMAT_dd_MMMM_yyyy,
                MFDatePicker.getDatePickedAsString()));
        presenter.submitSavingsAccountApplication(payload);
    }

    public void updateSavingAccount() {
        SavingsAccountUpdatePayload payload = new SavingsAccountUpdatePayload();
        payload.setClientId(template.getClientId());
        payload.setProductId(productOptions.get(spProductId.getSelectedItemPosition()).getId());
        presenter.updateSavingsAccount(savingsWithAssociations.getAccountNo(), payload);
    }

    @OnClick(R.id.btn_submit)
    void onSubmit() {
        if (state == SavingsAccountState.CREATE) {
            submitSavingsAccountApplication();
        } else {
            updateSavingAccount();
        }
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        showMifosProgressDialog(getString(R.string.progress_message_loading));
    }

    @Override
    public void hideProgress() {
        hideMifosProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgress();
        presenter.detachView();
    }
}
