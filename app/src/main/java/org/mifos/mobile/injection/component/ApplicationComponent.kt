package org.mifos.mobile.injection.component

import android.app.Application
import android.content.Context
import dagger.Component
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mifos.mobile.api.BaseApiManager
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.DatabaseHelper
import org.mifos.mobile.api.local.PreferencesHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import org.mifos.mobile.injection.module.ApplicationModule
import javax.inject.Singleton

/**
 * @author ishan
 * @since 08/07/16
 */
@Singleton
@InstallIn(SingletonComponent::class)
@EntryPoint
interface ApplicationComponent {
//    @ApplicationContext
//    fun context(): Context?
//    fun application(): Application?
//    fun dataManager(): DataManager?
//    fun prefManager(): PreferencesHelper?
//    fun baseApiManager(): BaseApiManager?
//    fun databaseHelper(): DatabaseHelper?
}
