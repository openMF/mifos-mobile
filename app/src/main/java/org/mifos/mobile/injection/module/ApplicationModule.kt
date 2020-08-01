package org.mifos.mobile.injection.module

import android.app.Application
import android.content.Context

import dagger.Module
import dagger.Provides

import org.mifos.mobile.api.BaseApiManager
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.injection.ApplicationContext

import javax.inject.Singleton

/**
 * @author ishan
 * @since 08/07/16
 */
@Module
class ApplicationModule(private val application: Application) {
    @Provides
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun providePrefManager(@ApplicationContext context: Context?): PreferencesHelper {
        return PreferencesHelper(context)
    }

    @Provides
    @Singleton
    fun provideBaseApiManager(preferencesHelper: PreferencesHelper?): BaseApiManager {
        return BaseApiManager(preferencesHelper!!)
    }
}