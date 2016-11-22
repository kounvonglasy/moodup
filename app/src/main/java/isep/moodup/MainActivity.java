package isep.moodup;

import android.os.AsyncTask;
import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.os.Bundle;

public class MainActivity extends Activity {

        private ListView liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        liste = (ListView) findViewById(R.id.liste); //liste in Java
    }

    public void routage(View view){
        AsyncTask reseau = new Reseau();
        reseau.execute(this,liste);

    }
}
