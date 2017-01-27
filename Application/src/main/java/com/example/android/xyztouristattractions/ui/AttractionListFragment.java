/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.xyztouristattractions.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.xyztouristattractions.R;
import com.example.android.xyztouristattractions.common.Attraction;
import com.example.android.xyztouristattractions.common.Constants;
import com.example.android.xyztouristattractions.common.Utils;
import com.example.android.xyztouristattractions.provider.TouristAttractions;
import com.example.android.xyztouristattractions.service.UtilityService;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.example.android.xyztouristattractions.provider.TouristAttractions.ATTRACTIONS;

/**
 * The main tourist attraction fragment which contains a list of attractions
 * sorted by distance (contained inside
 * {@link com.example.android.xyztouristattractions.ui.AttractionListActivity}).
 */
public class AttractionListFragment extends Fragment {

    private AttractionAdapter mAdapter;
    private AttractionListActivity listActivity;
    private LatLng mLatestLocation;
    private int mImageSize;
    private boolean mItemClicked;
    public static boolean sort = false;
    public static boolean sortByName = false;
    public static boolean sortNameDesc = false;
    public static boolean sortNameAsc = false;
    public static boolean sortDistanceAsc = false;
    private RadioGroup radioSortByName;
    private RadioButton radioNameAsc;
    private RadioButton radioNameDesc;
    private Button btnDisplay;
    private Button radioNameBtn;
    private Attraction mAttraction;

    private GoogleMap googleMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();




    public AttractionListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Load a larger size image to make the activity transition to the detail screen smooth
        mImageSize = getResources().getDimensionPixelSize(R.dimen.image_size)
                * Constants.IMAGE_ANIM_MULTIPLIER;
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //ToggleButton toggle = (ToggleButton) view.findViewById(R.id.sort);

        List<Attraction> attractions = loadAttractionsFromLocation(mLatestLocation, sort);

        mAdapter = new AttractionAdapter(getActivity(), attractions);


//        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                sortByName = false;
//                sort = true;
//                if (isChecked) {
//                    sortDistanceAsc = false;
//                    Toast.makeText(getActivity(), "Sortowanie wg odległości malejąco",
//                            Toast.LENGTH_SHORT).show();
//                } else {
//                    sortDistanceAsc = true;
//                    Toast.makeText(getActivity(), "Sortowanie wg odległości rosnąco",
//                            Toast.LENGTH_SHORT).show();
//                }
//                mAdapter.notifyDataSetChanged();
//                //listActivity.notifyAll();
//
//            }
//
//            // Refresh the view to show results after toggling button????
//        });

        //Defining Functions to show toast after choosing radio button and clicking POKAŻ
//        radioSortByName = (RadioGroup) view.findViewById(R.id.radioSortName);
//        radioNameAsc = (RadioButton) view.findViewById(R.id.radioNameAsc);

//        btnDisplay = (Button) view.findViewById(R.id.btnDisplay);

        //String attractionName = getArguments().getString(EXTRA_ATTRACTION);
        final Button mapAll = (Button) view.findViewById(R.id.btnDisplayMap);

        final List<Geofence> geofences = TouristAttractions.getGeofenceList();

        //tringgers sorting by name after choosing option descending/ascending
//        btnDisplay.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View v) {
//
//                sortByName = true;
//                sort = false;
//
//                // get selected radio button from radioGroup
//                int selectedId = radioSortByName.getCheckedRadioButtonId();
//
//                // find the radiobutton by returned id
//                radioNameBtn = (RadioButton) getActivity().findViewById(selectedId);
//
//                if (radioNameBtn == radioNameAsc) {
//                    sortNameAsc = true;
//                    sortNameDesc = false;
//                }
//                else {
//                    sortNameDesc = true;
//                    sortNameAsc = false;
//                }
//                mAdapter.notifyDataSetChanged();
//
//
//                Toast.makeText(getActivity(), radioNameBtn.getText(), Toast.LENGTH_SHORT).show();
//                //mAdapter.mAttractionList.notifyAll();
//            }
//
//        });

        //show map with all attractions
        mapAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Wszystkie atrakcje na mapie", Toast.LENGTH_LONG).show();
                Intent i = new Intent(v.getContext(),MapsMarkerActivity.class);
                startActivity(i);
            }
        });




        mLatestLocation = Utils.getLocation(getActivity());


        AttractionsRecyclerView recyclerView =
                (AttractionsRecyclerView) view.findViewById(android.R.id.list);
        recyclerView.setEmptyView(view.findViewById(android.R.id.empty));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    private Attraction getAttraction() {
        for (Map.Entry<String, List<Attraction>> attractionsList : ATTRACTIONS.entrySet()) {
            List<Attraction> attractions = attractionsList.getValue();
            for (Attraction attraction : attractions) {
                    return attraction;
            }
        }
        return null;
    }

//    //open the map with all attractions
//    public void showAllAttractions(View view)
//    {
//        Intent intent = new Intent( .ui.MapsMarkerActivity.class);
//        startActivity(intent);
//    }



    //Button that triggers sorting by name after choosing radio opotion (acsending/descending)
//    public void addListenerOnButton() {
//        final Activity activity = this.getActivity();
//        radioSortByName = (RadioGroup) activity.findViewById(R.id.radioSortName);
//        btnDisplay = (Button) activity.findViewById(R.id.btnDisplay);
//
//
//        btnDisplay.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                // get selected radio button from radioGroup
//                int selectedId = radioSortByName.getCheckedRadioButtonId();
//
//                // find the radiobutton by returned id
//                radioNameBtn = (RadioButton) activity.findViewById(selectedId);
//
//                Toast.makeText(activity, radioNameBtn.getText(), Toast.LENGTH_SHORT).show();
//
//            }
//
//        });
//
//    }



//
//    public void checkToggleBtn (ToggleButton toggle){
//        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    sort = true;
//                    Toast.makeText(getActivity(), "Sortowanie rosnące",
//                            Toast.LENGTH_SHORT).show();
//                } else {
//                    sort = false;
//                    Toast.makeText(getActivity(),"Sortowanie malejące",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }




    @Override
    public void onResume() {
        super.onResume();
        mItemClicked = false;
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mBroadcastReceiver, UtilityService.getLocationUpdatedIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location =
                    intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);
            if (location != null) {
                mLatestLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mAdapter.mAttractionList = loadAttractionsFromLocation(mLatestLocation, sort);
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    private static List<Attraction> loadAttractionsFromLocation(final LatLng curLatLng, boolean sort) {
        //String closestCity = TouristAttractions.getClosestCity(curLatLng);
        String farestCity = TouristAttractions.getFarestCity(curLatLng);
        //if (closestCity != null) {
        if (farestCity != null) {
            //List<Attraction> attractions = ATTRACTIONS.get(closestCity);
            List<Attraction> attractions = ATTRACTIONS.get(farestCity);

            //sorting attractions by name
            if (sortByName == true) {
                if (sortNameAsc == true) {
                    Collections.sort(attractions, new Comparator<Attraction>() {
                        @Override
                        public int compare(Attraction name, Attraction name2) {
                            return name.getName().compareToIgnoreCase(name2.getName());
                        }
                    });
                }
                else if (sortNameDesc == true) {
                    Collections.sort(attractions, new Comparator<Attraction>() {
                        @Override
                        public int compare(Attraction name, Attraction name2) {
                            return name2.getName().compareToIgnoreCase(name.getName());
                        }
                    });
                }
            }


            if (sort == true && curLatLng != null  ) {
                  if (sortDistanceAsc == false) {
                    Collections.sort(attractions, Collections.reverseOrder(
                                    new Comparator<Attraction>() {
                                        @Override
                                        public int compare(Attraction lhs, Attraction rhs) {
                                            double lhsDistance = SphericalUtil.computeDistanceBetween(
                                                    lhs.location, curLatLng);
                                            double rhsDistance = SphericalUtil.computeDistanceBetween(
                                                    rhs.location, curLatLng);
                                            return (int) (lhsDistance - rhsDistance);
                                        }
                                    }
                            )
                    );
                }
                else {
                    Collections.sort(attractions,
                            new Comparator<Attraction>() {
                                @Override
                                public int compare(Attraction lhs, Attraction rhs) {
                                    double lhsDistance = SphericalUtil.computeDistanceBetween(
                                            lhs.location, curLatLng);
                                    double rhsDistance = SphericalUtil.computeDistanceBetween(
                                            rhs.location, curLatLng);
                                    return (int) (lhsDistance - rhsDistance);
                                }
                            }
                    );

                }
            }

            return attractions;
        }
        return null;
    }



    private class AttractionAdapter extends RecyclerView.Adapter<ViewHolder>
            implements ItemClickListener {

        public List<Attraction> mAttractionList;
        private Context mContext;

        public AttractionAdapter(Context context, List<Attraction> attractions) {
            super();
            mContext = context;
            mAttractionList = attractions;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.list_row, parent, false);
            return new ViewHolder(view, this);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Attraction attraction = mAttractionList.get(position);

            holder.mTitleTextView.setText(attraction.name);
            holder.mDescriptionTextView.setText(attraction.description);
            Glide.with(mContext)
                    .load(attraction.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.empty_photo)
                    .override(mImageSize, mImageSize)
                    .into(holder.mImageView);

            String distance =
                    Utils.formatDistanceBetween(mLatestLocation, attraction.location);
            if (TextUtils.isEmpty(distance)) {
                holder.mOverlayTextView.setVisibility(View.GONE);
            } else {
                holder.mOverlayTextView.setVisibility(View.VISIBLE);
                holder.mOverlayTextView.setText(distance);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mAttractionList == null ? 0 : mAttractionList.size();
        }

        @Override
        public void onItemClick(View view, int position) {
            if (!mItemClicked) {
                mItemClicked = true;
                View heroView = view.findViewById(android.R.id.icon);
                DetailActivity.launch(
                        getActivity(), mAdapter.mAttractionList.get(position).name, heroView);
            }
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView mTitleTextView;
        TextView mDescriptionTextView;
        TextView mOverlayTextView;
        ImageView mImageView;
        ItemClickListener mItemClickListener;

        public ViewHolder(View view, ItemClickListener itemClickListener) {
            super(view);
            mTitleTextView = (TextView) view.findViewById(android.R.id.text1);
            mDescriptionTextView = (TextView) view.findViewById(android.R.id.text2);
            mOverlayTextView = (TextView) view.findViewById(R.id.overlaytext);
            mImageView = (ImageView) view.findViewById(android.R.id.icon);
            mItemClickListener = itemClickListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
