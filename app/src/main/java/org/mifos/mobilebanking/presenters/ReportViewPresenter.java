package org.mifos.mobilebanking.presenters;

/*
 * Created by saksham on 02/June/2018
 */

import android.content.Context;
import android.os.Environment;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.services.ReportService;
import org.mifos.mobilebanking.injection.ActivityContext;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.ReportViewMvpView;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ReportViewPresenter extends BasePresenter<ReportViewMvpView> {

    CompositeDisposable compositeDisposable;

    @Inject
    protected ReportViewPresenter(@ActivityContext Context context) {
        super(context);
        compositeDisposable = new CompositeDisposable();
    }

    public void downloadReportPDF(String reportName, String id, String export) {
        getMvpView().showMessage(context.getString(R.string.download_started));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://drive.google.com/")
                .client(new OkHttpClient.Builder().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        ReportService reportService = retrofit.create(ReportService.class);

        reportService.downloadReport(reportName, id, export)
                .flatMap(new Function<Response<ResponseBody>, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(
                            final Response<ResponseBody> responseBodyResponse) throws Exception {
                        return new Observable<File>() {
                            @Override
                            protected void subscribeActual(Observer<? super File> observer) {
                                try {
                                    File file = new File(Environment
                                            .getExternalStoragePublicDirectory(Environment
                                                .DIRECTORY_DOWNLOADS)
                                            .getAbsoluteFile(), "mifos.pdf");
                                    BufferedSink sink = Okio.buffer(Okio.sink(file));
                                    sink.writeAll(responseBodyResponse.body().source());
                                    sink.close();
                                    observer.onNext(file);
                                    observer.onComplete();
                                } catch (IOException e) {
                                    observer.onError(e);
                                }
                            }
                        };
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<File>() {
                    @Override
                    public void onNext(File file) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        getMvpView().showMessage(context.getString(R.string.download_completed));
                    }
                });
    }

    @Override
    public void attachView(ReportViewMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }
}
