package org.mifos.mobile.injection.module

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mifos.mobile.injection.ActivityContext

/**
 * @author ishan
 * @since 08/07/16
 */
@Module
@InstallIn(SingletonComponent::class)
class ActivityModule(private val activity: Activity? = null) {
    @Provides
    fun providesActivity(): Activity {
        return activity!!
    }

    @Provides
    @ActivityContext
    fun providesContext(): Context {
        return activity!!
    }
}
