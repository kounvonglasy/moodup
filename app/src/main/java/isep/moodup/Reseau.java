package isep.moodup;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gilles on 19/10/16.
 */

public class Reseau extends AsyncTask<Object, Void, String>
{
    private Activity main;
    private ListView liste;
    private String serveur = "http://projets-tomcat.isep.fr:8080/appService";


    protected String doInBackground(Object... params)
    {
        this.main = (Activity) params[0]; //graphical interface
        this.liste = (ListView) params[1];
        String reponse = "";
        Log.w("Reseau", "vérification de la connectivité");
        ConnectivityManager connMgr = (ConnectivityManager) main.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            Log.w("Reseau", "connecté au réseau");
            URL url = null;
            try
            {
                url = new URL(serveur);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("ACTION", "ROUTAGE");
                connection.setRequestProperty("TEAM", "0001");
                connection.connect();
                int statut = connection.getResponseCode();
                Log.w("Reseau", "requete envoyee " + statut);

                String ligne = null;
                //Read as if it is a file
                //BufferReader directly transform the flow of data in String character
                BufferedReader inStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                //Always do it that way for a file
                while((ligne = inStream.readLine()) != null)
                {
                    reponse = reponse + ligne;
                }
                inStream.close();
                connection.disconnect();
                Log.w("Reseau", "Réponse reçue : " + reponse);

            }
            catch(Exception e) {
                reponse = "ERREUR : " + e.getMessage();
            }
        }
        else
        {
            reponse = "ERREUR : pas de réseau";
        }
        return reponse;
    }
    protected void onPostExecute(String result)
    {
        Log.w("Reseau", "resultat à afficher " + result);
        if (result.startsWith("ERREUR"))
        {
            Toast.makeText(main, result, Toast.LENGTH_LONG).show(); //Floating popup notification
        }
        else
        {
            String[] tableau = result.split(";");
            Log.w("Reseau", "nombre de lignes = " + tableau.length);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(main, android.R.layout.simple_list_item_1, tableau);// If a value change, it will change automatically in the user interface
            liste.setAdapter(adapter);
        }
    }
}