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

import java.util.HashMap;


public class RegistrationUser extends AppCompatActivity implements View.OnClickListener {

    //Defining views
    private EditText editTextName;
    private EditText editTextFirstname;
    private EditText editTextEmail;
    private EditText editTextLogin;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonAddProfile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextFirstname = (EditText) findViewById(R.id.editTextFirstname);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextLogin = (EditText) findViewById(R.id.editTextLogin);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);

        buttonAddProfile = (Button) findViewById(R.id.buttonAddProfile);
        //Setting listeners to button
        buttonAddProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonAddProfile) {
            addProfile();
        }
    }

    //Adding a user
    private void addProfile() {
        final String name = editTextName.getText().toString().trim();
        final String firstname = editTextFirstname.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String login = editTextLogin.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String confirmpassword = editTextConfirmPassword.getText().toString().trim();

        class AddUserTask extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegistrationUser.this, "Adding...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.isEmpty()) {
                    Toast.makeText(RegistrationUser.this, "No Internet connection.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegistrationUser.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_USER_NAME, name);
                params.put(Config.KEY_USER_FIRSTNAME, firstname);
                params.put(Config.KEY_USER_EMAIL, email);
                params.put(Config.KEY_USER_LOGIN, login);
                params.put(Config.KEY_USER_PASSWORD, password);
                params.put(Config.KEY_USER_PASSWORD_CONFIRM, confirmpassword);
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
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
        finish();

    }
}
