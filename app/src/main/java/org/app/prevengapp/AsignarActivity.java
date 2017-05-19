package org.app.prevengapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class AsignarActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etReporte;
    Button btnBuscar;
    TextView tvTitulo;
    TextView tvDireccion;
    TextView tvCoordenadas;
    TextView tvEstado;
    TextView tvFecha;
    TextView tvDocumento;
    TextView tvNombre;
    TextView tvTelefono;
    TextView tvDescripcion;
    TextView tvPrioridad;
    String reporte;
    Spinner spUsuario;
    Button btnGuardar;
    ImageView ivFoto;
    List<String[]> listaTpo=new ArrayList<>();
    private ProgressDialog pd = null;
    int control =0;
    boolean encontrado=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etReporte=(EditText)findViewById(R.id.etReporteAsignar);
        btnBuscar=(Button)findViewById(R.id.btnBuscarAsignar);
        tvNombre=(TextView)findViewById(R.id.tvNombreAsignar);
        tvTitulo=(TextView)findViewById(R.id.tvTituloAsignar);
        tvDireccion=(TextView)findViewById(R.id.tvDireccionAsignar);
        tvCoordenadas=(TextView)findViewById(R.id.tvCoordenadaAsignar);
        tvEstado=(TextView)findViewById(R.id.tvEstadoAsignar);
        tvFecha=(TextView)findViewById(R.id.tvFechaAsignar);
        tvDocumento=(TextView)findViewById(R.id.tvDocumentoAsignar);
        tvNombre=(TextView)findViewById(R.id.tvNombreAsignar);
        tvTelefono=(TextView)findViewById(R.id.tvTelefonoAsignar);
        tvDescripcion=(TextView)findViewById(R.id.tvDescripcionAsignar);
        tvPrioridad=(TextView)findViewById(R.id.tvPrioridadAsignar);
        spUsuario=(Spinner)findViewById(R.id.spUsuarioAsignar);
        btnGuardar=(Button)findViewById(R.id.btnGuardarAsignar);
        ivFoto=(ImageView)findViewById(R.id.ivImagenAsignar);
        btnGuardar.setOnClickListener(this);
        btnBuscar.setOnClickListener(this);

        pd = ProgressDialog.show(this, "Reporte", "Cargando...", true, false);
        control=1;
        new MiTareaGet("http://semgerd.com/semgerd/index.php?PATH_INFO=usuario/usuariosasignar","").execute();

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
            case R.id.btnBuscarAsignar:
                String docu=etReporte.getText().toString();
                if (docu.length()>0){
                    pd = ProgressDialog.show(this, "Reporte", "Cargando...", true, false);
                    control=2;
                    new MiTareaGet("http://semgerd.com/semgerd/index.php?PATH_INFO=reporte/consultarreporte/",docu).execute();
                }else{
                    showAlertDialog(AsignarActivity.this,"Reporte","Introducir numero de reporte",false);
                }
                break;
            case R.id.btnGuardarAsignar:
                if (encontrado){
                    int tip=spUsuario.getSelectedItemPosition();
                    if (tip<0||tip==listaTpo.size()){
                        showAlertDialog(AsignarActivity.this,"Reporte","Seleccione un usuario",false);
                    }else{
                        pd = ProgressDialog.show(this, "Reporte", "Cargando...", true, false);
                        control=3;
                        new MiTareaGet("http://semgerd.com/semgerd/index.php?PATH_INFO=reporte/consultarasignar/",reporte).execute();
                    }
                }else {
                    showAlertDialog(AsignarActivity.this,"Usuario","Debe buscar un usuario primero",false);
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
                        if (!(objO.get("DOCUMENTO").isJsonNull())) {

                            if (!objO.get("DOCUMENTO").getAsString().equals("-1")){
                                reporte[0]=objO.get("DOCUMENTO").getAsString();
                                if (!(objO.get("NOMBRES").isJsonNull())) {
                                    reporte[1]=objO.get("NOMBRES").getAsString();
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

    public void cargarReporte(String datos){

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
                                reporte=objO.get("REPOCONS").getAsString();

                                if (!(objO.get("REPOCOOR").isJsonNull())) {
                                    tvCoordenadas.setText("COORDENADAS: "+objO.get("REPOCOOR").getAsString());
                                }if (!(objO.get("REPODIRE").isJsonNull())) {
                                    tvDireccion.setText("DIRECCION: "+objO.get("REPODIRE").getAsString());
                                }if (!(objO.get("REPODESCRI").isJsonNull())) {
                                    tvDescripcion.setText("DESCRIPCION: "+objO.get("REPODESCRI").getAsString());
                                }if (!(objO.get("REPOESTAREPO").isJsonNull())) {
                                    tvEstado.setText("ESTADO: "+objO.get("REPOESTAREPO").getAsString());
                                }if (!(objO.get("REPOTITU").isJsonNull())) {
                                    tvTitulo.setText("TITULO: "+objO.get("REPOTITU").getAsString());
                                }if (!(objO.get("REPOFERE").isJsonNull())) {
                                    tvFecha.setText("FECHA: "+objO.get("REPOFERE").getAsString());
                                }if (!(objO.get("DOCUMENTO").isJsonNull())) {
                                    tvDocumento.setText("DOCUMENTO: "+objO.get("DOCUMENTO").getAsString());
                                }if (!(objO.get("NOMBREREPORTA").isJsonNull())) {
                                    tvNombre.setText("NOMBRE: "+objO.get("NOMBREREPORTA").getAsString());
                                }if (!(objO.get("TELEFONO").isJsonNull())) {
                                    tvTelefono.setText("TELEFONO: "+objO.get("TELEFONO").getAsString());
                                }
                                if (!(objO.get("PRIORIDAD").isJsonNull())) {
                                    tvPrioridad.setText("PRIORIDAD: "+objO.get("PRIORIDAD").getAsString());
                                }if (!(objO.get("IMAGEN1").isJsonNull())) {
                                    Display display = getWindowManager().getDefaultDisplay();
                                    int width = display.getWidth()-56;
                                    String img=objO.get("IMAGEN1").getAsString();
                                    byte[] decodedString1 = Base64.decode(img, Base64.DEFAULT);
                                    Bitmap mImageBitmap1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                                    ivFoto.setImageBitmap(mImageBitmap1);
                                    ivFoto.getLayoutParams().height=width;
                                }
                                String[] t=new String[listaTpo.size()+1];
                                for (int i=0;i<listaTpo.size();i++){
                                    t[i]=listaTpo.get(i)[0]+" "+listaTpo.get(i)[1];
                                }
                                //tvPrioridad.setTextColor(Color.parseColor(objO.get("COLORPRIORIDAD").getAsString()));
                                t[listaTpo.size()]="Seleccione un tipo usuario";
                                ArrayAdapter<String> spFormatoAdaptener=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,t);
                                spUsuario.setAdapter(spFormatoAdaptener);
                                spUsuario.setSelection(listaTpo.size());
                                encontrado =true;
                            }else {
                                showAlertDialog(this, "Reporte", "No hay un reporte con el numero "+etReporte.getText().toString(),false);
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

    public void verificarReporte(String datos){
        //showAlertDialog(this, "Error", datos,false);
        if (pd != null) {
            pd.dismiss();
        }
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    for (int x = 0; x < array.size(); x++) {
                        String[] report=new String[3];
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("ASICONS").isJsonNull())) {

                            if (!objO.get("ASICONS").getAsString().equals("0")){
                                report[0]=objO.get("ASICONS").getAsString();
                                if (!(objO.get("USUARIOASIGNADO").isJsonNull())) {
                                    report[1]=objO.get("USUARIOASIGNADO").getAsString();
                                }if (!(objO.get("USUARIOASIGNADO").isJsonNull())) {
                                    report[2]=objO.get("FECHAASIGNADO").getAsString();
                                }
                                showAlertDialog(AsignarActivity.this,"Reporte","Este reporte ya se asignó al usuario "+report[1]+" el día "+report[2],false);
                                //listaTpo.add(reporte);

                            }else{
                                pd = ProgressDialog.show(this, "Reporte", "Cargando...", true, false);
                                control=4;
                                int tip=spUsuario.getSelectedItemPosition();
                                Bundle bolsa=getIntent().getExtras();
                                String docu=bolsa.getString("documento");
                                new MiTareaGet("http://semgerd.com/semgerd/index.php?PATH_INFO=reporte/reasignarreporte/",reporte+"/"+listaTpo.get(tip)[0]+"/"+docu).execute();
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

    }

    public void guardarReporte(String datos){
        //showAlertDialog(this, "Error", datos,false);
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("ASICODI").isJsonNull())) {

                            if (!objO.get("ASICODI").getAsString().equals("0")){
                                showAlertDialog(AsignarActivity.this,"Reporte","Reporte Asignado exitosamente",true);
                                etReporte.setText("");
                                tvNombre.setText("NOMBRE: ");
                                tvTitulo.setText("TITULO: ");
                                tvDireccion.setText("DIRECCION: ");
                                tvTelefono.setText("TELEFONO: ");
                                tvDescripcion.setText("DESCRIPCION: ");
                                tvDocumento.setText("DOCUMENTO: ");
                                tvFecha.setText("FECHA: ");
                                tvPrioridad.setText("PRIORIDAD: ");

                                tvCoordenadas.setText("Coordenadas: ");
                                tvEstado.setText("ESTADO:");
                                ivFoto.setImageBitmap(null);
                                ivFoto.getLayoutParams().height=0;
                                encontrado=false;
                                control=0;
                                String[] t={};
                                ArrayAdapter<String> spFormatoAdaptener=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,t);
                                spUsuario.setAdapter(spFormatoAdaptener);
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
                    cargarReporte(tiraJson);
                    break;
                case 3:
                    verificarReporte(tiraJson);
                    break;
                case 4:
                    guardarReporte(tiraJson);
                    break;
            }
        }
    }
}
