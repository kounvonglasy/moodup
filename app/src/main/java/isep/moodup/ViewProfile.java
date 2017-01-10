package isep.moodup;

import android.os.Bundle;
import android.widget.FrameLayout;

/**
 * Created by h on 10/01/2017.
 */

public class ViewProfile extends BaseActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_userprofile, contentFrameLayout);

    }


}
