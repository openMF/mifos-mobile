package org.mifos.mobile.ui.qr_code_import

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.zxing.Result
import com.isseiaoki.simplecropview.CropImageView
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentQrCodeImportBinding
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.beneficiary_application.BeneficiaryApplicationComposeFragment
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.common.utils.ParcelableAndSerializableUtils.getCheckedParcelable
import org.mifos.mobile.utils.Toaster
import java.io.FileNotFoundException
import java.io.InputStream

/**
 * Created by manishkumar on 19/05/18.
 */

class QrCodeImportFragment : BaseFragment() {

    private var _binding: FragmentQrCodeImportBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QrCodeImportViewModel by viewModels()

    private lateinit var qrUri: Uri
    private var uriValue: String? = null
    private var mFrameRect: RectF? = null
    private var inputStream: InputStream? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            uriValue = arguments?.getString(Constants.QR_IMAGE_URI)
            qrUri = Uri.parse(uriValue)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentQrCodeImportBinding.inflate(inflater, container, false)
        setToolbarTitle(getString(R.string.import_qr))
        // load the uri
        setBitmapImage(qrUri)
        with(binding) {
            ivCropQrCode.setCompressFormat(Bitmap.CompressFormat.JPEG)
            ivCropQrCode.setOutputMaxSize(150, 150)
            ivCropQrCode.load(qrUri)
                ?.initialFrameRect(mFrameRect)
                ?.executeAsCompletable()
            ivCropQrCode.setCropMode(CropImageView.CropMode.FREE)
            ivCropQrCode.setInitialFrameScale(0.8f)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.qrCodeImportUiState.collect {
                    when (it) {
                        is QrCodeImportUiState.Loading -> showProgress()

                        is QrCodeImportUiState.ShowError -> {
                            hideProgress()
                            showErrorReadingQr(getString(it.message))
                        }

                        is QrCodeImportUiState.HandleDecodedResult -> {
                            hideProgress()
                            handleDecodedResult(it.result)
                        }

                        QrCodeImportUiState.Initial -> {}
                    }
                }
            }
        }

        binding.btnProceed.setOnClickListener {
            proceed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // save data
        outState.putParcelable(org.mifos.mobile.core.common.Constants.FRAME_RECT, binding.ivCropQrCode.actualCropRect)
        outState.putParcelable(org.mifos.mobile.core.common.Constants.SOURCE_URI, binding.ivCropQrCode.sourceUri)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            // restore data
            mFrameRect = savedInstanceState.getCheckedParcelable(RectF::class.java, org.mifos.mobile.core.common.Constants.FRAME_RECT)
            qrUri = savedInstanceState.getCheckedParcelable(Uri::class.java, org.mifos.mobile.core.common.Constants.SOURCE_URI)!!
        }
    }

    fun proceed() {
//        viewModel.getDecodedResult(qrUri, binding.ivCropQrCode)
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param message Error message that tells the user about the problem.
     */
    private fun showErrorReadingQr(message: String?) {
        Toaster.show(binding.root, message)
    }

    /**
     * CallBack for[CropImageView] which retrieves data from QRCode
     * Opens [BeneficiaryApplicationFragment] with [BeneficiaryState] as
     * `BeneficiaryState.CREATE_QR`
     *
     * @param result contains the results from decoded QR bitmap
     */
    private fun handleDecodedResult(result: Result?) {
        val gson = Gson()
        try {
            val beneficiary = gson.fromJson(result?.text, Beneficiary::class.java)
            activity?.supportFragmentManager?.popBackStack()
            (activity as BaseActivity?)?.replaceFragment(
                BeneficiaryApplicationComposeFragment.newInstance(BeneficiaryState.CREATE_QR, beneficiary),
                true,
                R.id.container,
            )
        } catch (e: JsonSyntaxException) {
            Toast.makeText(
                activity,
                getString(R.string.invalid_qr),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    /**
     * Shows [org.mifos.mobile.utils.ProgressBarHandler]
     */
    fun showProgress() {
        showProgressBar()
    }

    /**
     * Hides [org.mifos.mobile.utils.ProgressBarHandler]
     */
    fun hideProgress() {
        hideProgressBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgress()
        _binding = null
    }

    /**
     * Initializing UI
     *
     * @param qrImageUri contains Uri of qr code image
     */
    private fun setBitmapImage(qrImageUri: Uri) {
        try {
            inputStream = context?.contentResolver?.openInputStream(qrImageUri)
        } catch (e: FileNotFoundException) {
            Toaster.show(binding.root, getString(R.string.error_fetching_image))
        }
        val b = BitmapFactory.decodeStream(inputStream, null, null)
        try {
            if (inputStream != null) {
                inputStream?.close()
            }
        } catch (e: Exception) {
            Toaster.show(binding.root, getString(R.string.error_fetching_image))
        }
        binding.ivCropQrCode.imageBitmap = b
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
