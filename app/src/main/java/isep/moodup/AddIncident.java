package isep.moodup;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Kevin on 04/12/2016.
 */

public class AddIncident  extends AppCompatActivity implements View.OnClickListener {
    private String TAG = ViewAllIncident.class.getSimpleName();
    //Defining indexes
    private Integer indexUser;
    private Integer indexSeverite;
    private Integer indexType;

    //Defining lists
    private ArrayList<String> userList = new ArrayList<>();
    private ArrayList<Integer> idUserList = new ArrayList<>();
    private ArrayList<String> severiteList = new ArrayList<>();
    private ArrayList<Integer> idSeveriteList = new ArrayList<>();
    private ArrayList<String> typeList = new ArrayList<>();
    private ArrayList<Integer> idTypeList = new ArrayList<>();

    //Defining views
    private EditText editTextTitle;
    private EditText editTextDescription;
    private String spinnerContent;

    private Button buttonAddIncident;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_incident);

        //Get user list
        MyTaskParams params = new MyTaskParams(Config.URL_GET_ALL_USERS, userList, idUserList,R.id.editTextUser);
        new GetList().execute(params);

        //Get severite list
        params = new MyTaskParams(Config.URL_GET_ALL_SEVERITES, severiteList, idSeveriteList,R.id.editTextSeverite);
        new GetList().execute(params);

        //Get type list
        params = new MyTaskParams(Config.URL_GET_ALL_TYPES, typeList, idTypeList , R.id.editTextType);
        new GetList().execute(params);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        buttonAddIncident = (Button) findViewById(R.id.buttonAddIncident);
        //Setting listeners to button
        buttonAddIncident.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonAddIncident){
            addIncident();
        }
    }

    //Adding an incident
    private void addIncident() {
        final String title = editTextTitle.getText().toString().trim();
        final String description = editTextDescription.getText().toString().trim();
        final Integer user = idUserList.get(indexUser);
        final Integer severite = idSeveriteList.get(indexSeverite);
        final Integer type = idTypeList.get(indexType);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        final String creationDate = dateFormat.format(date);

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
                HashMap<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("description", description);
                params.put("idUser", Integer.toString(user));
                params.put("idSeverite", Integer.toString(severite));
                params.put("idType", Integer.toString(type));
                params.put("creationDate", creationDate);
                HttpHandler rh = new HttpHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_INCIDENT, params);
                return res;
            }
        }

        AddIncidentTask ai = new AddIncidentTask();
        ai.execute();
    }

    private static class MyTaskParams {
        String URL;
        ArrayList<String> list;
        ArrayList<Integer> idList;
        Integer id;
        MyTaskParams(String URL, ArrayList<String> list, ArrayList<Integer> idList,Integer id) {
            this.URL = URL;
            this.list = list;
            this.idList = idList;
            this.id = id;
        }
    }
    public class Wrapper
    {
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
            final Spinner spinner =(Spinner)findViewById(w.id);
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<>(AddIncident.this, android.R.layout.simple_list_item_1,w.list);
            spinner.setAdapter(adapter);
            //Adding setOnItemSelectedListener method on spinner.
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    Spinner spinner = (Spinner) parent;
                    if(spinner.getId() == R.id.editTextUser)
                    {
                        spinnerContent = spinner.getSelectedItem().toString();
                        indexUser = userList.indexOf(spinnerContent);
                    }
                    else if(spinner.getId() == R.id.editTextSeverite)
                    {
                        spinnerContent = spinner.getSelectedItem().toString();
                        indexSeverite = severiteList.indexOf(spinnerContent);
                    }
                    else if(spinner.getId() == R.id.editTextType){
                        spinnerContent = spinner.getSelectedItem().toString();
                        indexType = typeList.indexOf(spinnerContent);
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
                        String id = c.getString("id");
                        params[0].list.add(name);
                        params[0].idList.add(Integer.parseInt(id));
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

}
