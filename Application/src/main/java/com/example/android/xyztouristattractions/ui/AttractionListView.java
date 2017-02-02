package com.example.android.xyztouristattractions.ui;

import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.xyztouristattractions.R;
import com.example.android.xyztouristattractions.provider.AttractionContract;
import com.example.android.xyztouristattractions.provider.AttractionDbHelper;


/**
 * Created by Dawid on 2017-01-28.
 */

public class AttractionListView extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener, LocationListener {
    private static final int ATTLACTION_LOADER = 0;
    /**
     * Database helper that will provide us access to the database
     */
    private AttractionDbHelper mDbHelper;

    private AttractionsCursorAdapter mAttractionCursorAdapter;

    private android.widget.SearchView searchView;
    private MenuItem searchItem;
    private ListView attractionListView;

    LocationListener locationListener;

    private static float distance;

    private TextView txtDistance;
    private boolean canGetLocation;

    public static float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        // Setup FAB to open EditorActivity
        ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttractionListView.this, MapsMarkerActivity.class);
                startActivity(intent);
            }
        });

        Button refreshBtn = (Button) findViewById(R.id.btnRefresh);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attractionListView.invalidateViews();
            }
        });


        mAttractionCursorAdapter = new AttractionsCursorAdapter(this, null);

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new AttractionDbHelper(this);

        // Find the ListView which will be populated with the attraction data
        attractionListView = (ListView) findViewById(R.id.attractions_list);

        //ToDo Show en empty view when there are no attractions
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

        dropDb();
        insertAttraction();

        //Kick off the loader
        getLoaderManager().initLoader(ATTLACTION_LOADER, null, this);


        //txtDistance = (TextView) findViewById(R.id.overlaytext);

    }

    public static void distanceBetween(double startLatitude, double startLongitude, double endLatitude, double endLongitude, float[] results) {

    }


    //Location permission dialog
    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);

                    }
                });
        builder.create().show();
    }

    //GPS permission dialog
    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sort_by_distance_asc:
                mAttractionCursorAdapter.changeCursor(orderByDistanceASC());
                attractionListView.invalidateViews();
                return true;
            case R.id.sort_by_distance_desc:
                mAttractionCursorAdapter.changeCursor(orderByDistanceDESC());
                attractionListView.invalidateViews();
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
        }
    }

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
                AttractionContract.AttractionEntry.COLUMN_NAME_LOCATION,
                AttractionContract.AttractionEntry.COLUMN_NAME_ATTRACTION_DISTANCE
        };

        return new CursorLoader(this,
                AttractionContract.AttractionEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 1;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
            }
            Log.d("Location", "ERROR!!!");

            return;
        }


        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the next row of the cursor and reading data from it
        while (cursor.moveToNext()) {

            int attractionLatitude = cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_LATITUDE);
            int attractionLongitude = cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_LONGITUDE);
            int nameColumnIndex = cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_NAME);


            int attractionIdCol = cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_ID);
            String attractionName = cursor.getString(nameColumnIndex);


            Float attractionLat = cursor.getFloat(attractionLatitude);
            Float attractionLong = cursor.getFloat(attractionLongitude);
            int attractionID = cursor.getInt(attractionIdCol);


            double attractionLatDouble = attractionLat.doubleValue();
            double attractionLongDouble = attractionLong.doubleValue();

            if (getLocation() != null) {

                double longitude = getLocation().getLongitude();
                double latitude = getLocation().getLatitude();
                distance = countDistance(attractionLatDouble, attractionLongDouble, latitude, longitude);

                System.out.println("User location: " + getLocation().toString());
                System.out.println("Current attraction distance: " + distance + " Name " + attractionName);

                ContentValues values = new ContentValues();

                values.put(AttractionContract.AttractionEntry.COLUMN_NAME_ATTRACTION_DISTANCE, distance);


                Uri currentAttractionUri =
                        ContentUris
                                .withAppendedId(AttractionContract.AttractionEntry.CONTENT_URI,
                                        attractionID);

                System.out.println("Current attraction URI: " + currentAttractionUri);

                if (currentAttractionUri == null) {
                    // Since no fields were modified, we can return early without creating a new pet.
                    // No need to create ContentValues and no need to do any ContentProvider operations.
                    Toast.makeText(this, "No URI", Toast.LENGTH_SHORT);
                    return;
                }

                //Update One attraction at a time
                    int rowsAffected = getContentResolver().update(currentAttractionUri, values, null, null);

                //Update all attractions at a time
               // int rowsAffected = getContentResolver().update(AttractionContract.AttractionEntry.CONTENT_URI, values, null, null);

                    // Show a toast message depending on whether or not the update was successful.
                    if (rowsAffected == 0) {
                        // If no rows were affected, then there was an error with the update.
                        Toast.makeText(this, "Update failed",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the update was successful and we can display a toast.
                        Toast.makeText(this, "Update successful, id: " + attractionID,
                                Toast.LENGTH_SHORT).show();
                        cursor.moveToNext();
                    }
            } else {
                Toast.makeText(this, "User location is null!",
                        Toast.LENGTH_SHORT).show();
            }

        }

        mAttractionCursorAdapter.swapCursor(cursor);
    }

    //Get the user GPS location
    public Location getLocation() {
        Location location = null;

        try {
            LocationManager locationManager = (LocationManager) getApplicationContext()
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            500,
                            5, this);
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                500,
                                5, this);
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    private void insertDistanceToDb(double distance) {
        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();

        values.put(AttractionContract.AttractionEntry.COLUMN_NAME_ATTRACTION_DISTANCE, distance);
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
        values.put(AttractionContract.AttractionEntry.COLUMN_NAME_LOCATION, "Wrocław");
        values.put(AttractionContract.AttractionEntry.COLUMN_NAME_ATTRACTION_DISTANCE, 0);

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
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_LATITUDE, 51.1109061);
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_LONGITUDE, 17.0476092);
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_LOCATION, "Wrocław");
        values1.put(AttractionContract.AttractionEntry.COLUMN_NAME_ATTRACTION_DISTANCE, 0);


        ContentValues values2 = new ContentValues();
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_NAME, "Zoo");
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION, "Ogród Zoologiczny we Wrocławiu (Zoo Wrocław) – ogród zoologiczny znajdujący się przy ul. Wróblewskiego 1–5 we Wrocławiu, otwarty 10 lipca 1865.");
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_DESCRIPTION, "Ogród Zoologiczny we Wrocławiu (Zoo Wrocław) – ogród zoologiczny znajdujący się przy ul. Wróblewskiego 1–5 we Wrocławiu, otwarty 10 lipca 1865. Jest najstarszym[1] na obecnych ziemiach polskich ogrodem zoologicznym w Polsce. Powierzchnia ogrodu to 33 hektary[2]. Oficjalną nazwą ogrodu jest Zoo Wrocław Sp. z o.o.[3]\n" +
                "\n" + "Pod koniec 2015 wrocławskie Zoo prezentowało ponad 10 500 zwierząt (nie wliczając bezkręgowców) z 1132 gatunków[4]. Jest piątym najchętniej odwiedzanym ogrodem zoologicznym w Europie[5].");
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_MAIN, "https://upload.wikimedia.org/wikipedia/commons/0/09/1935_Brama_g%C5%82%C3%B3wna_teren%C3%B3w_wystawowych.jpg");
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_DETAIL, "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dc/Wroc%C5%82aw%2C_Miejski_Ogr%C3%B3d_Zoologiczny_-_fotopolska.eu_%28259320%29.jpg/1024px-Wroc%C5%82aw%2C_Miejski_Ogr%C3%B3d_Zoologiczny_-_fotopolska.eu_%28259320%29.jpg");
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_LATITUDE, 51.1041429);
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_LONGITUDE, 17.0742114);
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_LOCATION, "Wrocław");
        values2.put(AttractionContract.AttractionEntry.COLUMN_NAME_ATTRACTION_DISTANCE, 0);


        ContentValues values3 = new ContentValues();
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_NAME, "Hala Stulecia");
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION, "Hala Stulecia (niem. Jahrhunderthalle, ang. Centennial Hall), daw. Hala Ludowa[2] – hala widowiskowo-sportowa znajdująca się we Wrocławiu, w dzielnicy Śródmieście, na osiedlu Zalesie, w parku Szczytnickim. Wzniesiona w latach 1911–1913 według projektu Maxa Berga, w stylu ekspresjonistycznym.");
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_DESCRIPTION, "Hala Stulecia (niem. Jahrhunderthalle, ang. Centennial Hall), daw. Hala Ludowa[2] – hala widowiskowo-sportowa znajdująca się we Wrocławiu, w dzielnicy Śródmieście, na osiedlu Zalesie, w parku Szczytnickim. Wzniesiona w latach 1911–1913 według projektu Maxa Berga, w stylu ekspresjonistycznym.\n" +
                "\n" +
                "W 2006 roku hala została uznana za obiekt światowego dziedzictwa UNESCO. Wpisana do rejestru zabytków w 1962 oraz ponownie w 1977, wraz z zespołem architektonicznym obejmującym m.in. Pawilon Czterech Kopuł, Pergolę i Iglicę.\n" +
                "\n" +
                "Obecnie hala i jej okolice są bardzo licznie odwiedzane przez zwiedzających nie tylko ze względu na samą halę, ale także na bliskość Pergoli z Fontanną Multimedialną, Ogrodem Japońskim oraz zoo.");
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_MAIN, "https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/Wroc%C5%82aw_-_Jahrhunderthalle1.jpg/1280px-Wroc%C5%82aw_-_Jahrhunderthalle1.jpg");
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_DETAIL, "http://www.ltt.com.pl/sites/default/files/imagecache/mainpic/m_kulczynski_1.jpg");
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_LATITUDE, 51.1068577);
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_LONGITUDE, 17.0772959);
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_LOCATION, "Wrocław");
        values3.put(AttractionContract.AttractionEntry.COLUMN_NAME_ATTRACTION_DISTANCE, 0);


        ContentValues values4 = new ContentValues();
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_NAME, "Hala Targowa");
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION, "Hala Stulecia (niem. Jahrhunderthalle, ang. Centennial Hall), daw. Hala Ludowa[2] – hala widowiskowo-sportowa znajdująca się we Wrocławiu, w dzielnicy Śródmieście, na osiedlu Zalesie, w parku Szczytnickim. Wzniesiona w latach 1911–1913 według projektu Maxa Berga, w stylu ekspresjonistycznym.");
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_DESCRIPTION, "Hala Stulecia (niem. Jahrhunderthalle, ang. Centennial Hall), daw. Hala Ludowa[2] – hala widowiskowo-sportowa znajdująca się we Wrocławiu, w dzielnicy Śródmieście, na osiedlu Zalesie, w parku Szczytnickim. Wzniesiona w latach 1911–1913 według projektu Maxa Berga, w stylu ekspresjonistycznym.\n" +
                "\n" +
                "W 2006 roku hala została uznana za obiekt światowego dziedzictwa UNESCO. Wpisana do rejestru zabytków w 1962 oraz ponownie w 1977, wraz z zespołem architektonicznym obejmującym m.in. Pawilon Czterech Kopuł, Pergolę i Iglicę.\n" +
                "\n" +
                "Obecnie hala i jej okolice są bardzo licznie odwiedzane przez zwiedzających nie tylko ze względu na samą halę, ale także na bliskość Pergoli z Fontanną Multimedialną, Ogrodem Japońskim oraz zoo.");
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_MAIN, "https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Wroclaw_Hala_Targowa_plNankera.jpg/800px-Wroclaw_Hala_Targowa_plNankera.jpg");
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_DETAIL, "https://upload.wikimedia.org/wikipedia/commons/e/e1/Richard_Pl%C3%BCddemann_Market_Hall_photo_interior_1_Wroc%C5%82aw_Poland_2006-04-25.jpg");
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_LATITUDE, 51.1126439);
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_LONGITUDE, 17.0397772);
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_LOCATION, "Wrocław");
        values4.put(AttractionContract.AttractionEntry.COLUMN_NAME_ATTRACTION_DISTANCE, 0);


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
    private Cursor searchForAttraction(String userQuery) {
        String selection;
        String[] whereArgs;
        String[] tableColumns = new String[]{
                AttractionContract.AttractionEntry.COLUMN_NAME_NAME,
                AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION,
        };

        if (userQuery == null || userQuery.length() == 0) {
            selection = null;
            whereArgs = null;
        } else {
            selection = AttractionContract.AttractionEntry.COLUMN_NAME_NAME + " LIKE ? " + " OR " + AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION + " LIKE ? ";
            whereArgs = new String[]{
                    "%" + userQuery + "%", "%" + userQuery + "%"
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

    private Cursor orderByDistanceDESC() {
        Cursor cursor = getContentResolver().query(AttractionContract.AttractionEntry.CONTENT_URI, null, null, null, AttractionContract.AttractionEntry.COLUMN_NAME_ATTRACTION_DISTANCE + " DESC");
        return cursor;
    }

    private Cursor orderByDistanceASC() {
        Cursor cursor = getContentResolver().query(AttractionContract.AttractionEntry.CONTENT_URI, null, null, null, AttractionContract.AttractionEntry.COLUMN_NAME_ATTRACTION_DISTANCE + " ASC");
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

    /**
     * using WSG84
     * using the Metric system
     */


    @Override
    public void onLocationChanged(Location location) {

//        Cursor cursor = getContentResolver().query(AttractionContract.AttractionEntry.CONTENT_URI, null, null, null, null);
//        int attractionLatitude = cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_LATITUDE);
//        int attractionLongitude = cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_LONGITUDE);
//
//        Float attractionLat = cursor.getFloat(attractionLatitude);
//        Float attractionLong = cursor.getFloat(attractionLongitude);
//
//        double attractionLatDouble = attractionLat.doubleValue();
//        double attractionLongDouble = attractionLong.doubleValue();
//
//        double userLatitude = location.getLatitude();
//        double userLongitude = location.getLongitude();
//
//        float distance = getDistance(userLatitude, userLongitude, attractionLatDouble, attractionLongDouble);
//
//        setDistance(distance);
//
//        TextView txtLat = (TextView) findViewById(R.id.overlaytext);
//        txtLat.setText(String.valueOf(distance) + " m");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
        Toast.makeText(getApplicationContext(), "Loaction DISABLED", Toast.LENGTH_LONG);

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
        Toast.makeText(getApplicationContext(), "Loaction ENABLED", Toast.LENGTH_LONG);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
        Toast.makeText(getApplicationContext(), "STATUS CHANGED", Toast.LENGTH_LONG);

    }

    public float countDistance(double startLati, double startLongi, double goalLati, double goalLongi) {
        float[] resultArray = new float[99];
        Location.distanceBetween(startLati, startLongi, goalLati, goalLongi, resultArray);
        return resultArray[0];
    }

}
