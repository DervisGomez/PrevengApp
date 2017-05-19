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
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class IngresarActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etCedula;
    EditText etContrasenna;
    Button btnIngresar;
    TextView tvOlvidar;
    private ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etCedula=(EditText)findViewById(R.id.etCedulaIng);
        etContrasenna=(EditText)findViewById(R.id.etContraseñaIng);
        btnIngresar=(Button)findViewById(R.id.btnIngresar);
        tvOlvidar=(TextView)findViewById(R.id.tvOlvidar);
        btnIngresar.setOnClickListener(this);
        tvOlvidar.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnIngresar:
                String ced=etCedula.getText().toString();
                String con=etContrasenna.getText().toString();
                if(ced.length()>0&&con.length()>0){
                    pd = ProgressDialog.show(this, "Iniciar Sesión", "Validando Datos...", true, false);
                    new  MiTareaGet("http://semgerd.com/semgerd/?PATH_INFO=usuario/login/",ced+"/"+con).execute();
                }else{
                    showAlertDialog(IngresarActivity.this, "Ingresar","Debe introducir cédula y contraseña",false);
                }
                break;
            case R.id.tvOlvidar:
                etCedula.setText("");
                etContrasenna.setText("");;
                Intent intent=new Intent(IngresarActivity.this,RecuperarActivity.class);
                startActivity(intent);
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
                                showAlertDialog(IngresarActivity.this,"Ingresar","Cedula y Clave incorrectas",false);
                            }else if (docu.equals("1")){
                                showAlertDialog(IngresarActivity.this,"Ingresar","Clave incorrectas para la cedula "+etCedula.getText().toString(),false);
                            }else{
                                etCedula.setText("");
                                etContrasenna.setText("");
                                Intent intent=new Intent(IngresarActivity.this,MapaActivity.class);
                                intent.putExtra("documento",docu);
                                intent.putExtra("nombre",objO.get("NOMBRES").getAsString());
                                intent.putExtra("tipo",objO.get("TIPOUSUARIO").getAsString());
                                startActivity(intent);
                                finish();
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
