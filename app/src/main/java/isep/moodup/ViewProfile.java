package isep.moodup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import java.util.HashMap;

public class ViewProfile extends BaseActivity implements View.OnClickListener {

    private EditText editTextName;
    private EditText editTextFirstName;
    private EditText editTextMail;
    private EditText editTextLogin;
    private Button buttonEditProfile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_userprofile, contentFrameLayout);

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        String login = user.get(Config.KEY_USER_LOGIN);
        String name = user.get(Config.KEY_USER_NAME);
        String firstName = user.get(Config.KEY_USER_FIRSTNAME);
        String email =  user.get(Config.KEY_USER_EMAIL);

        editTextName =(EditText) findViewById(R.id.editTextName);
        editTextFirstName =(EditText) findViewById(R.id.editTextFirstName);
        editTextMail =(EditText) findViewById(R.id.editTextMail);
        editTextLogin =(EditText) findViewById(R.id.editTextLogin);

        buttonEditProfile = (Button) findViewById(R.id.editProfile);
        buttonEditProfile.setOnClickListener(this);


        editTextName.setText(name);
        editTextFirstName.setText(firstName);
        editTextMail.setText(email);
        editTextLogin.setText(login);

    }

    @Override
    public void onClick(View v) {
        if (v == buttonEditProfile) {
            UpdateProfile();
        }
    }

    //Adding a user
    private void UpdateProfile() {
        final String name = editTextName.getText().toString().trim();
        final String firstname = editTextFirstName.getText().toString().trim();
        final String login = editTextLogin.getText().toString().trim();
        final String mail = editTextMail.getText().toString().trim();

        class AddUserTask extends AsyncTask<Void, Void, String> {

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
                Toast.makeText(ViewProfile.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_USER_NAME, name);
                params.put(Config.KEY_USER_FIRSTNAME, firstname);
                params.put(Config.KEY_USER_EMAIL, mail);
                params.put(Config.KEY_USER_LOGIN, login);
                HttpHandler rh = new HttpHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_USER, params);
                return res;
            }
        }
        AddUserTask ai = new AddUserTask();
        ai.execute();
    }

    @Override
    public void onBackPressed() {
        // Launched from notification, handle as special case
        Intent intent = new Intent(this, ViewAllIncident.class);
        this.startActivity(intent);
        finish();

    }

}


