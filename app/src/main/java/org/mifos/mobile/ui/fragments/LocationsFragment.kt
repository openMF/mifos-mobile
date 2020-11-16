package org.mifos.mobile.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.mifos.mobile.R
import org.mifos.mobile.ui.fragments.base.BaseFragment

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/ /**
 * Created by dilpreet on 25/2/17.
 */
class LocationsFragment : BaseFragment(), OnMapReadyCallback {
    @kotlin.jvm.JvmField
    @BindView(R.id.map)
    var mapView: MapView? = null
    private var rootView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_locations, container, false)
        ButterKnife.bind(this, rootView!!)
        mapView?.onCreate(savedInstanceState)
        mapView?.onResume()
        try {
            MapsInitializer.initialize(activity?.applicationContext)
        } catch (e: Exception) {
            Log.d(LocationsFragment::class.java.simpleName, e.toString())
        }
        mapView?.getMapAsync(this)
        return rootView
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val headquarterLatLng = LatLng(47.61115, -122.34481)
        addMarker(googleMap, headquarterLatLng)
        addAnimationToHeadquarter(googleMap, headquarterLatLng)
    }

    private fun addMarker(googleMap: GoogleMap, headquarterLatLng: LatLng) {
        googleMap.addMarker(MarkerOptions().position(headquarterLatLng)
                .title(getString(R.string.mifos_initiative)))
    }

    private fun addAnimationToHeadquarter(googleMap: GoogleMap, headquarterLatLng: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(headquarterLatLng, 16.0f))
    }

    companion object {
        fun newInstance(): LocationsFragment {
            return LocationsFragment()
        }
    }
}