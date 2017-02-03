package com.example.android.xyztouristattractions.provider;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/* Klasa wspomagająca obsługę bazy SQLite */
public class CRMDbAdapter {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CRM.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Attractions.TABLE_NAME + " (" +
                    Attractions._ID + " INTEGER PRIMARY KEY," +
                    Attractions.COLUMN_NAME_NAME + " TEXT," +
                    Attractions.COLUMN_NAME_SHORT_DESCRIPTION + " TEXT," +
                    Attractions.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    Attractions.COLUMN_NAME_FOTO_MAIN + " TEXT," +
                    Attractions.COLUMN_NAME_FOTO_DETAIL + " TEXT," +
                    Attractions.COLUMN_NAME_LATITUDE + " TEXT," +
                    Attractions.COLUMN_NAME_LONGITUDE + " TEXT," +
                    Attractions.COLUMN_NAME_LOCATION + " TEXT," +
                    Attractions.COLUMN_NAME_ATTRACTION_DISTANCE + " TEXT)";

    /* Definiujemy nazwy kolumn i tabeli */
    public static class Attractions implements BaseColumns {
        public static final String TABLE_NAME = "attractions";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SHORT_DESCRIPTION = "short_description";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_FOTO_MAIN = "foto_main";
        public static final String COLUMN_NAME_FOTO_DETAIL = "foto_detail";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_ATTRACTION_DISTANCE = "attraction_distance";
        public static final String COLUMN_NAME_ID = "_id";
    }

    private static final String TAG = "CRMDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;


    public class DatabaseHelper extends SQLiteOpenHelper {
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Attractions.TABLE_NAME;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public CRMDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public CRMDbAdapter open() throws SQLException {
        mDbHelper = new CRMDbAdapter.DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }
}