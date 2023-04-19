package org.mifos.mobile.ui.activities

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_registration.*

import org.mifos.mobile.R
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.RegistrationFragment
import org.mifos.mobile.utils.MaterialDialog

class RegistrationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        replaceFragment(RegistrationFragment.newInstance(), false, R.id.container)

        dismissSoftKeyboardOnBackgroundTap(nsv_background)
    }

    private fun dismissSoftKeyboardOnBackgroundTap(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { view, event ->
                hideKeyboard(this@RegistrationActivity)
                false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                dismissSoftKeyboardOnBackgroundTap(innerView)
            }
        }
    }

    override fun onBackPressed() {
        MaterialDialog.Builder().init(this)
                .setTitle(getString(R.string.dialog_cancel_registration_title))
                .setMessage(getString(R.string.dialog_cancel_registration_message))
                .setPositiveButton(getString(R.string.cancel),
                        DialogInterface.OnClickListener { _, _ -> super.onBackPressed() })
                .setNegativeButton(R.string.continue_str,
                        DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                .createMaterialDialog()
                .show()
    }
}