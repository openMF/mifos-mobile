package org.mifos.selfserviceapp.ui.fragments;
/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.models.accounts.share.ShareAccount;
import org.mifos.selfserviceapp.presenters.AccountsPresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.ViewPagerAdapter;
import org.mifos.selfserviceapp.ui.enums.AccountType;
import org.mifos.selfserviceapp.ui.enums.LoanState;
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

    private AccountType accountType;

    public static ClientAccountsFragment newInstance(long clientId, AccountType accountType) {
        ClientAccountsFragment clientAccountsFragment = new ClientAccountsFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.CLIENT_ID, clientId);
        args.putSerializable(Constants.ACCOUNT_TYPE, accountType);
        clientAccountsFragment.setArguments(args);
        return clientAccountsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            accountType = (AccountType) getArguments().getSerializable(Constants.ACCOUNT_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_accounts, container, false);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, view);
        accountsPresenter.attachView(this);
        setToolbarTitle(getString(R.string.accounts));

        setUpViewPagerAndTabLayout();

        accountsPresenter.loadClientAccounts();

        return view;
    }

    private void setUpViewPagerAndTabLayout() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        viewPagerAdapter.addFragment(AccountsFragment.newInstance(Constants.SAVINGS_ACCOUNTS),
                getString(R.string.savings));
        viewPagerAdapter.addFragment(AccountsFragment.newInstance(Constants.LOAN_ACCOUNTS),
                getString(R.string.loan));
        viewPagerAdapter.addFragment(AccountsFragment.newInstance(Constants.SHARE_ACCOUNTS),
                getString(R.string.share));

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);
        switch (accountType) {
            case SAVINGS:
                viewPager.setCurrentItem(0);
                break;
            case LOAN:
                viewPager.setCurrentItem(1);
                break;
            case SHARE:
                viewPager.setCurrentItem(2);
                break;
        }
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + R.id.viewpager + ":" + position;
    }

    @Override
    public void showShareAccounts(List<ShareAccount> shareAccounts) {
        ((AccountsView) getChildFragmentManager().findFragmentByTag(getFragmentTag(2))).
                showShareAccounts(shareAccounts);
        ((AccountsView) getChildFragmentManager().findFragmentByTag(getFragmentTag(2))).
                hideProgress();
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
        ((AccountsView) getChildFragmentManager().findFragmentByTag(getFragmentTag(2)))
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account, menu);
        if (viewPager.getCurrentItem() == 0) {
            menu.findItem(R.id.menu_add_loan).setVisible(false);
        } else if (viewPager.getCurrentItem() == 1) {
            menu.findItem(R.id.menu_add_loan).setVisible(true);
        } else if (viewPager.getCurrentItem() == 2) {
            menu.findItem(R.id.menu_add_loan).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_loan:
                ((BaseActivity) getActivity()).replaceFragment(LoanApplicationFragment.
                        newInstance(LoanState.CREATE), true, R.id.container);
                break;
        }
        return true;
    }
}