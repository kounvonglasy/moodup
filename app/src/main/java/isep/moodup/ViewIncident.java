package isep.moodup;

/**
 * Created by Kevin on 05/12/2016.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewIncident extends AppCompatActivity{
    private EditText editTextId;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextUser;
    private EditText editTextSeverite;
    private EditText editTextType;
    private EditText editTextCreationDate;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_incident);

        Intent intent = getIntent();

        id = intent.getStringExtra(Config.INCIDENT_ID);

        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextUser = (EditText) findViewById(R.id.editTextUser);
        editTextSeverite = (EditText) findViewById(R.id.editTextSeverite);
        editTextType = (EditText) findViewById(R.id.editTextType);
        editTextCreationDate = (EditText) findViewById(R.id.editTextCreationDate);
        editTextId.setText(id);

        getIncident();


    }

    private void getIncident(){
        class GetIncident extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewIncident.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showIncident(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                HttpHandler rh = new HttpHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_INCIDENT, id);
                return s;
            }
        }
        GetIncident ge = new GetIncident();
        ge.execute();
    }

    private void showIncident(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String title = c.getString(Config.TAG_INCIDENT_TITLE);
            String description = c.getString(Config.TAG_INCIDENT_DESCRIPTION);
            String user = c.getString(Config.TAG_INCIDENT_USER);
            String severite = c.getString(Config.TAG_INCIDENT_SEVERITE);
            String type = c.getString(Config.TAG_INCIDENT_TYPE);
            String creationDate = c.getString(Config.TAG_INCIDENT_CREATION_DATE);

            editTextTitle.setText(title);
            editTextDescription.setText(description);
            editTextUser.setText(user);
            editTextSeverite.setText(severite);
            editTextType.setText(type);
            editTextCreationDate.setText(creationDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateEmployee(){
        //Working in progress
    }
}
