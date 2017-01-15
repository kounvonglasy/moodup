package isep.moodup;

/**
 * Created by Kevin on 05/12/2016.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import android.widget.Button;
import android.view.View;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Spinner;

public class ViewIncident extends AppCompatActivity implements View.OnClickListener {
    private String TAG = ViewIncident.class.getSimpleName();
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextDuration;

    private String id;
    private Button buttonUpdate;
    private Button buttonDelete;

    private String spinnerSeverite;
    private String spinnerType;

    //Defining lists
    private ArrayList<String> severiteList = new ArrayList<>();
    private ArrayList<String> typeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_incident);

        Intent intent = getIntent();

        id = intent.getStringExtra(Config.INCIDENT_ID);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextDuration = (EditText) findViewById(R.id.editTextDuration);


        //Get severite list
        ViewIncident.MyTaskParams params = new ViewIncident.MyTaskParams(Config.URL_GET_ALL_SEVERITES, severiteList, R.id.editSpinnerSeverite);
        new ViewIncident.GetList().execute(params);

        //Get type list
        params = new ViewIncident.MyTaskParams(Config.URL_GET_ALL_TYPES, typeList, R.id.editSpinnerType);
        new ViewIncident.GetList().execute(params);

        getIncident();

        buttonUpdate = (Button) findViewById(R.id.buttonUpdateIncident);
        buttonDelete = (Button) findViewById(R.id.buttonDeleteIncident);

        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

    }

    private void getIncident() {
        class GetIncident extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewIncident.this, "Fetching...", "Wait...", false, false);
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

    private void showIncident(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String title = c.getString(Config.TAG_INCIDENT_TITLE);
            String description = c.getString(Config.TAG_INCIDENT_DESCRIPTION);
            String duration = c.getString(Config.TAG_INCIDENT_DURATION);
            String severiteName = c.getString(Config.TAG_INCIDENT_SEVERITE);
            String typeName = c.getString(Config.TAG_INCIDENT_TYPE);

            editTextTitle.setText(title);
            editTextDescription.setText(description);
            editTextDuration.setText(duration);

            Spinner spinner = (Spinner) findViewById(R.id.editSpinnerSeverite);
            selectSpinnerValue(spinner, severiteName);
            spinner = (Spinner) findViewById(R.id.editSpinnerType);
            selectSpinnerValue(spinner, typeName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void selectSpinnerValue(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                spinner.setSelection(i);
                break;
            }
        }
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

    private class GetList extends AsyncTask<ViewIncident.MyTaskParams, Void, Wrapper> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            loading = ProgressDialog.show(ViewIncident.this, "Adding...", "Wait...", false, false);
        }

        @Override
        protected void onPostExecute(final Wrapper w) {
            loading.dismiss();
            final Spinner spinner = (Spinner) findViewById(w.id);
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<>(ViewIncident.this, android.R.layout.simple_list_item_1, w.list);
            spinner.setAdapter(adapter);
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
        protected Wrapper doInBackground(ViewIncident.MyTaskParams... params) {
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

    private void updateIncident() {
        final String title = editTextTitle.getText().toString().trim();
        final String description = editTextDescription.getText().toString().trim();
        final String duration = editTextDuration.getText().toString().trim();
        final String severiteName = spinnerSeverite;
        final String typeName = spinnerType;

        class UpdateIncident extends AsyncTask<Void, Void, String> {
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
                Toast.makeText(ViewIncident.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_INCIDENT_ID, id);
                hashMap.put(Config.KEY_INCIDENT_TITLE, title);
                hashMap.put(Config.KEY_INCIDENT_DESCRIPTION, description);
                hashMap.put(Config.KEY_INCIDENT_SEVERITE_NAME, severiteName);
                hashMap.put(Config.KEY_INCIDENT_TYPE_NAME, typeName);
                // Session class instance
                SessionManager session = new SessionManager(getApplicationContext());
                // get user data from session
                HashMap<String, String> user = session.getUserDetails();
                // name
                String userName = user.get(Config.KEY_USER_NAME);
                hashMap.put(Config.KEY_INCIDENT_USER_NAME, userName);
                hashMap.put(Config.KEY_INCIDENT_DURATION, duration);
                HttpHandler rh = new HttpHandler();
                String s = rh.sendPostRequest(Config.URL_UPDATE_INCIDENT, hashMap);
                return s;
            }
        }
        UpdateIncident ui = new UpdateIncident();
        ui.execute();
    }

    private void deleteIncident() {
        class DeleteIncident extends AsyncTask<Void, Void, String> {
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

    private void confirmDeleteIncident() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this incident?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteIncident();
                        startActivity(new Intent(ViewIncident.this, ViewAllIncident.class));
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

    public void ReturnHome(View view) {
        super.onBackPressed();
        startActivity(new Intent(this, ViewAllIncident.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonUpdate) {
            updateIncident();
        }
        if (v == buttonDelete) {
            confirmDeleteIncident();
        }
    }
}
