package isep.moodup;

/**
 * Created by Kevin on 28/11/2016.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.content.Intent;

public class ViewAllIncident extends AppCompatActivity implements ListView.OnItemClickListener  {

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
        listView.setOnItemClickListener(this);
        getIncidents();
    }

    private void showIncident(){
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

                    HashMap<String, String> incident = new HashMap<>();

                    // adding each child node to HashMap key => value
                    incident.put(Config.TAG_INCIDENT_ID,id);
                    incident.put(Config.TAG_INCIDENT_TITLE, title);
                    incident.put(Config.TAG_INCIDENT_DESCRIPTION, description);
                    // adding incident to incident list
                    incidentList.add(incident);
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
        ListAdapter adapter = new SimpleAdapter(
                ViewAllIncident.this, incidentList, R.layout.list_incident,
                new String[]{Config.TAG_INCIDENT_ID,Config.TAG_INCIDENT_TITLE,Config.TAG_INCIDENT_DESCRIPTION},
                new int[]{R.id.id, R.id.title, R.id.description});
        listView.setAdapter(adapter);

    }
    private void getIncidents(){
       class GetIncidents extends AsyncTask<Void, Void, String> {

           ProgressDialog loading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(ViewAllIncident.this,"Fetching Data","Wait...",false,false);
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
        gi.execute();}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ViewIncident.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String idIncident = map.get(Config.TAG_INCIDENT_ID).toString();
        intent.putExtra(Config.INCIDENT_ID,idIncident);
        startActivity(intent);
    }

}