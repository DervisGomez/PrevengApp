package org.app.prevengapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.app.prenengapp.DAOApp;
import org.app.prenengapp.Reporte;
import org.app.prenengapp.ReporteDao;

public class DetalleSinConexionActivity extends AppCompatActivity {

    TextView tvNumero;
    TextView tvTitulo;
    //TextView tvCoordenadas;
    TextView tvDescripcion;
    Button btnVerFoto;
    Button btnVerMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_sin_conexion);
        tvNumero=(TextView)findViewById(R.id.tvNumeroRepoSin);
        tvTitulo=(TextView)findViewById(R.id.tvTituloRepoSin);
        //tvCoordenadas=(TextView)findViewById(R.id.tvCoordenadasRepoSin);
        tvDescripcion=(TextView)findViewById(R.id.tvDescripcionRepoSin);
        btnVerFoto=(Button)findViewById(R.id.btnSinVerfoto);
        btnVerMapa=(Button)findViewById(R.id.btnSinVerMapa);
        btnVerMapa.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //tvCoordenadas.setVisibility(View.GONE);

        Bundle bundle=getIntent().getExtras();
        String id=bundle.getString("id");

        DAOApp daoApp=new DAOApp();
        ReporteDao reporteDao=daoApp.getUsuarioDao();
        final Reporte reporte=reporteDao.load(Long.valueOf(id));

        tvNumero.setText(String.valueOf(reporte.getId()));
        tvTitulo.setText(reporte.getTitulo());
        //tvCoordenadas.setText(reporte.getCoordenadas());
        tvDescripcion.setText(reporte.getDescripcion());

        btnVerFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetalleSinConexionActivity.this,VerFotoActivity.class);
                intent.putExtra("imagen",String.valueOf(reporte.getId()));
                intent.putExtra("reporte","-1");
                startActivity(intent);
            }
        });
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
}
