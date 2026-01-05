/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package cmp.android.app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp.shared.SharedApp
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init
import org.koin.android.ext.android.inject
import org.mifos.core.data.repository.NetworkMonitor
import org.mifos.core.data.repository.UserDataRepository
import template.core.base.analytics.AnalyticsHelper
import template.core.base.analytics.lifecycleTracker
import template.core.base.platform.update.AppUpdateManager
import template.core.base.platform.update.AppUpdateManagerImpl
import template.core.base.ui.ShareUtils
import java.util.Locale

/**
 * Main activity class. This class is used to set the content view of the
 * activity.
 *
 * @constructor Create empty Main activity
 * @see ComponentActivity
 */
@Suppress("UnusedPrivateProperty")
class MainActivity : ComponentActivity() {

    private lateinit var appUpdateManager: AppUpdateManager

    private val userPreferencesRepository: UserDataRepository by inject()

    private val networkMonitor: NetworkMonitor by inject()

    private val analyticsHelper: AnalyticsHelper by inject()
    private val lifecycleTracker by lazy { analyticsHelper.lifecycleTracker() }

    override fun onCreate(savedInstanceState: Bundle?) {
        var shouldShowSplashScreen = true
        installSplashScreen().setKeepOnScreenCondition { shouldShowSplashScreen }

        super.onCreate(savedInstanceState)
        appUpdateManager = AppUpdateManagerImpl(this)

        val darkThemeConfigFlow = userPreferencesRepository.observeDarkThemeConfig

        setupEdgeToEdge(darkThemeConfigFlow)

        ShareUtils.setActivityProvider { return@setActivityProvider this }
        FileKit.init(this)

        analyticsHelper.setUserId(deviceData)

        setContent {
            val status by networkMonitor.isOnline.collectAsStateWithLifecycle(false)

            if (status) {
                appUpdateManager.checkForAppUpdate()
            }

            lifecycleTracker.markAppLaunchComplete()

            SharedApp(
                updateScreenCapture = ::updateScreenCapture,
                handleRecreate = ::handleRecreate,
                handleThemeMode = {
                    AppCompatDelegate.setDefaultNightMode(it)
                },
                handleAppLocale = {
                    it?.let {
                        AppCompatDelegate.setApplicationLocales(
                            LocaleListCompat.forLanguageTags(it),
                        )
                        Locale.setDefault(Locale(it))
                    }
                },
                onSplashScreenRemoved = {
                    shouldShowSplashScreen = false
                },
            )
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.checkForResumeUpdateState()
        lifecycleTracker.markAppBackground()
    }

    override fun onStart() {
        super.onStart()
        lifecycleTracker.markAppLaunchStart()
    }

    private fun handleRecreate() {
        recreate()
    }

    private fun updateScreenCapture(isScreenCaptureAllowed: Boolean) {
        if (isScreenCaptureAllowed) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}
