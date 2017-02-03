package com.example.android.xyztouristattractions.ui;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.xyztouristattractions.R;
import com.example.android.xyztouristattractions.provider.AttractionContract;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Dawid on 2017-01-03.
 */
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {


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

        Float[] latitudes = getMapAllCoordinates(true);
        Float[] longitudes = getMapAllCoordinates(false);
        ArrayList<LatLng> attractionsLatLngs = new ArrayList<LatLng>();

        for (int i = 0; i < latitudes.length; i++) {
            attractionsLatLngs.add(new LatLng(latitudes[i], longitudes[i]));
            googleMap.addMarker(new MarkerOptions().position(attractionsLatLngs.get(i))
                    .title("Atrakcja"));
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(attractionsLatLngs.get(1), 12.0f));
    }

    //Get coordinates (longitude or latitude) for Google map
    private Float[] getMapAllCoordinates(boolean isLatitude) {
        Cursor cursor = getContentResolver().query(AttractionContract.AttractionEntry.CONTENT_URI, null, null, null, null);

        cursor.moveToFirst();
        ArrayList<Float> coords = new ArrayList<Float>();
        if (isLatitude) {
            while (!cursor.isAfterLast()) {
                coords.add(cursor.getFloat(cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_LATITUDE)));
                cursor.moveToNext();
            }

        } else {
            while (!cursor.isAfterLast()) {
                coords.add(cursor.getFloat(cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_LONGITUDE)));
                cursor.moveToNext();
            }

        }
        cursor.close();
        return coords.toArray(new Float[coords.size()]);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

