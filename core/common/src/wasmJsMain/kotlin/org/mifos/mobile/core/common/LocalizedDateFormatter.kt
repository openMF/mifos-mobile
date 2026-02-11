/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.common

@JsFun(
    """ (year, month, day) => {
        if (month < 1 || month > 12) return "";

        return new Date(year, month - 1, day)
            .toLocaleDateString(undefined, {
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            });
    } """,
)
private external fun formatFullDateJs(
    year: Int,
    month: Int,
    day: Int,
): String

@JsFun(
    """ (month, abbreviated) => {
        if (month < 1 || month > 12) return "";

        const style = abbreviated ? 'short' : 'long';
        return new Date(2000, month - 1, 1)
            .toLocaleDateString(undefined, { month: style });
    } """,
)
private external fun relativePrefixJs(
    year: Int,
    month: Int,
    day: Int,
): String?

actual object LocalizedDateFormatter {

    actual fun formatFullDate(year: Int, month: Int, day: Int): String =
        formatFullDateJs(year, month, day)

    actual fun getRelativePrefix(day: Int, month: Int, year: Int): String? =
        relativePrefixJs(year, month, day)
}
