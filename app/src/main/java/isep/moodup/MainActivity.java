package isep.moodup;

import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.widget.Toast;
import java.util.HashMap;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Defining views
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextIdUser;
    private EditText editTextIdSeverite;
    private EditText editTextIdType;

    private Button buttonAdd;
    private Button buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing views
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextIdUser = (EditText) findViewById(R.id.editTextIdUser);
        editTextIdSeverite = (EditText) findViewById(R.id.editTextIdSeverite);
        editTextIdType = (EditText) findViewById(R.id.editTextIdType);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonView = (Button) findViewById(R.id.buttonView);
        //Setting listeners to button
        buttonAdd.setOnClickListener(this);
        buttonView.setOnClickListener(this);
    }

    //Adding an incident
    private void addIncident() {

        final String title = editTextTitle.getText().toString().trim();
        final String description = editTextDescription.getText().toString().trim();
        final String idUser = editTextIdUser.getText().toString().trim();
        final String idSeverite = editTextIdSeverite.getText().toString().trim();
        final String idType = editTextIdType.getText().toString().trim();

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
                params.put("idUser", idUser);
                params.put("idSeverite", idSeverite);
                params.put("idType", idType);

                HttpHandler rh = new HttpHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_INCIDENT, params);
                return res;
            }
        }

        AddIncident ai = new AddIncident();
        ai.execute();
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
