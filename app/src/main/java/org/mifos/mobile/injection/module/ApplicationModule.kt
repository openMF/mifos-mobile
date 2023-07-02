package org.mifos.mobile.injection.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.mifos.mobile.api.BaseApiManager
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.DatabaseHelper
import org.mifos.mobile.api.local.PreferencesHelper
import javax.inject.Singleton

/**
 * @author ishan
 * @since 08/07/16
 */
@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule() {

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

    @Provides
    @Singleton
    fun providesDataManager(preferencesHelper: PreferencesHelper?, baseApiManager: BaseApiManager?, databaseHelper: DatabaseHelper): DataManager {
        return DataManager(preferencesHelper!!, baseApiManager!!, databaseHelper!!)
    }
}
