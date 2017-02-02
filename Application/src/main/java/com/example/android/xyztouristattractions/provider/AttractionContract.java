package com.example.android.xyztouristattractions.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the attractions app.
 */
public final class AttractionContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private AttractionContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.xyztouristattractions";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.attractions/attraction/ is a valid path for
     * looking at attraction data. content://com.example.android.attractions/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_ATTRACTIONS = "attractions";

    /**
     * Inner class that defines constant values for the peattractionts database table.
     * Each entry in the table represents a single attraction.
     */
    public static final class AttractionEntry implements BaseColumns {
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of attractions.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTRACTIONS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single attraction.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTRACTIONS;



        /** The content URI to access the attraction data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ATTRACTIONS);

        /** Name of database table for attractions */
        public final static String TABLE_NAME = "attractions";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SHORT_DESCRIPTION= "short_description";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_FOTO_MAIN = "foto_main";
        public static final String COLUMN_NAME_FOTO_DETAIL = "foto_detail";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_ATTRACTION_DISTANCE = "attraction_distance";
        public static final String COLUMN_NAME_ID = "_id";


        /**
         * Unique ID number for the aTTRACTION (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;


    }

}

