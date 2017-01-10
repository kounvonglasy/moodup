package isep.moodup;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by h on 10/01/2017.
 */

public class ViewProfile extends BaseActivity {
    private String JSON_STRING;
    ArrayAdapter<String> adapter;
    private String TAG = ViewProfile.class.getSimpleName();
    private EditText editTextName;
    private EditText editTextFirstName;
    private EditText editTextMail;
    private EditText editTextLogin;
    private String login;
  //  private EditText editTextPassword;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // setContentView(R.layout.activity_main);
        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        login = user.get(SessionManager.KEY_NAME);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_userprofile, contentFrameLayout);


        editTextName =(EditText) findViewById(R.id.editTextName);
        editTextFirstName =(EditText) findViewById(R.id.editTextFirstName);
        editTextMail =(EditText) findViewById(R.id.editTextMail);
        editTextLogin =(EditText) findViewById(R.id.editTextLogin);
        //editTextPassword=(EditText) findViewById(R.id.editTextPassword);
        getProfile();

    }
    private void getProfile() {
        class GetProfiles extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewProfile.this, "Fetching Data", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showProfile();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> param = new HashMap<>();
                param.put(Config.KEY_USERNAME_LOGIN,login);
                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                String jsonStr = sh.sendPostRequest(Config.URL_GET_PROFILE,param);
                return jsonStr;
            }
        }
        GetProfiles gi = new GetProfiles();
        gi.execute();
    }

    private void showProfile() {
        if (JSON_STRING != null) {
            try {
                JSONObject jsonObj = new JSONObject(JSON_STRING);

                // Getting JSON Array node
                JSONArray profiles = jsonObj.getJSONArray(Config.TAG_JSON_ARRAY);

                // looping through All Incidents
                for (int i = 0; i < profiles.length(); i++) {
                    JSONObject c = profiles.getJSONObject(i);
                    String name = c.getString(Config.KEY_USERNAME_NAME);
                    String firstname = c.getString(Config.KEY_USERNAME_FIRSTNAME);
                    String mail = c.getString(Config.KEY_USERNAME_EMAIL);
                    String login = c.getString(Config.KEY_USERNAME_LOGIN);
                    String password = c.getString(Config.KEY_USERNAME_PASSWORD);
                    editTextName.setText(name);
                    editTextFirstName.setText(firstname);
                    editTextMail.setText(mail);
                    editTextLogin.setText(login);
                   // editTextPassword.setText(password);
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

}
