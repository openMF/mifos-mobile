/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.store

import org.mobilenativefoundation.store.store5.Validator
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

/**
 * A TTL-based [Validator] that marks cached data as stale after a given duration.
 *
 * Tracks when data was last fetched using [TimeSource.Monotonic] and considers
 * it invalid once the [ttl] has elapsed. Call [markFresh] when fresh data arrives.
 *
 * @param Output The cached data type.
 * @param ttl Maximum age before data is considered stale. Defaults to 30 minutes.
 */
class DefaultValidator<Output : Any>(
    private val ttl: Duration = 30.minutes,
) : Validator<Output> {

    private var lastFetchMark: TimeSource.Monotonic.ValueTimeMark? = null

    /**
     * Marks the current moment as the last fetch time.
     * Call this after successfully fetching fresh data.
     */
    fun markFresh() {
        lastFetchMark = TimeSource.Monotonic.markNow()
    }

    override suspend fun isValid(item: Output): Boolean {
        val mark = lastFetchMark ?: return false
        return mark.elapsedNow() < ttl
    }

    companion object {

        /**
         * Creates a [Validator] that always considers data valid (no TTL).
         */
        fun <Output : Any> alwaysValid(): Validator<Output> {
            return Validator.by { true }
        }

        /**
         * Creates a TTL-based [Validator] with the given duration.
         *
         * @param ttl Maximum age before data is stale.
         */
        fun <Output : Any> withTtl(
            ttl: Duration = 30.minutes,
        ): DefaultValidator<Output> {
            return DefaultValidator(ttl = ttl)
        }
    }
}
