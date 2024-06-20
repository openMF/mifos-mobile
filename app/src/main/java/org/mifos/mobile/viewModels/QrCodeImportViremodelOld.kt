//package org.mifos.mobile.viewModels
//
//import android.net.Uri
//import androidx.lifecycle.ViewModel
//import com.google.zxing.*
//import com.google.zxing.common.HybridBinarizer
//import com.isseiaoki.simplecropview.CropImageView
//import dagger.hilt.android.lifecycle.HiltViewModel
//import io.reactivex.Single
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.disposables.CompositeDisposable
//import io.reactivex.schedulers.Schedulers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import org.mifos.mobile.R
//import org.mifos.mobile.ui.qr_code_import.QrCodeUiState
//import java.util.*
//import javax.inject.Inject
//
//@HiltViewModel
//class QrCodeImportViremodelOld @Inject constructor() : ViewModel() {
//
//    private val compositeDisposables: CompositeDisposable = CompositeDisposable()
//
//    private var result: Result? = null
//    private var hasErrorOccurred = false
//
//    private val _qrCodeUiState = MutableStateFlow<QrCodeUiState>(QrCodeUiState.Initial)
//    val qrCodeUiState: StateFlow<QrCodeUiState> = _qrCodeUiState
//
//    fun getDecodedResult(sourceUri: Uri?, cropImageView: CropImageView?) {
//        _qrCodeUiState.value = QrCodeUiState.Loading
//        cropImageView?.crop(sourceUri)
//            ?.executeAsSingle()
//            ?.flatMap { bMap ->
//                val intArray = IntArray(bMap.width * bMap.height)
//                // copy pixel data from the Bitmap into the 'intArray' array
//                bMap.getPixels(
//                    intArray,
//                    0,
//                    bMap.width,
//                    0,
//                    0,
//                    bMap.width,
//                    bMap.height,
//                )
//                val source: LuminanceSource = RGBLuminanceSource(
//                    bMap.width,
//                    bMap.height,
//                    intArray,
//                )
//                val bitmap = BinaryBitmap(HybridBinarizer(source))
//                // flags for decoding since it is large file
//                val tmpHintsMap: MutableMap<DecodeHintType, Boolean?> =
//                    EnumMap(DecodeHintType::class.java)
//                tmpHintsMap[DecodeHintType.TRY_HARDER] = java.lang.Boolean.TRUE
//                tmpHintsMap[DecodeHintType.PURE_BARCODE] = java.lang.Boolean.TRUE
//                val reader: Reader = MultiFormatReader()
//                try {
//                    result = reader.decode(bitmap, tmpHintsMap)
//                } catch (e: Exception) {
//                    _qrCodeUiState.value = QrCodeUiState.ShowError(R.string.error_reading_qr)
//                }
//                Single.just(result)
//            }
//            ?.subscribeOn(Schedulers.newThread())
//            ?.observeOn(AndroidSchedulers.mainThread())
//            ?.subscribe({ result ->
//                _qrCodeUiState.value = result?.let { QrCodeUiState.HandleDecodedResult(it) }!!
//            }) {
//                if (!hasErrorOccurred) {
//                    _qrCodeUiState.value = QrCodeUiState.ShowError(R.string.error_reading_qr)
//                }
//                hasErrorOccurred = false
//            }?.let { compositeDisposables.add(it) }
//    }
//}