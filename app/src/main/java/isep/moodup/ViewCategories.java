package isep.moodup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;


public class ViewCategories extends BaseActivity implements View.OnClickListener  {
    private Button buttonTaxi;
    private Button buttonMetro;
    private Button buttonRER;
    private Button buttonBUS;
    private Button buttonTGV;
    private Button buttonTRAM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_view_categories, contentFrameLayout);

        buttonTaxi = (Button) findViewById(R.id.buttonTaxi);
        buttonMetro = (Button) findViewById(R.id.buttonMetro);
        buttonRER = (Button) findViewById(R.id.buttonRER);
        buttonBUS = (Button) findViewById(R.id.buttonBUS);
        buttonTGV = (Button) findViewById(R.id.buttonTGV);
        buttonTRAM = (Button) findViewById(R.id.buttonTRAM);

        buttonTaxi.setOnClickListener(this);
        buttonMetro.setOnClickListener(this);
        buttonRER.setOnClickListener(this);
        buttonBUS.setOnClickListener(this);
        buttonTGV.setOnClickListener(this);
        buttonTRAM.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ViewIncidentsByCategory.class);
        if (v == buttonTaxi) {
            intent.putExtra(Config.INCIDENT_CATEGORY, "1");
        }
        if (v == buttonMetro) {
            intent.putExtra(Config.INCIDENT_CATEGORY, "2");
        }
        if (v == buttonRER) {
            intent.putExtra(Config.INCIDENT_CATEGORY, "3");
        }
        if (v == buttonBUS) {
            intent.putExtra(Config.INCIDENT_CATEGORY, "4");
        }
        if (v == buttonTGV) {
            intent.putExtra(Config.INCIDENT_CATEGORY, "5");
        }
        if (v == buttonTRAM) {
            intent.putExtra(Config.INCIDENT_CATEGORY, "6");
        }
        startActivity(intent);

    }
}
