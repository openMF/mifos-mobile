package org.mifos.mobile.ui.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.zxing.Result
import com.isseiaoki.simplecropview.CropImageView
import org.mifos.mobile.R
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.presenters.QrCodeImportPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.BeneficiaryState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.QrCodeImportView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Toaster
import java.io.FileNotFoundException
import java.io.InputStream
import javax.inject.Inject

/**
 * Created by manishkumar on 19/05/18.
 */
class QrCodeImportFragment : BaseFragment(), QrCodeImportView {
    private var rootView: View? = null
    private var qrUri: Uri? = null
    private var uriValue: String? = null
    private var mFrameRect: RectF? = null
    private var inputStream: InputStream? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.iv_crop_qr_code)
    var cropImageView: CropImageView? = null

    @kotlin.jvm.JvmField
    @Inject
    var qrCodeImportPresenter: QrCodeImportPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            uriValue = arguments!!.getString(Constants.QR_IMAGE_URI)
            qrUri = Uri.parse(uriValue)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_qr_code_import, container, false)
        (activity as BaseActivity?)!!.activityComponent!!.inject(this)
        setToolbarTitle(getString(R.string.import_qr))
        ButterKnife.bind(this, rootView!!)
        //load the uri
        setBitmapImage(qrUri)
        cropImageView!!.setCompressFormat(Bitmap.CompressFormat.JPEG)
        cropImageView!!.setOutputMaxSize(150, 150)
        cropImageView!!.load(qrUri)
                .initialFrameRect(mFrameRect)
                .executeAsCompletable()
        cropImageView!!.setCropMode(CropImageView.CropMode.FREE)
        cropImageView!!.setInitialFrameScale(0.8f)
        qrCodeImportPresenter!!.attachView(this)
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // save data
        outState.putParcelable(Constants.FRAME_RECT, cropImageView!!.actualCropRect)
        outState.putParcelable(Constants.SOURCE_URI, cropImageView!!.sourceUri)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            // restore data
            mFrameRect = savedInstanceState.getParcelable(Constants.FRAME_RECT)
            qrUri = savedInstanceState.getParcelable(Constants.SOURCE_URI)
        }
    }

    @OnClick(R.id.btn_proceed)
    fun proceed() {
        qrCodeImportPresenter!!.getDecodedResult(qrUri, cropImageView)
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param message Error message that tells the user about the problem.
     */
    override fun showErrorReadingQr(message: String?) {
        Toaster.show(rootView, message)
    }

    /**
     * CallBack for[CropImageView] which retrieves data from QRCode
     * Opens [BeneficiaryApplicationFragment] with [BeneficiaryState] as
     * `BeneficiaryState.CREATE_QR`
     *
     * @param result contains the results from decoded QR bitmap
     */
    override fun handleDecodedResult(result: Result?) {
        val gson = Gson()
        try {
            val beneficiary = gson.fromJson(result!!.text, Beneficiary::class.java)
            activity!!.supportFragmentManager.popBackStack()
            (activity as BaseActivity?)!!.replaceFragment(BeneficiaryApplicationFragment.Companion.newInstance(BeneficiaryState.CREATE_QR, beneficiary),
                    true, R.id.container)
        } catch (e: JsonSyntaxException) {
            Toast.makeText(activity, getString(R.string.invalid_qr),
                    Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Shows [org.mifos.mobile.utils.ProgressBarHandler]
     */
    override fun showProgress() {
        showProgressBar()
    }

    /**
     * Hides [org.mifos.mobile.utils.ProgressBarHandler]
     */
    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgress()
        qrCodeImportPresenter!!.detachView()
    }

    /**
     * Initializing UI
     *
     * @param qrImageUri contains Uri of qr code image
     */
    fun setBitmapImage(qrImageUri: Uri?) {
        try {
            inputStream = context!!.contentResolver.openInputStream(qrImageUri)
        } catch (e: FileNotFoundException) {
            Toaster.show(rootView, getString(R.string.error_fetching_image))
        }
        val b = BitmapFactory.decodeStream(inputStream, null, null)
        try {
            if (inputStream != null) {
                inputStream!!.close()
            }
        } catch (e: Exception) {
            Toaster.show(rootView, getString(R.string.error_fetching_image))
        }
        cropImageView!!.imageBitmap = b
    }

    companion object {
        fun newInstance(uri: Uri): QrCodeImportFragment {
            val fragment = QrCodeImportFragment()
            val args = Bundle()
            args.putString(Constants.QR_IMAGE_URI, uri.toString())
            fragment.arguments = args
            return fragment
        }
    }
}