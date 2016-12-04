package isep.moodup;

import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = ViewAllIncident.class.getSimpleName();
    private Integer index;

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

    private Button buttonAdd;
    private Button buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get user list
        MyTaskParams params = new MyTaskParams(Config.URL_GET_ALL_USERS, userList, idUserList);
        GetList getList = new GetList();
        getList.execute(params);
        //Get severite list
        params = new MyTaskParams(Config.URL_GET_ALL_SEVERITES, severiteList, idSeveriteList);
        getList = new GetList();
        getList.execute(params);
        //Get type list
        params = new MyTaskParams(Config.URL_GET_ALL_TYPES, typeList, idTypeList);
        getList = new GetList();
        getList.execute(params);

        //Set content view
        setContentView(R.layout.activity_main);

        //Initializing spinners and views
        setSpinner(R.id.editTextUser, userList);
        setSpinner(R.id.editTextSeverite,severiteList);
        setSpinner(R.id.editTextType, typeList);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonView = (Button) findViewById(R.id.buttonView);

        //Setting listeners to button
        buttonAdd.setOnClickListener(this);
        buttonView.setOnClickListener(this);
    }
    private void setSpinner(Integer id, final ArrayList<String> list){
        final Spinner spinner =(Spinner)findViewById(id);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,list);
        spinner.setAdapter(adapter);
        //Adding setOnItemSelectedListener method on spinner.
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(MainActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                spinnerContent = spinner.getSelectedItem().toString();
                index = list.indexOf(spinnerContent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }
    //Adding an incident
    private void addIncident() {
        final String title = editTextTitle.getText().toString().trim();
        final String description = editTextDescription.getText().toString().trim();
        final Integer user = idUserList.get(index);
        final Integer severite = idSeveriteList.get(index);
        final Integer type = idTypeList.get(index);

        class AddIncident extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Adding...", "Wait...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("description", description);
                params.put("idUser", Integer.toString(user));
                params.put("idSeverite", Integer.toString(severite));
                params.put("idType", Integer.toString(type));
                HttpHandler rh = new HttpHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_INCIDENT, params);
                return res;
            }
        }
        AddIncident ai = new AddIncident();
        ai.execute();
    }

    private static class MyTaskParams {
        String URL;
        ArrayList<String> list;
        ArrayList<Integer> idList;
        MyTaskParams(String URL, ArrayList<String> list, ArrayList<Integer> idList) {
            this.URL = URL;
            this.list = list;
            this.idList = idList;
        }
    }

    private class GetList extends AsyncTask<MyTaskParams, Void, Void> {
        @Override
        protected Void doInBackground(MyTaskParams... params) {
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

    @Override
    public void onClick(View v) {
        if(v == buttonAdd){
            addIncident();
        }
        if(v == buttonView){
            startActivity(new Intent(this,ViewAllIncident.class));
        }
    }
}
