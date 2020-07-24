package org.mifos.mobile.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import org.mifos.mobile.R
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.BeneficiaryState
import org.mifos.mobile.ui.enums.RequestAccessType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.CheckSelfPermissionAndRequest
import org.mifos.mobile.utils.CheckSelfPermissionAndRequest.checkSelfPermission
import org.mifos.mobile.utils.CheckSelfPermissionAndRequest.requestPermission
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Toaster

/**
 * Created by dilpreet on 5/7/17.
 */
class BeneficiaryAddOptionsFragment : BaseFragment() {
    private var rootView: View? = null
    private var external_storage_read_status = false
    private var external_storage_write_status = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_beneficiary_add_options, container, false)
        setToolbarTitle(getString(R.string.add_beneficiary))
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView!!)
        return rootView
    }

    /**
     * Opens [BeneficiaryApplicationFragment] with [BeneficiaryState] as
     * `BeneficiaryState.CREATE_MANUAL`
     */
    @OnClick(R.id.ll_add_beneficiary_manually)
    fun addManually() {
        (activity as BaseActivity?)?.replaceFragment(BeneficiaryApplicationFragment.Companion.newInstance(BeneficiaryState.CREATE_MANUAL, null), true, R.id.container)
    }

    /**
     * It first checks CAMERA runtime permission and if it returns true then it opens
     * [QrCodeReaderFragment] , if it returns false then ask for permissions.
     */
    @OnClick(R.id.ll_add_beneficiary_qrcode)
    fun addUsingQrCode() {
        if (checkSelfPermission(activity,
                        Manifest.permission.CAMERA)) {
            (activity as BaseActivity?)?.replaceFragment(QrCodeReaderFragment.Companion.newInstance(), true, R.id.container)
        } else {
            requestPermission(RequestAccessType.CAMERA)
        }
    }

    /**
     * It first checks Storage Read and Write Permission then if both of them are true then it opens
     * Intent to all gallery app
     */
    @OnClick(R.id.ll_upload_beneficiary_qrcode)
    fun addByImportingQrCode() {

        //request permission for writing external storage
        accessReadWriteAccess()
        if (external_storage_write_status && external_storage_read_status) {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"
            val pickIntent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"
            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
            startActivityForResult(chooserIntent, Constants.GALLERY_QR_PICK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.GALLERY_QR_PICK && data != null) {
            activity?.supportFragmentManager?.popBackStack()
            (activity as BaseActivity?)?.replaceFragment(QrCodeImportFragment.Companion.newInstance(data.data), true, R.id.container)
        }
    }

    private fun accessReadWriteAccess() {
        if (checkSelfPermission(activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
            external_storage_read_status = true
        } else {
            requestPermission(RequestAccessType.EXTERNAL_STORAGE_READ)
        }
        if (checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            external_storage_write_status = true
        } else {
            requestPermission(RequestAccessType.EXTERNAL_STORAGE_WRITE)
        }
    }

    /**
     * Uses [CheckSelfPermissionAndRequest] to check for runtime permissions
     */
    private fun requestPermission(requestAccessType: RequestAccessType) {
        when (requestAccessType) {
            RequestAccessType.CAMERA -> {
                requestPermission(
                        (activity as BaseActivity?)!!,
                        Manifest.permission.CAMERA,
                        Constants.PERMISSIONS_REQUEST_CAMERA,
                        resources.getString(
                                R.string.dialog_message_camera_permission_denied_prompt),
                        resources.getString(R.string.dialog_message_camera_permission_never_ask_again),
                        Constants.PERMISSIONS_CAMERA_STATUS)
            }
            RequestAccessType.EXTERNAL_STORAGE_READ -> {
                requestPermission((activity as BaseActivity?)!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Constants.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE,
                        resources.getString(R.string.dialog_message_storage_permission_denied_prompt),
                        resources
                                .getString(R.string.dialog_message_read_storage_permission_never_ask_again),
                        Constants.PERMISSIONS_STORAGE_STATUS)
            }
            RequestAccessType.EXTERNAL_STORAGE_WRITE -> {
                requestPermission((activity as BaseActivity?)!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Constants.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE,
                        resources.getString(R.string.dialog_message_storage_permission_denied_prompt),
                        resources.getString(R.string.dialog_message_write_storage_permission_never_ask_again),
                        Constants.PERMISSIONS_STORAGE_STATUS)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            Constants.PERMISSIONS_REQUEST_CAMERA -> {
                if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    (activity as BaseActivity?)?.replaceFragment(QrCodeReaderFragment.Companion.newInstance(), true, R.id.container)
                } else {
                    Toaster.show(rootView, resources
                            .getString(R.string.permission_denied_camera))
                }
            }
            Constants.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    external_storage_read_status = true
                } else {
                    Toaster.show(rootView, resources
                            .getString(R.string.permission_denied_storage))
                }
            }
            Constants.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    external_storage_write_status = true
                } else {
                    Toaster.show(rootView, resources
                            .getString(R.string.permission_denied_storage))
                }
            }
        }
    }

    companion object {
        @kotlin.jvm.JvmStatic
        fun newInstance(): BeneficiaryAddOptionsFragment {
            val fragment = BeneficiaryAddOptionsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}