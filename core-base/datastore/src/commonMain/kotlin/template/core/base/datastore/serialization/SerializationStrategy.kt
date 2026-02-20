/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.serialization

import kotlinx.serialization.KSerializer

/**
 * Strategy for handling serialization operations.
 * Follows Single Responsibility Principle.
 */
interface SerializationStrategy {
    suspend fun <T> serialize(value: T, serializer: KSerializer<T>): Result<String>
    suspend fun <T> deserialize(data: String, serializer: KSerializer<T>): Result<T>
}
