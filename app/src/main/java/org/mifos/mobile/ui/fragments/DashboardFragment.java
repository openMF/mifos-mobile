package org.mifos.mobile.ui.fragments;

/*
 * Created by saksham on 16/July/2018
 */

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.mifos.mobile.R;
import org.mifos.mobile.models.client.ClientAccounts;
import org.mifos.mobile.presenters.DashboardPresenter;
import org.mifos.mobile.ui.activities.base.BaseActivity;
import org.mifos.mobile.ui.fragments.base.BaseFragment;
import org.mifos.mobile.ui.views.DashboardView;
import org.mifos.mobile.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardFragment extends BaseFragment implements DashboardView {

    @BindView(R.id.tv_total_accounts)
    TextView tvTotalAccounts;

    @BindView(R.id.tv_total_savings)
    TextView tvTotalSavings;

    @BindView(R.id.tv_total_loan)
    TextView tvTotalLoan;

    @BindView(R.id.tv_total_share)
    TextView tvTotalShare;

    @BindView(R.id.ll_dashboard_container)
    LinearLayout llDashboardContainer;

    @BindView(R.id.pc_savings_accounts)
    PieChart pcSavingsAccounts;

    @BindView(R.id.pc_loan_accounts)
    PieChart pcLoanAccounts;

    @BindView(R.id.pc_share_accounts)
    PieChart pcShareAccounts;

    @Inject
    DashboardPresenter presenter;

    View rootView;
    ClientAccounts clientAccounts;
    public static final int[] MATERIAL_DESIGN_COLORS = {
            Color.rgb(117, 117, 117),
            Color.rgb(221, 44, 0),
            Color.rgb(170, 0, 255),
            Color.rgb(0, 188, 212),
            Color.rgb(29, 233, 182),
            Color.rgb(255, 234, 0),
            Color.rgb(230, 81, 0),
            Color.rgb(197, 17, 98)
    };

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientAccounts = getArguments().getParcelable(Constants.ACCOUNTS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, rootView);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);

        setToolbarTitle(getString(R.string.dashboard));
        presenter.attachView(this);
        presenter.getAccountsDetails();
        return rootView;
    }

    @Override
    public void showTotalAccountsDetail(HashMap<String, Integer> list) {
        tvTotalAccounts.setText(String.valueOf(list.get(Constants.SAVINGS_ACCOUNTS)
                + list.get(Constants.LOAN_ACCOUNTS) + list.get(Constants.SHARE_ACCOUNTS)));
        tvTotalSavings.setText(String.valueOf(list.get(Constants.SAVINGS_ACCOUNTS)));
        tvTotalLoan.setText(String.valueOf(list.get(Constants.LOAN_ACCOUNTS)));
        tvTotalShare.setText(String.valueOf(list.get(Constants.SHARE_ACCOUNTS)));

    }

    @Override
    public void showAccountsOverview(HashMap<String, Integer> savingAccountStatus,
                                     HashMap<String, Integer> loanAccountStatus,
                                     HashMap<String, Integer> shareAccountStatus) {
        setSavingAccountsChart(savingAccountStatus);
        setLoanAccountsChart(loanAccountStatus);
        setShareAccountsChart(shareAccountStatus);
    }

    public void setSavingAccountsChart(HashMap<String, Integer> savingAccountsStatus) {
        Legend legend = pcSavingsAccounts.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);

        List<PieEntry> yVals = new ArrayList<>();

        for (String key : savingAccountsStatus.keySet()) {
            yVals.add(new PieEntry(savingAccountsStatus.get(key), keyShortener(key)));
        }

        PieDataSet set = new PieDataSet(yVals, null);
        set.setColors(MATERIAL_DESIGN_COLORS);

        PieData data = new PieData(set);

        pcSavingsAccounts.animateY(500);
        pcSavingsAccounts.setData(data);
        pcSavingsAccounts.setDescription(null);
        pcSavingsAccounts.setDrawEntryLabels(false);
        pcSavingsAccounts.invalidate();

    }

    public void setLoanAccountsChart(HashMap<String, Integer> loanAccountsStatus) {

        Legend legend = pcLoanAccounts.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setWordWrapEnabled(true);

        List<PieEntry> yVals = new ArrayList<>();

        for (String key : loanAccountsStatus.keySet()) {
            yVals.add(new PieEntry(loanAccountsStatus.get(key), keyShortener(key)));
        }

        PieDataSet set = new PieDataSet(yVals, null);
        set.setColors(MATERIAL_DESIGN_COLORS);

        PieData data = new PieData(set);

        pcLoanAccounts.animateY(500);
        pcLoanAccounts.setData(data);
        pcLoanAccounts.setDescription(null);
        pcLoanAccounts.setDrawEntryLabels(false);
        pcLoanAccounts.invalidate();
    }

    public void setShareAccountsChart(HashMap<String, Integer> shareAccountsStatus) {
        Legend legend = pcShareAccounts.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);

        List<PieEntry> yVals = new ArrayList<>();

        for (String key : shareAccountsStatus.keySet()) {
            yVals.add(new PieEntry(shareAccountsStatus.get(key), keyShortener(key)));
        }

        PieDataSet set = new PieDataSet(yVals, null);
        set.setColors(MATERIAL_DESIGN_COLORS);

        PieData data = new PieData(set);

        pcShareAccounts.animateY(500);
        pcShareAccounts.setData(data);
        pcShareAccounts.setDescription(null);
        pcShareAccounts.setDrawEntryLabels(false);
        pcShareAccounts.invalidate();
    }

    private String keyShortener(String key) {
        if (key.equals("Submitted and pending approval")) {
            return getString(R.string.approval_pending);
        }
        if (key.equals("Approved")) {
            return getString(R.string.approved);
        }
        if (key.equals("Closed (obligations met)")) {
            return getString(R.string.closed);
        }
        if (key.equals("Withdrawn by applicant")) {
            return getString(R.string.withdrawn);
        }
        if (key.equals("Overpaid")) {
            return getString(R.string.overpaid);
        }
        if (key.equals("Active")) {
            return getString(R.string.active);
        }
        return key;
    }

    @Override
    public void showProgress() {
        llDashboardContainer.setVisibility(View.GONE);
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        llDashboardContainer.setVisibility(View.VISIBLE);
        hideProgressBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
