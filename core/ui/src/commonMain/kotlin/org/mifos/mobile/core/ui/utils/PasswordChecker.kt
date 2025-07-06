/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils

import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.password_checker_digit_feedback
import mifos_mobile.core.ui.generated.resources.password_checker_empty_error
import mifos_mobile.core.ui.generated.resources.password_checker_lowercase_feedback
import mifos_mobile.core.ui.generated.resources.password_checker_min_length_feedback
import mifos_mobile.core.ui.generated.resources.password_checker_special_char_feedback
import mifos_mobile.core.ui.generated.resources.password_checker_strong_length_feedback
import mifos_mobile.core.ui.generated.resources.password_checker_too_long_error
import mifos_mobile.core.ui.generated.resources.password_checker_uppercase_feedback
import org.jetbrains.compose.resources.StringResource
import kotlin.math.log2
import kotlin.math.pow

object PasswordChecker {
    private const val MIN_PASSWORD_LENGTH = 12
    private const val STRONG_PASSWORD_LENGTH = 12
    private const val MIN_ENTROPY_BITS = 60.0
    private const val MAX_PASSWORD_LENGTH = 128

    fun getPasswordStrengthResult(password: String): PasswordStrengthResult {
        return when {
            password.isEmpty() -> PasswordStrengthResult.Error(
                Res.string.password_checker_empty_error,
            )
            password.length > MAX_PASSWORD_LENGTH -> {
                PasswordStrengthResult.Error(
                    Res.string.password_checker_too_long_error,
                )
            }

            else -> {
                val result = getPasswordStrength(password)
                PasswordStrengthResult.Success(result)
            }
        }
    }

    private fun getPasswordStrength(password: String): PasswordStrength {
        val length = password.length
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasNumbers = password.any { it.isDigit() }
        val hasSymbols = password.any { !it.isLetterOrDigit() }

        val numTypesPresent =
            listOf(hasUpperCase, hasLowerCase, hasNumbers, hasSymbols).count { it }
        val entropyBits = calculateEntropy(password)

        return when {
            length < MIN_PASSWORD_LENGTH -> PasswordStrength.LEVEL_0
            numTypesPresent == 1 -> PasswordStrength.LEVEL_1
            numTypesPresent == 2 -> PasswordStrength.LEVEL_2
            numTypesPresent == 3 && length >= STRONG_PASSWORD_LENGTH -> PasswordStrength.LEVEL_4
            numTypesPresent == 4 && length >= STRONG_PASSWORD_LENGTH &&
                entropyBits >= MIN_ENTROPY_BITS -> PasswordStrength.LEVEL_5

            else -> PasswordStrength.LEVEL_3
        }
    }

    private fun calculateEntropy(password: String): Double {
        val charPool = 26 + 26 + 10 + 33 // lowercase + uppercase + digits + symbols
        return log2(charPool.toDouble().pow(password.length))
    }

    fun getPasswordFeedback(password: String): List<StringResource> {
        val feedback = mutableListOf<StringResource>()

        if (password.length < MIN_PASSWORD_LENGTH) {
            feedback.add(
                Res.string.password_checker_min_length_feedback,
            )
        }
        if (!password.any { it.isUpperCase() }) {
            feedback.add(Res.string.password_checker_uppercase_feedback)
        }
        if (!password.any { it.isLowerCase() }) {
            feedback.add(Res.string.password_checker_lowercase_feedback)
        }
        if (!password.any { it.isDigit() }) {
            feedback.add(Res.string.password_checker_digit_feedback)
        }
        if (!password.any { !it.isLetterOrDigit() }) {
            feedback.add(Res.string.password_checker_special_char_feedback)
        }
        if (password.length < STRONG_PASSWORD_LENGTH) {
            feedback.add(Res.string.password_checker_strong_length_feedback)
        }

        return feedback
    }
}

sealed class PasswordStrengthResult {
    data class Success(val passwordStrength: PasswordStrength) : PasswordStrengthResult()

    data class Error(val message: StringResource) : PasswordStrengthResult()
}
