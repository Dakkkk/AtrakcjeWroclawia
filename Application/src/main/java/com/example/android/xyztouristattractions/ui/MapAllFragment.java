package com.example.android.xyztouristattractions.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.xyztouristattractions.R;
import com.example.android.xyztouristattractions.common.Attraction;
import com.example.android.xyztouristattractions.common.Constants;
import com.example.android.xyztouristattractions.common.Utils;
import com.google.android.gms.maps.model.LatLng;

import static com.example.android.xyztouristattractions.provider.TouristAttractions.ATTRACTIONS;


/**
 * Created by Dawid on 2016-12-28.
 */
public class MapAllFragment extends Fragment {

    private static final String EXTRA_ATTRACTION = "attraction";
    private Attraction mAttraction;
    public MapAllFragment() {
    }
    public static MapAllFragment createInstance(String attractionName) {
        MapAllFragment mapAllFragment = new MapAllFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ATTRACTION, attractionName);
        mapAllFragment.setArguments(bundle);
        return mapAllFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        String attractionName = getArguments().getString(EXTRA_ATTRACTION);

       Button mapAll = (Button) view.findViewById(R.id.btnDisplayMap);

        mapAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Wszystkie atrakcje", Toast.LENGTH_SHORT);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constants.MAPS_INTENT_URI +
                        Uri.encode(mAttraction.name + ", " + mAttraction.city)));
                startActivity(intent);
            }
        });

        return view;
    }



}
