package org.mifos.selfserviceapp.injection.module;

import android.app.Application;
import android.content.Context;

import org.mifos.selfserviceapp.injection.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * @author ishan
 * @since 08/07/16
 */
@Module
public class ApplicationModule {

    Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application providesApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    public Context provideContext() {
        return application;
    }

}
