package org.mifos.selfserviceapp.injection.component;

import android.app.Application;
import android.content.Context;

import org.mifos.selfserviceapp.api.BaseApiManager;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ApplicationContext;
import org.mifos.selfserviceapp.injection.module.ApplicationModule;
import org.mifos.selfserviceapp.utils.PrefManager;

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
    PrefManager prefManager();
    BaseApiManager baseApiManager();

}
