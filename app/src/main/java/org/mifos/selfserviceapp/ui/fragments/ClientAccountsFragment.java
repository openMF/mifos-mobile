package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.presenters.AccountsPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.ViewPagerAdapter;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.AccountsView;
import org.mifos.selfserviceapp.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientAccountsFragment extends BaseFragment implements AccountsView {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Inject
    AccountsPresenter accountsPresenter;

    public static ClientAccountsFragment newInstance(long clientId) {
        ClientAccountsFragment clientAccountsFragment = new ClientAccountsFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.CLIENT_ID, clientId);
        clientAccountsFragment.setArguments(args);
        return clientAccountsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_accounts, container, false);
        ButterKnife.bind(this, view);
        accountsPresenter.attachView(this);

        setUpViewPagerAndTabLayout();

        accountsPresenter.loadClientAccounts();

        return view;
    }

    private void setUpViewPagerAndTabLayout() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(AccountsFragment.newInstance(Constants.SAVINGS_ACCOUNTS),
                getString(R.string.saving_accounts));
        viewPagerAdapter.addFragment(AccountsFragment.newInstance(Constants.LOAN_ACCOUNTS),
                getString(R.string.loan_accounts));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + R.id.viewpager + ":" + position;
    }

    @Override
    public void showLoanAccounts(List<LoanAccount> loanAccounts) {
        ((AccountsView) getChildFragmentManager().findFragmentByTag(getFragmentTag(1)))
                .showLoanAccounts(loanAccounts);
        ((AccountsView) getChildFragmentManager().findFragmentByTag(getFragmentTag(1)))
                .hideProgress();
    }

    @Override
    public void showSavingsAccounts(List<SavingAccount> savingAccounts) {
        ((AccountsView) getChildFragmentManager().findFragmentByTag(getFragmentTag(0)))
                .showSavingsAccounts(savingAccounts);
        ((AccountsView) getChildFragmentManager().findFragmentByTag(getFragmentTag(0)))
                .hideProgress();
    }

    @Override
    public void showError(String errorMessage) {
        ((AccountsView) getChildFragmentManager().findFragmentByTag(getFragmentTag(0)))
                .showError(getString(R.string.error_fetching_accounts));
        ((AccountsView) getChildFragmentManager().findFragmentByTag(getFragmentTag(1)))
                .showError(getString(R.string.error_fetching_accounts));
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        accountsPresenter.detachView();
    }
}