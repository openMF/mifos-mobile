package org.mifos.mobilebanking.presenters;

/*
 * Created by saksham on 01/June/2018
 */

import android.content.Context;

import org.mifos.mobilebanking.injection.ActivityContext;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.ReportListView;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class ReportListPresenter extends BasePresenter<ReportListView> {

    CompositeDisposable compositeDisposable;

    @Inject
    protected ReportListPresenter(@ActivityContext Context context) {
        super(context);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(ReportListView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    public ArrayList<String> getReportList() {
        ArrayList<String> list = new ArrayList<>();

        list.add("report-1");
        list.add("report-2");
        list.add("report-3");
        list.add("report-4");
        list.add("report-5");
        list.add("report-6");
        return list;
    }
}
