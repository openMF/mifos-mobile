package org.mifos.selfserviceapp.api.local;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.mifos.selfserviceapp.models.Charge;
import org.mifos.selfserviceapp.models.Page;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by Rajan Maurya on 02/03/17.
 */
@Singleton
public class DatabaseHelper {

    @Inject
    public DatabaseHelper() {
    }

    public Observable<Page<Charge>> syncCharges(final Page<Charge> charges) {
        return Observable.defer(new Func0<Observable<Page<Charge>>>() {
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
        return Observable.defer(new Func0<Observable<Page<Charge>>>() {
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
}
