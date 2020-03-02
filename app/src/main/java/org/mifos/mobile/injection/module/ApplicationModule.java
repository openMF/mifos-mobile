package org.mifos.mobile.injection.module;

import android.app.Application;
import android.content.Context;

import org.mifos.mobile.api.BaseApiManager;
import org.mifos.mobile.injection.ApplicationContext;
import org.mifos.mobile.api.local.PreferencesHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author ishan
 * @since 08/07/16
 */
@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    PreferencesHelper providePrefManager(@ApplicationContext Context context) {
        return new PreferencesHelper(context);
    }

    @Provides
    @Singleton
    BaseApiManager provideBaseApiManager(PreferencesHelper preferencesHelper) {
        return new BaseApiManager(preferencesHelper);
    }
}
