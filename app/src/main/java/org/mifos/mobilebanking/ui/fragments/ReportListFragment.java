package org.mifos.mobilebanking.ui.fragments;

/*
 * Created by saksham on 01/June/2018
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.presenters.ReportListPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.adapters.ReportListAdapter;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.ReportListView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportListFragment extends BaseFragment
        implements ReportListView, ReportListAdapter.OnItemClickListener {

    @BindView(R.id.rv_list_reports)
    RecyclerView rvListReports;

    @Inject
    ReportListAdapter reportListAdapter;

    @Inject
    ReportListPresenter reportListPresenter;

    View rootView;
    ArrayList<String> reports;

    public static ReportListFragment newInstance() {
        ReportListFragment reportListFragment = new ReportListFragment();
        return reportListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_report_list, container, false);
        ButterKnife.bind(this, rootView);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        setToolbarTitle(getString(R.string.reports));
        reportListPresenter.attachView(this);
        reports = reportListPresenter.getReportList();
        reportListAdapter.setReports(reports);
        reportListAdapter.setOnItemClickListener(this);
        rvListReports.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListReports.setAdapter(reportListAdapter);
        return rootView;
    }

    @Override
    public void setOnItemClickListener(String reportName) {
        ((BaseActivity) getActivity()).
                replaceFragment(ReportViewFragment.newInstance(reportName), true, R.id.container);
    }

    @Override
    public void showError(String message) {

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
        reportListPresenter.detachView();
    }
}
