package org.app.prevengapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SinConexionOpcionesActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnNuevo;
    Button btnLista;

    double lat = 0.0;
    double lng = 0.0;
    String direccionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sin_conexion_opciones);
        btnNuevo=(Button)findViewById(R.id.btnNuevoSin);
        btnLista=(Button)findViewById(R.id.btnListaSin);
        btnNuevo.setOnClickListener(this);
        btnLista.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnNuevoSin:
                ubicar();
                break;
            case R.id.btnListaSin:
                Intent intent2=new Intent(SinConexionOpcionesActivity.this,ListaSinConexionActivity.class);
                startActivity(intent2);
                break;
        }
    }
    public void ubicar(){
        MiServicio miServicio=new MiServicio(SinConexionOpcionesActivity.this);
        lat=miServicio.getLatitud();
        lng=miServicio.getLongitud();

        // Add a marker in Sydney and move the camera
        if (lat!=0.0&&lng!=0.0){
            Intent intent=new Intent(SinConexionOpcionesActivity.this,NuevoReporteActivity.class);
            intent.putExtra("documento","-1");
            intent.putExtra("direccion",String.valueOf(lat)+","+String.valueOf(lng));
            intent.putExtra("coordenadas",String.valueOf(lat)+","+String.valueOf(lng));
            startActivity(intent);
            //posicionamiento.setText("Posicionamiento Inactivado");
        }else{
            showAlertDialog(SinConexionOpcionesActivity.this,"Error","Posicionamiento inactivo",true);
        }

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
