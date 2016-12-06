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
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import android.widget.Button;
import android.view.View;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

public class ViewIncident extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextId;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextUser;
    private EditText editTextSeverite;
    private EditText editTextType;
    private EditText editTextCreationDate;
    private String id;
    private Button buttonUpdate;
    private Button buttonDelete;

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

        buttonUpdate = (Button) findViewById(R.id.buttonUpdateIncident);
        buttonDelete = (Button) findViewById(R.id.buttonDeleteIncident);

        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);


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

    private void updateIncident(){
        final String title = editTextTitle.getText().toString().trim();
        final String description = editTextDescription.getText().toString().trim();
        final String severiteName = editTextSeverite.getText().toString().trim();
        final String typeName = editTextType.getText().toString().trim();
        final String userName = editTextUser.getText().toString().trim();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        final String creationDate = dateFormat.format(date);

        class UpdateIncident extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewIncident.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ViewIncident.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_INCIDENT_ID,id);
                hashMap.put(Config.KEY_INCIDENT_TITLE,title);
                hashMap.put(Config.KEY_INCIDENT_DESCRIPTION,description);
                hashMap.put(Config.KEY_INCIDENT_SEVERITE_NAME,severiteName);
                hashMap.put(Config.KEY_INCIDENT_TYPE_NAME,typeName);
                hashMap.put(Config.KEY_INCIDENT_USER_NAME,userName);
                hashMap.put(Config.KEY_INCIDENT_CREATION_DATE,creationDate);

                HttpHandler rh = new HttpHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_INCIDENT,hashMap);

                return s;
            }
        }

        UpdateIncident ui = new UpdateIncident();
        ui.execute();

    }

    private void deleteIncident(){
        class DeleteIncident extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewIncident.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ViewIncident.this, "Incident supprimé avec succès", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HttpHandler rh = new HttpHandler();
                String s = rh.sendGetRequestParam(Config.URL_DELETE_INCIDENT, id);
                return s;
            }
        }

        DeleteIncident di = new DeleteIncident();
        di.execute();
    }

    private void confirmDeleteIncident(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this incident?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteIncident();
                        startActivity(new Intent(ViewIncident.this,ViewAllIncident.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void ReturnHome(View view){
        super.onBackPressed();
        startActivity(new Intent(this,ViewAllIncident.class));
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            updateIncident();
        }

        if(v == buttonDelete){
            confirmDeleteIncident();
        }
    }
}
