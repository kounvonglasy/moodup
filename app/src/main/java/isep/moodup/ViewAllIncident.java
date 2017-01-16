package isep.moodup;

/**
 * Created by Kevin on 28/11/2016.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import android.view.View;
import android.content.Intent;

public class ViewAllIncident extends BaseActivity {
    protected IncidentListAdapter adapter;
    protected String TAG = ViewAllIncident.class.getSimpleName();
    protected ArrayList<HashMap<String, String>> incidentList;
    protected String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_view_incidents, contentFrameLayout);
        incidentList = new ArrayList<>();
        setupListViewAdapter();
        getIncidents();
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
                    String id = c.getString(Config.INCIDENT_ID);
                    String description = c.getString(Config.TAG_INCIDENT_DESCRIPTION);
                    String title = c.getString(Config.TAG_INCIDENT_TITLE);
                    String creationDate = c.getString(Config.TAG_INCIDENT_CREATION_DATE);
                    String userLogin = c.getString(Config.TAG_INCIDENT_USER_LOGIN);
                    String duration = c.getString(Config.TAG_INCIDENT_DURATION);
                    String nbLike = c.getString(Config.TAG_INCIDENT_NB_LIKE);
                    Incident incident = new Incident(id, title, description, creationDate, duration, userLogin, nbLike);
                    adapter.add(incident);
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

    protected void getIncidents() {
        class GetIncidents extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewAllIncident.this, "Fetching Data", "Wait...", false, false);
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

    public void getIncidentOnClickHandler(View v) {
        Incident item = (Incident) v.getTag();
        Intent intent = new Intent(this, ViewIncident.class);
        String idIncident = item.getId();
        intent.putExtra(Config.INCIDENT_ID, idIncident);
        startActivity(intent);
    }

    public void addLikeOnClickHandler(View v) {
        //Working in Progress => need to manage user session
        Incident item = (Incident) v.getTag();
        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        final String idUser = user.get(Config.KEY_USER_ID);
        addLike(item, idUser);
    }

    private void addLike(final Incident incidentParam, final String idUserParam) {
        class AddLikeTask extends AsyncTask<Void, Void, String> {
            String idIncident = incidentParam.getId();
            String idUser = idUserParam;
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewAllIncident.this, "Liking...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ViewAllIncident.this, s, Toast.LENGTH_LONG).show();
                Incident incident = incidentParam;
                incident.setNbLike(s.substring(s.lastIndexOf(":") + 1));
                adapter.remove(incidentParam);
                adapter.add(incidentParam);
                adapter.notifyDataSetChanged();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_INCIDENT_ID_USER, idUser);
                params.put(Config.KEY_INCIDENT_ID, idIncident);
                HttpHandler rh = new HttpHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_LIKE, params);
                return res;
            }
        }

        AddLikeTask al = new AddLikeTask();
        al.execute();
    }

    protected void setupListViewAdapter() {
        adapter = new IncidentListAdapter(ViewAllIncident.this, R.layout.list_incident, new ArrayList<Incident>());
        ListView incidentListView = (ListView) findViewById(R.id.listView);
        incidentListView.setAdapter(adapter);
    }
}