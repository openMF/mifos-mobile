package org.mifos.mobile.ui.fragments;

/*
 * Created by saksham on 02/June/2018
 */

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.mifos.mobile.R;
import org.mifos.mobile.presenters.ReportViewPresenter;
import org.mifos.mobile.ui.activities.base.BaseActivity;
import org.mifos.mobile.ui.fragments.base.BaseFragment;
import org.mifos.mobile.ui.views.ReportViewMvpView;
import org.mifos.mobile.utils.CheckSelfPermissionAndRequest;
import org.mifos.mobile.utils.Constants;
import org.mifos.mobile.utils.Toaster;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class ReportViewFragment extends BaseFragment implements ReportViewMvpView {

    String reportName;

    @Inject
    ReportViewPresenter reportViewPresenter;

    View rootView;

    public static ReportViewFragment newInstance(String reportName) {
        ReportViewFragment reportViewFragment = new ReportViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.REPORT_NAME, reportName);
        reportViewFragment.setArguments(bundle);
        return reportViewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reportName = getArguments().getString(Constants.REPORT_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_report_view, container, false);
        ButterKnife.bind(this, rootView);
        setToolbarTitle(reportName);
        setHasOptionsMenu(true);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        reportViewPresenter.attachView(this);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_report, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_report_download:
                //https:drive.google.com/uc?export=download&id=1k-mifizLiGShYpyKcBLPIgB4BxXR3fbB
                if (CheckSelfPermissionAndRequest.checkSelfPermission(getActivity(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //this should be executed only when the permission is granted
                    downloadReport();
                } else {
                    requestPermission();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void requestPermission() {
        CheckSelfPermissionAndRequest.requestPermissionFragment(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Constants.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE,
                "dialogmessageretry",
                "neveraskagain",
                "permissiondeniedstatus");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadReport();
        } else {
            showError(getString(R.string.permission_denied_cannot_download));
        }
    }

    @Override
    public void downloadReport() {
        //in future it will only contain report name
        reportViewPresenter.downloadReportPDF("uc",
                "1k-mifizLiGShYpyKcBLPIgB4BxXR3fbB", "download");
    }

    @Override
    public void showError(String message) {
        Toaster.show(rootView, message);
    }

    @Override
    public void showMessage(String message) {
        Toaster.show(rootView, message);
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
        reportViewPresenter.detachView();
    }
}
