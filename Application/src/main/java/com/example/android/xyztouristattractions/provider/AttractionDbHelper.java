package com.example.android.xyztouristattractions.provider;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.xyztouristattractions.provider.AttractionContract.AttractionEntry;



/**
 * Created by Dawid on 2017-01-28.
 */

public class AttractionDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = AttractionDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "attractions.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;


    public AttractionDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public AttractionDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public AttractionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the attractions table
        String SQL_CREATE_ATTRACTIONS_TABLE =  "CREATE TABLE " + AttractionEntry.TABLE_NAME + " ("
                +  CRMDbAdapter.Attractions._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CRMDbAdapter.Attractions.COLUMN_NAME_NAME + " TEXT," +
                CRMDbAdapter.Attractions.COLUMN_NAME_SHORT_DESCRIPTION + " TEXT," +
                CRMDbAdapter.Attractions.COLUMN_NAME_DESCRIPTION + " TEXT," +
                CRMDbAdapter.Attractions.COLUMN_NAME_FOTO_MAIN  + " TEXT," +
                CRMDbAdapter.Attractions.COLUMN_NAME_FOTO_DETAIL + " TEXT," +
                CRMDbAdapter.Attractions.COLUMN_NAME_LATITUDE + " FLOAT," +
                CRMDbAdapter.Attractions.COLUMN_NAME_LONGITUDE+ " FLOAT," +
                CRMDbAdapter.Attractions.COLUMN_NAME_LOCATION+  " TEXT," +
                CRMDbAdapter.Attractions.COLUMN_NAME_ATTRACTION_DISTANCE+  " FLOAT)";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ATTRACTIONS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
