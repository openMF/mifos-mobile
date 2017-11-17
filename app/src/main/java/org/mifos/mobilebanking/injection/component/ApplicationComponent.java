package org.mifos.mobilebanking.injection.component;

import android.app.Application;
import android.content.Context;

import org.mifos.mobilebanking.api.BaseApiManager;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.api.local.DatabaseHelper;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author ishan
 * @since 08/07/16
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();
    DataManager dataManager();
    PreferencesHelper prefManager();
    BaseApiManager baseApiManager();
    DatabaseHelper databaseHelper();

}
