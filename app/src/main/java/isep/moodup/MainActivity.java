package isep.moodup;

import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonAdd;
    private Button buttonView;
    private Button buttonMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set content view
        setContentView(R.layout.activity_main);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonView = (Button) findViewById(R.id.buttonView);
        buttonMap = (Button) findViewById(R.id.buttonMap);

        //Setting listeners to button
        buttonAdd.setOnClickListener(this);
        buttonView.setOnClickListener(this);
        buttonMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonAdd) {
            startActivity(new Intent(this, AddIncident.class));
        }
        else if (v == buttonView) {
            startActivity(new Intent(this, ViewAllIncident.class));
        }
        else if (v == buttonMap) {
            startActivity(new Intent(this, MapsActivity.class));
        }
    }
}
