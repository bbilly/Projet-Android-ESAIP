package org.esaip.projetandroidbbvp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Baptiste on 28/11/2014.
 */
class ListerMessagesTask extends AsyncTask<String, Void, ArrayList<String> > {


    interface OnTaskEvent {
        public void onPreExecute();
        public ArrayList<String> onDoIn(String... strings);
        public void onFinish(ArrayList<String> result);

    }

    OnTaskEvent onTaskEvent;

    ListerMessagesTask(OnTaskEvent onTaskEvent) {
        this.onTaskEvent = onTaskEvent;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onTaskEvent.onPreExecute();
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {

        // Defined Array values to show in ListView
       return onTaskEvent.onDoIn(strings);

    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        super.onPostExecute(result);
        onTaskEvent.onFinish(result);

    }
}
