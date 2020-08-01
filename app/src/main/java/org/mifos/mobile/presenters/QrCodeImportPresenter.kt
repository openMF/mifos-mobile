package org.mifos.mobile.presenters

import android.content.Context
import android.net.Uri

import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer

import com.isseiaoki.simplecropview.CropImageView

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import org.mifos.mobile.R
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.QrCodeImportView

import java.util.*
import javax.inject.Inject

/**
 * Created by manishkumar on 19/05/18.
 */
class QrCodeImportPresenter @Inject constructor(@ApplicationContext context: Context?) :
        BasePresenter<QrCodeImportView?>(context) {

    private var result: Result? = null
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var hasErrorOccured = false

    override fun attachView(mvpView: QrCodeImportView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
        context = null
    }

    /**
     * This method decodes the Qr code from the selected region
     *
     * @param sourceUri     contains the Uri of qr code from gallery
     * @param cropImageView contains cropImageView of layout
     */
    fun getDecodedResult(sourceUri: Uri?, cropImageView: CropImageView?) {
        checkViewAttached()
        mvpView?.showProgress()
        cropImageView?.crop(sourceUri)
                ?.executeAsSingle()
                ?.flatMap { bMap ->
                    val intArray = IntArray(bMap.width * bMap.height)
                    //copy pixel data from the Bitmap into the 'intArray' array
                    bMap.getPixels(intArray, 0, bMap.width, 0, 0,
                            bMap.width, bMap.height)
                    val source: LuminanceSource = RGBLuminanceSource(bMap.width,
                            bMap.height, intArray)
                    val bitmap = BinaryBitmap(HybridBinarizer(source))
                    //flags for decoding since it is large file
                    val tmpHintsMap: MutableMap<DecodeHintType, Boolean?> = EnumMap(DecodeHintType::class.java)
                    tmpHintsMap[DecodeHintType.TRY_HARDER] = java.lang.Boolean.TRUE
                    tmpHintsMap[DecodeHintType.PURE_BARCODE] = java.lang.Boolean.TRUE
                    val reader: Reader = MultiFormatReader()
                    try {
                        result = reader.decode(bitmap, tmpHintsMap)
                    } catch (e: Exception) {
                        mvpView?.hideProgress()
                        hasErrorOccured = true
                        mvpView?.showErrorReadingQr(context
                                ?.getString(R.string.error_reading_qr))
                    }
                    Single.just(result)
                }
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ result ->
                    mvpView?.hideProgress()
                    mvpView?.handleDecodedResult(result)
                }) {
                    mvpView?.hideProgress()
                    if (!hasErrorOccured) {
                        mvpView
                                ?.showErrorReadingQr(
                                        context?.getString(R.string.error_reading_qr))
                    }
                    hasErrorOccured = false
                }?.let { compositeDisposable.add(it) }
    }

}