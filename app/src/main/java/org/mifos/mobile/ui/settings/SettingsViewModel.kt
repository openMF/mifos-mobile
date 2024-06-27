package org.mifos.mobile.ui.settings


import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.R
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.enums.AppTheme
import org.mifos.mobile.core.model.enums.MifosAppLanguage
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
) : ViewModel() {

    val tenant: StateFlow<String?> = preferencesHelper
        .getStringFlowForKey(PreferencesHelper.TENANT)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val baseUrl: StateFlow<String?> = preferencesHelper
        .getStringFlowForKey(PreferencesHelper.BASE_URL)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val passcode: StateFlow<String?> = preferencesHelper
        .getStringFlowForKey(PreferencesHelper.PASSCODE)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val theme: StateFlow<AppTheme> = preferencesHelper
        .getIntFlowForKey(PreferencesHelper.APPLICATION_THEME)
        .flatMapLatest {
            flow { emit(AppTheme.entries[preferencesHelper.appTheme]) }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, AppTheme.SYSTEM)

    val language: StateFlow<MifosAppLanguage> = preferencesHelper
        .getStringFlowForKey(PreferencesHelper.LANGUAGE_TYPE)
        .flatMapLatest {
            flow { emit(MifosAppLanguage.fromCode(preferencesHelper.language)) }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, MifosAppLanguage.SYSTEM_LANGUAGE)


    fun tryUpdatingEndpoint(selectedBaseUrl: String, selectedTenant: String): Boolean {
        if (!(baseUrl.equals(selectedBaseUrl) && tenant.equals(selectedTenant))) {
            preferencesHelper.updateConfiguration(selectedBaseUrl, selectedTenant)
            preferencesHelper.clear()
            return true
        }
        return false
    }

    fun updateLanguage(language: MifosAppLanguage): Boolean {
        preferencesHelper.language = language.code
        val isSystemLanguage = (language == MifosAppLanguage.SYSTEM_LANGUAGE)
        preferencesHelper.isDefaultSystemLanguage = isSystemLanguage
        return !isSystemLanguage
    }

    fun updateTheme(theme: AppTheme) {
        AppCompatDelegate.setDefaultNightMode(
            when (theme) {
                AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
        preferencesHelper.appTheme = theme.ordinal
        preferencesHelper.applySavedTheme()
    }
}

enum class SettingsCardItem(
    val title: Int,
    val details: Int,
    val icon: Int,
    val subclassOf: Int,
    val firstItemInSubclass: Boolean = false,
    val showDividerInBottom: Boolean = false
) {
    PASSWORD(
        title = R.string.change_password,
        details = R.string.change_account_password,
        icon = R.drawable.ic_lock_black_24dp,
        firstItemInSubclass = true,
        subclassOf = R.string.accounts
    ),
    PASSCODE(
        title = R.string.change_passcode,
        details = R.string.change_app_passcode,
        icon = R.drawable.ic_passcode,
        showDividerInBottom = true,
        subclassOf = R.string.accounts
    ),
    LANGUAGE(
        title = R.string.language,
        details = R.string.choose_language,
        icon = R.drawable.ic_translate,
        firstItemInSubclass = true,
        subclassOf = R.string.other
    ),
    THEME(
        title = R.string.theme,
        details = R.string.change_app_theme,
        icon = R.drawable.ic_baseline_dark_mode_24,
        subclassOf = R.string.other
    ),
    ENDPOINT(
        title = R.string.pref_base_url_title,
        details = R.string.pref_base_url_desc,
        icon = R.drawable.ic_update,
        subclassOf = R.string.other
    )
}


