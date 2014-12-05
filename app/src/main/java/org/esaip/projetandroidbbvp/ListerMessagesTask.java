package org.esaip.projetandroidbbvp;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by Baptiste on 28/11/2014.
 */
class ListerMessagesTask extends AsyncTask<String, Void, ArrayList<Message> > {


    interface OnTaskEvent {
        public void onPreExecute();
        public ArrayList<Message> onDoIn(String... strings);
        public void onFinish(ArrayList<Message> result);

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
    protected ArrayList<Message> doInBackground(String... strings) {

        // Defined Array values to show in ListView
       return onTaskEvent.onDoIn(strings);

    }

    @Override
    protected void onPostExecute(ArrayList<Message> result) {
        super.onPostExecute(result);
        onTaskEvent.onFinish(result);

    }
}
