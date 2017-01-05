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
import org.mifos.selfserviceapp.presenters.HomePresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.ViewPagerAdapter;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.views.HomeView;
import org.mifos.selfserviceapp.ui.views.AccountsView;
import org.mifos.selfserviceapp.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by michaelsosnick on 1/3/17.
 */

public class HomeScreenFragment extends BaseFragment implements HomeView {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Inject
    HomePresenter homePresenter;

    public static HomeScreenFragment newInstance(long clientId) {
        HomeScreenFragment homeScreenFragment = new HomeScreenFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.CLIENT_ID, clientId);
        homeScreenFragment.setArguments(args);
        return homeScreenFragment;
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
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        ButterKnife.bind(this, view);
        homePresenter.attachView(this);

        setUpViewPagerAndTabLayout();

        homePresenter.loadClientAccounts();

        return view;
    }

    private void setUpViewPagerAndTabLayout() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(HomeFragment.newInstance(Constants.SAVINGS_ACCOUNTS),
                getString(R.string.saving_accounts));
        viewPagerAdapter.addFragment(HomeFragment.newInstance(Constants.LOAN_ACCOUNTS),
                getString(R.string.loan_accounts));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + R.id.viewpager + ":" + position;
    }

    @Override
    public void showLoanAccounts(List<LoanAccount> loanAccounts) {
        ((HomeView) getChildFragmentManager().findFragmentByTag(getFragmentTag(1)))
                .showLoanAccounts(loanAccounts);
        ((HomeView) getChildFragmentManager().findFragmentByTag(getFragmentTag(1)))
                .hideProgress();
    }

    @Override
    public void showSavingsAccounts(List<SavingAccount> savingAccounts) {
        ((HomeView) getChildFragmentManager().findFragmentByTag(getFragmentTag(0)))
                .showSavingsAccounts(savingAccounts);
        ((HomeView) getChildFragmentManager().findFragmentByTag(getFragmentTag(0)))
                .hideProgress();
    }

    @Override
    public void showError(String errorMessage) {
        ((HomeView) getChildFragmentManager().findFragmentByTag(getFragmentTag(0)))
                .showError(getString(R.string.error_fetching_accounts));
        ((HomeView) getChildFragmentManager().findFragmentByTag(getFragmentTag(1)))
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
        homePresenter.detachView();
    }
}