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
import org.mifos.mobile.feature.qr.navigation.QrDisplayScreenRoute

internal class QrCodeDisplayViewModel(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<QrCodeDisplayState, QrCodeDisplayEvent, QrCodeDisplayAction>(
    initialState = QrCodeDisplayState(
        qrArgs = savedStateHandle.toRoute<QrDisplayScreenRoute>().qrString,
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

    override fun handleAction(action: QrCodeDisplayAction) {
        when (action) {
            QrCodeDisplayAction.OnNavigate -> sendEvent(QrCodeDisplayEvent.Navigate)
        }
    }

    private fun updateState(update: (QrCodeDisplayState) -> QrCodeDisplayState) {
        mutableStateFlow.update(update)
    }
}

data class QrCodeDisplayState(
    val option: String = "",
    val qrArgs: String? = null,
    val viewState: QrViewState = QrViewState.Loading,
) {

    sealed interface QrViewState {
        data object Loading : QrViewState

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

sealed interface QrCodeDisplayEvent {
    data object Navigate : QrCodeDisplayEvent
}

sealed interface QrCodeDisplayAction {
    data object OnNavigate : QrCodeDisplayAction
}
