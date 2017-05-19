package org.app.prevengapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dervis on 25/01/17.
 */
public class MisReportesAdater extends BaseAdapter {

    private Context context;
    private List<String[]> items;

    public MisReportesAdater(Context context, List<String[]> items) {
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
            rowView = inflater.inflate(R.layout.adapter_mis_reportes, parent, false);

        }

        TextView titulo=(TextView) rowView.findViewById(R.id.TituloMisReporte);
        TextView estado=(TextView) rowView.findViewById(R.id.EstadoMisReporte);
        TextView fecha=(TextView)rowView.findViewById(R.id.tvFechaR);
        TextView id=(TextView)rowView.findViewById(R.id.tvNumero);
        id.setText(items.get(position)[0]);
        titulo.setText(items.get(position)[5]);
        estado.setText(items.get(position)[4]);
        fecha.setText(items.get(position)[6].substring(0,10));

        // Set data into the view


        return rowView;
    }
}
