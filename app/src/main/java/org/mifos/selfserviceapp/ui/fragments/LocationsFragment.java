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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 25/2/17.
 */

public class LocationsFragment extends BaseFragment implements OnMapReadyCallback {


    @BindView(R.id.map)
    MapView mapView;

    private View rootView;

    public static LocationsFragment newInstance() {
        LocationsFragment locationsFragment = new LocationsFragment();
        return locationsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_locations, container, false);

        ButterKnife.bind(this, rootView);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Log.d(LocationsFragment.class.getSimpleName(), e.toString());
        }
        mapView.getMapAsync(this);

        return rootView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        addMarker(googleMap);

    }

    private void addMarker(GoogleMap googleMap) {
        for (LatLng latLng: getLatLngList()) {
            googleMap.addMarker(new MarkerOptions().position(latLng));
        }
    }

    private List<LatLng> getLatLngList() {
        ArrayList<LatLng> latLngs = new ArrayList<>();
        latLngs.add(new LatLng(47.61115, -122.34481));
        latLngs.add(new LatLng(28.6466758, 76.8123806));
        latLngs.add(new LatLng(52.1557846, 4.1118158));
        latLngs.add(new LatLng(37.4024713, -122.3219762));
        return latLngs;
    }
}

