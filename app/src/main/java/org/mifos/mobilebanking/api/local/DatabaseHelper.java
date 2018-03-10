package org.mifos.mobilebanking.api.local;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.mifos.mobilebanking.models.Charge;
import org.mifos.mobilebanking.models.Page;
import org.mifos.mobilebanking.models.notification.MifosNotification;
import org.mifos.mobilebanking.models.notification.MifosNotification_Table;
import org.mifos.mobilebanking.utils.NotificationComparator;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;


/**
 * Created by Rajan Maurya on 02/03/17.
 */
@Singleton
public class DatabaseHelper {

    @Inject
    public DatabaseHelper() {
    }

    public Observable<Page<Charge>> syncCharges(final Page<Charge> charges) {
        return Observable.defer(new Callable<ObservableSource<? extends Page<Charge>>>() {
            @Override
            public Observable<Page<Charge>> call() {
                for (Charge charge : charges.getPageItems()) {
                    charge.save();
                }
                return Observable.just(charges);
            }
        });
    }

    public Observable<Page<Charge>> getClientCharges() {
        return Observable.defer(new Callable<ObservableSource<? extends Page<Charge>>>() {
            @Override
            public Observable<Page<Charge>> call() {
                List<Charge> charges = SQLite.select()
                        .from(Charge.class)
                        .queryList();
                Page<Charge> chargePage = new Page<>();
                chargePage.setPageItems(charges);
                return Observable.just(chargePage);
            }
        });
    }

    public Observable<List<MifosNotification>> getNotifications() {
        return Observable.defer(new Callable<Observable<List<MifosNotification>>>() {
            @Override
            public Observable<List<MifosNotification>> call() {
                deleteOldNotifications();
                List<MifosNotification> notifications = SQLite.select()
                        .from(MifosNotification.class)
                        .queryList();
                Collections.sort(notifications, new NotificationComparator());
                return  Observable.just(notifications);
            }
        });
    }

    public Observable<Integer> getUnreadNotificationsCount() {
        return Observable.defer(new Callable<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                deleteOldNotifications();
                int count = SQLite.select()
                        .from(MifosNotification.class)
                        .where(MifosNotification_Table.read.eq(false))
                        .queryList().size();
                return Observable.just(count);
            }
        });
    }

    private void deleteOldNotifications() {
        Observable.defer(new Callable<Observable<Void>>() {
            @Override
            public Observable<Void> call() {
                long thirtyDaysInSeconds = 2592000;
                long thirtyDaysFromCurrentTimeInSeconds = (System.currentTimeMillis() / 1000) -
                        thirtyDaysInSeconds;
                SQLite.delete(MifosNotification.class)
                        .where(MifosNotification_Table.timeStamp.
                                lessThan(thirtyDaysFromCurrentTimeInSeconds * 1000))
                        .execute();
                return null;
            }
        });
    }

}
