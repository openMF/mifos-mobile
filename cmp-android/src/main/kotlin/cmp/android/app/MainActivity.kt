/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.android.app

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import cmp.shared.SharedApp
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import template.core.base.ui.ShareUtils
import java.util.Locale
import kotlin.getValue

/**
 * Main activity class. This class is used to set the content view of the
 * activity.
 *
 * @constructor Create empty Main activity
 * @see AppCompatActivity
 */
@Suppress("UnusedPrivateProperty")
class MainActivity : AppCompatActivity() {
    /**
     * Called when the activity is starting.
     * This is where most initialization should go: calling [setContentView(int)] to inflate the activity's UI,
     */

    private val userPreferencesRepository: UserPreferencesRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            val userThemeConfig = userPreferencesRepository.observeDarkThemeConfig.first()
            AppCompatDelegate.setDefaultNightMode(userThemeConfig.osValue)
        }

        var shouldShowSplashScreen = true
        installSplashScreen().setKeepOnScreenCondition { shouldShowSplashScreen }

        val darkThemeConfigFlow = userPreferencesRepository.observeDarkThemeConfig

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setupEdgeToEdge(darkThemeConfigFlow)
        ShareUtils.setActivityProvider { return@setActivityProvider this }
        FileKit.init(this)
        /**
         * Set the content view of the activity.
         * @see setContent
         */
        setContent {
            SharedApp(
                handleThemeMode = {
                    AppCompatDelegate.setDefaultNightMode(it)
                },
                handleAppLocale = { localeTag ->
                    val currentLocales = AppCompatDelegate.getApplicationLocales()
                    val newLocales = if (localeTag != null) {
                        LocaleListCompat.forLanguageTags(localeTag)
                    } else {
                        // System Default: clear app-specific locale
                        LocaleListCompat.getEmptyLocaleList()
                    }

                    // Only update if the locale has actually changed
                    if (currentLocales != newLocales) {
                        AppCompatDelegate.setApplicationLocales(newLocales)
                        // Update Locale.setDefault for non-UI formatting
                        if (localeTag != null) {
                            // Use forLanguageTag to properly parse locales like "en-GB", "pt-BR"
                            Locale.setDefault(Locale.forLanguageTag(localeTag))
                        } else {
                            // Reset to true system default locale from device configuration
                            // Use Resources.getSystem() to get device locale unaffected by app overrides
                            val systemLocale = Resources.getSystem().configuration.locales[0]
                            Locale.setDefault(systemLocale)
                        }
                    }
                },
                onSplashScreenRemoved = {
                    shouldShowSplashScreen = false
                },
            )
        }
    }
}
