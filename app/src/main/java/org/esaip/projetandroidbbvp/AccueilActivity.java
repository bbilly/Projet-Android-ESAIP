package org.esaip.projetandroidbbvp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
public class AccueilActivity extends Activity  {
    Button btn_lister = null;
    Button btn_envoyer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        //parametres
        final String user = getIntent().getExtras().getString("user");
        final String password = getIntent().getExtras().getString("password");

        //on recupere les boutons dans la vue grace à leurs ids
        btn_lister = (Button) findViewById(R.id.btn_lister);
        btn_envoyer = (Button) findViewById(R.id.btn_envoyer);

        //evenement lors du clic sur le bouton lister messages
        btn_lister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ListerMessagesActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("password",password);
                //Permet de fermer les fenetres precedentes
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        //evenemet lors du clic sur le bouton envoyer message
        btn_envoyer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), EnvoyerMessageActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("password",password);
                //Permet de fermer les fenetres precedentes
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

                            AccueilActivity.this.finish();
                            //on vide les shared preferences
                            SharedPreferences.Editor editor = getSharedPreferences("myAppPrefs", MODE_PRIVATE).edit();
                            editor.clear();
                            editor.commit();
                            Intent intent = new Intent(AccueilActivity.this, ConnexionActivity.class);
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
                            AccueilActivity.this.finish();
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

    /*
    Surcharge de la méthode onbackpressed pour permettre de quitter l'application en
    cliquant sur le bouton retour
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Etes vous sûr de vouloir quitter?")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AccueilActivity.this.finish();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}