/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation.utils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

/**
 * Gets the route string for an object.
 */
@OptIn(InternalSerializationApi::class)
fun <T : Any> T.toObjectNavigationRoute(): String = this::class.toObjectKClassNavigationRoute()

/**
 * Gets the route string for a [KClass] of an object.
 */
@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
fun <T : Any> KClass<T>.toObjectKClassNavigationRoute(): String =
    this.serializer().descriptor.serialName
