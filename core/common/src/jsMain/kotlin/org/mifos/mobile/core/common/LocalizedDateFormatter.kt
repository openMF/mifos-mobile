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

import kotlin.js.Date

actual object LocalizedDateFormatter {

    actual fun formatFullDate(year: Int, month: Int, day: Int): String {
        val date = Date(year, month - 1, day)

        val options = dateLocaleOptions {
            this.year = "numeric"
            this.month = "long"
            this.day = "numeric"
        }

        return date.asDynamic()
            .toLocaleDateString(undefined, options) as String
    }

    actual fun getRelativePrefix(day: Int, month: Int, year: Int): String? {
        val date = Date(year, month - 1, day)
        val now = Date()

        fun toLocalDateOnly(d: Date) =
            Date(d.getFullYear(), d.getMonth(), d.getDate())

        val today = toLocalDateOnly(now)
        val target = toLocalDateOnly(date)

        val msPerDay = 24 * 60 * 60 * 1000
        val diffDays = ((today.getTime() - target.getTime()) / msPerDay).toInt()

        val rtf = js(
            "new Intl.RelativeTimeFormat(undefined, { numeric: 'auto' })",
        )

        return when (diffDays) {
            0 -> rtf.format(0, "day") as? String
            1 -> rtf.format(-1, "day") as? String
            else -> null
        }
    }
}

fun dateLocaleOptions(init: dynamic.() -> Unit): dynamic {
    val options = js("{}")
    init(options)
    return options
}
