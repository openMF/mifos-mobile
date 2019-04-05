package org.mifos.mobile.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler;

import org.mifos.mobile.R;
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations;
import org.mifos.mobile.models.accounts.savings.Transactions;
import org.mifos.mobile.presenters.SavingAccountsTransactionPresenter;
import org.mifos.mobile.ui.activities.base.BaseActivity;
import org.mifos.mobile.ui.adapters.SavingAccountsTransactionListAdapter;
import org.mifos.mobile.ui.fragments.base.BaseFragment;
import org.mifos.mobile.ui.views.SavingAccountsTransactionView;
import org.mifos.mobile.utils.Constants;
import org.mifos.mobile.utils.DateHelper;
import org.mifos.mobile.utils.DatePick;
import org.mifos.mobile.utils.MFDatePicker;
import org.mifos.mobile.utils.MaterialDialog;
import org.mifos.mobile.utils.Network;
import org.mifos.mobile.utils.Toaster;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilpreet on 6/3/17.
 */

public class SavingAccountsTransactionFragment extends BaseFragment
        implements SavingAccountsTransactionView, MFDatePicker.OnDatePickListener {

    @BindView(R.id.ll_account)
    LinearLayout layoutAccount;

    @BindView(R.id.layout_error)
    View layoutError;

    TextView tvStartDate;

    TextView tvEndDate;

    @BindView(R.id.rv_saving_accounts_transaction)
    RecyclerView rvSavingAccountsTransaction;

    @Inject
    SavingAccountsTransactionListAdapter transactionListAdapter;

    @Inject
    SavingAccountsTransactionPresenter savingAccountsTransactionPresenter;

    private SweetUIErrorHandler sweetUIErrorHandler;
    private View rootView;
    private long savingsId;
    private long startDateFromPicker, endDateFromPicker;
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
        setHasOptionsMenu(true);
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

        mfDatePicker = MFDatePicker.newInstance(this, MFDatePicker.ALL_DAYS);
    }

    /**
     * Provides with {@code savingsWithAssociations} fetched from server which is used to update the
     * {@code transactionListAdapter}
     *
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
     *
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
     * A CallBack for {@link MFDatePicker} which provides us with the date selected from the
     * {@link android.app.DatePickerDialog} by {@code mfDatePicker}
     *
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
     * Checks if {@code startDateFromPicker} is less than {@code endDateFromPicker}
     *
     * @return Returns true if {@code startDateFromPicker} is less than {@code endDateFromPicker}
     */
    private boolean isEndDateLargeThanStartDate() {
        return startDateFromPicker <= endDateFromPicker;
    }

    @Override
    public void showProgress() {
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        hideProgressBar();
    }

    /**
     * Will filter {@code transactionsList} according to {@code startDate}
     * and {@code endDate} and {@code type}
     *
     * @param startDate Starting date
     * @param endDate   Ending date
     * @param type      Deposit/ Withdrawal type
     */
    private void filter(long startDate, long endDate, int type) {
        dummyTransactionList = new ArrayList<>(transactionsList);
        savingAccountsTransactionPresenter.filterTransactionList(dummyTransactionList,
                startDate, endDate, type);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgress();
        savingAccountsTransactionPresenter.detachView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account, menu);
        menu.findItem(R.id.menu_filter_savings).setVisible(true);
        menu.findItem(R.id.menu_filter_loan).setVisible(false);
        menu.findItem(R.id.menu_filter_shares).setVisible(false);
        menu.findItem(R.id.menu_search_saving).setVisible(false);
        menu.findItem(R.id.menu_search_loan).setVisible(false);
        menu.findItem(R.id.menu_search_share).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter_savings:
                showFilterDialog();
                break;
        }
        return true;
    }

    private void showFilterDialog() {
        String title = getString(R.string.filter_transaction);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_filter_dialog, null);

        final CheckBox cbPeriod = dialogView.findViewById(R.id.cb_period);
        final CheckBox cbType = dialogView.findViewById(R.id.cb_type);
        final RadioGroup rgPeriod = dialogView.findViewById(R.id.rg_date_filter);
        final RadioGroup rgType = dialogView.findViewById(R.id.rg_type_filter);
        tvStartDate = dialogView.findViewById(R.id.tv_start_date);
        tvEndDate = dialogView.findViewById(R.id.tv_end_date);

        rgPeriod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                cbPeriod.setChecked(true);
                if (radioGroup.getCheckedRadioButtonId() == R.id.rb_date) {
                    tvStartDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            datePick = DatePick.START;
                            mfDatePicker.show(getActivity().getSupportFragmentManager(), Constants
                                    .DFRAG_DATE_PICKER);
                        }
                    });
                    tvEndDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            datePick = DatePick.END;
                            mfDatePicker.show(getActivity().getSupportFragmentManager(), Constants
                                    .DFRAG_DATE_PICKER);
                        }
                    });
                } else {
                    tvStartDate.setOnClickListener(null);
                    tvEndDate.setOnClickListener(null);
                }
            }
        });
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                cbType.setChecked(true);
            }
        });
        cbPeriod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    rgPeriod.clearCheck();
                    cbPeriod.setChecked(false);
                } else {
                    if (rgPeriod.getCheckedRadioButtonId() == -1) {
                        RadioButton temp = dialogView.findViewById(R.id.rb_date);
                        temp.setChecked(true);
                    }
                }
            }
        });
        cbType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    rgType.clearCheck();
                    cbType.setChecked(false);
                } else {
                    if (rgType.getCheckedRadioButtonId() == -1) {
                        RadioButton temp = dialogView.findViewById(R.id.rb_deposit);
                        temp.setChecked(true);
                    }
                }
            }
        });

        new MaterialDialog.Builder().init(getActivity())
                .setTitle(title)
                .setCancelable(false)
                .setMessage(getString(R.string.select_you_want))
                .addView(dialogView)
                .setPositiveButton(getString(R.string.filter), new DialogInterface.
                        OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final long startDate, endDate;
                        final int type;
                        if (cbPeriod.isChecked()) {
                            switch (rgPeriod.getCheckedRadioButtonId()) {
                                case R.id.rb_date:

                                    String startDateText =
                                            getContext().getResources()
                                                    .getString(R.string.start_date);
                                    String endDateText =
                                            getContext().getResources()
                                                    .getString(R.string.end_date);
                                    if (!tvStartDate.getText().equals(startDateText)
                                            && isEndDateLargeThanStartDate() &&
                                            !tvEndDate.getText().equals(endDateText)) {
                                        startDate = startDateFromPicker;
                                        endDate = endDateFromPicker;
                                    } else if (!isEndDateLargeThanStartDate()) {
                                        Toaster.show(rootView,
                                                getString(R.string.end_date_must_be_greater));
                                        return;
                                    } else {
                                        Toast.makeText(getContext(),
                                                getResources().getText(R.string.select_date),
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    break;
                                case R.id.rb_four_weeks:
                                    startDate = DateHelper.subtractWeeks(4);
                                    endDate = System.currentTimeMillis();
                                    break;
                                case R.id.rb_three_months:
                                    startDate = DateHelper.subtractMonths(3);
                                    endDate = System.currentTimeMillis();
                                    break;
                                case R.id.rb_six_months:
                                    startDate = DateHelper.subtractMonths(6);
                                    endDate = System.currentTimeMillis();
                                    break;
                                default:
                                    startDate = -1;
                                    endDate = -1;
                                    break;
                            }
                        } else {
                            startDate = -1;
                            endDate = -1;
                        }

                        if (cbType.isChecked()) {
                            switch (rgType.getCheckedRadioButtonId()) {
                                case R.id.rb_deposit:
                                    type = 0;
                                    break;
                                case R.id.rb_withdrawal:
                                    type = 1;
                                    break;
                                default:
                                    type = -1;
                                    break;
                            }
                        } else {
                            type = -1;
                        }
                        filter(startDate, endDate, type);
                    }
                })
                .setNeutralButton(R.string.clear_filters, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        filter(-1, -1, -1);
                    }
                })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                .createMaterialDialog()
                .show();
    }
}
