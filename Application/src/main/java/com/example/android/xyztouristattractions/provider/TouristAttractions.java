///*
// * Copyright 2015 Google Inc. All rights reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.example.android.xyztouristattractions.provider;
//
//import android.net.Uri;
//
//import com.example.android.xyztouristattractions.common.Attraction;
//import com.google.android.gms.location.Geofence;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.maps.android.SphericalUtil;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Static data content provider.
// */
//public class TouristAttractions {
//
//    public static final String CITY_WROCLAW = "Wrocław";
//
//    public static final String TEST_CITY = CITY_WROCLAW;
//
//    private static final float TRIGGER_RADIUS = 2000; // 2KM
//    private static final int TRIGGER_TRANSITION = Geofence.GEOFENCE_TRANSITION_ENTER |
//            Geofence.GEOFENCE_TRANSITION_EXIT;
//    private static final long EXPIRATION_DURATION = Geofence.NEVER_EXPIRE;
//
//    public static final Map<String, LatLng> CITY_LOCATIONS = new HashMap<String, LatLng>() {{
//        put(CITY_WROCLAW, new LatLng(51.1078852, 17.03853760000004));
//    }};
//
//    /**
//     * All photos used with permission under the Creative Commons Attribution-ShareAlike License.
//     */
//    public static final HashMap<String, List<Attraction>> ATTRACTIONS =
//            new HashMap<String, List<Attraction>>() {{
//
//        put(CITY_WROCLAW, new ArrayList<Attraction>() {{
//            add(new Attraction(
//                    "Rynek wrocławski",
//                    "Po pierwsze każdy, kto przyjeżdża do Wrocławia, MUSI zaliczyć spacer po Starym Mieście — Rynek i jego okolice, spacer nad Odrą w stronę Ostrowa Tumskiego i sam Ostrów Tumski, a później przez most w stronę Muzeum Narodowego i przez park z powrotem w stronę Rynku. To takie minimum wrocławskiego turysty..",
//                    "Wrocław znalazł sposób na to, żeby dzieci podczas takiego spaceru się nie nudziły. W całym Wrocławiu znajduje się prawie 300 niewielkich pomników różnych krasnali. Znajdują się one naprawdę w bardzo wielu miejscach, najwięcej jest ich właśnie w okolicach Rynku, więc kiedy wy spacerujecie i podziwiacie Wrocław, wasze dzieci z wypiekami na policzkach odnajdują kolejne krasnale. Wypróbowałam na Lili, która normalnie po 300 metrach spaceru krzyczy, że bolą ją nogi i, że chce na ręce. We Wrocławiu przez ponad 1 kilometr marszu ani razu nie powiedziała nic o sobie, bo była zbyt zajęta szukaniem i odnajdowaniem krasnali. Zresztą dorośli też krasnale bardzo lubią. Mój ulubiony to ten stojący przy bankomacie krasnoludzkiego oddziału Banku Zachodniego WBK :) Jeśli chcecie poczytać o krasnalach więcej, to zapraszam na krasnale.pl.",
//                    Uri.parse("http://mamygadzety.pl/wp-content/uploads/2016/01/dollarphotoclub_61018707.jpg"),
//                    Uri.parse("http://mamygadzety.pl/wp-content/uploads/2016/01/dollarphotoclub_61018707.jpg"),
//                    new LatLng(51.1078852, 17.03853760000004),
//                    CITY_WROCLAW
//            ));
//
//            add(new Attraction(
//                    "Muzeum Narodowe",
//                    "Muzeum Narodowe we Wrocławiu – jedno z głównych muzeów Wrocławia i Dolnego Śląska. Zbiory muzeum obejmują przede wszystkim malarstwo i rzeźbę, ze szczególnym uwzględnieniem sztuki całego Śląska.",
//                    "Zbiory[edytuj]\n" +
//                            "Ekspozycja w gmachu głównym przy pl. Powstańców Warszawy jest podzielona na następujące działy:\n" +
//                            "\n" +
//                            "Śląska rzeźba kamienna XII–XVI w.[edytuj]\n" +
//                            "Zawiera m.in. nagrobki książąt śląskich, figury ołtarzowe, rzeźby i reliefy.\n" +
//                            "\n" +
//                            "Sztuka śląska XIV–XIX w.[edytuj]\n" +
//                            "Najcenniejsza część kolekcji Muzeum, zawiera przede wszystkim wysokiej klasy obiekty gotyckiej sztuki sakralnej przeniesione tu ze śląskich kościołów (rzeźby, obrazy, ołtarze), np. w stylu Madonn na Lwach, Pięknych Madonn, realizmu mieszczańskiego, tworzone pod wpływem Wita Stwosza i inne, uzupełnione o zabytki rzemiosła artystycznego[4]. Od dominującej sztuki sakralnej renesansu i baroku (na szczególną uwagę zasługują tu obrazy Michaela Willmanna przez sztukę świecką (portrety) z tych epok, klasycyzm, biedermeier, do początku XX wieku; obrazom i rzeźbom towarzyszy rzemiosło artystyczne (szkło, porcelana, fajans ze śląskich wytwórni)[5].\n" +
//                            "\n" +
//                            "Sztuka polska XVII–XIX w.[edytuj]\n" +
//                            "Główny trzon kolekcji pochodzi ze zbiorów muzealnych Lwowa (Galerii Miejskiej we Lwowie, Muzeum Narodowego im. Króla Jana III, Muzeum Lubomirskich) i Kijowa, przekazanych Wrocławiowi w 1946 r. Dominuje malarstwo, w tym dzieła Marcello Bacciarellego, Bernarda Bellotto (Canaletto), Jacka Malczewskiego, Jana Matejki, Piotra Michałowskiego, )[6].\n" +
//                            "\n" +
//                            "Sztuka europejska XV–XX w.[edytuj]\n",
//                    Uri.parse("https://upload.wikimedia.org/wikipedia/commons/b/b2/Muzeum_Narodowe%2C_Wroc%C5%82aw.jpg"),
//                    Uri.parse("https://upload.wikimedia.org/wikipedia/commons/6/6d/Muzeum_Narodowe_noc%C4%85_fot_BMaliszewska.jpg"),
//                    new LatLng(51.1109061,17.0476092),
//                    CITY_WROCLAW
//            ));
//
//            add(new Attraction(
//                    "Hala Targowa",
//                    "Hala Targowa we Wrocławiu (właśc. Hala Targowa nr I, niem. Markthalle Nummer 1) – zbudowana według projektu Richarda Plüddemanna i Heinricha Küstera w latach 1906–1908[a]. Mieści się przy ul. Piaskowej",
//                    "Hala Targowa we Wrocławiu (właśc. Hala Targowa nr I, niem. Markthalle Nummer 1) – zbudowana według projektu Richarda Plüddemanna i Heinricha Küstera w latach 1906–1908[a]. Mieści się przy ul. Piaskowej (Sandstraße), u zbiegu z pl. Nankiera (Ritterplatz) i ul. św. Ducha (Heiligegeiststraße), w bezpośredniej bliskości wyspy Piasek, mostu Piaskowego i najstarszych dzielnic miasta. Hala stanowi cenny zabytek sztuki inżynierskiej.\n" +
//                            "\n" +
//                            "Halę Targową nr I wzniesiono dla uporządkowania handlu w centrum miasta, który odbywał się wcześniej m.in. na Nowym Targu. Prawie identyczna Hala Targowa nr II tych samych projektantów powstała w tym samym czasie przy ul. Kolejowej, lecz po poważnym uszkodzeniu w 1945 została wyburzona w 1973.",
//                    Uri.parse("https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Wroclaw_Hala_Targowa_plNankera.jpg/800px-Wroclaw_Hala_Targowa_plNankera.jpg"),
//                    Uri.parse("https://upload.wikimedia.org/wikipedia/commons/e/e1/Richard_Pl%C3%BCddemann_Market_Hall_photo_interior_1_Wroc%C5%82aw_Poland_2006-04-25.jpg"),
//                    new LatLng(51.1126439,17.0397772),
//                    CITY_WROCLAW
//            ));
//
//            add(new Attraction(
//                    "Hala Stulecia",
//                    "Hala Stulecia (niem. Jahrhunderthalle, ang. Centennial Hall), daw. Hala Ludowa[2] – hala widowiskowo-sportowa znajdująca się we Wrocławiu, w dzielnicy Śródmieście, na osiedlu Zalesie, w parku Szczytnickim. Wzniesiona w latach 1911–1913 według projektu Maxa Berga, w stylu ekspresjonistycznym.",
//                    "Hala Stulecia (niem. Jahrhunderthalle, ang. Centennial Hall), daw. Hala Ludowa[2] – hala widowiskowo-sportowa znajdująca się we Wrocławiu, w dzielnicy Śródmieście, na osiedlu Zalesie, w parku Szczytnickim. Wzniesiona w latach 1911–1913 według projektu Maxa Berga, w stylu ekspresjonistycznym.\n" +
//                            "\n" +
//                            "W 2006 roku hala została uznana za obiekt światowego dziedzictwa UNESCO. Wpisana do rejestru zabytków w 1962 oraz ponownie w 1977, wraz z zespołem architektonicznym obejmującym m.in. Pawilon Czterech Kopuł, Pergolę i Iglicę.\n" +
//                            "\n" +
//                            "Obecnie hala i jej okolice są bardzo licznie odwiedzane przez zwiedzających nie tylko ze względu na samą halę, ale także na bliskość Pergoli z Fontanną Multimedialną, Ogrodem Japońskim oraz zoo.",
//                    Uri.parse("https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/Wroc%C5%82aw_-_Jahrhunderthalle1.jpg/1280px-Wroc%C5%82aw_-_Jahrhunderthalle1.jpg"),
//                    Uri.parse("https://upload.wikimedia.org/wikipedia/commons/3/31/Wroc%C5%82aw_-_Jahrhunderthalle6.jpg"),
//                    new LatLng(51.1068577,17.0772959),
//                    CITY_WROCLAW
//            ));
//
//            add(new Attraction(
//                    "Zoo",
//                    "Ogród Zoologiczny we Wrocławiu (Zoo Wrocław) – ogród zoologiczny znajdujący się przy ul. Wróblewskiego 1–5 we Wrocławiu, otwarty 10 lipca 1865.",
//                    "Ogród Zoologiczny we Wrocławiu (Zoo Wrocław) – ogród zoologiczny znajdujący się przy ul. Wróblewskiego 1–5 we Wrocławiu, otwarty 10 lipca 1865. Jest najstarszym[1] na obecnych ziemiach polskich ogrodem zoologicznym w Polsce. Powierzchnia ogrodu to 33 hektary[2]. Oficjalną nazwą ogrodu jest Zoo Wrocław Sp. z o.o.[3]\n" +
//                            "\n" + "Pod koniec 2015 wrocławskie Zoo prezentowało ponad 10 500 zwierząt (nie wliczając bezkręgowców) z 1132 gatunków[4]. Jest piątym najchętniej odwiedzanym ogrodem zoologicznym w Europie[5].",
//                    Uri.parse("https://upload.wikimedia.org/wikipedia/commons/0/09/1935_Brama_g%C5%82%C3%B3wna_teren%C3%B3w_wystawowych.jpg"),
//                    Uri.parse("https://upload.wikimedia.org/wikipedia/commons/thumb/d/dc/Wroc%C5%82aw%2C_Miejski_Ogr%C3%B3d_Zoologiczny_-_fotopolska.eu_%28259320%29.jpg/1024px-Wroc%C5%82aw%2C_Miejski_Ogr%C3%B3d_Zoologiczny_-_fotopolska.eu_%28259320%29.jpg"),
//                    new LatLng(51.1041429,17.0742114),
//                    CITY_WROCLAW
//            ));
//        }});
//
//    }};
//
//    /**
//     * Creates a list of geofences based on the city locations
//     */
//    public static List<Geofence> getGeofenceList() {
//        List<Geofence> geofenceList = new ArrayList<Geofence>();
//        for (String city : CITY_LOCATIONS.keySet()) {
//            LatLng cityLatLng = CITY_LOCATIONS.get(city);
//            geofenceList.add(new Geofence.Builder()
//                    .setCircularRegion(cityLatLng.latitude, cityLatLng.longitude, TRIGGER_RADIUS)
//                    .setRequestId(city)
//                    .setTransitionTypes(TRIGGER_TRANSITION)
//                    .setExpirationDuration(EXPIRATION_DURATION)
//                    .build());
//        }
//        return geofenceList;
//    }
//
//    public static String getClosestCity(LatLng curLatLng) {
//        if (curLatLng == null) {
//            // If location is unknown return test city so some data is shown
//            return TEST_CITY;
//        }
//
//        double minDistance = 0;
//        String closestCity = null;
//        for (Map.Entry<String, LatLng> entry: CITY_LOCATIONS.entrySet()) {
//            double distance = SphericalUtil.computeDistanceBetween(curLatLng, entry.getValue());
//            if (minDistance == 0 || distance < minDistance) {
//                minDistance = distance;
//                closestCity = entry.getKey();
//            }
//        }
//        return closestCity;
//    }
//
//    public static String getFarestCity(LatLng curLatLng) {
//        if (curLatLng == null) {
//            // If location is unknown return test city so some data is shown
//            return TEST_CITY;
//        }
//
//        double maxDistance = 0;
//        String farestCity = null;
//        for (Map.Entry<String, LatLng> entry: CITY_LOCATIONS.entrySet()) {
//            double distance = SphericalUtil.computeDistanceBetween(curLatLng, entry.getValue());
//            if (maxDistance == 0 || distance > maxDistance) {
//                maxDistance = distance;
//                farestCity = entry.getKey();
//            }
//        }
//        return farestCity;
//    }
//
//
//}
