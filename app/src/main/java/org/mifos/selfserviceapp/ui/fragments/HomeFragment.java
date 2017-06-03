package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.enums.AccountType;
import org.mifos.selfserviceapp.ui.enums.LoanState;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by michaelsosnick on 1/1/17.
 */

public class HomeFragment extends BaseFragment {

    public static final String LOG_TAG = HomeFragment.class.getSimpleName();

    View rootView;

    private long clientId;

    public static HomeFragment newInstance(Long clientId) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientId = getArguments().getLong(Constants.CLIENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        setToolbarTitle(getString(R.string.home));
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.fl_apply_for_loan)
    public void onApplicationClicked() {
        ((BaseActivity) getActivity()).replaceFragment(
                LoanApplicationFragment.newInstance(LoanState.CREATE), true, R.id.container);
    }

    @OnClick(R.id.fl_recent_transaction)
    public void onTransactionClicked() {
        ((BaseActivity) getActivity()).replaceFragment(
                RecentTransactionsFragment.newInstance(clientId), true, R.id.container);
    }

    @OnClick(R.id.fl_quick_transfer)
    public void onQuickTransferClicked() {
        ((BaseActivity) getActivity()).replaceFragment(SavingsMakeTransferFragment.newInstance(1,
                Constants.TRANSFER_QUICK), true, R.id.container);
    }

    @OnClick(R.id.btn_savings)
    public void onSavingsClicked() {
        openAccount(AccountType.SAVINGS);
    }

    @OnClick(R.id.btn_loans)
    public void onLoansClicked() {
        openAccount(AccountType.LOAN);
    }

    @OnClick(R.id.btn_shares)
    public void onSharesClicked() {
        openAccount(AccountType.SHARE);
    }

    public void openAccount(AccountType accountType) {
        ((BaseActivity) getActivity()).replaceFragment(
                ClientAccountsFragment.newInstance(clientId, accountType), true, R.id.container);
    }
}


