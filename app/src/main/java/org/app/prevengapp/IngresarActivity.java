package org.app.prevengapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.app.prenengapp.DAOApp;
import org.app.prenengapp.Reporte;
import org.app.prenengapp.ReporteDao;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IngresarActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etCedula;
    EditText etContrasenna;
    Button btnIngresar;
    TextView tvOlvidar;
    private ProgressDialog pd = null;
    TextView tvSin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etCedula=(EditText)findViewById(R.id.etCedulaIng);
        etContrasenna=(EditText)findViewById(R.id.etContraseñaIng);
        btnIngresar=(Button)findViewById(R.id.btnIngresar);
        tvOlvidar=(TextView)findViewById(R.id.tvOlvidar);
        tvSin=(TextView)findViewById(R.id.tvReportarSin);
        btnIngresar.setOnClickListener(this);
        tvOlvidar.setOnClickListener(this);
        tvSin.setOnClickListener(this);
        Bundle bolsa=getIntent().getExtras();
        String repo=bolsa.getString("reporte");
        if (!repo.equals("-1")){
            tvOlvidar.setVisibility(View.GONE);
            tvSin.setVisibility(View.GONE);
        }
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
            case R.id.tvReportarSin:
                Intent intent2=new Intent(IngresarActivity.this,SinConexionOpcionesActivity.class);
                startActivity(intent2);
                break;
        }

    }

    public void verificarDatos(String datos){
        //showAlertDialog(IngresarActivity.this,"Ingresar",datos,false);
        Bundle bolsa=getIntent().getExtras();
        String repo=bolsa.getString("reporte");
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
                                if (repo.equals("-1")){
                                    etCedula.setText("");
                                    etContrasenna.setText("");
                                    Intent intent=new Intent(IngresarActivity.this,MapaActivity.class);
                                    intent.putExtra("documento",docu);
                                    intent.putExtra("nombre",objO.get("NOMBRES").getAsString());
                                    intent.putExtra("tipo",objO.get("TIPOUSUARIO").getAsString());
                                    startActivity(intent);
                                    finish();
                                }else {
                                    DAOApp daoApp=new DAOApp();
                                    ReporteDao reporteDao=daoApp.getUsuarioDao();
                                    Reporte reporte=reporteDao.load(Long.valueOf(repo));
                                    JSONObject persObject = new JSONObject();
                                    String diree="";
                                    if (pd != null) {
                                        pd.dismiss();
                                    }
                                    try {
                                        persObject.put("imagen1",reporte.getImagen());
                                        persObject.put("imagen2","");
                                        persObject.put("imagen3","");
                                        persObject.put("coordenadas","");
                                        //diree=elimianrAcento(ubicar(reporte.getCoordenadas()));
                                        persObject.put("direccion","");
                                        persObject.put("titulo",reporte.getTitulo());
                                        persObject.put("usuario",docu);
                                        persObject.put("descripcion",reporte.getDescripcion());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    pd = ProgressDialog.show(this, "Reporte", "Sincronizando datos Datos...", true, false);
                                    //showAlertDialog(IngresarActivity.this,"Reporte",diree,true);
                                    new MiTareaPost("http://semgerd.com/semgerd/index.php?PATH_INFO=reporte/registro",persObject.toString()).execute();

                                }

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
        if (repo.equals("-1")){
            if (pd != null) {
                pd.dismiss();
            }
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

    public String ubicar(String coordenadas){
        String[] coor=coordenadas.split(",");
        double lat=Double.valueOf(coor[0]);
        double lng=Double.valueOf(coor[1]);
        String direccionText="";

        // Add a marker in Sydney and move the camera

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> direcciones = geocoder
                    .getFromLocation(lat, lng, 1);
            Address direccion=direcciones.get(0);

            direccionText = String.format("%s, %s, %s",
                    direccion.getMaxAddressLineIndex() > 0 ? direccion.getAddressLine(0) : "",
                    direccion.getLocality(),
                    direccion.getCountryName());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return direccionText;

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

    public void guardar(String datos){
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("repocodi").isJsonNull())) {
                            String docu=objO.get("repocodi").getAsString();
                            if (docu.equals("-1")){
                                showAlertDialog2(IngresarActivity.this,"Reporte","Reporte no guardado",false);
                            }else{
                                DAOApp daoApp=new DAOApp();
                                ReporteDao reporteDao=daoApp.getUsuarioDao();
                                Bundle bolsa=getIntent().getExtras();
                                String repo=bolsa.getString("reporte");
                                reporteDao.deleteByKey(Long.valueOf(repo));
                                showAlertDialog2(IngresarActivity.this,"Reporte","Registro guardado exitosamente",false);
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
