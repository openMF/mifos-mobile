/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.ui

/**
 * Almost all the events in the app involve navigation or toasts. To prevent accidentally
 * navigating to the same view twice, by default, events are ignored if the view is not currently
 * resumed. To avoid that restriction, specific events can implement [BackgroundEvent].
 */
interface BackgroundEvent
