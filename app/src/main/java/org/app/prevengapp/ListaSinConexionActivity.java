package org.app.prevengapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.app.prenengapp.DAOApp;
import org.app.prenengapp.Reporte;
import org.app.prenengapp.ReporteDao;

import java.util.ArrayList;
import java.util.List;

public class ListaSinConexionActivity extends AppCompatActivity {
    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_sin_conexion);
        lista=(ListView)findViewById(R.id.lvReportesSin);
        cargarLista();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void cargarLista(){
        DAOApp daoApp=new DAOApp();
        ReporteDao reporteDao=daoApp.getUsuarioDao();
        List<Reporte> reportes=reporteDao.loadAll();
        List<String[]> listaReporteMostrar=new ArrayList<>();

        if (reportes.size()>0){
            for (int x=0;x<reportes.size();x++){
                Reporte reporte=reportes.get(x);
                listaReporteMostrar.add(new String[]{String.valueOf(reporte.getId()),reporte.getTitulo(),reporte.getDescripcion(),reporte.getCoordenadas(),reporte.getImagen()});
                lista.setAdapter(new MisReportesSinConexionAdater(ListaSinConexionActivity.this, listaReporteMostrar));

            }
        }else{
            showAlertDialog(ListaSinConexionActivity.this,"Mis Reportes","No hay reporte registrados localmente",true);
        }
    }

    public void cerrarAplicacion(){
        finish();
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
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
