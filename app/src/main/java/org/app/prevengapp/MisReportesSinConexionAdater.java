package org.app.prevengapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dervis on 25/01/17.
 */
public class MisReportesSinConexionAdater extends BaseAdapter {

    private ListaSinConexionActivity context;
    private List<String[]> items;

    public MisReportesSinConexionAdater(ListaSinConexionActivity context, List<String[]> items) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.adapter_reportes_sin_conexion, parent, false);

        }

        TextView titulo=(TextView) rowView.findViewById(R.id.TituloMisReporte2);
        TextView estado=(TextView) rowView.findViewById(R.id.EstadoMisReporte2);
        TextView fecha=(TextView)rowView.findViewById(R.id.tvFechaR2);
        TextView id=(TextView)rowView.findViewById(R.id.tvNumero2);
        Button sincronizar=(Button)rowView.findViewById(R.id.btnSincronizar);
        id.setText(items.get(position)[0]);
        titulo.setText(items.get(position)[1]);
        estado.setText(items.get(position)[2]);
        fecha.setVisibility(View.GONE);
        final String dd=items.get(position)[0];

        final View finalRowView = rowView;
        sincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(finalRowView.getContext(),IngresarActivity.class);
                intent.putExtra("reporte",dd);
                finalRowView.getContext().startActivity(intent);
                context.cerrarAplicacion();
            }
        });

        // Set data into the view


        return rowView;
    }
}
