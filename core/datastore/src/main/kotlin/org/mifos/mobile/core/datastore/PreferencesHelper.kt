/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.datastore

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import org.mifos.mobile.core.model.enums.AppTheme
import org.mifos.mobile.core.model.enums.MifosAppLanguage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(@ApplicationContext context: Context?) {
    private val sharedPreferences: SharedPreferences? =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun clear() {
        val editor = sharedPreferences?.edit()
        // prevent deletion of url and tenant
        sharedPreferences?.all?.keys?.forEach { key ->
            if (key != BASE_URL && key != TENANT) {
                editor?.remove(key)
            }
        }
        editor?.apply()
    }

    fun getInt(preferenceKey: String?, preferenceDefaultValue: Int): Int? {
        return sharedPreferences?.getInt(preferenceKey, preferenceDefaultValue)
    }

    fun putInt(preferenceKey: String?, preferenceValue: Int) {
        sharedPreferences?.edit()?.putInt(preferenceKey, preferenceValue)?.apply()
    }

    fun getLong(preferenceKey: String?, preferenceDefaultValue: Long): Long? {
        return sharedPreferences?.getLong(preferenceKey, preferenceDefaultValue)
    }

    fun putLong(preferenceKey: String?, preferenceValue: Long) {
        sharedPreferences?.edit()?.putLong(preferenceKey, preferenceValue)?.apply()
    }

    fun getString(preferenceKey: String?, preferenceDefaultValue: String?): String? {
        return sharedPreferences?.getString(preferenceKey, preferenceDefaultValue)
    }

    private fun putString(preferenceKey: String?, preferenceValue: String?) {
        sharedPreferences?.edit()?.putString(preferenceKey, preferenceValue)?.apply()
    }

    fun putBoolean(preferenceKey: String?, preferenceValue: Boolean) {
        sharedPreferences?.edit()?.putBoolean(preferenceKey, preferenceValue)?.apply()
    }

    fun getBoolean(preferenceKey: String?, preferenceDefaultValue: Boolean): Boolean? {
        return sharedPreferences?.getBoolean(preferenceKey, preferenceDefaultValue)
    }

    fun saveToken(token: String?) {
        putString(TOKEN, token)
    }

    fun clearToken() {
        putString(TOKEN, "")
    }

    val token: String?
        get() = getString(TOKEN, "")

    val isAuthenticated: Boolean
        get() = !TextUtils.isEmpty(token)

    var userId: Long?
        get() = getLong(USER_ID, -1)
        set(id) {
            id?.let {
                putLong(USER_ID, it)
            }
        }

    var tenant: String?
        get() = getString(TENANT, DEFAULT_TENANT)
        set(tenant) {
            putString(TENANT, tenant)
        }

    var passcode: String?
        get() = getString(PASSCODE, "")
        set(passcode) {
            putString(PASSCODE, passcode)
        }

    var clientId: Long?
        get() = getLong(CLIENT_ID, -1)
        set(clientId) {
            clientId?.let {
                putLong(CLIENT_ID, it)
            }
        }

    var userName: String?
        get() = getString(USER_NAME, "")
        set(userName) {
            putString(USER_NAME, userName)
        }

    var clientName: String?
        get() = getString(CLIENT_NAME, "")
        set(clientName) {
            putString(CLIENT_NAME, clientName)
        }

    var officeName: String?
        get() = getString(OFFICE_NAME, "")
        set(officeName) {
            putString(OFFICE_NAME, officeName)
        }

    fun setOverviewState(state: Boolean) {
        putBoolean(OVERVIEW_STATE, state)
    }

    fun overviewState(): Boolean? {
        return getBoolean(OVERVIEW_STATE, true)
    }

    fun saveGcmToken(token: String?) {
        putString(GCM_TOKEN, token)
    }

    var userProfileImage: String?
        get() = getString(PROFILE_IMAGE, null)
        set(image) {
            putString(PROFILE_IMAGE, image)
        }

    val gcmToken: String?
        get() = getString(GCM_TOKEN, "")

    fun setSentTokenToServer(sentTokenToServer: Boolean) {
        putBoolean(SENT_TOKEN_TO_SERVER, sentTokenToServer)
    }

    fun sentTokenToServerState(): Boolean? {
        return getBoolean(SENT_TOKEN_TO_SERVER, false)
    }

    fun updateConfiguration(baseUrl: String?, tenant: String?) {
        sharedPreferences?.edit()
            ?.putString(BASE_URL, baseUrl)
            ?.putString(TENANT, tenant)
            ?.apply()
    }

    val baseUrl: String?
        get() = getString(BASE_URL, DEFAULT_BASE_URL)

    var appTheme
        get() = getInt(APPLICATION_THEME, AppTheme.SYSTEM.ordinal) ?: AppTheme.SYSTEM.ordinal
        set(value) {
            putInt(APPLICATION_THEME, value)
        }

    var language
        get() = getString(LANGUAGE_TYPE, MifosAppLanguage.ENGLISH.code)
            ?: MifosAppLanguage.SYSTEM_LANGUAGE.code
        set(language) {
            putString(LANGUAGE_TYPE, language)
        }

    var isDefaultSystemLanguage
        get() = getBoolean(DEFAULT_SYSTEM_LANGUAGE, false) == true
        set(value) {
            putBoolean(DEFAULT_SYSTEM_LANGUAGE, value)
        }

    companion object {
        private const val USER_ID = "preferences_user_id"
        private const val TOKEN = "preferences_token"
        private const val CLIENT_ID = "preferences_client"
        private const val OFFICE_NAME = "preferences_office_name"
        private const val USER_NAME = "preferences_user_name"
        const val PASSCODE = "preferences_passcode"
        private const val OVERVIEW_STATE = "preferences_overview_state"
        private const val SENT_TOKEN_TO_SERVER = "sentTokenToServer"
        private const val GCM_TOKEN = "gcm_token"
        const val TENANT = "preferences_base_tenant"
        const val BASE_URL = "preferences_base_url_key"
        private const val PROFILE_IMAGE = "preferences_profile_image"
        const val CLIENT_NAME = "client_name"
        const val APPLICATION_THEME = "application_theme"
        const val LANGUAGE_TYPE = "language_type"
        const val DEFAULT_SYSTEM_LANGUAGE = "default_system_language"

        private const val DEFAULT_TENANT = "default"
        private const val DEFAULT_BASE_URL = "https://gsoc.mifos.community"
    }

    fun getStringFlowForKey(keyForString: String) = callbackFlow<String?> {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (keyForString == key) {
                trySend(getString(keyForString, null))
            }
        }
        sharedPreferences?.registerOnSharedPreferenceChangeListener(listener)
        send(getString(keyForString, null))
        awaitClose { sharedPreferences?.unregisterOnSharedPreferenceChangeListener(listener) }
    }.buffer(Channel.Factory.UNLIMITED)

    fun getIntFlowForKey(keyForInt: String) = callbackFlow<Int?> {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (keyForInt == key) {
                trySend(getInt(keyForInt, -1))
            }
        }
        sharedPreferences?.registerOnSharedPreferenceChangeListener(listener)
        send(getInt(keyForInt, -1))
        awaitClose { sharedPreferences?.unregisterOnSharedPreferenceChangeListener(listener) }
    }.buffer(Channel.Factory.UNLIMITED)
}
