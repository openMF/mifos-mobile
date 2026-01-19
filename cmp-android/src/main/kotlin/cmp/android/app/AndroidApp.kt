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

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import cmp.shared.utils.initKoin
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.request.CachePolicy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mifos.core.data.repository.UserDataRepository
import template.core.base.ui.getDefaultImageLoader

/**
 * Android application class.
 * This class is used to initialize Koin modules for dependency injection in the Android application.
 * It sets up the Koin framework, providing the necessary dependencies for the app.
 *
 * @constructor Create empty Android app
 * @see Application
 */
class AndroidApp : Application(), SingletonImageLoader.Factory, KoinComponent {

    private val userDataRepository: UserDataRepository by inject()

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AndroidApp)
            androidLogger()
        }

        // Restore the user's saved language preference to AppCompatDelegate.
        // This ensures the app always launches with the user's chosen language,
        // regardless of system settings or device language.
        restoreSavedLanguage()
    }

    /**
     * Restores the user's saved language preference from the repository to AppCompatDelegate.
     *
     * This runs BEFORE any Activities are created, ensuring the app launches with the
     * correct language. The app's saved preference always takes precedence.
     */
    private fun restoreSavedLanguage() {
        runBlocking {
            val userData = userDataRepository.userData.first()
            val savedLanguage = userData.appLanguage

            // Convert the saved LanguageConfig to LocaleListCompat
            val desiredLocales = if (savedLanguage.localeName != null) {
                LocaleListCompat.forLanguageTags(savedLanguage.localeName)
            } else {
                // System default
                LocaleListCompat.getEmptyLocaleList()
            }

            // Only update if the current locale differs from saved preference
            val currentLocales = AppCompatDelegate.getApplicationLocales()
            if (currentLocales != desiredLocales) {
                AppCompatDelegate.setApplicationLocales(desiredLocales)
            }
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader =
        getDefaultImageLoader(context)
            .newBuilder()
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.25)
                    .build()
            }
            .build()
}
