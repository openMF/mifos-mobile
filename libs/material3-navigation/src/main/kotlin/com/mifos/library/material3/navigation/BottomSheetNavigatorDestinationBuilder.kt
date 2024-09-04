/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package com.mifos.library.material3.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestinationBuilder
import androidx.navigation.NavDestinationDsl
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import kotlin.reflect.KClass
import kotlin.reflect.KType

/** DSL for constructing a new [ComposeNavigator.Destination] */
@NavDestinationDsl
class BottomSheetNavigatorDestinationBuilder :
    NavDestinationBuilder<BottomSheetNavigator.Destination> {

    private val composeNavigator: BottomSheetNavigator
    private val content: @Composable ColumnScope.(NavBackStackEntry) -> Unit

    constructor(
        navigator: BottomSheetNavigator,
        route: String,
        content: @Composable ColumnScope.(NavBackStackEntry) -> Unit,
    ) : super(navigator, route) {
        this.composeNavigator = navigator
        this.content = content
    }

    constructor(
        navigator: BottomSheetNavigator,
        route: KClass<*>,
        typeMap: Map<KType, @JvmSuppressWildcards NavType<*>>,
        content: @Composable ColumnScope.(NavBackStackEntry) -> Unit,
    ) : super(navigator, route, typeMap) {
        this.composeNavigator = navigator
        this.content = content
    }

    override fun instantiateDestination(): BottomSheetNavigator.Destination {
        return BottomSheetNavigator.Destination(composeNavigator, content)
    }

    override fun build(): BottomSheetNavigator.Destination {
        return super.build()
    }
}
