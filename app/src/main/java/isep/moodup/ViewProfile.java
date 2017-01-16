package isep.moodup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ViewProfile extends BaseActivity implements View.OnClickListener {

    private EditText editTextName;
    private EditText editTextFirstName;
    private EditText editTextMail;
    private TextView textLogin;
    private String login;
    private String name;
    private String firstName;
    private String email;
    private String idUser;
    private Button buttonEditProfile;
    private SessionManager session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_userprofile, contentFrameLayout);

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        login = user.get(Config.KEY_USER_LOGIN);
        name = user.get(Config.KEY_USER_NAME);
        firstName = user.get(Config.KEY_USER_FIRSTNAME);
        email = user.get(Config.KEY_USER_EMAIL);
        idUser = user.get(Config.KEY_USER_ID);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextMail = (EditText) findViewById(R.id.editTextMail);
        textLogin = (TextView) findViewById(R.id.userLogin);

        buttonEditProfile = (Button) findViewById(R.id.editProfile);
        buttonEditProfile.setOnClickListener(this);

        editTextName.setText(name);
        editTextFirstName.setText(firstName);
        editTextMail.setText(email);
        textLogin.setText("Profil de " + login);

    }

    @Override
    public void onClick(View v) {
        if (v == buttonEditProfile) {
            name = editTextName.getText().toString().trim();
            firstName = editTextFirstName.getText().toString().trim();
            email = editTextMail.getText().toString().trim();
            UpdateProfile();
        }
    }

    //Adding a user
    private void UpdateProfile() {
        class UpdateUserTask extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewProfile.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equals("Profile updated succesfully.")) {
                    // Clear old Session
                    session.clearSession();
                    // Create new Session
                    session.createLoginSession(login, name, firstName, email, idUser);
                } else if (s.isEmpty()) {
                    Toast.makeText(ViewProfile.this, "No Internet connection.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ViewProfile.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_USER_NAME, name);
                params.put(Config.KEY_USER_FIRSTNAME, firstName);
                params.put(Config.KEY_USER_EMAIL, email);
                params.put(Config.KEY_USER_LOGIN, login);
                HttpHandler rh = new HttpHandler();
                String res = rh.sendPostRequest(Config.URL_UPDATE_USER, params);
                return res;
            }
        }
        UpdateUserTask uu = new UpdateUserTask();
        uu.execute();
    }

    @Override
    public void onBackPressed() {
        // Launched from notification, handle as special case
        Intent intent = new Intent(this, ViewAllIncident.class);
        this.startActivity(intent);
        finish();
    }

}


