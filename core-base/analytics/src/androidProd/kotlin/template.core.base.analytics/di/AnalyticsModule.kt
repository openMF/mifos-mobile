/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
@file:Suppress("InvalidPackageDeclaration")

package template.core.base.analytics.di

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.FirebaseAnalytics
import dev.gitlive.firebase.analytics.analytics
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import template.core.base.analytics.AnalyticsHelper
import template.core.base.analytics.FirebaseAnalyticsHelper

actual val analyticsModule = module {
    single<FirebaseAnalytics> { Firebase.analytics }
    singleOf(::FirebaseAnalyticsHelper) bind AnalyticsHelper::class
}
