package org.mifos.mobile.ui.beneficiary.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.databinding.FragmentBeneficiaryAddOptionsBinding
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.beneficiary_application.BeneficiaryApplicationComposeFragment
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.model.enums.RequestAccessType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.qr.QrCodeReaderFragment
import org.mifos.mobile.ui.qr_code_import.QrCodeImportComposeFragment
import org.mifos.mobile.utils.CheckSelfPermissionAndRequest
import org.mifos.mobile.utils.CheckSelfPermissionAndRequest.checkSelfPermission
import org.mifos.mobile.utils.CheckSelfPermissionAndRequest.requestPermission
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.feature.beneficiary.presentation.BeneficiaryScreen
import org.mifos.mobile.utils.Toaster

/**
 * Created by dilpreet on 5/7/17.
 */
@AndroidEntryPoint
class BeneficiaryAddOptionsFragment : BaseFragment() {

    private var _binding: FragmentBeneficiaryAddOptionsBinding? = null
    private val binding get() = _binding!!

    private var read_media_image_status = false
    private var external_storage_read_status = false
    private var external_storage_write_status = false

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                MifosMobileTheme {
                    BeneficiaryScreen(
                        topAppbarNavigateback = {
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        },
                        addiconClicked = { addManually() },
                        scaniconClicked = { addUsingQrCode() },
                        uploadIconClicked = { addByImportingQrCode() },
                    )
                }

            }

        }
    }

    /**
     * Opens [BeneficiaryApplicationFragment] with [BeneficiaryState] as
     * `BeneficiaryState.CREATE_MANUAL`
     */
    fun addManually() {
        (activity as BaseActivity?)?.replaceFragment(
            org.mifos.mobile.ui.beneficiary_application.BeneficiaryApplicationComposeFragment.newInstance(
                BeneficiaryState.CREATE_MANUAL,
                null,
            ),
            true,
            R.id.container,
        )
    }

    /**
     * It first checks CAMERA runtime permission and if it returns true then it opens
     * [QrCodeReaderFragment] , if it returns false then ask for permissions.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun addUsingQrCode() {
        if (checkSelfPermission(
                activity,
                Manifest.permission.CAMERA,
            )
        ) {
            (activity as BaseActivity?)?.replaceFragment(
                QrCodeReaderFragment.newInstance(),
                true,
                R.id.container,
            )
        } else {
            requestPermission(RequestAccessType.CAMERA)
        }
    }

    /**
     * It first checks Storage Read and Write Permission then if both of them are true then it opens
     * Intent to all gallery app
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun addByImportingQrCode() {
        (activity as BaseActivity?)?.replaceFragment(
            QrCodeImportComposeFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    /**
     * Uses [CheckSelfPermissionAndRequest] to check for runtime permissions
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermission(requestAccessType: RequestAccessType) {
        when (requestAccessType) {
            RequestAccessType.CAMERA -> {
                requestPermission(
                    (activity as BaseActivity?)!!,
                    Manifest.permission.CAMERA,
                    Constants.PERMISSIONS_REQUEST_CAMERA,
                    resources.getString(
                        R.string.dialog_message_camera_permission_denied_prompt,
                    ),
                    resources.getString(R.string.dialog_message_camera_permission_never_ask_again),
                    Constants.PERMISSIONS_CAMERA_STATUS,
                )
            }

            RequestAccessType.EXTERNAL_STORAGE_READ -> TODO()
            RequestAccessType.EXTERNAL_STORAGE_WRITE -> TODO()
            RequestAccessType.READ_MEDIA_IMAGES -> TODO()
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            org.mifos.mobile.core.common.Constants.PERMISSIONS_REQUEST_CAMERA -> {
                if (grantResults.size > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    (activity as BaseActivity?)?.replaceFragment(
                        QrCodeReaderFragment.newInstance(),
                        true,
                        R.id.container,
                    )
                } else {
                    Toaster.show(
                        binding.root,
                        resources
                            .getString(R.string.permission_denied_camera),
                    )
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.hideToolbar()
    }

    companion object {
        fun newInstance(): BeneficiaryAddOptionsFragment {
            val fragment = BeneficiaryAddOptionsFragment()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }
}
