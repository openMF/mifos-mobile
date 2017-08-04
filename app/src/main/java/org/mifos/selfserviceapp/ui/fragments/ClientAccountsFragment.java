package org.mifos.selfserviceapp.ui.fragments;
/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import org.mifos.selfserviceapp.ui.adapters.CheckBoxAdapter;
import org.mifos.selfserviceapp.ui.adapters.ViewPagerAdapter;
import org.mifos.selfserviceapp.ui.enums.AccountType;
import org.mifos.selfserviceapp.ui.enums.LoanState;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.AccountsView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.MaterialDialog;
import org.mifos.selfserviceapp.utils.StatusUtils;

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

    @Inject
    CheckBoxAdapter checkBoxAdapter;

    private RecyclerView checkBoxRecyclerView;
    private AccountType accountType;

    public static ClientAccountsFragment newInstance(AccountType accountType) {
        ClientAccountsFragment clientAccountsFragment = new ClientAccountsFragment();
        Bundle args = new Bundle();
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
            menu.findItem(R.id.menu_filter_savings).setVisible(true);
            menu.findItem(R.id.menu_filter_loan).setVisible(false);
            menu.findItem(R.id.menu_filter_shares).setVisible(false);
            menu.findItem(R.id.menu_search_saving).setVisible(true);
            menu.findItem(R.id.menu_search_loan).setVisible(false);
            menu.findItem(R.id.menu_search_share).setVisible(false);
            initSearch(menu, AccountType.SAVINGS);
        } else if (viewPager.getCurrentItem() == 1) {
            menu.findItem(R.id.menu_add_loan).setVisible(true);
            menu.findItem(R.id.menu_filter_savings).setVisible(false);
            menu.findItem(R.id.menu_filter_loan).setVisible(true);
            menu.findItem(R.id.menu_filter_shares).setVisible(false);
            menu.findItem(R.id.menu_search_saving).setVisible(false);
            menu.findItem(R.id.menu_search_loan).setVisible(true);
            menu.findItem(R.id.menu_search_share).setVisible(false);
            initSearch(menu, AccountType.LOAN);
        } else if (viewPager.getCurrentItem() == 2) {
            menu.findItem(R.id.menu_add_loan).setVisible(false);
            menu.findItem(R.id.menu_filter_savings).setVisible(false);
            menu.findItem(R.id.menu_filter_loan).setVisible(false);
            menu.findItem(R.id.menu_filter_shares).setVisible(true);
            menu.findItem(R.id.menu_search_saving).setVisible(false);
            menu.findItem(R.id.menu_search_loan).setVisible(false);
            menu.findItem(R.id.menu_search_share).setVisible(true);
            initSearch(menu, AccountType.SHARE);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter_savings:
                showFilterDialog(AccountType.SAVINGS);
                break;
            case R.id.menu_filter_loan:
                showFilterDialog(AccountType.LOAN);
                break;
            case R.id.menu_filter_shares:
                showFilterDialog(AccountType.SHARE);
                break;
            case R.id.menu_add_loan:
                ((BaseActivity) getActivity()).replaceFragment(LoanApplicationFragment.
                        newInstance(LoanState.CREATE), true, R.id.container);
                break;
        }
        return true;
    }

    private void initSearch(Menu menu, final AccountType account) {
        SearchManager manager = (SearchManager) getActivity().
                getSystemService(Context.SEARCH_SERVICE);
        SearchView search = null;

        if (account == AccountType.SAVINGS) {
            search = (SearchView) menu.findItem(R.id.menu_search_saving).getActionView();
        } else if (account == AccountType.LOAN) {
            search = (SearchView) menu.findItem(R.id.menu_search_loan).getActionView();
        } else if (account == AccountType.SHARE) {
            search = (SearchView) menu.findItem(R.id.menu_search_share).getActionView();
        }

        search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (account == AccountType.SAVINGS) {
                    ((AccountsFragment) getChildFragmentManager().findFragmentByTag(
                            getFragmentTag(0))).searchSavingsAccount(newText);
                } else if (account == AccountType.LOAN) {
                    ((AccountsFragment) getChildFragmentManager().findFragmentByTag(
                            getFragmentTag(1))).searchLoanAccount(newText);
                } else if (account == AccountType.SHARE) {
                    ((AccountsFragment) getChildFragmentManager().findFragmentByTag(
                            getFragmentTag(2))).searchSharesAccount(newText);
                }

                return false;
            }
        });
    }

    private void showFilterDialog(final AccountType account) {
        String title = "";
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        checkBoxRecyclerView = new RecyclerView(getActivity());
        checkBoxRecyclerView.setLayoutManager(layoutManager);
        checkBoxRecyclerView.setAdapter(checkBoxAdapter);

        if (account == AccountType.SAVINGS) {
            checkBoxAdapter.setStatusList(StatusUtils.
                    getSavingsAccountStatusList(getActivity()));
            title = getString(R.string.filter_savings);
        } else if (account == AccountType.LOAN) {
            checkBoxAdapter.setStatusList(StatusUtils.
                    getLoanAccountStatusList(getActivity()));
            title = getString(R.string.filter_loan);
        } else if (account == AccountType.SHARE) {
            checkBoxAdapter.setStatusList(StatusUtils.
                    getShareAccountStatusList(getActivity()));
            title = getString(R.string.filter_share);
        }

        new MaterialDialog.Builder().init(getActivity())
                .setTitle(title)
                .setMessage(getString(R.string.select_you_want))
                .addView(checkBoxRecyclerView)
                .setPositiveButton(getString(R.string.filter), new DialogInterface.
                        OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (account == AccountType.SAVINGS) {
                            ((AccountsFragment) getChildFragmentManager().findFragmentByTag(
                                    getFragmentTag(0))).filterSavingsAccount(checkBoxAdapter.
                                    getStatusList());
                        } else if (account == AccountType.LOAN) {
                            ((AccountsFragment) getChildFragmentManager().findFragmentByTag(
                                    getFragmentTag(1))).filterLoanAccount(checkBoxAdapter.
                                    getStatusList());
                        } else if (account == AccountType.SHARE) {
                            ((AccountsFragment) getChildFragmentManager().findFragmentByTag(
                                    getFragmentTag(2))).filterShareAccount(checkBoxAdapter.
                                    getStatusList());
                        }

                    }
                })
                .setNegativeButton(getString(R.string.cancel))
                .createMaterialDialog()
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).hideToolbarElevation();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BaseActivity) getActivity()).setToolbarElevation();
    }
}