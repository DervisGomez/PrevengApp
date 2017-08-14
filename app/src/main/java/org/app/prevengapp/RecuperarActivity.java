package org.app.prevengapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RecuperarActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etCedula;
    Button btnRecuperar;
    private ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etCedula=(EditText)findViewById(R.id.etCedulaRec);
        btnRecuperar=(Button)findViewById(R.id.btnRecuperar);
        btnRecuperar.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRecuperar:
                String cedu=etCedula.getText().toString();
                if (etCedula.length()>0){
                    pd = ProgressDialog.show(this, "Recuperar Contraseña", "Validando Usuario...", true, false);
                    new MiTareaGet("http://semgerdcucuta.com/semgerd/index.php?PATH_INFO=usuario/recuperar/",cedu).execute();
                }else{
                    showAlertDialog(RecuperarActivity.this,"Recuperar Contraseña","El campo cedula no puede estar vacio",false);
                }
                break;
        }
    }

    public void verificarDatos(String datos){
        //showAlertDialog(IngresarActivity.this,"Ingresar",datos,false);
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("DOCUMENTO").isJsonNull())) {
                            String docu=objO.get("DOCUMENTO").getAsString();
                            if (docu.equals("0")){
                                showAlertDialog(RecuperarActivity.this,"Recuperar Contraseña","Usuario no encontraro",false);
                            }else{
                                showAlertDialog2(RecuperarActivity.this,"Recuperar Contraseña","Usuario '"+docu+"' se le ha envido un correo con su contraseña ",false);
                            }
                        }

                    }
                }
            }catch (Exception e){
                showAlertDialog(this, "Error", e.toString(),false);
            }
        }else{
            showAlertDialog(this, "Error", "Servidor no disponible", false);
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
            verificarDatos(tiraJson);
        }
    }
}
