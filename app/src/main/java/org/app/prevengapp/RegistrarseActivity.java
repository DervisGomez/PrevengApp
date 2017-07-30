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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RegistrarseActivity extends AppCompatActivity implements View.OnClickListener{
    EditText etNombre;
    EditText etCedula;
    EditText etCorreo;
    EditText etContrasenna;
    EditText etConfirmar;
    EditText etTelefono;
    Button btnRegistrarme;
    private ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etNombre=(EditText)findViewById(R.id.etNombre);
        etCedula=(EditText)findViewById(R.id.etCedula);
        etCorreo=(EditText)findViewById(R.id.etCorreo);
        etContrasenna=(EditText)findViewById(R.id.etContraseña);
        etConfirmar=(EditText)findViewById(R.id.etConfirme);
        etTelefono=(EditText)findViewById(R.id.etTelefono);
        btnRegistrarme=(Button) findViewById(R.id.btnRegistrarme);
        btnRegistrarme.setOnClickListener(this);
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
                Intent intent=new Intent(RegistrarseActivity.this,IngresarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alertDialog.show();
    }

    public void guardar(String datos){
        //showAlertDialog(RegistrarseActivity.this,"Ingresar",datos,false);
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("documento").isJsonNull())) {
                            String docu=objO.get("documento").getAsString();
                            if (docu.equals("0")){
                                showAlertDialog(RegistrarseActivity.this,"Ingresar","Cedula y Clave incorrectas",false);
                            }else if (docu.equals("1")){
                                showAlertDialog(RegistrarseActivity.this,"Ingresar","Clave incorrectas para la cedula "+etCedula.getText().toString(),false);
                            }else{
                                showAlertDialog2(RegistrarseActivity.this,"Ingresar","Registro Exitoso",true);
                            }
                        }else{
                            showAlertDialog(this, "Error", "docu null",false);
                        }

                    }
                }else{
                    showAlertDialog(this, "Error", "no arreglo",false);
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

    public String elimianrAcento(String item){
        item=item.replaceAll("á","a");
        item=item.replaceAll("é","e");
        item=item.replaceAll("í","i");
        item=item.replaceAll("ó","o");
        item=item.replaceAll("ú","u");
        item=item.replaceAll("Á","A");
        item=item.replaceAll("É","E");
        item=item.replaceAll("Í","I");
        item=item.replaceAll("Ó","O");
        item=item.replaceAll("Ú","U");
        return item;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegistrarme:
                String nomb=etNombre.getText().toString();
                String cedu=etCedula.getText().toString();
                String corr=etCorreo.getText().toString();
                String cont=etContrasenna.getText().toString();
                String conf=etConfirmar.getText().toString();
                String tele=etTelefono.getText().toString();
                if(nomb.length()>0&&cedu.length()>0&&corr.length()>0&&cont.length()>0&&conf.length()>0&&tele.length()>0){
                    if (cont.equals(conf)){
                        JSONObject persObject = new JSONObject();
                        try {
                            persObject.put("documento",elimianrAcento(cedu));
                            persObject.put("nombres",elimianrAcento(nomb));
                            persObject.put("email",elimianrAcento(corr));
                            persObject.put("telefono",elimianrAcento(tele));
                            persObject.put("clave",elimianrAcento(cont));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pd = ProgressDialog.show(this, "Iniciar Sesión", "Validando Datos...", true, false);
                        new MiTareaPost("http://semgerd.com/semgerd/index.php?PATH_INFO=usuario/registro",persObject.toString()).execute();
                    }else{
                        showAlertDialog(RegistrarseActivity.this, "Registrarme","Las contraseña no coinciden",false);
                    }
                }else{
                    showAlertDialog(RegistrarseActivity.this, "Registrarme","Aun hay campos vacios",false);
                }
                break;
        }
    }

    private class MiTareaPost extends AsyncTask<String, Float, String> {
        private String jsonObject;
        private final String HTTP_EVENT;
        private HttpClient httpclient;
        BufferedReader in = null;

        public MiTareaPost(String url,String jsonObject){
            this.HTTP_EVENT=url;
            this.jsonObject=jsonObject;
        }
        protected void onPreExecute() {

        }

        protected String doInBackground(String... urls){
            String resul = "";
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost post = new HttpPost(HTTP_EVENT);
                StringEntity stringEntity = new StringEntity( jsonObject);
                post.setHeader("Content-type", "application/json");
                post.setEntity(stringEntity);
                HttpResponse response = httpClient.execute(post);
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                resul=sb.toString();

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resul;
        }

        protected void onProgressUpdate (Float... valores) {

        }

        protected void onPostExecute(String tiraJson) {
            guardar(tiraJson);
        }
        private StringBuilder inputStreamToString(InputStream is) {
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader rd = new BufferedReader( new InputStreamReader(is) );
            try{
                while( (line = rd.readLine()) != null ){
                    stringBuilder.append(line);
                }
            }catch( IOException e){
                e.printStackTrace();
            }
            return stringBuilder;
        }
    }

}
