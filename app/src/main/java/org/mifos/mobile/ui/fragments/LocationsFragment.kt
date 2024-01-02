package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentLocationsBinding
import org.mifos.mobile.ui.fragments.base.BaseFragment

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/ /**
 * Created by dilpreet on 25/2/17.
 */
@AndroidEntryPoint
class LocationsFragment : BaseFragment(), OnMapReadyCallback {

    private var _binding: FragmentLocationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()
        try {
            MapsInitializer.initialize(activity?.applicationContext!!)
        } catch (e: Exception) {
            Log.d(LocationsFragment::class.java.simpleName, e.toString())
        }
        binding.map.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val headquarterLatLng = LatLng(47.61115, -122.34481)
        addMarker(googleMap, headquarterLatLng)
        addAnimationToHeadquarter(googleMap, headquarterLatLng)
    }

    private fun addMarker(googleMap: GoogleMap, headquarterLatLng: LatLng) {
        googleMap.addMarker(
            MarkerOptions().position(headquarterLatLng)
                .title(getString(R.string.mifos_initiative)),
        )
    }

    private fun addAnimationToHeadquarter(googleMap: GoogleMap, headquarterLatLng: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(headquarterLatLng, 16.0f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): LocationsFragment {
            return LocationsFragment()
        }
    }
}
