package org.mifos.mobile.injection.component;

import android.app.Application;
import android.content.Context;

import org.mifos.mobile.api.BaseApiManager;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.api.local.DatabaseHelper;
import org.mifos.mobile.api.local.PreferencesHelper;
import org.mifos.mobile.injection.ApplicationContext;
import org.mifos.mobile.injection.module.ApplicationModule;

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
