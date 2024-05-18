package org.mifos.mobile.ui.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.core.ui.theme.MifosMobileTheme

class LocationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mifosComposeView(requireContext()) {
            MifosMobileTheme {
                LocationsScreen()
            }
        }
    }

    companion object {
        fun newInstance(): LocationsFragment {
            return LocationsFragment()
        }
    }
}
