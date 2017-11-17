package org.mifos.mobilebanking.injection.module;

import android.app.Activity;
import android.content.Context;

import org.mifos.mobilebanking.injection.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * @author ishan
 * @since 08/07/16
 */
@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity providesActivity() {
        return activity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return activity;
    }

}
