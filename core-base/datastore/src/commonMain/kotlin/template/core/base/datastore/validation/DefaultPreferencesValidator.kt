/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.validation

import template.core.base.datastore.exceptions.InvalidKeyException

/**
 * Default implementation of [PreferencesValidator] for validating keys and values in the data store.
 *
 * This implementation enforces constraints such as non-blank keys, maximum key length, and value size limits.
 *
 * Example usage:
 * ```kotlin
 * val validator = DefaultPreferencesValidator()
 * validator.validateKey("theme")
 * validator.validateValue("dark")
 * ```
 */
class DefaultPreferencesValidator : PreferencesValidator {

    /**
     * {@inheritDoc}
     */
    override fun validateKey(key: String): Result<Unit> {
        return when {
            key.isBlank() -> Result.failure(
                InvalidKeyException("Key cannot be blank"),
            )

            key.length > 255 -> Result.failure(
                InvalidKeyException("Key length cannot exceed 255 characters: '$key'"),
            )

            key.contains('\u0000') -> Result.failure(
                InvalidKeyException("Key cannot contain null characters: '$key'"),
            )

            else -> Result.success(Unit)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun <T> validateValue(value: T): Result<Unit> {
        return when (value) {
            null -> Result.failure(
                IllegalArgumentException("Value cannot be null"),
            )

            is String -> {
                if (value.length > 10000) {
                    Result.failure(
                        IllegalArgumentException("String value too large: ${value.length} characters"),
                    )
                } else {
                    Result.success(Unit)
                }
            }

            else -> Result.success(Unit)
        }
    }
}
