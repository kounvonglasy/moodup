package isep.moodup;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.HashMap;


public class ViewIncidentsByCategory extends ViewIncidents {

    @Override
    protected void getIncidents() {
        class GetIncidents extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewIncidentsByCategory.this, "Fetching Data", "Wait...", false, false);
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
                Intent intent = getIntent();
                String idCategory = intent.getStringExtra(Config.INCIDENT_CATEGORY);
                // Making a request to url and getting response
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_INCIDENT_ID, idCategory);
                HttpHandler rh = new HttpHandler();
                String s = rh.sendPostRequest(Config.URL_GET_INCIDENTS_BY_CATEGORY, hashMap);
                return s;
            }
        }
        GetIncidents gi = new GetIncidents();
        gi.execute();
    }


}