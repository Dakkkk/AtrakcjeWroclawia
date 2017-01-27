package com.example.android.xyztouristattractions.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

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
                    Attractions.COLUMN_NAME_FOTO_MAIN  + " TEXT," +
                    Attractions.COLUMN_NAME_FOTO_DETAIL + " TEXT," +
                    Attractions.COLUMN_NAME_LATITUDE + " TEXT," +
                    Attractions.COLUMN_NAME_LONGITUDE+ " TEXT" +
                    Attractions.COLUMN_NAME_LOCATION+  " TEXT,)";

    /* Definiujemy nazwy kolumn i tabeli */
    public static class Attractions implements BaseColumns {
        public static final String TABLE_NAME = "attractions";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SHORT_DESCRIPTION= "short_description";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_FOTO_MAIN = "foto_main";
        public static final String COLUMN_NAME_FOTO_DETAIL = "foto_detail";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_ID = "_id";
    }

    private static final String TAG = "CRMDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;

    private String orderBy = null;

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

    public long createAttraction(String name, String short_description, String description, String foto_main, String foto_detail, double latitude, double longitude, String location) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Attractions.COLUMN_NAME_NAME, name);
        initialValues.put(Attractions.COLUMN_NAME_SHORT_DESCRIPTION, short_description);
        initialValues.put(Attractions.COLUMN_NAME_DESCRIPTION, description);
        initialValues.put(Attractions.COLUMN_NAME_FOTO_MAIN, foto_main);
        initialValues.put(Attractions.COLUMN_NAME_FOTO_DETAIL, foto_detail);
        initialValues.put(Attractions.COLUMN_NAME_LATITUDE, latitude);
        initialValues.put(Attractions.COLUMN_NAME_LONGITUDE, longitude);
        initialValues.put(Attractions.COLUMN_NAME_LOCATION, location);

        return mDb.insert(Attractions.TABLE_NAME, null, initialValues);
    }

    public boolean deleteAllClients() {
        int doneDelete = 0;
        doneDelete = mDb.delete(Attractions.TABLE_NAME, null , null);

        Log.w(TAG, Integer.toString(doneDelete));   // Logujemy ilosc usunietych wpisow
        return doneDelete > 0;
    }

//    public Cursor fetchClientsByName(String inputText) throws SQLException {
//        Log.w(TAG, "Szukamy: " + inputText);
//
//        Cursor mCursor = null;
//
//        if (inputText == null || inputText.length () == 0) {
//            mCursor = mDb.query(Klienci.TABLE_NAME, new String[] {Klienci._ID, Klienci.COLUMN_NAME_NAZWA,
//                    Klienci.COLUMN_NAME_ADRES, Klienci.COLUMN_NAME_TELEFON}, null, null, null, null, orderBy, null);
//
//        } else {
//            mCursor = mDb.query(Klienci.TABLE_NAME, new String[] {Klienci._ID, Klienci.COLUMN_NAME_NAZWA,
//                    Klienci.COLUMN_NAME_ADRES, Klienci.COLUMN_NAME_TELEFON},
//                    Klienci.COLUMN_NAME_NAZWA + " like '%" + inputText + "%'",
//                    null, null, null, orderBy, null);
//        }
//
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
//
//        return mCursor;
//    }

    public Cursor fetchAttractionsByNameOrDescription(String inputText) throws SQLException {
        Log.w(TAG, "Szukamy: " + inputText);

        Cursor mCursor = null;

        if (inputText == null || inputText.length () == 0) {
            mCursor = mDb.query(Attractions.TABLE_NAME, new String[] {Attractions._ID, Attractions.COLUMN_NAME_NAME, Attractions.COLUMN_NAME_SHORT_DESCRIPTION,
                    Attractions.COLUMN_NAME_DESCRIPTION, Attractions.COLUMN_NAME_FOTO_MAIN, Attractions.COLUMN_NAME_FOTO_DETAIL, Attractions.COLUMN_NAME_LATITUDE, Attractions.COLUMN_NAME_LONGITUDE, Attractions.COLUMN_NAME_LOCATION}, null, null, null, null, orderBy, null);

        } else {
            mCursor = mDb.query(Attractions.TABLE_NAME, new String[] {Attractions._ID, Attractions.COLUMN_NAME_NAME, Attractions.COLUMN_NAME_SHORT_DESCRIPTION,
                            Attractions.COLUMN_NAME_DESCRIPTION, Attractions.COLUMN_NAME_FOTO_MAIN, Attractions.COLUMN_NAME_FOTO_DETAIL, Attractions.COLUMN_NAME_LATITUDE, Attractions.COLUMN_NAME_LONGITUDE, Attractions.COLUMN_NAME_LOCATION},
                    "(" + Attractions.COLUMN_NAME_NAME + " like '%" + inputText + "%') OR (" + Attractions.COLUMN_NAME_DESCRIPTION + " like '%" + inputText + "%')" , null, null, null, orderBy, null);
        }

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    /* TODO - dodać funkcję do wyszukiwania po adresie i nazwie równocześnie */

    public Cursor fetchAllAttractions() {
        Cursor mCursor = mDb.query(Attractions.TABLE_NAME, new String[] {Attractions._ID, Attractions.COLUMN_NAME_NAME, Attractions.COLUMN_NAME_SHORT_DESCRIPTION,
                Attractions.COLUMN_NAME_DESCRIPTION, Attractions.COLUMN_NAME_FOTO_MAIN, Attractions.COLUMN_NAME_FOTO_DETAIL, Attractions.COLUMN_NAME_LATITUDE, Attractions.COLUMN_NAME_LONGITUDE, Attractions.COLUMN_NAME_LOCATION}, null, null, null, null, orderBy, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public void setOrderBy(String txt) {
        orderBy = txt;
    }

    public Cursor getChoosenAttractionDetails(int id) throws SQLException {
       // Cursor mCursor = null;

        Cursor mCursor = mDb.query(Attractions.TABLE_NAME, new String[] {Attractions._ID, Attractions.COLUMN_NAME_NAME, Attractions.COLUMN_NAME_SHORT_DESCRIPTION,
                        Attractions.COLUMN_NAME_DESCRIPTION, Attractions.COLUMN_NAME_FOTO_MAIN, Attractions.COLUMN_NAME_FOTO_DETAIL, Attractions.COLUMN_NAME_LATITUDE, Attractions.COLUMN_NAME_LONGITUDE, Attractions.COLUMN_NAME_LOCATION},
                Attractions.COLUMN_NAME_ID + " like " + id
                ,null, null, null, orderBy, null);
        System.out.println("mCursor " + mCursor);

        return mCursor;
    }

    public void insertAttractions() {
        createAttraction("Rynek wrocławski",
                "Po pierwsze każdy, kto przyjeżdża do Wrocławia, MUSI zaliczyć spacer po Starym Mieście — Rynek i jego okolice, spacer nad Odrą w stronę Ostrowa Tumskiego i sam Ostrów Tumski, a później przez most w stronę Muzeum Narodowego i przez park z powrotem w stronę Rynku. To takie minimum wrocławskiego turysty..",
                "Wrocław znalazł sposób na to, żeby dzieci podczas takiego spaceru się nie nudziły. W całym Wrocławiu znajduje się prawie 300 niewielkich pomników różnych krasnali. Znajdują się one naprawdę w bardzo wielu miejscach, najwięcej jest ich właśnie w okolicach Rynku, więc kiedy wy spacerujecie i podziwiacie Wrocław, wasze dzieci z wypiekami na policzkach odnajdują kolejne krasnale. Wypróbowałam na Lili, która normalnie po 300 metrach spaceru krzyczy, że bolą ją nogi i, że chce na ręce. We Wrocławiu przez ponad 1 kilometr marszu ani razu nie powiedziała nic o sobie, bo była zbyt zajęta szukaniem i odnajdowaniem krasnali. Zresztą dorośli też krasnale bardzo lubią. Mój ulubiony to ten stojący przy bankomacie krasnoludzkiego oddziału Banku Zachodniego WBK :) Jeśli chcecie poczytać o krasnalach więcej, to zapraszam na krasnale.pl.",
                "http://mamygadzety.pl/wp-content/uploads/2016/01/dollarphotoclub_61018707.jpg",
                "http://mamygadzety.pl/wp-content/uploads/2016/01/dollarphotoclub_61018707.jpg",
                51.1078852,
                17.03853760000004,
                "Wrocław"
                );
    }
}