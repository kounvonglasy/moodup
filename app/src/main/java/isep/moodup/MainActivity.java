package isep.moodup;

import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonView = (Button) findViewById(R.id.buttonView);
        buttonView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v == buttonView){
            startActivity(new Intent(this,ViewAllIncident.class));
        }
    }
}
