package com.example.android.xyztouristattractions.ui;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import com.example.android.xyztouristattractions.service.Base64CODEC;


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
    private Base64CODEC base64;
    private Bitmap bitmap = null;



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

         base64 = new Base64CODEC();

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

        dropDb();
        insertAttraction();

        //Kick off the loader
        getLoaderManager().initLoader(ATTLACTION_LOADER, null, this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sort_by_distance_asc:
                //ToDo sort by distance - data from the database ASC
                return true;
            case R.id.sort_by_distance_desc:
                //ToDo sort by distance - data from the database DESC
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
                AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION,
                AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_MAIN,
                AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_DETAIL,
                AttractionContract.AttractionEntry.COLUMN_NAME_LATITUDE,
                AttractionContract.AttractionEntry.COLUMN_NAME_LONGITUDE,
                AttractionContract.AttractionEntry.COLUMN_NAME_LOCATION
        };

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

    //ToDo Rewrite this function not to be repetitive
    private void insertAttraction() {

        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(AttractionContract.AttractionEntry.COLUMN_NAME_NAME, "Rynek wrocławski");
        values.put(AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION, "Po pierwsze każdy, kto przyjeżdża do Wrocławia, MUSI zaliczyć spacer po Starym Mieście — Rynek i jego okolice, spacer nad Odrą w stronę Ostrowa Tumskiego i sam Ostrów Tumski, a później przez most w stronę Muzeum Narodowego i przez park z powrotem w stronę Rynku. To takie minimum wrocławskiego turysty..");
        values.put(AttractionContract.AttractionEntry.COLUMN_NAME_DESCRIPTION, "Wrocław znalazł sposób na to, żeby dzieci podczas takiego spaceru się nie nudziły. W całym Wrocławiu znajduje się prawie 300 niewielkich pomników różnych krasnali. Znajdują się one naprawdę w bardzo wielu miejscach, najwięcej jest ich właśnie w okolicach Rynku, więc kiedy wy spacerujecie i podziwiacie Wrocław, wasze dzieci z wypiekami na policzkach odnajdują kolejne krasnale. Wypróbowałam na Lili, która normalnie po 300 metrach spaceru krzyczy, że bolą ją nogi i, że chce na ręce. We Wrocławiu przez ponad 1 kilometr marszu ani razu nie powiedziała nic o sobie, bo była zbyt zajęta szukaniem i odnajdowaniem krasnali. Zresztą dorośli też krasnale bardzo lubią. Mój ulubiony to ten stojący przy bankomacie krasnoludzkiego oddziału Banku Zachodniego WBK :) Jeśli chcecie poczytać o krasnalach więcej, to zapraszam na krasnale.pl.");
        values.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_MAIN, "http://mamygadzety.pl/wp-content/uploads/2016/01/dollarphotoclub_61018707.jpg");
        values.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_DETAIL, "http://mamygadzety.pl/wp-content/uploads/2016/01/dollarphotoclub_61018707.jpg");
        values.put(AttractionContract.AttractionEntry.COLUMN_NAME_LATITUDE, 51.1078852);
        values.put(AttractionContract.AttractionEntry.COLUMN_NAME_LONGITUDE, 17.03853760000004);
        values.put(AttractionContract.AttractionEntry.COLUMN_NAME_LOCATION, "");

        ContentValues values1 = new ContentValues();
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_NAME, "Muzeum Narodowe");
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION, "Muzeum Narodowe we Wrocławiu – jedno z głównych muzeów Wrocławia i Dolnego Śląska. Zbiory muzeum obejmują przede wszystkim malarstwo i rzeźbę, ze szczególnym uwzględnieniem sztuki całego Śląska.");
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_DESCRIPTION, "Zbiory[edytuj]\n" +
                "Ekspozycja w gmachu głównym przy pl. Powstańców Warszawy jest podzielona na następujące działy:\n" +
                "\n" +
                "Śląska rzeźba kamienna XII–XVI w.[edytuj]\n" +
                "Zawiera m.in. nagrobki książąt śląskich, figury ołtarzowe, rzeźby i reliefy.\n" +
                "\n" +
                "Sztuka śląska XIV–XIX w.[edytuj]\n" +
                "Najcenniejsza część kolekcji Muzeum, zawiera przede wszystkim wysokiej klasy obiekty gotyckiej sztuki sakralnej przeniesione tu ze śląskich kościołów (rzeźby, obrazy, ołtarze), np. w stylu Madonn na Lwach, Pięknych Madonn, realizmu mieszczańskiego, tworzone pod wpływem Wita Stwosza i inne, uzupełnione o zabytki rzemiosła artystycznego[4]. Od dominującej sztuki sakralnej renesansu i baroku (na szczególną uwagę zasługują tu obrazy Michaela Willmanna przez sztukę świecką (portrety) z tych epok, klasycyzm, biedermeier, do początku XX wieku; obrazom i rzeźbom towarzyszy rzemiosło artystyczne (szkło, porcelana, fajans ze śląskich wytwórni)[5].\n" +
                "\n" +
                "Sztuka polska XVII–XIX w.[edytuj]\n" +
                "Główny trzon kolekcji pochodzi ze zbiorów muzealnych Lwowa (Galerii Miejskiej we Lwowie, Muzeum Narodowego im. Króla Jana III, Muzeum Lubomirskich) i Kijowa, przekazanych Wrocławiowi w 1946 r. Dominuje malarstwo, w tym dzieła Marcello Bacciarellego, Bernarda Bellotto (Canaletto), Jacka Malczewskiego, Jana Matejki, Piotra Michałowskiego, )[6].\n" +
                "\n" +
                "Sztuka europejska XV–XX w.[edytuj]\n");
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_MAIN, "https://upload.wikimedia.org/wikipedia/commons/b/b2/Muzeum_Narodowe%2C_Wroc%C5%82aw.jpg");
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_DETAIL, "https://upload.wikimedia.org/wikipedia/commons/6/6d/Muzeum_Narodowe_noc%C4%85_fot_BMaliszewska.jpg");
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_LATITUDE, 51.1078852);
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_LONGITUDE, 17.03853760000004);
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_LOCATION, "");

        ContentValues values2 = new ContentValues();
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_NAME, "Zoo");
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION, "Ogród Zoologiczny we Wrocławiu (Zoo Wrocław) – ogród zoologiczny znajdujący się przy ul. Wróblewskiego 1–5 we Wrocławiu, otwarty 10 lipca 1865.");
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_DESCRIPTION,  "Ogród Zoologiczny we Wrocławiu (Zoo Wrocław) – ogród zoologiczny znajdujący się przy ul. Wróblewskiego 1–5 we Wrocławiu, otwarty 10 lipca 1865. Jest najstarszym[1] na obecnych ziemiach polskich ogrodem zoologicznym w Polsce. Powierzchnia ogrodu to 33 hektary[2]. Oficjalną nazwą ogrodu jest Zoo Wrocław Sp. z o.o.[3]\n" +
                "\n" + "Pod koniec 2015 wrocławskie Zoo prezentowało ponad 10 500 zwierząt (nie wliczając bezkręgowców) z 1132 gatunków[4]. Jest piątym najchętniej odwiedzanym ogrodem zoologicznym w Europie[5].");
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_MAIN, "https://upload.wikimedia.org/wikipedia/commons/0/09/1935_Brama_g%C5%82%C3%B3wna_teren%C3%B3w_wystawowych.jpg");
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_DETAIL, "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dc/Wroc%C5%82aw%2C_Miejski_Ogr%C3%B3d_Zoologiczny_-_fotopolska.eu_%28259320%29.jpg/1024px-Wroc%C5%82aw%2C_Miejski_Ogr%C3%B3d_Zoologiczny_-_fotopolska.eu_%28259320%29.jpg");
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_LATITUDE, 51.1078852);
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_LONGITUDE, 17.03853760000004);
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_LOCATION, "");

        ContentValues values3 = new ContentValues();
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_NAME, "Hala Stulecia");
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION, "Hala Stulecia (niem. Jahrhunderthalle, ang. Centennial Hall), daw. Hala Ludowa[2] – hala widowiskowo-sportowa znajdująca się we Wrocławiu, w dzielnicy Śródmieście, na osiedlu Zalesie, w parku Szczytnickim. Wzniesiona w latach 1911–1913 według projektu Maxa Berga, w stylu ekspresjonistycznym.");
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_DESCRIPTION,  "Hala Stulecia (niem. Jahrhunderthalle, ang. Centennial Hall), daw. Hala Ludowa[2] – hala widowiskowo-sportowa znajdująca się we Wrocławiu, w dzielnicy Śródmieście, na osiedlu Zalesie, w parku Szczytnickim. Wzniesiona w latach 1911–1913 według projektu Maxa Berga, w stylu ekspresjonistycznym.\n" +
                "\n" +
                "W 2006 roku hala została uznana za obiekt światowego dziedzictwa UNESCO. Wpisana do rejestru zabytków w 1962 oraz ponownie w 1977, wraz z zespołem architektonicznym obejmującym m.in. Pawilon Czterech Kopuł, Pergolę i Iglicę.\n" +
                "\n" +
                "Obecnie hala i jej okolice są bardzo licznie odwiedzane przez zwiedzających nie tylko ze względu na samą halę, ale także na bliskość Pergoli z Fontanną Multimedialną, Ogrodem Japońskim oraz zoo.");
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_MAIN,"https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/Wroc%C5%82aw_-_Jahrhunderthalle1.jpg/1280px-Wroc%C5%82aw_-_Jahrhunderthalle1.jpg");
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_DETAIL,"http://www.ltt.com.pl/sites/default/files/imagecache/mainpic/m_kulczynski_1.jpg");
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_LATITUDE, 51.1078852);
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_LONGITUDE, 17.03853760000004);
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_LOCATION, "");

        ContentValues values4 = new ContentValues();
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_NAME, "Hala Targowa");
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION, "Hala Stulecia (niem. Jahrhunderthalle, ang. Centennial Hall), daw. Hala Ludowa[2] – hala widowiskowo-sportowa znajdująca się we Wrocławiu, w dzielnicy Śródmieście, na osiedlu Zalesie, w parku Szczytnickim. Wzniesiona w latach 1911–1913 według projektu Maxa Berga, w stylu ekspresjonistycznym.");
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_DESCRIPTION,  "Hala Stulecia (niem. Jahrhunderthalle, ang. Centennial Hall), daw. Hala Ludowa[2] – hala widowiskowo-sportowa znajdująca się we Wrocławiu, w dzielnicy Śródmieście, na osiedlu Zalesie, w parku Szczytnickim. Wzniesiona w latach 1911–1913 według projektu Maxa Berga, w stylu ekspresjonistycznym.\n" +
                "\n" +
                "W 2006 roku hala została uznana za obiekt światowego dziedzictwa UNESCO. Wpisana do rejestru zabytków w 1962 oraz ponownie w 1977, wraz z zespołem architektonicznym obejmującym m.in. Pawilon Czterech Kopuł, Pergolę i Iglicę.\n" +
                "\n" +
                "Obecnie hala i jej okolice są bardzo licznie odwiedzane przez zwiedzających nie tylko ze względu na samą halę, ale także na bliskość Pergoli z Fontanną Multimedialną, Ogrodem Japońskim oraz zoo.");
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_MAIN,"https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Wroclaw_Hala_Targowa_plNankera.jpg/800px-Wroclaw_Hala_Targowa_plNankera.jpg");
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_DETAIL, "https://upload.wikimedia.org/wikipedia/commons/e/e1/Richard_Pl%C3%BCddemann_Market_Hall_photo_interior_1_Wroc%C5%82aw_Poland_2006-04-25.jpg");
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_LATITUDE, 51.1078852);
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_LONGITUDE, 17.03853760000004);
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_LOCATION, "");

        Uri newUri = getContentResolver().insert(AttractionContract.AttractionEntry.CONTENT_URI, values);
        Uri newUri1 = getContentResolver().insert(AttractionContract.AttractionEntry.CONTENT_URI, values1);
        Uri newUri2 = getContentResolver().insert(AttractionContract.AttractionEntry.CONTENT_URI, values2);
        Uri newUri3 = getContentResolver().insert(AttractionContract.AttractionEntry.CONTENT_URI, values3);
        Uri newUri4 = getContentResolver().insert(AttractionContract.AttractionEntry.CONTENT_URI, values4);
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
