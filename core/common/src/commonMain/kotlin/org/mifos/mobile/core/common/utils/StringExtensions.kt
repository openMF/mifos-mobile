/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.common.utils

/**
 * Whether or not string is a valid email address.
 *
 * This just checks if the string contains the "@" symbol.
 */
fun String.isValidEmail(): Boolean = contains("@")

fun maskString(input: String, maskChar: Char = '*'): String {
    if (input.length <= 3) return input

    val visibleCount = 3
    val maskLength = input.length - visibleCount

    return buildString {
        append(maskChar.toString().repeat(maskLength))
        append(input.takeLast(visibleCount))
    }
}

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { string ->
    string.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
