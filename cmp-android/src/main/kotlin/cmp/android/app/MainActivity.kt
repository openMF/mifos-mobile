/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.android.app

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
import org.mifos.mobile.core.ui.utils.ShareUtils
import template.core.base.platform.LocalManagerProvider
import java.util.Locale
import kotlin.getValue

/**
 * Main activity class.
 * This class is used to set the content view of the activity.
 *
 * @constructor Create empty Main activity
 * @see AppCompatActivity
 */
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
            LocalManagerProvider(context = this) {
                SharedApp(
                    handleThemeMode = {
                        AppCompatDelegate.setDefaultNightMode(it)
                    },
                    handleAppLocale = {
                        if (it.isNullOrBlank()) {
                            AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.getEmptyLocaleList(),
                            )
                        } else {
                            AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags(
                                    it,
                                ),
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
    }
}
