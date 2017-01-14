package isep.moodup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.HashMap;

/**
 * Created by h on 10/01/2017.
 */

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
            Intent intent = new Intent(this, ViewProfile.class);
            this.startActivity(intent);
        }
    }

    private void EditProfile(){

    }

    @Override
    public void onBackPressed() {
        // Launched from notification, handle as special case
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
        finish();

    }

}


