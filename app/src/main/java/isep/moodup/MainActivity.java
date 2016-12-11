package isep.moodup;

import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonAddIncident;
    private Button buttonView;
    private Button buttonMap;
    private Button buttonAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set content view
        setContentView(R.layout.activity_main);

        buttonAddIncident = (Button) findViewById(R.id.buttonAddIncident);
        buttonView = (Button) findViewById(R.id.buttonView);
        buttonMap = (Button) findViewById(R.id.buttonMap);
        buttonAddUser = (Button) findViewById(R.id.buttonAddUser);

        //Setting listeners to button
        buttonAddIncident.setOnClickListener(this);
        buttonView.setOnClickListener(this);
        buttonMap.setOnClickListener(this);
        buttonAddUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonAddIncident) {
            startActivity(new Intent(this, AddIncident.class));
        }
        else if (v == buttonView) {
            startActivity(new Intent(this, ViewAllIncident.class));
        }
        else if (v == buttonMap) {
            startActivity(new Intent(this, MapsActivity.class));
        }
        else if (v == buttonAddUser) {
            startActivity(new Intent(this, RegistrationUser.class));
        }
    }
}
