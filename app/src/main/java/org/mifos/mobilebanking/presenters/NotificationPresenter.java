package org.mifos.mobilebanking.presenters;

import android.content.Context;

import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.injection.ActivityContext;
import org.mifos.mobilebanking.models.notification.MifosNotification;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.NotificationView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dilpreet on 14/9/17.
 */

public class NotificationPresenter extends BasePresenter<NotificationView> {

    private DataManager manager;
    private CompositeSubscription subscription;

    /**
     * Initialises the LoginPresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param manager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
     */
    @Inject
    public NotificationPresenter(DataManager manager, @ActivityContext Context context) {
        super(context);
        this.manager = manager;
        subscription = new CompositeSubscription();
    }

    @Override
    public void attachView(NotificationView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscription.clear();
    }

    public void loadNotifications() {
        checkViewAttached();
        subscription.add(manager.getNotifications()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<MifosNotification>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<MifosNotification> notificationModels) {
                        getMvpView().showNotifications(notificationModels);
                    }
                }));
    }
}
