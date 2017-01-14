package isep.moodup;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.HashMap;

/**
 * Created by h on 10/01/2017.
 */

public class ViewProfile extends BaseActivity {

    private EditText editTextName;
    private EditText editTextFirstName;
    private EditText editTextMail;
    private EditText editTextLogin;

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

        editTextName.setText(name);
        editTextFirstName.setText(firstName);
        editTextMail.setText(email);
        editTextLogin.setText(login);

    }

}


