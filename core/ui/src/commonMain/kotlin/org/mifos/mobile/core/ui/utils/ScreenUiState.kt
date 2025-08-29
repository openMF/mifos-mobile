/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils

import org.jetbrains.compose.resources.StringResource

/**
 * A sealed interface representing the different types of full-screen UI states
 * that can be used across various screens in an application.
 *
 * This generic state model provides a consistent way to handle common UI states
 * such as loading, errors, and success, which helps to standardize UI logic
 * and reduce boilerplate in ViewModels and UI layers.
 */
sealed interface ScreenUiState {
    /** * Represents a full-screen loading state.
     * * Use this state when the entire screen is busy fetching initial data,
     * and a loading indicator (e.g., a progress bar) should be displayed
     * over the whole screen.
     */
    data object Loading : ScreenUiState

    /** * Represents an empty state where no data is available to display.
     * * This state is typically used when a data fetch operation is successful
     * but returns an empty list or collection. The UI should show a message
     * or a graphic indicating that there is no content.
     */
    data object Empty : ScreenUiState

    /**
     * Represents a full-screen error state with a user-facing message.
     * * This state should be used when an unrecoverable error occurs, and the
     * entire screen needs to show an error message.
     *
     * @property message The [StringResource] for the error message to be displayed.
     */
    data class Error(val message: StringResource) : ScreenUiState

    /** * Represents a successful state where content can be displayed.
     * * This is the final state after a successful data fetch or operation.
     * The UI can now display the main content of the screen.
     */
    data object Success : ScreenUiState

    /** * Represents a state where there is a network connectivity issue.
     * * This state is useful for displaying a dedicated UI screen or a message
     * indicating that the device is offline or has lost its connection.
     */
    data object Network : ScreenUiState

    /** * Represents a state where an overlay loading spinner should be shown.
     * * Use this state when a background operation is in progress (e.g., a form
     * submission or a data refresh), but the existing content of the screen
     * should remain visible underneath a loading indicator.
     */
//    data object OverlayLoading : ScreenUiState
}
