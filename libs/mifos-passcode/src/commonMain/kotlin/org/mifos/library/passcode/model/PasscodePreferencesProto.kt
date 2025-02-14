/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.library.passcode.model

import kotlinx.serialization.Serializable

@Serializable
data class PasscodePreferencesProto(
    val passcode: String,
    val hasPasscode: Boolean,
) {
    companion object {
        val DEFAULT = PasscodePreferencesProto(passcode = "", hasPasscode = false)
    }
}
