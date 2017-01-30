package com.example.android.xyztouristattractions.ui;

import android.app.LoaderManager;
import android.app.SearchManager;
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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.android.xyztouristattractions.R;
import com.example.android.xyztouristattractions.provider.AttractionContract;
import com.example.android.xyztouristattractions.provider.AttractionDbHelper;

/**
 * Created by Dawid on 2017-01-28.
 */

public class AttractionListView extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener {
    private static final int ATTLACTION_LOADER = 0;
    /** Database helper that will provide us access to the database */
    private AttractionDbHelper mDbHelper;

    private AttractionsCursorAdapter mAttractionCursorAdapter;

    private Menu menu;

    private SearchManager searchManager;
    private android.widget.SearchView searchView;
    private MenuItem searchItem;
    private ListView attractionListView;


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
        attractionListView = (ListView) findViewById(R.id.attractions_list);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        //View emptyView = findViewById(R.id.empty_view);
        //attractionListView.setEmptyView(emptyView);

        mAttractionCursorAdapter = new AttractionsCursorAdapter(this, null);

        mAttractionCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return searchForAttraction(constraint.toString());

            }
        });



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



        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }


        dropDb();
        insertAttraction();

        //Kick off the loader
        getLoaderManager().initLoader(ATTLACTION_LOADER, null, this);
    }

    public void doMySearch(String string){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sort_by_distance_asc:

                return true;
            case R.id.sort_by_distance_desc:

                return true;
            case R.id.sort_by_name_asc:
                mAttractionCursorAdapter.changeCursor(orderByNameASC());
                attractionListView.invalidateViews();
                return true;
            case R.id.sort_by_name_desc:
                mAttractionCursorAdapter.changeCursor(orderByNameDESC());
                attractionListView.invalidateViews();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Inflate the menu; this adds items to the action bar if it is present.


        getMenuInflater().inflate(R.menu.main, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setSearchableInfo
//                (searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        //searchView.setOnCloseListener(this);
        searchView.requestFocus();
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
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



        Uri newUri = getContentResolver().insert(AttractionContract.AttractionEntry.CONTENT_URI, values);
        Uri newUri1 = getContentResolver().insert(AttractionContract.AttractionEntry.CONTENT_URI, values1);
        Uri newUri2 = getContentResolver().insert(AttractionContract.AttractionEntry.CONTENT_URI, values2);
    }

    private void dropDb() {
        int rowsDeleted = getContentResolver().delete(AttractionContract.AttractionEntry.CONTENT_URI, null, null);
        Log.v("AttractionListView", rowsDeleted + " rows deleted from attractions database");
    }


    //Search attraction by name or description
    private Cursor searchForAttraction (String userQuery) {
        String selection;
        String[] whereArgs;
        String[] tableColumns = new String[] {
                AttractionContract.AttractionEntry.COLUMN_NAME_NAME,
                AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION,
        };

        if (userQuery == null || userQuery.length () == 0) {
            selection = null;
            whereArgs = null;
        } else {
            selection = AttractionContract.AttractionEntry.COLUMN_NAME_NAME + " LIKE ? " + " OR " + AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION + " LIKE ? ";
            whereArgs = new String[] {
                    "%"+userQuery+"%", "%"+userQuery+"%"
            };
        }

        Log.v("AttractionListView", getContentResolver().query(AttractionContract.AttractionEntry.CONTENT_URI, null, selection, whereArgs, null).toString());

        Cursor cursor = getContentResolver().query(AttractionContract.AttractionEntry.CONTENT_URI, null, selection, whereArgs, null);

        return cursor;
    }

    private Cursor orderByNameASC() {
        Cursor cursor = getContentResolver().query(AttractionContract.AttractionEntry.CONTENT_URI, null, null, null, AttractionContract.AttractionEntry.COLUMN_NAME_NAME + " ASC");
        return cursor;
    }

    private Cursor orderByNameDESC() {
        Cursor cursor = getContentResolver().query(AttractionContract.AttractionEntry.CONTENT_URI, null, null, null, AttractionContract.AttractionEntry.COLUMN_NAME_NAME + " DESC");
        return cursor;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAttractionCursorAdapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAttractionCursorAdapter.getFilter().filter(newText);
        return true;
    }


    //Przerobić
//    public Cursor fetchClientsByNameOrAdress(String inputText) throws SQLException {
//         SQLiteDatabase mDb = new SQLiteDatabase(AttractionListView.this));
//
//
//        Cursor mCursor = null;
//
//        if (inputText == null || inputText.length () == 0) {
//            mCursor = mDb.query(AttractionContract.AttractionEntry.TABLE_NAME, new String[] {AttractionContract.AttractionEntry.COLUMN_NAME_NAME,
//                    AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION}, null, null, null, null, null, null);
//
//        } else {
//            mCursor = mDb.query(Klienci.TABLE_NAME, new String[] {Klienci._ID, Klienci.COLUMN_NAME_NAZWA,
//                            Klienci.COLUMN_NAME_ADRES, Klienci.COLUMN_NAME_TELEFON},
//                    "(" + Klienci.COLUMN_NAME_NAZWA + " like '%" + inputText + "%') OR (" + Klienci.COLUMN_NAME_ADRES + " like '%" + inputText + "%')" , null, null, null, orderBy, null);
//        }
//
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
//
//        return mCursor;
//    }


}
