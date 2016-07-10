package org.mifos.selfserviceapp.injection.module;

import android.content.Context;

import org.mifos.selfserviceapp.injection.ActivityContext;
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
public class ActivityDataModule extends DataModule {

    @Provides
    @Singleton
    @Inject
    PrefManager providePrefManager(@ActivityContext Context context) {
        return new PrefManager(context);
    }

}
