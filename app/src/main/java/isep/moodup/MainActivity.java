package isep.moodup;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private Toolbar mToolBar;

    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set content view
        setContentView(R.layout.activity_main);

        mToolBar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolBar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationView = (NavigationView) findViewById(R.id.id_nav_menu);
        if (mNavigationView != null){
            mNavigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else if (id ==  R.id.new_account) {
                Intent intent = new Intent(this, AddIncident.class);
                this.startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        Intent intent;
        switch(id){
            case R.id.add_incident:
                intent = new Intent(this, AddIncident.class);
                this.startActivity(intent);
                break;
            case R.id.view_map:
                intent = new Intent(this, MapsActivity.class);
                this.startActivity(intent);
                break;
            case R.id.view_incidents:
                intent = new Intent(this, ViewAllIncident.class);
                this.startActivity(intent);
                break;
            case R.id.add_user:
                intent = new Intent(this, RegistrationUser.class);
                this.startActivity(intent);
                break;

        }
        mDrawerLayout.closeDrawers();
        return true;
    }

}
