/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.library.passcode.utility

import android.content.Context
import com.mifos.library.passcode.R

class PreferenceManager(context: Context) {
    private val sharedPreference = context.getSharedPreferences(
        R.string.lib_mifos_passcode_pref_name.toString(),
        Context.MODE_PRIVATE,
    )

    var hasPasscode: Boolean
        get() = sharedPreference.getBoolean(R.string.lib_mifos_passcode_has_passcode.toString(), false)
        set(value) = sharedPreference.edit().putBoolean(R.string.lib_mifos_passcode_has_passcode.toString(), value)
            .apply()

    fun savePasscode(passcode: String) {
        sharedPreference.edit().putString(R.string.lib_mifos_passcode.toString(), passcode).apply()
        hasPasscode = true
    }

    fun getSavedPasscode(): String {
        return sharedPreference.getString(R.string.lib_mifos_passcode.toString(), "").toString()
    }

    fun clearPasscode() {
        sharedPreference.edit().clear().apply()
        hasPasscode = false
    }
}
