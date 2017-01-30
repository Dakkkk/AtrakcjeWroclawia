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
                CRMDbAdapter.Attractions.COLUMN_NAME_FOTO_MAIN  + " BLOB," +
                CRMDbAdapter.Attractions.COLUMN_NAME_FOTO_DETAIL + " TEXT," +
                CRMDbAdapter.Attractions.COLUMN_NAME_LATITUDE + " TEXT," +
                CRMDbAdapter.Attractions.COLUMN_NAME_LONGITUDE+ " TEXT," +
                CRMDbAdapter.Attractions.COLUMN_NAME_LOCATION+  " TEXT)";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ATTRACTIONS_TABLE);

    }


//    public void insertAttractions() {
//        String fot_rynek = ("http://mamygadzety.pl/wp-content/uploads/2016/01/dollarphotoclub_61018707.jpg");
//        //String fot_rynek_bitmap = encodeTobase64(fot_rynek);
//
//        createAttraction("Rynek wrocławski",
//                "Po pierwsze każdy, kto przyjeżdża do Wrocławia, MUSI zaliczyć spacer po Starym Mieście — Rynek i jego okolice, spacer nad Odrą w stronę Ostrowa Tumskiego i sam Ostrów Tumski, a później przez most w stronę Muzeum Narodowego i przez park z powrotem w stronę Rynku. To takie minimum wrocławskiego turysty..",
//                "Wrocław znalazł sposób na to, żeby dzieci podczas takiego spaceru się nie nudziły. W całym Wrocławiu znajduje się prawie 300 niewielkich pomników różnych krasnali. Znajdują się one naprawdę w bardzo wielu miejscach, najwięcej jest ich właśnie w okolicach Rynku, więc kiedy wy spacerujecie i podziwiacie Wrocław, wasze dzieci z wypiekami na policzkach odnajdują kolejne krasnale. Wypróbowałam na Lili, która normalnie po 300 metrach spaceru krzyczy, że bolą ją nogi i, że chce na ręce. We Wrocławiu przez ponad 1 kilometr marszu ani razu nie powiedziała nic o sobie, bo była zbyt zajęta szukaniem i odnajdowaniem krasnali. Zresztą dorośli też krasnale bardzo lubią. Mój ulubiony to ten stojący przy bankomacie krasnoludzkiego oddziału Banku Zachodniego WBK :) Jeśli chcecie poczytać o krasnalach więcej, to zapraszam na krasnale.pl.",
//                fot_rynek,
//                fot_rynek,
//                51.1078852,
//                17.03853760000004,
//                "Wrocław"
//        );
//        createAttraction("Rynek wrocławski",
//                "Po pierwsze każdy, kto przyjeżdża do Wrocławia, MUSI zaliczyć spacer po Starym Mieście — Rynek i jego okolice, spacer nad Odrą w stronę Ostrowa Tumskiego i sam Ostrów Tumski, a później przez most w stronę Muzeum Narodowego i przez park z powrotem w stronę Rynku. To takie minimum wrocławskiego turysty..",
//                "Wrocław znalazł sposób na to, żeby dzieci podczas takiego spaceru się nie nudziły. W całym Wrocławiu znajduje się prawie 300 niewielkich pomników różnych krasnali. Znajdują się one naprawdę w bardzo wielu miejscach, najwięcej jest ich właśnie w okolicach Rynku, więc kiedy wy spacerujecie i podziwiacie Wrocław, wasze dzieci z wypiekami na policzkach odnajdują kolejne krasnale. Wypróbowałam na Lili, która normalnie po 300 metrach spaceru krzyczy, że bolą ją nogi i, że chce na ręce. We Wrocławiu przez ponad 1 kilometr marszu ani razu nie powiedziała nic o sobie, bo była zbyt zajęta szukaniem i odnajdowaniem krasnali. Zresztą dorośli też krasnale bardzo lubią. Mój ulubiony to ten stojący przy bankomacie krasnoludzkiego oddziału Banku Zachodniego WBK :) Jeśli chcecie poczytać o krasnalach więcej, to zapraszam na krasnale.pl.",
//                fot_rynek,
//                fot_rynek,
//                51.1078852,
//                17.03853760000004,
//                "Wrocław"
//        );
//        createAttraction("Rynek wrocławski",
//                "Po pierwsze każdy, kto przyjeżdża do Wrocławia, MUSI zaliczyć spacer po Starym Mieście — Rynek i jego okolice, spacer nad Odrą w stronę Ostrowa Tumskiego i sam Ostrów Tumski, a później przez most w stronę Muzeum Narodowego i przez park z powrotem w stronę Rynku. To takie minimum wrocławskiego turysty..",
//                "Wrocław znalazł sposób na to, żeby dzieci podczas takiego spaceru się nie nudziły. W całym Wrocławiu znajduje się prawie 300 niewielkich pomników różnych krasnali. Znajdują się one naprawdę w bardzo wielu miejscach, najwięcej jest ich właśnie w okolicach Rynku, więc kiedy wy spacerujecie i podziwiacie Wrocław, wasze dzieci z wypiekami na policzkach odnajdują kolejne krasnale. Wypróbowałam na Lili, która normalnie po 300 metrach spaceru krzyczy, że bolą ją nogi i, że chce na ręce. We Wrocławiu przez ponad 1 kilometr marszu ani razu nie powiedziała nic o sobie, bo była zbyt zajęta szukaniem i odnajdowaniem krasnali. Zresztą dorośli też krasnale bardzo lubią. Mój ulubiony to ten stojący przy bankomacie krasnoludzkiego oddziału Banku Zachodniego WBK :) Jeśli chcecie poczytać o krasnalach więcej, to zapraszam na krasnale.pl.",
//                fot_rynek,
//                fot_rynek,
//                51.1078852,
//                17.03853760000004,
//                "Wrocław"
//        );
//    }

//    public long createAttraction(String name, String short_description, String description, String foto_main,
//                                 String foto_detail, double latitude, double longitude, String location) {
//        ContentValues initialValues = new ContentValues();
//        initialValues.put(CRMDbAdapter.Attractions.COLUMN_NAME_NAME, name);
//        initialValues.put(CRMDbAdapter.Attractions.COLUMN_NAME_SHORT_DESCRIPTION, short_description);
//        initialValues.put(CRMDbAdapter.Attractions.COLUMN_NAME_DESCRIPTION, description);
//        initialValues.put(CRMDbAdapter.Attractions.COLUMN_NAME_FOTO_MAIN, foto_main);
//        initialValues.put(CRMDbAdapter.Attractions.COLUMN_NAME_FOTO_DETAIL, foto_detail);
//        initialValues.put(CRMDbAdapter.Attractions.COLUMN_NAME_LATITUDE, latitude);
//        initialValues.put(CRMDbAdapter.Attractions.COLUMN_NAME_LATITUDE, latitude);
//        initialValues.put(CRMDbAdapter.Attractions.COLUMN_NAME_LOCATION, location);
//
//        return db.insert(CRMDbAdapter.Attractions.TABLE_NAME, null, initialValues);
//        SQLiteDatabase database = mDbHelper.getWritableDatabase();
//
//
//    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
