package org.app.prevengapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dervis on 25/01/17.
 */
public class MisReportesAdminAdater extends BaseAdapter {

    private Context context;
    private List<String[]> items;
    private String tipoUsu;

    public MisReportesAdminAdater(Context context, List<String[]> items,String usu) {
        this.context = context;
        this.items = items;
        this.tipoUsu=usu;
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
            rowView = inflater.inflate(R.layout.adapter_mis_reportes_admin, parent, false);

        }
        TextView titulo=(TextView) rowView.findViewById(R.id.TituloMisReporteAdmin);
        TextView estado=(TextView) rowView.findViewById(R.id.EstadoMisReporteAdmin);
        TextView fecha=(TextView)rowView.findViewById(R.id.tvFechaAdmin);
        TextView id=(TextView)rowView.findViewById(R.id.tvNumeroAdmin);
        TextView prioridad=(TextView)rowView.findViewById(R.id.tvPrioridadAdmin);
        TextView asignar=(TextView)rowView.findViewById(R.id.tvAsignarAdmin);

        String[] item=items.get(position);
        id.setText(item[0]);
        titulo.setText(item[5]);
        estado.setText(item[4]);
        fecha.setText(item[6].substring(0,10));
        prioridad.setBackgroundColor(Color.parseColor(item[8]));

        if (tipoUsu.equals("0")){
            if(item[9].equals("NO ASIGNADO")){
                asignar.setText("NA");
            }else{
                asignar.setText("A");
            }
        }else{
            asignar.setText("A");
        }
        return rowView;
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
//		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }
}
