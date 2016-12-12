package isep.moodup;

/**
 * Created by Kevin on 28/11/2016.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class ViewAllIncident extends AppCompatActivity {
    private IncidentListAdapter adapter;
    private ListView listView;
    private String TAG = ViewAllIncident.class.getSimpleName();
    ArrayList<HashMap<String, String>> incidentList;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_incident);
        incidentList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        Button btnReturnHome = new Button(this);
        btnReturnHome.setText("Retour à la page précédente");
        btnReturnHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReturnHome(v);
            }
        });
        setupListViewAdapter();
        listView.addFooterView(btnReturnHome);
        getIncidents();

    }

    private void showIncident() {
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
                    Incident incident = new Incident(id, description, title, creationDate);
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

    private void getIncidents() {
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

    public void ReturnHome(View view) {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void getIncidentOnClickHandler(View v) {
        Incident item = (Incident) v.getTag();
        Intent intent = new Intent(this, ViewIncident.class);
        String idIncident = item.getId();
        intent.putExtra(Config.INCIDENT_ID, idIncident);
        startActivity(intent);
    }

    public void addLikeOnClickHandler(View v) {
        //Working in Progress
        Toast.makeText(getApplicationContext(),
                "Incident liked",
                Toast.LENGTH_LONG)
                .show();
    }

    private void setupListViewAdapter() {
        adapter = new IncidentListAdapter(ViewAllIncident.this, R.layout.list_incident, new ArrayList<Incident>());
        ListView incidentListView = (ListView) findViewById(R.id.listView);
        incidentListView.setAdapter(adapter);
    }
}