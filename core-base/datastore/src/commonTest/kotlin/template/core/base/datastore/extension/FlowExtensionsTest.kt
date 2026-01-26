/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.extension

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import template.core.base.datastore.contracts.DataStoreChangeEvent
import template.core.base.datastore.extensions.mapWithDefault
import template.core.base.datastore.extensions.onlyAdditions
import template.core.base.datastore.extensions.onlyRemovals
import template.core.base.datastore.extensions.onlyUpdates
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FlowExtensionsTest {

    @Test
    fun mapWithDefault_HandlesErrors() = runTest {
        val flow = flowOf(1, 2, 3)
            .mapWithDefault("default") {
                if (it == 2) throw RuntimeException("Error")
                "value_$it"
            }

        flow.test {
            assertEquals("value_1", awaitItem())
            assertEquals("default", awaitItem()) // Error case
            awaitComplete()
        }
    }

    @Test
    fun filterChangeType_FiltersCorrectTypes() = runTest {
        val changes = flowOf(
            DataStoreChangeEvent.ValueAdded("key1", "value1"),
            DataStoreChangeEvent.ValueUpdated("key2", "old", "new"),
            DataStoreChangeEvent.ValueAdded("key3", "value3"),
            DataStoreChangeEvent.ValueRemoved("key4", "value4"),
        )

        changes.onlyAdditions().test {
            val first = awaitItem()
            assertTrue(true)
            assertEquals("key1", first.key)

            val second = awaitItem()
            assertTrue(true)
            assertEquals("key3", second.key)

            awaitComplete()
        }
    }

    @Test
    fun onlyUpdates_FiltersUpdateChanges() = runTest {
        val changes = flowOf(
            DataStoreChangeEvent.ValueAdded("key1", "value1"),
            DataStoreChangeEvent.ValueUpdated("key2", "old", "new"),
            DataStoreChangeEvent.ValueRemoved("key3", "value3"),
        )

        changes.onlyUpdates().test {
            val update = awaitItem()
            assertTrue(true)
            assertEquals("key2", update.key)
            assertEquals("old", update.oldValue)
            assertEquals("new", update.newValue)

            awaitComplete()
        }
    }

    @Test
    fun onlyRemovals_FiltersRemovalChanges() = runTest {
        val changes = flowOf(
            DataStoreChangeEvent.ValueAdded("key1", "value1"),
            DataStoreChangeEvent.ValueRemoved("key2", "value2"),
            DataStoreChangeEvent.ValueUpdated("key3", "old", "new"),
        )

        changes.onlyRemovals().test {
            val removal = awaitItem()
            assertTrue(true)
            assertEquals("key2", removal.key)
            assertEquals("value2", removal.oldValue)

            awaitComplete()
        }
    }
}
