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

import org.mobilenativefoundation.store.store5.Bookkeeper

/**
 * An in-memory [Bookkeeper] implementation for tracking unsynced local changes.
 *
 * Stores sync failure timestamps in a [MutableMap]. Suitable for lightweight
 * use cases where sync tracking doesn't need to survive process death.
 * For persistent tracking, use a Room-backed implementation in your app's
 * `core/data` layer.
 *
 * @param Key The store key type.
 */
class InMemoryBookkeeper<Key : Any> : Bookkeeper<Key> {

    private val failedSyncs = mutableMapOf<Key, Long>()

    override suspend fun getLastFailedSync(key: Key): Long? {
        return failedSyncs[key]
    }

    override suspend fun setLastFailedSync(key: Key, timestamp: Long): Boolean {
        failedSyncs[key] = timestamp
        return true
    }

    override suspend fun clear(key: Key): Boolean {
        failedSyncs.remove(key)
        return true
    }

    override suspend fun clearAll(): Boolean {
        failedSyncs.clear()
        return true
    }

    companion object {

        /**
         * Creates a [Bookkeeper] backed by a [MutableMap] (in-memory only).
         */
        fun <Key : Any> create(): InMemoryBookkeeper<Key> {
            return InMemoryBookkeeper()
        }
    }
}
