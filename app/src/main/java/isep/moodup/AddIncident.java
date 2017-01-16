package isep.moodup;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.location.Location;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static isep.moodup.Config.GEOMETRY;
import static isep.moodup.Config.GOOGLE_BROWSER_API_KEY;
import static isep.moodup.Config.LATITUDE;
import static isep.moodup.Config.LOCATION;
import static isep.moodup.Config.LONGITUDE;
import static isep.moodup.Config.OK;
import static isep.moodup.Config.PROXIMITY_RADIUS;
import static isep.moodup.Config.STATUS;
import static isep.moodup.Config.ZERO_RESULTS;


public class AddIncident extends BaseActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private String TAG = ViewIncidents.class.getSimpleName();

    //Defining lists
    private ArrayList<String> severiteList = new ArrayList<>();
    private ArrayList<String> typeList = new ArrayList<>();

    //Defining views
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextDuration;

    private String spinnerSeverite;
    private String spinnerType;
    private Button buttonAddIncident;

    //Defining Google Maps data
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Double userLatitude = null;
    Double userLongitude = null;
    Double nearPlaceLatitude = null;
    Double nearPlaceLongitude = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.add_incident, contentFrameLayout);

        //Get severite list
        MyTaskParams params = new MyTaskParams(Config.URL_GET_ALL_SEVERITES, severiteList, R.id.editSpinnerSeverite);
        new GetList().execute(params);

        //Get type list
        params = new MyTaskParams(Config.URL_GET_ALL_TYPES, typeList, R.id.editSpinnerType);
        new GetList().execute(params);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextDuration = (EditText) findViewById(R.id.editTextDuration);

        buttonAddIncident = (Button) findViewById(R.id.buttonAddIncident);
        //Setting listeners to button
        buttonAddIncident.setOnClickListener(this);

        /*Google Map Permission
        * Initialize Google Play Services
        */
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
            }
        } else {
            buildGoogleApiClient();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonAddIncident) {
            addIncident();
        }
    }

    //Adding an incident
    public void addIncident() {
        final String title = editTextTitle.getText().toString().trim();
        final String description = editTextDescription.getText().toString().trim();
        final String duration = editTextDuration.getText().toString().trim();
        final String severite = spinnerSeverite;
        final String type = spinnerType;

        class AddIncidentTask extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddIncident.this, "Adding...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(AddIncident.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                if (userLatitude != null || userLongitude != null) {
                    String latitude = userLatitude.toString().trim();
                    String longitude = userLongitude.toString().trim();
                    if (nearPlaceLatitude != null || nearPlaceLongitude != null) {
                        latitude = nearPlaceLatitude.toString().trim();
                        longitude = nearPlaceLongitude.toString().trim();
                    }
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Config.KEY_INCIDENT_TITLE, title);
                    params.put(Config.KEY_INCIDENT_DESCRIPTION, description);
                    params.put(Config.KEY_INCIDENT_DURATION, duration);
                    params.put(Config.KEY_INCIDENT_LATITUDE, latitude);
                    params.put(Config.KEY_INCIDENT_LONGITUDE, longitude);
                    // Session class instance
                    SessionManager session = new SessionManager(getApplicationContext());
                    // get user data from session
                    HashMap<String, String> user = session.getUserDetails();
                    // name
                    String userName = user.get(Config.KEY_USER_NAME);
                    params.put(Config.KEY_INCIDENT_USER_NAME, userName);
                    params.put(Config.KEY_INCIDENT_SEVERITE_NAME, severite);
                    params.put(Config.KEY_INCIDENT_TYPE_NAME, type);
                    HttpHandler rh = new HttpHandler();
                    String res = rh.sendPostRequest(Config.URL_ADD_INCIDENT, params);
                    return res;
                } else {
                    return "Your location is not available";
                }
            }
        }

        AddIncidentTask ai = new AddIncidentTask();
        ai.execute();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        setUserLatitude(location);
        setUserLongitude(location);

        loadNearByPlaces(userLatitude, userLongitude);
    }

    private static class MyTaskParams {
        String URL;
        ArrayList<String> list;
        Integer id;

        MyTaskParams(String URL, ArrayList<String> list, Integer id) {
            this.URL = URL;
            this.list = list;
            this.id = id;
        }
    }

    private class Wrapper {
        public ArrayList<String> list;
        public Integer id;
    }

    private class GetList extends AsyncTask<MyTaskParams, Void, Wrapper> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            loading = ProgressDialog.show(AddIncident.this, "Adding...", "Wait...", false, false);
        }

        @Override
        protected void onPostExecute(final Wrapper w) {
            loading.dismiss();
            final Spinner spinner = (Spinner) findViewById(w.id);
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<>(AddIncident.this, android.R.layout.simple_list_item_1, w.list);
            spinner.setAdapter(adapter);
            //Adding setOnItemSelectedListener method on spinner.
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    Spinner spinner = (Spinner) parent;
                    if (spinner.getId() == R.id.editSpinnerSeverite) {
                        spinnerSeverite = spinner.getSelectedItem().toString();
                    } else if (spinner.getId() == R.id.editSpinnerType) {
                        spinnerType = spinner.getSelectedItem().toString();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
        }

        @Override
        protected Wrapper doInBackground(MyTaskParams... params) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(params[0].URL);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray results = jsonObj.getJSONArray("result");
                    // looping through all results
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject c = results.getJSONObject(i);
                        String name = c.getString("name");
                        params[0].list.add(name);
                    }
                    Wrapper w = new Wrapper();
                    w.list = params[0].list;
                    w.id = params[0].id;
                    return w;
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
            return null;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public double getUserLatitude() {
        return this.userLatitude;
    }

    public double getUserLongitude() {
        return this.userLongitude;
    }

    public void setUserLatitude(Location location) {
        this.userLatitude = location.getLatitude();
    }

    public void setUserLongitude(Location location) {
        this.userLongitude = location.getLongitude();
    }

    /*
    * Requesting Location Permission
    */
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    /*
    * Handling permission request response
    */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                    }
                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    /*
    * Find the location of the nearest Station
    */
    private void loadNearByPlaces(double latitude, double longitude) {
//YOU Can change this type at your own will, e.g hospital, cafe, restaurant.... and see how it all works

        String type = "bus_station|train_station|subway_station|transit_station|airport";
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
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: Error= " + error);
                        Log.e(TAG, "onErrorResponse: Error= " + error.getMessage());
                    }
                });

        AppController.getInstance().addToRequestQueue(request);
    }

    private void parseLocationResult(JSONObject result) {
        double latitude, longitude;
        try {
            JSONArray jsonArray = result.getJSONArray("results");

            if (result.getString(STATUS).equalsIgnoreCase(OK)) {
                JSONObject place = jsonArray.getJSONObject(0);
                latitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                        .getDouble(LATITUDE);
                longitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)

                        .getDouble(LONGITUDE);

                nearPlaceLatitude = latitude;
                nearPlaceLongitude = longitude;
            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {
                nearPlaceLatitude = null;
                nearPlaceLongitude = null;
            }

        } catch (JSONException e) {

            e.printStackTrace();
            Log.e(TAG, "parseLocationResult: Error=" + e.getMessage());
        }
    }

}
