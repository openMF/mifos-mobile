package org.mifos.mobile

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mifos.mobile.passcode.utils.ForegroundChecker
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import dagger.hilt.android.HiltAndroidApp
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.common.utils.LanguageHelper.onAttach
import org.mifos.mobile.feature.settings.applySavedTheme

/**
 * @author ishan
 * @since 08/07/16
 */
@HiltAndroidApp
class MifosSelfServiceApp : MultiDexApplication() {

    companion object {
        private var instance: MifosSelfServiceApp? = null
        operator fun get(context: Context): MifosSelfServiceApp {
            return context.applicationContext as MifosSelfServiceApp
        }

        val context: Context?
            get() = instance

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        instance = this
        FlowManager.init(FlowConfig.Builder(this).build())
        ForegroundChecker.init(this)
        PreferencesHelper(this).applySavedTheme()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        context?.let { onAttach(it) }
    }

}
