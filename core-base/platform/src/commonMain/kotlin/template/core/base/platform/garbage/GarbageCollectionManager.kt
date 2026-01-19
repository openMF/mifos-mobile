/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.platform.garbage

interface GarbageCollectionManager {
    /**
     * Calls the garbage collector on the [Runtime] in an effort to clear the unused resources in
     * the heap.
     */
    fun tryCollect()
}

expect val garbageCollector: () -> Unit
