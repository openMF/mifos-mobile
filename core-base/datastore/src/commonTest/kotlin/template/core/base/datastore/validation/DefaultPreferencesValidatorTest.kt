/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.validation

import kotlin.test.Test
import kotlin.test.assertTrue

class DefaultPreferencesValidatorTest {
    private val validator = DefaultPreferencesValidator()

    @Test
    fun validateKey_AcceptsValidKey() {
        val result = validator.validateKey("validKey")
        assertTrue(result.isSuccess)
    }

    @Test
    fun validateKey_RejectsEmptyKey() {
        val result = validator.validateKey("")
        assertTrue(result.isFailure)
    }

    @Test
    fun validateValue_AcceptsNonNull() {
        val result = validator.validateValue(123)
        assertTrue(result.isSuccess)
    }

    @Test
    fun validateValue_RejectsNull() {
        val result = validator.validateValue(null)
        assertTrue(result.isFailure)
    }
}
