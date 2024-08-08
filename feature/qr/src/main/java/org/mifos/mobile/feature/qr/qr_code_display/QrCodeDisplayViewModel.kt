package org.mifos.mobile.feature.qr.qr_code_display

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.logs.AnalyticsEvent
import org.mifos.mobile.core.logs.AnalyticsHelper
import org.mifos.mobile.feature.qr.navigation.QR_ARGS
import org.mifos.mobile.feature.qr.utils.QrCodeGenerator
import javax.inject.Inject

@HiltViewModel
class QrCodeDisplayViewModel @Inject constructor(
    private var analyticsHelper: AnalyticsHelper,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val qrString = savedStateHandle.getStateFlow<String?>(key = QR_ARGS, initialValue = null)

    val qrCodeDisplayUiState = qrString
        .flatMapLatest { qrString ->
            flow {
                val qrBitmap = QrCodeGenerator.encodeAsBitmap(str = qrString)
                if(qrBitmap == null) {
                    emit(QrCodeDisplayUiState.Error())
                } else {
                    emit(QrCodeDisplayUiState.Success(qrBitmap = qrBitmap))
                }
            }
        }.catch {
            analyticsHelper.logEvent(
                AnalyticsEvent(
                    type = AnalyticsEvent.AnalyticsEventTypes.EXCEPTION.type,
                    listOf(
                        AnalyticsEvent.Param(key = AnalyticsEvent.ParamKeys.SCREEN_NAME, value = it.message ?: "")
                    )
                )
            )
            emit(QrCodeDisplayUiState.Error(errorMessage = it.message))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(300),
            initialValue = QrCodeDisplayUiState.Loading
        )
}

sealed class QrCodeDisplayUiState {
    data object Loading: QrCodeDisplayUiState()
    data class Success(val qrBitmap: Bitmap) : QrCodeDisplayUiState()
    data class Error(val errorMessage: String? = null): QrCodeDisplayUiState()
}

