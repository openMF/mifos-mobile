/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.common

object Utils {
    fun generateFormString(data: Array<Array<String?>>): String {
        val formString = StringBuilder()
        for (aData in data) {
            formString.append(aData[0]).append(" : ").append(aData[1]).append('\n')
        }
        return formString.toString()
    }

    fun formatTransactionType(type: String?): String =
        type?.lowercase()
            ?.split("_")
            ?.joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercase() }
            }
            ?.trim()
            ?: ""
}
