/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.registration.utils

import android.content.Context
import android.graphics.Color
import org.mifos.mobile.feature.auth.R

enum class PasswordStrength(private val resId: Int, val color: Int) {
    WEAK(R.string.password_strength_weak, Color.RED),
    MEDIUM(R.string.password_strength_medium, Color.argb(255, 220, 185, 0)),
    STRONG(R.string.password_strength_strong, Color.GREEN),
    VERY_STRONG(R.string.password_strength_very_strong, Color.BLUE),
    ;

    fun getText(ctx: Context?): CharSequence? = ctx?.getText(resId)

    companion object {
        private const val REQUIRED_LENGTH = 8
        private const val MAXIMUM_LENGTH = 15
        private const val REQUIRE_SPECIAL_CHARACTERS = true
        private const val REQUIRE_DIGITS = true
        private const val REQUIRE_LOWER_CASE = true
        private const val REQUIRE_UPPER_CASE = false

        fun calculateStrength(password: String): PasswordStrength {
            val score = calculateScore(password)
            return when {
                score >= 3 -> VERY_STRONG
                score == 2 -> STRONG
                score == 1 -> MEDIUM
                else -> WEAK
            }
        }

        @Suppress("ComplexCondition")
        private fun calculateScore(password: String): Int {
            if (password.length <= REQUIRED_LENGTH) return 0

            var score = 0
            val (hasUpper, hasLower, hasDigit, hasSpecial) = password.fold(
                BooleanArray(4),
            ) { acc, char ->
                when {
                    char.isUpperCase() -> acc[0] = true
                    char.isLowerCase() -> acc[1] = true
                    char.isDigit() -> acc[2] = true
                    !char.isLetterOrDigit() -> acc[3] = true
                }
                acc
            }

            if (hasUpper) score++
            if (hasLower) score++
            if (hasDigit) score++
            if (hasSpecial) score++

            if (REQUIRE_SPECIAL_CHARACTERS && !hasSpecial ||
                REQUIRE_UPPER_CASE && !hasUpper ||
                REQUIRE_LOWER_CASE && !hasLower ||
                REQUIRE_DIGITS && !hasDigit
            ) {
                score = 1
            } else {
                score = 2
                if (password.length > MAXIMUM_LENGTH) {
                    score = 3
                }
            }

            return score
        }
    }
}
