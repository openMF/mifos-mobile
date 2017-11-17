package org.mifos.mobilebanking.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.presenters.AccountOverviewPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.AccountOverviewMvpView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.CurrencyUtil;
import org.mifos.mobilebanking.utils.Toaster;
import org.mifos.mobilebanking.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Rajan Maurya
 *         On 16/10/17.
 */
public class AccountOverviewFragment extends BaseFragment implements AccountOverviewMvpView,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_total_savings)
    TextView tvTotalSavings;

    @BindView(R.id.tv_total_loan)
    TextView tvTotalLoan;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    AccountOverviewPresenter accountOverviewPresenter;

    View rootView;

    private double totalLoanBalance, totalSavingsBalance;

    public static AccountOverviewFragment newInstance() {
        AccountOverviewFragment fragment = new AccountOverviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_account_overview, container, false);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        ButterKnife.bind(this, rootView);
        accountOverviewPresenter.attachView(this);
        setToolbarTitle(getString(R.string.accounts_overview));

        swipeRefreshLayout.setColorSchemeResources(R.color.blue_light, R.color.green_light, R
                .color.orange_light, R.color.red_light);
        swipeRefreshLayout.setOnRefreshListener(this);

        if (savedInstanceState == null) {
            accountOverviewPresenter.loadClientAccountDetails();
        }

        return rootView;
    }

    @Override
    public void onRefresh() {
        accountOverviewPresenter.loadClientAccountDetails();
    }

    @Override
    public void showTotalLoanSavings(double totalLoan, double totalSavings) {
        this.totalLoanBalance = totalLoan;
        this.totalSavingsBalance = totalSavings;
        tvTotalLoan.setText(CurrencyUtil.formatCurrency(getContext(), totalLoan));
        tvTotalSavings.setText(CurrencyUtil.formatCurrency(getContext(), totalSavings));
    }

    @Override
    public void showError(String message) {
        Toaster.show(rootView, message);
    }

    @Override
    public void showProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_account_overview, menu);
        Utils.setToolbarIconColor(getActivity(), menu, R.color.white);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_refresh_account_overview) {
            accountOverviewPresenter.loadClientAccountDetails();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(Constants.TOTAL_LOAN, totalLoanBalance);
        outState.putDouble(Constants.TOTAL_SAVINGS, totalSavingsBalance);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            totalLoanBalance = savedInstanceState.getDouble(Constants.TOTAL_LOAN);
            totalSavingsBalance = savedInstanceState.getDouble(Constants.TOTAL_SAVINGS);
            showTotalLoanSavings(totalLoanBalance, totalSavingsBalance);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        accountOverviewPresenter.detachView();
    }
}
