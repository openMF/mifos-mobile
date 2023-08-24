package org.mifos.mobile.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.ui.fragments.base.BaseFragment

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/
@AndroidEntryPoint
class AboutUsFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AboutUsScreen()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): AboutUsFragment {
            val fragment = AboutUsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
