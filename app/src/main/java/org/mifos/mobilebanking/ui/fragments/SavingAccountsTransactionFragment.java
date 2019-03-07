package org.mifos.mobilebanking.ui.fragments;

import android.content.Intent;
import android.net.Uri;
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

import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.accounts.savings.SavingsWithAssociations;
import org.mifos.mobilebanking.models.accounts.savings.Transactions;
import org.mifos.mobilebanking.presenters.SavingAccountsTransactionPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.adapters.SavingAccountsTransactionListAdapter;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.SavingAccountsTransactionView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.DateHelper;
import org.mifos.mobilebanking.utils.DatePick;
import org.mifos.mobilebanking.utils.MFDatePicker;
import org.mifos.mobilebanking.utils.Network;
import org.mifos.mobilebanking.utils.Toaster;

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

    @BindView(R.id.layout_error)
    View layoutError;

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

    private SweetUIErrorHandler sweetUIErrorHandler;
    private View rootView;
    private long savingsId;
    private long startDateFromPicker , endDateFromPicker;
    private List<Transactions> transactionsList, dummyTransactionList;
    private SavingsWithAssociations savingsWithAssociations;
    private DatePick datePick;
    private MFDatePicker mfDatePicker;

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

        sweetUIErrorHandler = new SweetUIErrorHandler(getContext(), rootView);
        showUserInterface();
        if (savedInstanceState == null) {
            savingAccountsTransactionPresenter.loadSavingsWithAssociations(savingsId);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.SAVINGS_ACCOUNTS, savingsWithAssociations);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            showSavingAccountsDetail((SavingsWithAssociations) savedInstanceState.
                    getParcelable(Constants.SAVINGS_ACCOUNTS));
        }
    }

    /**
     * Setting up basic components
     */
    @Override
    public void showUserInterface() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSavingAccountsTransaction.setHasFixedSize(true);
        rvSavingAccountsTransaction.setLayoutManager(layoutManager);
        rvSavingAccountsTransaction.setAdapter(transactionListAdapter);

        radioGroup.setOnCheckedChangeListener(this);
        mfDatePicker = MFDatePicker.newInstance(this, MFDatePicker.ALL_DAYS);
    }

    /**
     * Provides with {@code savingsWithAssociations} fetched from server which is used to update the
     * {@code transactionListAdapter}
     * @param savingsWithAssociations Contains {@link Transactions} for given Savings account.
     */
    @Override
    public void showSavingAccountsDetail(SavingsWithAssociations
                                                     savingsWithAssociations) {
        layoutAccount.setVisibility(View.VISIBLE);
        this.savingsWithAssociations = savingsWithAssociations;
        transactionsList = savingsWithAssociations.getTransactions();

        if (transactionsList != null && !transactionsList.isEmpty()) {
            transactionListAdapter.setContext(getContext());
            transactionListAdapter.
                    setSavingAccountsTransactionList(transactionsList);
        } else {
            sweetUIErrorHandler.showSweetEmptyUI(getString(R.string.transactions),
                    R.drawable.ic_compare_arrows_black_24dp, rvSavingAccountsTransaction,
                    layoutError);
        }
    }

    /**
     * It is called whenever any error occurs while executing a request
     * @param message Error message that tells the user about the problem.
     */
    @Override
    public void showErrorFetchingSavingAccountsDetail(String message) {
        if (!Network.isConnected(getActivity())) {
            sweetUIErrorHandler.showSweetNoInternetUI(rvSavingAccountsTransaction, layoutError);
        } else {
            sweetUIErrorHandler.showSweetErrorUI(message, rvSavingAccountsTransaction, layoutError);
        }
    }

    @OnClick(R.id.btn_try_again)
    public void retryClicked() {
        if (Network.isConnected(getContext())) {
            sweetUIErrorHandler.hideSweetErrorLayoutUI(rvSavingAccountsTransaction, layoutError);
            savingAccountsTransactionPresenter.loadSavingsWithAssociations(savingsId);
        } else {
            Toast.makeText(getContext(), getString(R.string.internet_not_connected),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Provides with a filtered list according to the constraints used in {@code filter()} function
     * @param list
     */
    @Override
    public void showFilteredList(List<Transactions> list) {
        Toaster.show(rootView, getString(R.string.filtered));
        transactionListAdapter.
                setSavingAccountsTransactionList(list);
    }

    /**
     * Opens up Phone Dialer
     */
    @OnClick(R.id.tv_help_line_number)
    void dialHelpLineNumber() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + getString(R.string.help_line_number)));
        startActivity(intent);
    }

    /**
     * Shows a {@link DialogFragment} for selecting a starting Date
     */
    @OnClick(R.id.tv_start_date)
    public void startDatePick() {
        datePick = DatePick.START;
        mfDatePicker.show(getActivity().getSupportFragmentManager(), Constants
                .DFRAG_DATE_PICKER);
    }

    /**
     * Shows a {@link DialogFragment} for selecting an ending Date
     */
    @OnClick(R.id.tv_end_date)
    public void endDatePick() {
        datePick = DatePick.END;
        mfDatePicker.show(getActivity().getSupportFragmentManager(), Constants
                .DFRAG_DATE_PICKER);
    }

    /**
     * A CallBack for {@link MFDatePicker} which provides us with the date selected from the
     * {@link android.app.DatePickerDialog} by {@code mfDatePicker}
     * @param date Date selected by user in {@link String}
     */
    @Override
    public void onDatePicked(String date) {
        long timeInMillis = DateHelper.getDateAsLongFromString(date, "dd-MM-yyyy");
        if (datePick == DatePick.START) {
            startDateFromPicker = timeInMillis;
            tvEndDate.setEnabled(true);
            tvStartDate.setText(DateHelper.getDateAsStringFromLong(timeInMillis));

        } else {
            endDateFromPicker = timeInMillis;
            tvEndDate.setText(DateHelper.getDateAsStringFromLong(timeInMillis));
        }
    }

    /**
     * Filters the {@code transactionsList} according to the {@code startDateFromPicker} and
     * {@code endDateFromPicker} chosen.
     */
    @OnClick(R.id.btn_custom_filter)
    public void datePickerFilter() {
        String startDateText = getContext().getResources().getString(R.string.start_date);
        String endDateText = getContext().getResources().getString(R.string.end_date);

        if (!tvStartDate.getText().equals(startDateText) && isEndDateLargeThanStartDate() &&
                !tvEndDate.getText().equals(endDateText)) {
            if (radioGroup.getCheckedRadioButtonId() != -1) {
                radioGroup.clearCheck();
            }
            filter(startDateFromPicker, endDateFromPicker);
        } else if (!isEndDateLargeThanStartDate()) {
            Toaster.show(rootView, getString(R.string.end_date_must_be_greater));
        } else {
            Toast.makeText(getContext(), getResources().getText(R.string.select_date),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks if {@code startDateFromPicker} is less than {@code endDateFromPicker}
     * @return Returns true if {@code startDateFromPicker} is less than {@code endDateFromPicker}
     */
    private boolean isEndDateLargeThanStartDate() {
        return startDateFromPicker <= endDateFromPicker;
    }

    /**
     * Removes all filters applied to {@code transactionList}
     */
    @OnClick(R.id.btn_all)
    public void resetFilter() {
        radioGroup.clearCheck();
        tvStartDate.setText(getContext().getResources().
                getText(R.string.start_date));
        tvEndDate.setText(getContext().getResources().
                getText(R.string.end_date));
        transactionListAdapter.
                setSavingAccountsTransactionList(transactionsList);
        tvEndDate.setEnabled(false);
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
                resetFilter();
                filter(DateHelper.subtractWeeks(4), System.currentTimeMillis());
                break;
            case R.id.rb_three_months:
                resetFilter();
                filter(DateHelper.subtractMonths(3), System.currentTimeMillis());
                break;
            case R.id.rb_six_months:
                resetFilter();
                filter(DateHelper.subtractMonths(6), System.currentTimeMillis());
                break;
        }

    }

    /**
     * Will filter {@code transactionsList} according to {@code startDate} and {@code endDate}
     * @param startDate Starting date
     * @param endDate Ending date
     */
    private void filter(long startDate , long endDate) {

        dummyTransactionList = new ArrayList<>(transactionsList);
        savingAccountsTransactionPresenter.filterTransactionList(dummyTransactionList,
                                                                    startDate, endDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgress();
        savingAccountsTransactionPresenter.detachView();
    }
}
