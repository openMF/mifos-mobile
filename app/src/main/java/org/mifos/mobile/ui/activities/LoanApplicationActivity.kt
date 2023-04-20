package org.mifos.mobile.ui.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_loan_application.*

import org.mifos.mobile.R
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.ui.fragments.LoanApplicationFragment

class LoanApplicationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_application)
        if (savedInstanceState == null) {
            replaceFragment(LoanApplicationFragment.newInstance(LoanState.CREATE), false,
                    R.id.container)
        }
        showBackButton()

        dismissSoftKeyboardOnBkgTap(cl_background)
    }

    private fun dismissSoftKeyboardOnBkgTap(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { view, event ->
                hideKeyboard(this@LoanApplicationActivity)
                false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                dismissSoftKeyboardOnBkgTap(innerView)
            }
        }
    }
}