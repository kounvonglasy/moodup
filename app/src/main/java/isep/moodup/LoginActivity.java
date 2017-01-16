package isep.moodup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by h on 06/12/2016.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUserName;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        editTextUserName = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);

        buttonLogin = (Button) findViewById(R.id.buttonUserLogin);
        buttonRegister = (Button) findViewById(R.id.buttonUserRegistration);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }

    private void login() {
        session = new SessionManager(getApplicationContext());
        String login = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        userLogin(login, password);
    }

    private void userLogin(final String login, final String password) {
        class UserLoginClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {
                    createSession(login);
                } else if (s.isEmpty()){
                    Toast.makeText(LoginActivity.this, "No Internet connection.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("username", params[0]);
                data.put("password", params[1]);
                HttpHandler sh = new HttpHandler();
                String result = sh.sendPostRequest(Config.LOGIN_URL, data);
                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(login, password);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin) {
            login();
        } else if (v == buttonRegister) {
            Intent intent = new Intent(this, RegistrationUser.class);
            this.startActivity(intent);
        }
    }

    private void createSession(String login) {
        class GetProfile extends AsyncTask<Object, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(s);
                        // Getting JSON Array node
                        JSONArray profiles = jsonObj.getJSONArray(Config.TAG_JSON_ARRAY);
                        // looping through All Incidents
                        JSONObject c = profiles.getJSONObject(0);
                        String login = c.getString(Config.KEY_USER_LOGIN);
                        String name = c.getString(Config.KEY_USER_NAME);
                        String firstName = c.getString(Config.KEY_USER_FIRSTNAME);
                        String idUser = c.getString(Config.KEY_USER_ID);
                        String email = c.getString(Config.KEY_USER_EMAIL);
                        session.createLoginSession(login, name, firstName, email, idUser);
                        Intent intent = new Intent(LoginActivity.this, ViewAllIncident.class);
                        startActivity(intent);
                    } catch (final JSONException e) {
                        System.out.println("Json parsing error: " + e.getMessage());
                    }
                } else {
                    System.out.println("Couldn't get json from server. Check LogCat for possible errors!");
                }
            }

            @Override
            protected String doInBackground(Object... params) {
                HashMap<String, String> param = new HashMap<>();
                String login = (String) params[0];
                param.put(Config.KEY_USER_LOGIN, login);
                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                String jsonStr = sh.sendPostRequest(Config.URL_GET_PROFILE, param);
                return jsonStr;
            }
        }
        GetProfile gi = new GetProfile();
        gi.execute(login);
    }

}
