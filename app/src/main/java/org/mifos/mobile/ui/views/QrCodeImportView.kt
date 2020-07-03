package org.mifos.mobile.ui.views

import com.google.zxing.Result
import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by manishkumar on 19/05/18.
 */
interface QrCodeImportView : MVPView {
    fun showErrorReadingQr(message: String?)
    fun handleDecodedResult(result: Result?)
}