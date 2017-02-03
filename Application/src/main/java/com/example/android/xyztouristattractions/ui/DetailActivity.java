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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.xyztouristattractions.R;
import com.example.android.xyztouristattractions.common.Constants;
import com.example.android.xyztouristattractions.provider.AttractionContract;
import com.squareup.picasso.Picasso;

/**
 * The tourist attraction detail activity screen which contains the details of
 * a single attraction.
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the attraction data loader
     */
    private static final int EXISTING_ATTRACTION_LOADER = 0;

    /**
     * Content URI for the existing attraction (null if it's a new attraction)
     */
    private Uri mCurrentAttractionUri;

    /**
     * EditText field to enter the attraction's name
     */
    private TextView mNameText;

    /**
     * EditText field to enter the attraction's desc
     */
    private TextView mDescritionText;

    private ImageView imgDetailViewSource;

    private static final String EXTRA_ATTRACTION = "attraction";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void launch(Activity activity, String attraction, View heroView) {
        Intent intent = getLaunchIntent(activity, attraction);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, heroView, heroView.getTransitionName());
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    public static Intent getLaunchIntent(Context context, String attraction) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_ATTRACTION, attraction);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);


        //Nowy kod - przetestować
        Intent intent = getIntent();
        mCurrentAttractionUri = intent.getData();

        getLoaderManager().initLoader(EXISTING_ATTRACTION_LOADER, null, this);

        // Find all relevant views that we will need to read input from
        mNameText = (TextView) findViewById(R.id.nameTextView);
        mDescritionText = (TextView) findViewById(R.id.descriptionTextView);
        imgDetailViewSource = (ImageView) findViewById(R.id.imageView);


        String attraction = getIntent().getStringExtra(EXTRA_ATTRACTION);

        //This launches DetailFragment
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, DetailFragment.createInstance(attraction))
//                    .commit();
//        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all attraction attributes, define a projection that contains
        // all columns from the attractions table
        String[] projection = {
                AttractionContract.AttractionEntry._ID,
                AttractionContract.AttractionEntry.COLUMN_NAME_NAME,
                AttractionContract.AttractionEntry.COLUMN_NAME_DESCRIPTION,
                AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_DETAIL,
                AttractionContract.AttractionEntry.COLUMN_NAME_LOCATION
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentAttractionUri,         // Query the content URI for the current attraction
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of attraction attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_DESCRIPTION);
            int detailImgColumnIndex = cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_DETAIL);
            int locationColumnIndex = cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_LOCATION);


            // Extract out the value from the Cursor for the given column index
            final String name = cursor.getString(nameColumnIndex);
            String description = cursor.getString(descriptionColumnIndex);
            final String city = cursor.getString(locationColumnIndex);

            //   int x = cursor.getInt(x);

            String attractionDetailImgUrl = cursor.getString(detailImgColumnIndex);


            System.out.println("Attraction name: " + name);

            // Update the views on the screen with the values from the database
            mNameText.setText(name);
            mDescritionText.setText(description);
            // mWeightEditText.setText(Integer.toString(weight));

            Picasso.with(getApplicationContext()).load(attractionDetailImgUrl).placeholder(R.drawable.empty_photo).into(imgDetailViewSource);

            FloatingActionButton mapFab = (FloatingActionButton) findViewById(R.id.mapFab);


            mapFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(Constants.MAPS_INTENT_URI +
                            Uri.encode(name + ", " + city)));
                    startActivity(intent);
                }
            });


        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
