package org.mifos.mobile.utils

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import org.mifos.mobile.api.local.PreferencesHelper

enum class AppTheme(
    val themeName: String
) {
    SYSTEM(themeName = "System Theme"),
    LIGHT(themeName = "Light Theme"),
    DARK(themeName = "Dark Theme")
}

fun PreferencesHelper.applySavedTheme() {
    val applicationTheme = AppTheme.entries[this.appTheme]
    AppCompatDelegate.setDefaultNightMode(
        when {
            applicationTheme == AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            applicationTheme == AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            Build.VERSION.SDK_INT > Build.VERSION_CODES.P -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> AppCompatDelegate.MODE_NIGHT_NO
        },
    )
}