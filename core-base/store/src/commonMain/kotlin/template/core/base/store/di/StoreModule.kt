/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.store.di

import org.koin.dsl.module

/**
 * Koin module providing base Store infrastructure.
 *
 * Consumer apps should include this module and add their own store bindings
 * in their `core/data` DI module using [StoreFactory] to create store instances.
 */
val StoreModule = module {
    // Base store module — intentionally minimal.
    // StoreFactory is an object and doesn't need DI.
    // Consumer apps wire their own Store<Key, Output> instances
    // using their specific DAOs, API services, and converters.
}
