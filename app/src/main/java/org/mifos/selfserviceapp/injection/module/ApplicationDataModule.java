package org.mifos.selfserviceapp.injection.module;

import android.content.Context;

import org.mifos.selfserviceapp.injection.ApplicationContext;
import org.mifos.selfserviceapp.utils.PrefManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author ishan
 * @since 08/07/16
 */
@Module
public class ApplicationDataModule extends DataModule {

    @Provides
    @Singleton
    @Inject
    PrefManager providePrefManager(@ApplicationContext Context context) {
        return new PrefManager(context);
    }

}
