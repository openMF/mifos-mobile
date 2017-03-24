package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.savings.SavingsWithAssociations;
import org.mifos.selfserviceapp.models.accounts.savings.Transactions;
import org.mifos.selfserviceapp.presenters.SavingAccountsTransactionPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.SavingAccountsTransactionListAdapter;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.SavingAccountsTransactionView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.DateHelper;
import org.mifos.selfserviceapp.utils.DatePick;
import org.mifos.selfserviceapp.utils.MFDatePicker;
import org.mifos.selfserviceapp.utils.Toaster;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilpreet on 6/3/17.
 */

public class SavingAccountsTransactionFragment extends BaseFragment
        implements SavingAccountsTransactionView , RadioGroup.OnCheckedChangeListener ,
        MFDatePicker.OnDatePickListener {

    @BindView(R.id.ll_account)
    LinearLayout layoutAccount;

    @BindView(R.id.ll_error)
    View layoutError;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.tv_start_date)
    TextView tvStartDate;

    @BindView(R.id.tv_end_date)
    TextView tvEndDate;

    @BindView(R.id.rv_saving_accounts_transaction)
    RecyclerView rvSavingAccountsTransaction;

    @BindView(R.id.rg_transaction_filter)
    RadioGroup radioGroup;

    @Inject
    SavingAccountsTransactionListAdapter transactionListAdapter;

    @Inject
    SavingAccountsTransactionPresenter savingAccountsTransactionPresenter;

    private View rootView;
    private long savingsId;
    private long startDateFromPicker , endDateFromPicker;
    private List<Transactions> transactionsList, dummyTransactionList;
    private DatePick datePick;
    private DialogFragment mfDatePicker;

    public static SavingAccountsTransactionFragment newInstance(long savingsId) {
        SavingAccountsTransactionFragment fragment = new SavingAccountsTransactionFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.SAVINGS_ID, savingsId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        setToolbarTitle(getString(R.string.saving_account_transactions_details));
        if (getArguments() != null) {
            savingsId = getArguments().getLong(Constants.SAVINGS_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_saving_account_transactions,
                container, false);

        ButterKnife.bind(this, rootView);
        savingAccountsTransactionPresenter.attachView(this);

        showUserInterface();
        savingAccountsTransactionPresenter.loadSavingsWithAssociations(savingsId);

        return rootView;
    }

    @Override
    public void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSavingAccountsTransaction.setHasFixedSize(true);
        rvSavingAccountsTransaction.setLayoutManager(layoutManager);
        rvSavingAccountsTransaction.setAdapter(transactionListAdapter);

        radioGroup.setOnCheckedChangeListener(this);
        mfDatePicker = MFDatePicker.newInsance(this);
    }

    @Override
    public void showSavingAccountsDetail(SavingsWithAssociations
                                                     savingsWithAssociations) {
        layoutAccount.setVisibility(View.VISIBLE);

        transactionsList = savingsWithAssociations.getTransactions();
        transactionListAdapter.setContext(getContext());
        transactionListAdapter.
                setSavingAccountsTransactionList(transactionsList);

    }

    @Override
    public void showErrorFetchingSavingAccountsDetail(String message) {
        layoutAccount.setVisibility(View.GONE);
        layoutError.setVisibility(View.VISIBLE);
        tvStatus.setText(message);
    }

    @Override
    public void showFilteredList(List<Transactions> list) {
        Toaster.show(rootView, "" + getContext().getResources().getText(R.string.filterd));
        transactionListAdapter.
                setSavingAccountsTransactionList(list);
    }

    @OnClick(R.id.tv_start_date)
    public void startDatePick() {
        datePick = DatePick.START;
        mfDatePicker.show(getActivity().getSupportFragmentManager(), Constants
                .DFRAG_DATE_PICKER);
    }

    @OnClick(R.id.tv_end_date)
    public void endDatePick() {
        datePick = DatePick.END;
        mfDatePicker.show(getActivity().getSupportFragmentManager(), Constants
                .DFRAG_DATE_PICKER);
    }

    @Override
    public void onDatePicked(String date) {
        long timeInMillis = DateHelper.getDateAsLongFromString(date, "dd-MM-yyyy");
        if (datePick == DatePick.START) {
            startDateFromPicker = timeInMillis;
            tvStartDate.setText(DateHelper.getDateAsStringFromLong(timeInMillis));

        } else {
            endDateFromPicker = timeInMillis;
            tvEndDate.setText(DateHelper.getDateAsStringFromLong(timeInMillis));
        }
    }

    @OnClick(R.id.btn_custom_filter)
    public void datePickerFilter() {
        String startDateText = getContext().getResources().getString(R.string.start_date);
        String endDateText = getContext().getResources().getString(R.string.end_date);

        if (!tvStartDate.getText().equals(startDateText) &&
                !tvEndDate.getText().equals(endDateText)) {
            filter(startDateFromPicker, endDateFromPicker);
        } else {
            Toast.makeText(getContext(), getResources().getText(R.string.select_date),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_all)
    public void resetFilter() {
        radioGroup.clearCheck();
        tvStartDate.setText(getContext().getResources().
                getText(R.string.start_date));
        tvEndDate.setText(getContext().getResources().
                getText(R.string.end_date));
        transactionListAdapter.
                setSavingAccountsTransactionList(transactionsList);
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (group.getCheckedRadioButtonId()) {
            case R.id.rb_four_weeks:
                filter(DateHelper.subtractWeeks(4), System.currentTimeMillis());
                break;
            case R.id.rb_three_months:
                filter(DateHelper.subtractMonths(3), System.currentTimeMillis());
                break;
            case R.id.rb_six_months:
                filter(DateHelper.subtractMonths(6), System.currentTimeMillis());
                break;
        }

    }

    private void filter(long startDate , long endDate) {

        dummyTransactionList = new ArrayList<>(transactionsList);
        savingAccountsTransactionPresenter.filterTransactionList(dummyTransactionList ,
                                                                    startDate , endDate);
    }

}
