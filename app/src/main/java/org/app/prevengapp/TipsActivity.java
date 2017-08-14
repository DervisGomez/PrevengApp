package org.app.prevengapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TipsActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnDeslizamiento;
    Button btnInundacion;
    Button btnIncendio;
    Button btnSismo;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnDeslizamiento=(Button)findViewById(R.id.btnTIpsDeslizamiento);
        btnInundacion=(Button)findViewById(R.id.btnTIpsInundacion);
        btnIncendio=(Button)findViewById(R.id.btnTIpsIncendio);
        btnSismo=(Button)findViewById(R.id.btnTIpsSismo);
        btnDeslizamiento.setOnClickListener(this);
        btnInundacion.setOnClickListener(this);
        btnIncendio.setOnClickListener(this);
        btnSismo.setOnClickListener(this);

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
            case R.id.btnTIpsDeslizamiento:
                verFoto("1");
                break;
            case R.id.btnTIpsIncendio:
                verFoto("2");
                break;
            case R.id.btnTIpsInundacion:
                verFoto("3");
                break;
            case R.id.btnTIpsSismo:
                verFoto("4");
                break;
        }
    }

    public void verFoto(String item){
        AlertDialog.Builder builder = new AlertDialog.Builder(TipsActivity.this);
        //builder.setTitle("Seleccione un Perfil");

        List<String> list=new ArrayList<>();
        list.add(item);
        ListAdapter listAdapter=new TipsAdater(this, list);
        builder.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public void cerrarFoto(){
        dialog.hide();
    }
}
