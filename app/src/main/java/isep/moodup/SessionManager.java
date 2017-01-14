package isep.moodup;

/**
 * Created by Kevin on 09/01/2017.
 */

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "MyPrefs";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String login, String name, String firstName, String email, String idUser) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        //Function getUserDetails
        // Storing fields in pref
        editor.putString(Config.KEY_USER_LOGIN, login);
        editor.putString(Config.KEY_USER_NAME, name);
        editor.putString(Config.KEY_USER_FIRSTNAME, firstName);
        editor.putString(Config.KEY_USER_EMAIL, email);
        editor.putString(Config.KEY_USER_ID, idUser);
        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(Config.KEY_USER_LOGIN, pref.getString(Config.KEY_USER_LOGIN, null));
        user.put(Config.KEY_USER_ID, pref.getString(Config.KEY_USER_ID, null));
        user.put(Config.KEY_USER_NAME, pref.getString(Config.KEY_USER_NAME, null));
        user.put(Config.KEY_USER_FIRSTNAME, pref.getString(Config.KEY_USER_FIRSTNAME, null));
        user.put(Config.KEY_USER_EMAIL, pref.getString(Config.KEY_USER_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        clearSession();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public void clearSession() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }
}