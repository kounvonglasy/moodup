package isep.moodup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * Created by Kevin on 15/01/2017.
 */

public class ViewCategories extends BaseActivity implements View.OnClickListener  {
    private Button buttonCategoryTC;
    private Button buttonCategoryTR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_view_categories, contentFrameLayout);

        buttonCategoryTC = (Button) findViewById(R.id.buttonCategoryTC);
        buttonCategoryTR = (Button) findViewById(R.id.buttonCategoryTR);

        buttonCategoryTC.setOnClickListener(this);
        buttonCategoryTR.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ViewIncidentsByCategory.class);
        if (v == buttonCategoryTC) {
            intent.putExtra(Config.INCIDENT_CATEGORY, "1");
        }
        if (v == buttonCategoryTR) {
            intent.putExtra(Config.INCIDENT_CATEGORY, "2");
        }
        startActivity(intent);
    }
}
