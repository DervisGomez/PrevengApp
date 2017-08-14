package org.app.prevengapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class CambiarEstadoActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tvNumero;
    TextView tvTitulo;
    TextView tvPrioridad;
    String cedula;
    Spinner spPrioridad;
    Button btnGuardar;
    String[] lista;
    private ProgressDialog pd = null;
    int control =0;
    boolean encontrado=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_estado);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvNumero=(TextView)findViewById(R.id.tvNumeroCamEst);
        tvTitulo=(TextView)findViewById(R.id.tvTituloCamEst);
        tvPrioridad=(TextView)findViewById(R.id.tvPrioridadCamEst);
        spPrioridad=(Spinner)findViewById(R.id.spPrioridadCamEst);
        btnGuardar=(Button)findViewById(R.id.btnCambiarEst);
        btnGuardar.setOnClickListener(this);

        Bundle bolsa=getIntent().getExtras();
        String num=bolsa.getString("reporte");
        String pri=bolsa.getString("prioridad");
        String tit=bolsa.getString("titulo");

        tvNumero.setText("NUMERO: "+num);
        tvTitulo.setText("TITULO: "+tit);
        tvPrioridad.setText("ESTADO: "+pri);

        pd = ProgressDialog.show(this, "Cambiar", "Cargando...", true, false);
        control=1;
        new MiTareaGet("http://semgerdcucuta.com/semgerd/index.php?PATH_INFO=reporte/listadoestados","").execute();

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
            case R.id.btnCambiarEst:
                if (encontrado){
                    int tip=spPrioridad.getSelectedItemPosition();
                    if (tip<0||tip==lista.length-1){
                        showAlertDialog(CambiarEstadoActivity.this,"Cambiar","Seleccione un tipo de usuario",false);
                    }else{
                        pd = ProgressDialog.show(this, "Cambiar", "Cargando...", true, false);
                        control=2;
                        Bundle bolsa=getIntent().getExtras();
                        String doc=bolsa.getString("reporte");
                        new MiTareaGet("http://semgerdcucuta.com/semgerd/index.php?PATH_INFO=reporte/asignarestado/",doc+"/"+lista[tip]).execute();
                    }
                }else {
                    showAlertDialog(CambiarEstadoActivity.this,"Cambiar","Debe buscar un usuario primero",false);
                }

        }

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

    public void showAlertDialog2(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
//		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.show();
    }

    public void cargarSpinner(String datos){
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    lista=new String[array.size()+1];
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("ESTADO").isJsonNull())) {
                            lista[x]=objO.get("ESTADO").getAsString();
                        }
                    }
                    lista[array.size()]="Seleccione un estado";
                    ArrayAdapter<String> spFormatoAdaptener=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lista);
                    spPrioridad.setAdapter(spFormatoAdaptener);
                    spPrioridad.setSelection(array.size());
                }
            }catch (Exception e){
                //showAlertDialog(this, "Error", e.toString(),false);
            }
        }else{
            //showAlertDialog(this, "Error", "Servidor no disponible", false);
        }
        if (pd != null) {
            pd.dismiss();
        }
    }

    public void guardarUsuario(String datos){
        //showAlertDialog(this, "Error", datos,false);
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("REPOCONS").isJsonNull())) {

                            if (!objO.get("REPOCONS").getAsString().equals("0")){
                                showAlertDialog2(CambiarEstadoActivity.this,"Cambiar","Cambio realizado exitosamente",true);

                            }
                        }else{
                            showAlertDialog(this, "Error", "Usuario no encontrado",false);
                        }
                    }
                }
            }catch (Exception e){
                showAlertDialog(this, "Error", e.toString(),false);
            }
        }else{
            //showAlertDialog(this, "Error", "Servidor no disponible", false);
        }
        if (pd != null) {
            pd.dismiss();
        }
    }

    //Clase para consumir servicio por get
    private class MiTareaGet extends AsyncTask<String, Float, String> {
        private String ur;

        public MiTareaGet(String url, String x) {
            Log.d("url", url);
            this.ur = url + x;

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
            String responce = "";

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet(ur);
            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                responce = EntityUtils.toString(resp.getEntity());
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
            }
            return responce;
        }

        @Override
        protected void onProgressUpdate(Float... valores) {

        }

        @Override//Acción a realizar despues de consumir el servicio
        protected void onPostExecute(String tiraJson) {

            switch (control){
                case 1:
                    cargarSpinner(tiraJson);
                    break;
                case 2:
                    guardarUsuario(tiraJson);
                    break;
            }
        }
    }
}
