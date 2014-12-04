package org.esaip.projetandroidbbvp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class EnvoyerMessageActivity extends Activity implements EnvoyerMessageTask.OnTaskEvent {
    private Button envoie;
    private EditText message;
    private ProgressDialog progress;
    //parametres
    String user;
    String password;
    String preText ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //on recupere les parametres
        user = getIntent().getExtras().getString("user");
        password = getIntent().getExtras().getString("password");
        preText = getIntent().getExtras().getString("preText");
        setContentView(R.layout.activity_envoyer_message);
        message = (EditText) findViewById(R.id.texte_message);

        message.setText(preText);

        envoie = (Button) findViewById(R.id.btn_envoyer_msg);
        envoie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EnvoyerMessageTask(EnvoyerMessageActivity.this).execute(message.getText().toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accueil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.deconnexion) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Etes vous sûr de vouloir vous deconnecter?")
                    .setCancelable(false)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            EnvoyerMessageActivity.this.finish();
                            //on vide les shared preferences
                            SharedPreferences.Editor editor = getSharedPreferences("myAppPrefs", MODE_PRIVATE).edit();
                            editor.clear();
                            editor.commit();
                            Intent intent = new Intent(EnvoyerMessageActivity.this, ConnexionActivity.class);
                            //Permet de fermer les fenetres precedentes
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        if (id == R.id.quitter) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Etes vous sûr de vouloir quitter?")
                    .setCancelable(false)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EnvoyerMessageActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPreExecute() {
        progress = new ProgressDialog(EnvoyerMessageActivity.this);
        progress.setMessage("Envoie du message...");
        progress.show();
    }

    @Override
    public boolean onDoIn(String... params) {
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            URI uri = new URI("http","formation-android-esaip.herokuapp.com","/message/"+user+"/"+password+"/"+params[0],"");
            HttpGet request = new HttpGet(uri.toASCIIString());
            HttpResponse response = client.execute(request);
            String res  = InputStreamToString.convert(response.getEntity().getContent());
            if(res.equals(""))
                return true;
            else
                return false;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onFinish(Boolean result) {
        //On vérifie la connexion internet
        if(isOnline()){
            if(result) {
                Intent intent = new Intent(getApplicationContext(), ListerMessagesActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("password",password);
                intent.putExtra("preText",preText);
                //Permet de fermer les fenetres precedentes
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else
                Toast.makeText(getApplicationContext(),"Erreur ! Réessayer plus tard",Toast.LENGTH_SHORT);

            if (progress.isShowing()) {
                progress.dismiss();
            }
        }else {
                /*
                Permet d'appliquer un delai de 5s
                 */
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),R.string.noServer, Toast.LENGTH_SHORT).show();
                    EnvoyerMessageActivity.this.finish();
                }
            }, 5000);
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
