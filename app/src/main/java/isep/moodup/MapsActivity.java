package isep.moodup;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import android.location.LocationListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Criteria;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.common.GoogleApiAvailability;

import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import static java.lang.Double.parseDouble;


import static isep.moodup.Config.*;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    CoordinatorLayout mainCoordinatorLayout;

    protected String JSON_STRING;
    protected String TAG = MapsActivity.class.getSimpleName();

    double myLat=0;
    double myLng=0;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isGooglePlayServicesAvailable()) {
            return;
        }
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mainCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.mainCoordinatorLayout);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showLocationSettings();
        }
    }

    private void showLocationSettings() {
        Snackbar snackbar = Snackbar
                .make(mainCoordinatorLayout, "Location Error: GPS Disabled!",

                        Snackbar.LENGTH_LONG)
                .setAction("Enable", new View.OnClickListener() {
                    @Override                    public void onClick(View v) {

                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView

                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();
    }

    @Override

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)

                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,

                android.Manifest.permission.ACCESS_COARSE_LOCATION)

                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        showCurrentLocation();
        getIncidents();
    }

    private void showCurrentLocation() {
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this,

                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&

                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)

                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, MIN_TIME_BW_UPDATES,

                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
    }

    private void loadNearByPlaces(double latitude, double longitude) {
//YOU Can change this type at your own will, e.g hospital, cafe, restaurant.... and see how it all works

        String type="bus_station|train_station|subway_station|transit_station|airport";/*route new String[3];
        type[0]="train_station";
        type[1]="bus_station";
        type[2]="subway_station";*///{"train_station or bus_station"};//"grocery_or_supermarket";
        StringBuilder googlePlacesUrl =

                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlacesUrl.append("&radius=").append(PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=").append(type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + GOOGLE_BROWSER_API_KEY);

        JsonObjectRequest request = new JsonObjectRequest(googlePlacesUrl.toString(),

                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject result) {

                        Log.i(TAG, "onResponse: Result= " + result.toString());
                        parseLocationResult(result);
                    }
                },
                new Response.ErrorListener() {
                    @Override                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: Error= " + error);
                        Log.e(TAG, "onErrorResponse: Error= " + error.getMessage());
                    }
                });

        AppController.getInstance().addToRequestQueue(request);
    }

    private void parseLocationResult(JSONObject result) {

        String id, place_id, placeName = null, reference, icon, vicinity = null;
        double latitude, longitude;

        try {
            JSONArray jsonArray = result.getJSONArray("results");

            if (result.getString(STATUS).equalsIgnoreCase(OK)) {

                //mMap.clear();

                    JSONObject place = jsonArray.getJSONObject(0);

                    id = place.getString(STATION_ID);
                    place_id = place.getString(PLACE_ID);
                    if (!place.isNull(NAME)) {
                        placeName = place.getString(NAME);
                    }
                    if (!place.isNull(VICINITY)) {
                        vicinity = place.getString(VICINITY);
                    }
                    latitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)

                            .getDouble(LATITUDE);
                    longitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)

                            .getDouble(LONGITUDE);
                    reference = place.getString(REFERENCE);
                    icon = place.getString(ICON);

                    /*MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(latitude, longitude);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);

                    mMap.addMarker(markerOptions);
                    */
                /*
                JSONArray contacts =  result.getJSONArray("results");
                String[] contactNames = new String[contacts.length()];
                for(int i = 0 ; i < contactNames.length; i++) {
                    contactNames[i] = contacts.getJSONObject(0).getString("lat");
                }*/
                //String coordonnates=jsonArray.getJSONObject(0).getString("lng").toString();
                //JSONObject jObj=jsonArray.getJSONObject("location");

                //JSONObject location=jObj.getJSONObject("geometry");
                //JSONObject jObja=jObj.getJSONObject("location");

                //JSONArray locationa=jObja.getJSONArray("location");
                //JSONArray jobj2 = location.getJSONArray(0);//).getJSONObject(0);
               // Toast.makeText(getBaseContext(), /*jsonArray.length() jsonArray.getJSONObject(0).get("lat").getDouble("lng")jsonArray.getJSONObject(0).getJSONArray("geometry").getJSONArray(0).getString(0)*/placeName+ " comme station de transport trouvé!",

                 //       Toast.LENGTH_LONG).show();
                myLat=latitude;
                myLng=longitude;
            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {
                //Toast.makeText(getBaseContext(), "Pas de station de transport trouvé!!!",
                myLat=0;
                myLng=0;

                //      Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {

            e.printStackTrace();
            Log.e(TAG, "parseLocationResult: Error=" + e.getMessage());
        }
    }

    @Override

    public void onLocationChanged(Location location) {
        double latitude;
        double longitude;
        if(myLat!=0 && myLng!=0)
        {
            latitude = myLat;//location.getLatitude();
            longitude = myLng;//location.getLongitude();
        }
        else
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        loadNearByPlaces(latitude, longitude);
    }

    @Override

    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override

    public void onProviderEnabled(String s) {
    }

    @Override

    public void onProviderDisabled(String s) {
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /*
    * Display All incidents
    */
    protected void getIncidents() {
        class GetIncidents extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MapsActivity.this, "Fetching Incidents", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showIncident();
            }


            @Override
            protected String doInBackground(Void... params) {
                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(Config.URL_GET_ALL_INCIDENTS);
                return jsonStr;
            }
        }
        GetIncidents gi = new GetIncidents();
        gi.execute();
    }

    protected void showIncident() {
        if (JSON_STRING != null) {
            try {
                JSONObject jsonObj = new JSONObject(JSON_STRING);

                // Getting JSON Array node
                JSONArray incidents = jsonObj.getJSONArray(Config.TAG_JSON_ARRAY);

                // looping through All Incidents
                for (int i = 0; i < incidents.length(); i++) {
                    JSONObject c = incidents.getJSONObject(i);
                    String title = c.getString(Config.TAG_INCIDENT_TITLE);
                    String incidentLatitude = c.getString(Config.TAG_INCIDENT_LATITUDE);
                    String incidentLongitude = c.getString(Config.TAG_INCIDENT_LONGITUDE);

                    //Place incident location marker
                    if(incidentLatitude != null || incidentLongitude != null){
                        LatLng latLng = new LatLng(parseDouble(incidentLatitude), parseDouble(incidentLongitude));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(title);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.warning));
                        mMap.addMarker(markerOptions);
                    }
                }
            } catch (final JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
    }
}