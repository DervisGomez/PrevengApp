package org.app.prevengapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

public class CambiarUsuario extends AppCompatActivity implements View.OnClickListener{

    EditText etDocumento;
    Button btnBuscar;
    TextView tvNombre;
    TextView tvCedula;
    TextView tvCorreo;
    TextView tvTelefono;
    String cedula;
    Spinner spTipo;
    Button btnGuardar;
    List<String[]> listaTpo=new ArrayList<>();
    private ProgressDialog pd = null;
    int control =0;
    boolean encontrado=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_usuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etDocumento=(EditText)findViewById(R.id.etDocumentoCamUsu);
        btnBuscar=(Button)findViewById(R.id.btnBuscarCamUsu);
        tvNombre=(TextView)findViewById(R.id.tvNombreCamUsu);
        tvCedula=(TextView)findViewById(R.id.tvCedulaCamUsu);
        tvCorreo=(TextView)findViewById(R.id.tvCorreoCamUsu);
        tvTelefono=(TextView)findViewById(R.id.tvTelefonoCamUsu);
        spTipo=(Spinner)findViewById(R.id.spTipoCamUsu);
        btnGuardar=(Button)findViewById(R.id.btnGuardarCamUsu);
        btnGuardar.setOnClickListener(this);
        btnBuscar.setOnClickListener(this);

        pd = ProgressDialog.show(this, "Usuario", "Cargando...", true, false);
        control=1;
        new MiTareaGet("http://semgerdcucuta.com/semgerd/index.php?PATH_INFO=usuario/listadotiposusuarios","").execute();

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
            case R.id.btnBuscarCamUsu:
                String docu=etDocumento.getText().toString();
                if (docu.length()>0){
                    pd = ProgressDialog.show(this, "Usuario", "Cargando...", true, false);
                    control=2;
                    new MiTareaGet("http://semgerdcucuta.com/semgerd/index.php?PATH_INFO=usuario/consultarusuario/",docu).execute();
                }else{
                    showAlertDialog(CambiarUsuario.this,"Usuario","Introducir numero de documento",false);
                }
                break;
            case R.id.btnGuardarCamUsu:
                if (encontrado){
                    int tip=spTipo.getSelectedItemPosition();
                    if (tip<0||tip==listaTpo.size()){
                        showAlertDialog(CambiarUsuario.this,"Usuario","Seleccione un tipo de usuario",false);
                    }else{
                        pd = ProgressDialog.show(this, "Usuario", "Cargando...", true, false);
                        control=3;
                        new MiTareaGet("http://semgerdcucuta.com/semgerd/index.php?PATH_INFO=usuario/cambiotipousu/",cedula+"/"+listaTpo.get(tip)[0]).execute();
                    }
                }else {
                    showAlertDialog(CambiarUsuario.this,"Usuario","Debe buscar un usuario primero",false);
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

    public void cargarSpinner(String datos){
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    for (int x = 0; x < array.size(); x++) {
                        String[] reporte=new String[2];
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("CODIGO").isJsonNull())) {

                            if (!objO.get("CODIGO").getAsString().equals("-1")){
                                reporte[0]=objO.get("CODIGO").getAsString();
                                if (!(objO.get("NOMBRE").isJsonNull())) {
                                    reporte[1]=objO.get("NOMBRE").getAsString();
                                }
                                listaTpo.add(reporte);
                            }
                        }
                    }
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

    public void cargarUsuario(String datos){

        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("DOCUMENTO").isJsonNull())) {

                            if (!objO.get("DOCUMENTO").getAsString().equals("0")){
                                cedula=objO.get("DOCUMENTO").getAsString();
                                tvCedula.setText("DOCUMENTO: "+cedula);
                                if (!(objO.get("NOMBRES").isJsonNull())) {
                                    tvNombre.setText("NOMBRE: "+objO.get("NOMBRES").getAsString());
                                }if (!(objO.get("EMAIL").isJsonNull())) {
                                    tvCorreo.setText("CORREO: "+objO.get("EMAIL").getAsString());
                                }if (!(objO.get("TELEFONO").isJsonNull())) {
                                    tvTelefono.setText("TELEFONO: "+objO.get("TELEFONO").getAsString());
                                }
                                String[] t=new String[listaTpo.size()+1];
                                for (int i=0;i<listaTpo.size();i++){
                                    t[i]=listaTpo.get(i)[1];
                                }
                                t[listaTpo.size()]="Seleccione un tipo usuario";
                                ArrayAdapter<String> spFormatoAdaptener=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,t);
                                spTipo.setAdapter(spFormatoAdaptener);
                                spTipo.setSelection(listaTpo.size());
                                encontrado =true;
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
                        if (!(objO.get("DOCUMENTO").isJsonNull())) {

                            if (!objO.get("DOCUMENTO").getAsString().equals("0")){
                                showAlertDialog(CambiarUsuario.this,"Usuario","Cambio realizado exitosamente",true);
                                etDocumento.setText("");
                                tvNombre.setText("NOMBRE: ");
                                tvCedula.setText("CEDULA: ");
                                tvCorreo.setText("CORREO: ");
                                tvTelefono.setText("TELEFONO: ");
                                encontrado=false;
                                control=0;
                                String[] t={};
                                ArrayAdapter<String> spFormatoAdaptener=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,t);
                                spTipo.setAdapter(spFormatoAdaptener);
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
                    cargarUsuario(tiraJson);
                    break;
                case 3:
                    guardarUsuario(tiraJson);
                    break;
            }
        }
    }
}
