/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.onboarding.permissions

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class OnboardingPermissionsViewModel : BaseViewModel<
    OnboardingPermissionsState,
    OnboardingPermissionsEvent,
    OnboardingPermissionsAction,
    >(
    OnboardingPermissionsState(),
) {
    override fun handleAction(action: OnboardingPermissionsAction) {
        when (action) {
            is OnboardingPermissionsAction.CompletePermissionOnboarding -> handleCompletePermissionOnboarding()
        }
    }

    private fun handleCompletePermissionOnboarding() {
        viewModelScope.launch {
            sendEvent(OnboardingPermissionsEvent.NavigateToLanguage)
        }
    }
}

internal class OnboardingPermissionsState

internal sealed interface OnboardingPermissionsEvent {
    data object NavigateToLanguage : OnboardingPermissionsEvent
}

internal sealed interface OnboardingPermissionsAction {
    data object CompletePermissionOnboarding : OnboardingPermissionsAction
}
