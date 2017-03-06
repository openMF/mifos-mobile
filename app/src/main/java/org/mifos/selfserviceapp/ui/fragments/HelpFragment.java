package org.mifos.selfserviceapp.ui.fragments;

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 25/2/17.
 */

public class HelpFragment extends BaseFragment implements OnMapReadyCallback {


    @BindView(R.id.map)
    MapView mapView;

    private View rootView;

    public static HelpFragment getInstance() {
        HelpFragment helpFragment = new HelpFragment();
        return helpFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_help, container, false);

        ButterKnife.bind(this, rootView);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Log.d(HelpFragment.class.getSimpleName(), e.toString());
        }
        mapView.getMapAsync(this);

        return rootView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Longitude latitude of Mifos
        LatLng mifos = new LatLng(47.61115, -122.34481);
        Marker locationMarker = googleMap.addMarker(new MarkerOptions().position(mifos)
                .title(getActivity().getResources().getString(R.string.map_marker_heading))
                .snippet(getActivity().getResources().getString(R.string.map_marker_desc)));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(mifos).zoom(15).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        locationMarker.showInfoWindow();

    }
}
