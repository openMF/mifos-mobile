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
    """ (year, month, day) => {
        const date = new Date(year, month - 1, day);
        const now = new Date();

        const toLocalDateOnly = (d) =>
            new Date(d.getFullYear(), d.getMonth(), d.getDate());

        const today = toLocalDateOnly(now);
        const target = toLocalDateOnly(date);

        const msPerDay = 24 * 60 * 60 * 1000;
        const diffDays = Math.floor((today - target) / msPerDay);

        const rtf = new Intl.RelativeTimeFormat(undefined, { numeric: 'auto' });

        if (diffDays === 0) return rtf.format(0, 'day');
        if (diffDays === 1) return rtf.format(-1, 'day');
        return null;
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

    actual fun getRelativePrefix(year: Int, month: Int, day: Int): String? =
        relativePrefixJs(year, month, day)
}
