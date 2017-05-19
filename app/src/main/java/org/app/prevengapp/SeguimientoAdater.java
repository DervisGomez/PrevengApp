package org.app.prevengapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dervis on 25/01/17.
 */
public class SeguimientoAdater extends BaseAdapter {

    private Context context;
    private List<String[]> items;

    public SeguimientoAdater(Context context, List<String[]> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.adapter_seguimiento, parent, false);
            TextView titulo=(TextView) rowView.findViewById(R.id.Nota);
            TextView fecha=(TextView) rowView.findViewById(R.id.tvFechaSeguimiento);
            TextView usuario=(TextView) rowView.findViewById(R.id.tvEscritorSeguimiento);
            LinearLayout seguimiento=(LinearLayout) rowView.findViewById(R.id.llSeguimiento);
            titulo.setText(items.get(position)[2]+": "+items.get(position)[1]);
            fecha.setText(items.get(position)[3]);
            titulo.setBackgroundColor(Color.parseColor(items.get(position)[4]));
            seguimiento.setBackgroundColor(Color.parseColor(items.get(position)[4]));
        }

        // Set data into the view


        return rowView;
    }
}
