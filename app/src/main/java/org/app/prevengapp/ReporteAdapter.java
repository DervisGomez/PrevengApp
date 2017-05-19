package org.app.prevengapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dervis on 25/01/17.
 */
public class ReporteAdapter extends BaseAdapter {

    private Context context;
    private List<String> items;

    public ReporteAdapter(Context context, List<String> items) {
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
            rowView = inflater.inflate(R.layout.adapter_reporte, parent, false);

        }
        TextView titu=(TextView)rowView.findViewById(R.id.tvTituloReporte);
        String item=items.get(position);
        titu.setText(item);

        // Set data into the view


        return rowView;
    }
}
