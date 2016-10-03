package org.mifos.selfserviceapp.injection.component;

import android.app.Application;
import android.content.Context;

import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ApplicationContext;
import org.mifos.selfserviceapp.injection.module.ApplicationDataModule;
import org.mifos.selfserviceapp.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author ishan
 * @since 08/07/16
 */
@Singleton
@Component(modules = {ApplicationModule.class, ApplicationDataModule.class})
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

    DataManager dataManager();

}
