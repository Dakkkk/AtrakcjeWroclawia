package com.example.android.xyztouristattractions.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.xyztouristattractions.provider.AttractionContract.AttractionEntry;


/**
 * Created by Dawid on 2017-01-28.
 */

public class AttractionProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = AttractionProvider.class.getSimpleName();

    //Database helper object
    private AttractionDbHelper mDbHelper;

    /**
     * URI matcher code for the content URI for the ATTRACTIONS table
     */
    private static final int ATTRACTIONS = 100;

    /**
     * URI matcher code for the content URI for a single attraction in the attractions table
     */
    private static final int ATTRACTION_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // TODO: Add 2 content URIs to URI matcher
        sUriMatcher.addURI(AttractionContract.CONTENT_AUTHORITY, AttractionContract.PATH_ATTRACTIONS, ATTRACTIONS);

        sUriMatcher.addURI(AttractionContract.CONTENT_AUTHORITY, AttractionContract.PATH_ATTRACTIONS + "/#", ATTRACTION_ID);

    }

    @Override
    public boolean onCreate() {
        mDbHelper = new AttractionDbHelper(getContext());

        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ATTRACTIONS:
                // For the attractions code, query the attractions table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the attractions table.
                // TODO: Perform database query on attractions table
                cursor = database.query(AttractionEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ATTRACTION_ID:
                // For the attractionID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.attractions/attractions/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = AttractionEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the attractions table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(AttractionEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        //Notify the cursor when the data in the database changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ATTRACTIONS:
                return insertAttraction(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ATTRACTIONS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(AttractionEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case ATTRACTION_ID:
                // Delete a single row given by the ID in the URI
                selection = AttractionEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // For case ATTRACTION_ID:
                // Delete a single row given by the ID in the URI
                rowsDeleted = database.delete(AttractionEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);

        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ATTRACTIONS:
                return updateAttraction(uri, values, selection, selectionArgs);
            case ATTRACTION_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = AttractionEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateAttraction(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateAttraction(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(AttractionEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            // getContext().getContentResolver().notifyChange(uri, null);
            Log.v("AttractionProvider", String.valueOf(rowsUpdated));
        }

        System.out.println("AttrProvidr, rows updated: " + rowsUpdated);

        // Return the number of rows updated
        return rowsUpdated;
    }

    private Uri insertAttraction(Uri uri, ContentValues values) {

        // Check that the name is not null
        String name = values.getAsString(AttractionEntry.COLUMN_NAME_NAME);
        if (name == null || name == "" || name.isEmpty()) {
            throw new IllegalArgumentException("Attraction requires a name");
        }

        // Check that the desc is not null
        String breed = values.getAsString(AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION);
        if (breed == null) {
            throw new IllegalArgumentException("Attr requires a desc");
        }


        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new attraction with the given values
        long id = database.insert(AttractionEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //Notify all listeners that the data has changed
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

}
