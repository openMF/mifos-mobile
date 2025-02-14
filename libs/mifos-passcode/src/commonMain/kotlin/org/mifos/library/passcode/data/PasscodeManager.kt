/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.library.passcode.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.mifos.library.passcode.model.PasscodePreferencesProto

class PasscodeManager(
    private val source: PasscodePreferencesDataSource,
    dispatcher: CoroutineDispatcher,
) {
    private val coroutineScope = CoroutineScope(dispatcher)

    val getPasscode = source.passcode.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = "",
    )

    val hasPasscode = source.hasPasscode.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = false,
    )

    suspend fun savePasscode(passcode: String) {
        source.updatePasscodeSettings(
            PasscodePreferencesProto(
                passcode = passcode,
                hasPasscode = passcode.isNotEmpty(),
            ),
        )
    }

    suspend fun clearPasscode() {
        source.clearInfo()
    }
}
