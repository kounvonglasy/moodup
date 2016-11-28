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


public class ViewAllIncident extends AppCompatActivity{

    private ListView listView;
    private ProgressDialog pDialog;
    private String TAG = ViewAllIncident.class.getSimpleName();
    private static String url = "http://10.0.2.2:8888/getAllIncidents";
    ArrayList<HashMap<String, String>> incidentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_incident);
        incidentList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        new GetIncidents().execute();
    }

    private class GetIncidents extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ViewAllIncident.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

            @Override
            protected Void doInBackground(Void... arg0) {
                HttpHandler sh = new HttpHandler();

                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(url);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONArray incidents = jsonObj.getJSONArray("result");

                        // looping through All Incidents
                        for (int i = 0; i < incidents.length(); i++) {
                            JSONObject c = incidents.getJSONObject(i);

                            String description = c.getString("description");
                            String title = c.getString("title");

                            HashMap<String, String> incident = new HashMap<>();

                            // adding each child node to HashMap key => value
                            incident.put("title", title);
                            incident.put("description", description);
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

                return null;
            }

            @Override
            protected void onPostExecute(Void result){
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    ViewAllIncident.this, incidentList,
                    R.layout.list_incident, new String[]{"title","description"}, new int[]{
                    R.id.title,R.id.description});
                listView.setAdapter(adapter);
        }
    }

}