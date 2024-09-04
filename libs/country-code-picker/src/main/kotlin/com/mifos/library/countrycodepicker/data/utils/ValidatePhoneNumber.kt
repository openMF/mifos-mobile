/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package com.mifos.library.countrycodepicker.data.utils

import android.content.Context
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

private const val MIN_PHONE_LENGTH = 6

internal class ValidatePhoneNumber(private val context: Context) {
    private val phoneUtil: PhoneNumberUtil by lazy { PhoneNumberUtil.createInstance(context) }

    operator fun invoke(fullPhoneNumber: String): Boolean =
        if (fullPhoneNumber.length > MIN_PHONE_LENGTH) {
            try {
                phoneUtil.isValidNumber(phoneUtil.parse(fullPhoneNumber, null))
            } catch (ex: NumberParseException) {
                false
            }
        } else {
            false
        }
}
