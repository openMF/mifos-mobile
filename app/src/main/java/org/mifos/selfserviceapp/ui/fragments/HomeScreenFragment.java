package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.models.client.Client;
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

        homePresenter.loadInfo();

        return view;
    }

    private void setUpViewPagerAndTabLayout() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(HomeFragment.newInstance(), "Home Page");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + R.id.viewpager + ":" + position;
    }

    @Override
    public void showClientInfo(Client client) {
        ((HomeView) getChildFragmentManager().findFragmentByTag(getFragmentTag(0)))
                .showClientInfo(client);
        ((HomeView) getChildFragmentManager().findFragmentByTag(getFragmentTag(0)))
                .hideProgress();
    }

    @Override
    public void showInfo(List<LoanAccount> loanAccounts, List<SavingAccount> savingAccounts) {
        ((HomeView) getChildFragmentManager().findFragmentByTag(getFragmentTag(0)))
                .showInfo(loanAccounts, savingAccounts);
        ((HomeView) getChildFragmentManager().findFragmentByTag(getFragmentTag(0)))
                .hideProgress();
    }

    @Override
    public void showLoanAccountsDetail(LoanAccount loanAccount, TextView tv) {
        ((HomeView) getChildFragmentManager().findFragmentByTag(getFragmentTag(0)))
                .showLoanAccountsDetail(loanAccount, tv);
        ((HomeView) getChildFragmentManager().findFragmentByTag(getFragmentTag(0)))
                .hideProgress();
    }

    @Override
    public void showErrorFetchingLoanAccountsDetail(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSavingAccountsDetail(SavingAccount savingAccount, TextView tv) {
        ((HomeView) getChildFragmentManager().findFragmentByTag(getFragmentTag(0)))
                .showSavingAccountsDetail(savingAccount, tv);
        ((HomeView) getChildFragmentManager().findFragmentByTag(getFragmentTag(0)))
                .hideProgress();
    }

    @Override
    public void showErrorFetchingSavingAccountsDetail(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
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