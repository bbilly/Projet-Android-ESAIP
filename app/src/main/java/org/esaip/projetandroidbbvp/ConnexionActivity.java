package org.esaip.projetandroidbbvp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class ConnexionActivity extends Activity {

    EditText editTextUsername;
    EditText editTextPassword;
    TextView errorTextView;
    Button buttonClear;
    Button buttonValidate;
    ProgressBar progressBar;
    String connectValide="false";


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        /*
        Permet de garder l'affichage du message d'erreur lors du basculement de l'affichage
         */
        errorTextView = (TextView)findViewById(R.id.errorTextView);
        if (savedInstanceState.getInt("Error") == View.VISIBLE){
            errorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        //Permet de garder les "credentials" en mémoire
        SharedPreferences mPrefs = this.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
        Log.i("test", mPrefs.getAll().toString());
        if(mPrefs.getBoolean("connected",false)){

            editTextUsername = (EditText)findViewById(R.id.userNameEditText);
            editTextPassword = (EditText)findViewById(R.id.passwordEditText);
            editTextUsername.setVisibility(View.GONE);
            editTextPassword.setVisibility(View.GONE);
            LinearLayout linearLayoutConnexion = (LinearLayout)findViewById(R.id.LinearLayoutConnexion);
            linearLayoutConnexion.setVisibility(View.VISIBLE);
            ProgressBar progressBarConnexion = (ProgressBar)findViewById(R.id.progressBarConnexion);
            progressBarConnexion.setVisibility(View.VISIBLE);


            activityTask connection = new activityTask();



            editTextUsername.setText(mPrefs.getString("user","none").toString());
            editTextPassword.setText(mPrefs.getString("password","none").toString());

            connection.execute(editTextUsername.getText().toString(),editTextPassword.getText().toString());

        }else{

            buttonClear = (Button)findViewById(R.id.clearButton);
            buttonValidate = (Button)findViewById(R.id.validateButton);

            buttonClear.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    editTextUsername = (EditText)findViewById(R.id.userNameEditText);
                    editTextPassword = (EditText)findViewById(R.id.passwordEditText);

                    editTextUsername.setText("");
                    editTextPassword.setText("");
                }
            });

            buttonValidate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    editTextUsername = (EditText)findViewById(R.id.userNameEditText);
                    editTextPassword = (EditText)findViewById(R.id.passwordEditText);
                    errorTextView = (TextView)findViewById(R.id.errorTextView);

                    //Affiche un message d'erreur si les deux champs sont vides
                    if (editTextPassword.length() ==0 || editTextUsername.length() == 0){

                        errorTextView.setVisibility(View.VISIBLE);

                    }else{

                        activityTask connection = new activityTask();
                        connection.execute(editTextUsername.getText().toString(),editTextPassword.getText().toString());
                        errorTextView.setVisibility(View.INVISIBLE);

                    }
                }
            });
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_connexion, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.quitter) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /*
    L'activityTask permet de faire la connection au serveur en thread
     */
    class activityTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar)findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean retour = false;
            try {

                String param = "/connect/"+params[0]+"/"+params[1];

                URI uri = new URI("http","formation-android-esaip.herokuapp.com",param,"");

                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(uri.toASCIIString());
                HttpResponse response = client.execute(request);

                InputStream content = response.getEntity().getContent();
                connectValide = InputStreamToString.convert(content);

                retour = Boolean.parseBoolean(connectValide);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return retour;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressBar.setVisibility(View.INVISIBLE);
            if(result) {
                CheckBox checkBoxConnection = (CheckBox) findViewById(R.id.checkBoxConnected);

                if (checkBoxConnection.isChecked()) {

                    //Si la checkbox est checker, on met les "credentials" en sharedPreference
                    SharedPreferences.Editor editor = getSharedPreferences("myAppPrefs", MODE_PRIVATE).edit();
                    editor.putBoolean("connected", true);
                    editor.putString("user", editTextUsername.getText().toString());
                    editor.putString("password", editTextPassword.getText().toString());
                    editor.commit();
                }
                Intent intent = new Intent(ConnexionActivity.this, AccueilActivity.class);
                intent.putExtra("user", editTextUsername.getText().toString());
                startActivity(intent);
                ConnexionActivity.this.finish();

            }else{
                //On vérifie la connexion internet
                if(isOnline()){
                    errorTextView.setVisibility(View.VISIBLE);
                }else {
                /*
                Permet d'appliquer un delai de 5s
                 */
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Impossible d'accèder au serveur, réesayez plus tard.", Toast.LENGTH_SHORT).show();
                            ConnexionActivity.this.finish();
                        }
                    }, 5000);
                }
            }
        }

        /**
         * Permet de retourner l'etat de la connexion internet
         * @return true si la connexion existe
         */
        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
    }
}
