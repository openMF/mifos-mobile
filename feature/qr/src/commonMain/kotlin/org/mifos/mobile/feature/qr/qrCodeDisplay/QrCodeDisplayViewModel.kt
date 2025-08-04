/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.qrCodeDisplay

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.github.alexzhirkevich.qrose.options.QrBallShape
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.QrCodeShape
import io.github.alexzhirkevich.qrose.options.QrColors
import io.github.alexzhirkevich.qrose.options.QrErrorCorrectionLevel
import io.github.alexzhirkevich.qrose.options.QrFrameShape
import io.github.alexzhirkevich.qrose.options.QrOptions
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.QrShapes
import io.github.alexzhirkevich.qrose.options.circle
import io.github.alexzhirkevich.qrose.options.roundCorners
import io.github.alexzhirkevich.qrose.options.solid
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.qr.generated.resources.Res
import mifos_mobile.feature.qr.generated.resources.choose_option
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.ui.utils.BaseViewModel

/**
 * `ViewModel` for the QR Code Display screen.
 *
 * This ViewModel is responsible for receiving a string from navigation arguments and
 * preparing the data and styling for displaying a QR code. It uses a [BaseViewModel]
 * to manage its state ([QrCodeDisplayState]), handle actions ([QrCodeDisplayAction]),
 * and emit events ([QrCodeDisplayEvent]).
 *
 * The QR code's styling (shapes and colors) is defined within the state itself
 * for easy access from the UI.
 *
 * @property savedStateHandle A handle to saved state data, used to retrieve navigation arguments.
 */
internal class QrCodeDisplayViewModel(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<QrCodeDisplayState, QrCodeDisplayEvent, QrCodeDisplayAction>(
    initialState = QrCodeDisplayState(
        qrArgs = savedStateHandle.toRoute<QrCodeDisplayRoute>().qrString,
    ),
) {

    init {
        viewModelScope.launch {
            val option = getString(Res.string.choose_option)
            updateState {
                it.copy(
                    option = option,
                )
            }
        }
        updateState {
            it.copy(
                viewState = QrCodeDisplayState.QrViewState.Content(
                    data = state.qrArgs ?: "",
                ),
            )
        }
    }

    /**
     * Handles incoming actions from the UI.
     *
     * @param action The [QrCodeDisplayAction] to be handled.
     */
    override fun handleAction(action: QrCodeDisplayAction) {
        when (action) {
            QrCodeDisplayAction.OnNavigate -> sendEvent(QrCodeDisplayEvent.Navigate)
        }
    }

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (QrCodeDisplayState) -> QrCodeDisplayState) {
        mutableStateFlow.update(update)
    }
}

/**
 * Represents the UI state for the QR Code Display screen.
 *
 * @property option A string to display on the screen, often a descriptive text.
 * @property qrArgs The string content to be encoded in the QR code, received from navigation.
 * @property viewState The current view state, which can be [QrViewState.Loading] or [QrViewState.Content].
 */
data class QrCodeDisplayState(
    val option: String = "",
    val qrArgs: String? = null,
    val viewState: QrViewState = QrViewState.Loading,
) {
    /**
     * A sealed interface representing the different view states of the QR code display.
     */
    sealed interface QrViewState {
        /** Represents a state where the QR code is still being prepared or loaded. */
        data object Loading : QrViewState

        /**
         * Represents a state where the QR code content is ready to be displayed.
         *
         * @property data The string content to be encoded in the QR code.
         */
        data class Content(
            val data: String,
        ) : QrViewState {

            private val shapes: QrShapes
                get() = QrShapes(
                    code = QrCodeShape.Default,
                    lightPixel = QrPixelShape.circle(),
                    darkPixel = QrPixelShape.circle(),
                    ball = QrBallShape.roundCorners(0.2f),
                    frame = QrFrameShape.roundCorners(0.2f),
                )

            private val colors: QrColors
                get() = QrColors(
                    light = QrBrush.solid(Color(0xFFFFFFFF)),
                    dark = QrBrush.solid(Color(0xFF0673BA)),
                    ball = QrBrush.solid(Color(0xFF6e6e6e)),
                    frame = QrBrush.solid(Color(0xFF6e6e6e)),
                )

            /**
             * The options for customizing the appearance of the QR code.
             * This is a [Composable] property because it uses UI-specific types like `Color`.
             */
            val options: QrOptions
                @Composable
                get() = QrOptions(
                    shapes = shapes,
                    colors = colors,
                    errorCorrectionLevel = QrErrorCorrectionLevel.Medium,
                )
        }
    }
}

/**
 * A sealed interface representing one-time events that trigger UI side effects,
 * such as navigation.
 */
sealed interface QrCodeDisplayEvent {
    /** Event to trigger a generic navigation action. */
    data object Navigate : QrCodeDisplayEvent
}

/**
 * A sealed interface representing user actions for the QR Code Display screen.
 */
sealed interface QrCodeDisplayAction {
    /** Action to navigate from the screen. */
    data object OnNavigate : QrCodeDisplayAction
}
