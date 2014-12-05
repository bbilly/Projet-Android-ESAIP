package org.esaip.projetandroidbbvp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Baptiste on 04/12/2014.
 */
public class ListAdapter extends ArrayAdapter<Message> {

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, ArrayList<Message> lesmessages) {
        super(context, resource, lesmessages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_messages, null);

        }

        Message p = getItem(position);

        if (p != null) {

            TextView tt = (TextView) v.findViewById(R.id.message_id);
            TextView tt1 = (TextView) v.findViewById(R.id.auteur_id);

            if (tt != null) {
                tt.setText(p.getContenu_message());
            }
            if (tt1 != null) {

                tt1.setText(p.getAuteur());
            }
        }

        return v;

    }
}