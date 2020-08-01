package org.mifos.mobile.injection.module

import android.app.Activity
import android.content.Context

import dagger.Module
import dagger.Provides

import org.mifos.mobile.injection.ActivityContext

/**
 * @author ishan
 * @since 08/07/16
 */
@Module
class ActivityModule(private val activity: Activity) {
    @Provides
    fun providesActivity(): Activity {
        return activity
    }

    @Provides
    @ActivityContext
    fun providesContext(): Context {
        return activity
    }
}