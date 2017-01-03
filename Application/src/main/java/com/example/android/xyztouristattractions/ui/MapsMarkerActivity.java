package com.example.android.xyztouristattractions.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.xyztouristattractions.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Dawid on 2017-01-03.
 */
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */

    //TODO get geolacation parameters from TouristAttractions instead of hardcoding it
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng rynek = new LatLng(51.1078852, 17.03853760000004);
        LatLng muzeum = new LatLng(51.1109061,17.0476092);
        LatLng hala_targowa = new LatLng(51.1126439,17.0397772);
        LatLng zoo = new LatLng(51.1041429,17.0742114);


        googleMap.addMarker(new MarkerOptions().position(rynek)
                .title("Rynek Wrocławski"));
        googleMap.addMarker(new MarkerOptions().position(muzeum)
                .title("Muzeum Narodowe"));
        googleMap.addMarker(new MarkerOptions().position(hala_targowa)
                .title("Hala Targowa"));
        googleMap.addMarker(new MarkerOptions().position(zoo)
                .title("Zoo"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hala_targowa, 12.0f));
    }
}

