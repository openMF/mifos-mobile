package org.mifos.mobile.feature.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.enums.AppTheme
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
) {
    private val _themeFlow = MutableStateFlow(AppTheme.entries[preferencesHelper.appTheme])
    val themeFlow: StateFlow<AppTheme> = _themeFlow

    fun notifyThemeUpdated(theme: AppTheme) {
        _themeFlow.value = theme
    }

    fun isDarkTheme(theme: AppTheme): String {
        return when (theme) {
            AppTheme.DARK -> "DARK"
            AppTheme.LIGHT -> "LIGHT"
            else -> "SYSTEM"
        }
    }
}
