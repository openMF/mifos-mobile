package org.mifos.mobile.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment

/**
 * Created by dilpreet on 13/9/17.
 */
@AndroidEntryPoint
class NotificationFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        (activity as? BaseActivity)?.hideToolbar()
        return mifosComposeView(requireContext()) {
            NotificationScreen(
                navigateBack = { activity?.onBackPressed() }
            )
        }
    }

    companion object {
        fun newInstance(): NotificationFragment {
            return NotificationFragment()
        }
    }
}
