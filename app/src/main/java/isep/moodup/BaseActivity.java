package isep.moodup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private Toolbar mToolBar;

    private NavigationView mNavigationView;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Session class instance
        session = new SessionManager(getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String login = user.get(Config.KEY_USER_LOGIN);

        NavigationView navigationView = (NavigationView) findViewById(R.id.id_nav_menu);
        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.nav_header);
        nav_user.setText("Bienvenue " + login);

        mToolBar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolBar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationView = (NavigationView) findViewById(R.id.id_nav_menu);
        if (mNavigationView != null) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        Intent intent;
        switch (id) {
            case R.id.edit_profile:
                intent = new Intent(this, ViewProfile.class);
                this.startActivity(intent);
                break;
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
            case R.id.view_incidents_by_category:
                intent = new Intent(this, ViewCategories.class);
                this.startActivity(intent);
                break;
            case R.id.log_out:
                session.logoutUser();
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        mDrawerLayout.closeDrawers();
        return true;
    }

}