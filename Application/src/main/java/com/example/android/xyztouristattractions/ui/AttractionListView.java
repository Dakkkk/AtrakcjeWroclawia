package com.example.android.xyztouristattractions.ui;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.xyztouristattractions.R;
import com.example.android.xyztouristattractions.provider.AttractionContract;
import com.example.android.xyztouristattractions.provider.AttractionDbHelper;

/**
 * Created by Dawid on 2017-01-28.
 */

public class AttractionListView extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ATTLACTION_LOADER = 0;
    /** Database helper that will provide us access to the database */
    private AttractionDbHelper mDbHelper;

    private AttractionsCursorAdapter mAttractionCursorAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttractionListView.this, MapsMarkerActivity.class);
                startActivity(intent);
            }
        });

        mAttractionCursorAdapter = new AttractionsCursorAdapter(this, null);

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new AttractionDbHelper(this);

        // Find the ListView which will be populated with the attraction data
        ListView attractionListView = (ListView) findViewById(R.id.attractions_list);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        //View emptyView = findViewById(R.id.empty_view);
        //attractionListView.setEmptyView(emptyView);

        mAttractionCursorAdapter = new AttractionsCursorAdapter(this, null);
        attractionListView.setAdapter(mAttractionCursorAdapter);

        //Set on attraction click listener
        attractionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AttractionListView.this, DetailActivity.class);

                System.out.println("List attraction clicked");

                //Get the uri of a current attraction, that was clicked on. F.e. when we click on attraction with id=2, the uri="content://example.android.attractions/attractions/2
                //It appends the id of that specific item that was clicked on to the URI
                Uri currentAttractionUri = ContentUris.withAppendedId(AttractionContract.AttractionEntry.CONTENT_URI, id);

                intent.setData(currentAttractionUri);

                startActivity(intent);
            }
        });

       // final Button mapAll = (Button) findViewById(R.id.btnDisplayMap);


        //show map with all attractions
//        mapAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Wszystkie atrakcje na mapie", Toast.LENGTH_LONG).show();
//                Intent i = new Intent(v.getContext(),MapsMarkerActivity.class);
//                startActivity(i);
//            }
//        });


        dropDb();
        insertAttraction();

        //Kick off the loader
        getLoaderManager().initLoader(ATTLACTION_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                AttractionContract.AttractionEntry._ID,
                AttractionContract.AttractionEntry.COLUMN_NAME_NAME,
                AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION };

        return new CursorLoader(this,
                AttractionContract.AttractionEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAttractionCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAttractionCursorAdapter.swapCursor(null);

    }

    private void insertAttraction() {
        // Create a ContentValues object where column names are the keys,
        // and Toto's attraction attributes are the values.
        ContentValues values = new ContentValues();
        values.put(AttractionContract.AttractionEntry.COLUMN_NAME_NAME, "Rynek Wrocławski");
        values.put(AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION, "Fajna atrakcja");
        values.put(AttractionContract.AttractionEntry.COLUMN_NAME_DESCRIPTION, "Rynek to bardzo fajna atrakcja");

        ContentValues values1 = new ContentValues();
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_NAME, "Muzeum Narodowe");
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION, "Ciekawe");
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_DESCRIPTION, "Bardzo ciekawe");

        ContentValues values2 = new ContentValues();
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_NAME, "Zoo");
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION, "Zoo");
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_DESCRIPTION, "Wielkie Zoo");


        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link AttractionEntry#CONTENT_URI} to indicate that we want to insert
        // into the attractions database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(AttractionContract.AttractionEntry.CONTENT_URI, values);
        Uri newUri1 = getContentResolver().insert(AttractionContract.AttractionEntry.CONTENT_URI, values1);
        Uri newUri2 = getContentResolver().insert(AttractionContract.AttractionEntry.CONTENT_URI, values2);
    }

    private void dropDb() {
        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        int rowsDeleted = getContentResolver().delete(AttractionContract.AttractionEntry.CONTENT_URI, null, null);
        Log.v("AttractionListView", rowsDeleted + " rows deleted from attractions database");
        //Toast.makeText(this, "Just deleted " + rowsDeleted + "rows", Toast.LENGTH_SHORT );
    }

}
