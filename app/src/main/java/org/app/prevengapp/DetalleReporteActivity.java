package org.app.prevengapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class DetalleReporteActivity extends AppCompatActivity implements View.OnClickListener{

    TextView titulo;
    TextView estado;
    TextView fecha;
    TextView direccion;
    TextView coordenadas;
    TextView descripcion;
    TextView verMapa;
    TextView numero;
    TextView nombre;
    TextView telefono;
    TextView cambiar;
    TextView verFoto;
    TextView nombreAsignado;
    TextView fechaAsignado;
    ImageView foto1;
    ImageView foto2;
    ImageView foto3;
    LinearLayout llFoto1;
    LinearLayout llFoto2;
    LinearLayout llFoto3;
    LinearLayout llNombre;
    LinearLayout llFecha;
    String[] reporte={"","","","","","","","","","","","","","",""};
    private ProgressDialog pd = null;
    Button sig;
    Button ant;
    int actual=0;
    int cantidad=0;
    FloatingActionButton fab3;
    FloatingActionButton fab2;
    boolean controlFab=false;
    String prioridad="";
    int control=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_reporte);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titulo=(TextView)findViewById(R.id.tvTituloRepo);
        estado=(TextView)findViewById(R.id.tvEstadoRepo);
        fecha=(TextView)findViewById(R.id.tvFechaRepo);
        direccion=(TextView)findViewById(R.id.tvDireccionRepo);
        coordenadas=(TextView)findViewById(R.id.tvCoordenadasRepo);
        descripcion=(TextView)findViewById(R.id.tvDescripcionRepo);
        numero=(TextView)findViewById(R.id.tvNumeroRepo);
        nombre=(TextView)findViewById(R.id.tvNombreRepo);
        telefono=(TextView)findViewById(R.id.tvTelefonoRepo);
        verMapa=(TextView)findViewById(R.id.tvVerMapa);
        cambiar=(TextView)findViewById(R.id.btnCambiarEstado);
        verFoto=(TextView)findViewById(R.id.btnVerFoto);
        foto1=(ImageView) findViewById(R.id.ivFoto1);
        foto2=(ImageView) findViewById(R.id.ivFoto2);
        foto3=(ImageView) findViewById(R.id.ivFoto3);
        nombreAsignado=(TextView)findViewById(R.id.tvNombreAsinadoRepo);
        fechaAsignado=(TextView)findViewById(R.id.tvFechaAsignadoRepo);
        llNombre=(LinearLayout)findViewById(R.id.llNombreAsignado);
        llFecha=(LinearLayout)findViewById(R.id.llFechaAsignado);
        llNombre.setVisibility(View.GONE);
        llFecha.setVisibility(View.GONE);
        llFoto1=(LinearLayout)findViewById(R.id.llFoto1);
        llFoto2=(LinearLayout)findViewById(R.id.llFoto2);
        llFoto3=(LinearLayout)findViewById(R.id.llFoto3);
        sig=(Button)findViewById(R.id.btnSig);
        ant=(Button)findViewById(R.id.btnAnt);
        /*sig.setOnClickListener(this);
        ant.setOnClickListener(this);*/
        cambiar.setOnClickListener(this);
        verFoto.setOnClickListener(this);

        verMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetalleReporteActivity.this,MapsActivity.class);
                intent.putExtra("coordenadas",reporte[1]);
                intent.putExtra("direccion",reporte[2]);
                startActivity(intent);
            }
        });

        sig.setVisibility(View.GONE);
        ant.setVisibility(View.GONE);
        llFoto1.setVisibility(View.GONE);
        llFoto2.setVisibility(View.GONE);
        llFoto3.setVisibility(View.GONE);

        Bundle bundle=getIntent().getExtras();
        String id=bundle.getString("id");

        control=1;
        pd = ProgressDialog.show(this, "Reportes", "Buscando Reportes...", true, false);
        new MiTareaGet("http://semgerd.com/semgerd/index.php?PATH_INFO=reporte/listadoimagenes/",id).execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetalleReporteActivity.this,SeguimientoActivity.class);
                Bundle bolsa=getIntent().getExtras();
                String docu=bolsa.getString("documento");
                intent.putExtra("documento",docu);
                intent.putExtra("reporte",reporte[0]);
                startActivity(intent);
            }
        });

        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(controlFab){
                    Intent intent=new Intent(DetalleReporteActivity.this,CambiarAsignarActivity.class);
                    Bundle bolsa=getIntent().getExtras();
                    String docu=bolsa.getString("documento");
                    intent.putExtra("documento",docu);
                    intent.putExtra("reporte",reporte[0]);
                    intent.putExtra("prioridad",prioridad);
                    intent.putExtra("titulo",reporte[5]);
                    startActivityForResult(intent,1);
                }
            }
        });

        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controlFab){
                    Intent intent=new Intent(DetalleReporteActivity.this,CambiarPrioridadActivity.class);
                    Bundle bolsa=getIntent().getExtras();
                    String docu=bolsa.getString("documento");
                    intent.putExtra("documento",docu);
                    intent.putExtra("reporte",reporte[0]);
                    intent.putExtra("prioridad",prioridad);
                    intent.putExtra("titulo",reporte[5]);
                    startActivityForResult(intent,2);
                }

            }
        });

        Bundle bolsa=getIntent().getExtras();
        String usua=bolsa.getString("usuario");

        if (usua.equals("0")){
            llNombre.setVisibility(View.VISIBLE);
            llFecha.setVisibility(View.VISIBLE);
            controlFab=true;
        }else if(usua.equals("1")){
            fab2.setVisibility(View.GONE);
            fab3.setVisibility(View.GONE);
            cambiar.setVisibility(View.GONE);
        }else{
            //cambiar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle=getIntent().getExtras();
        String id=bundle.getString("id");

        control=1;
        pd = ProgressDialog.show(this, "Reportes", "Buscando Reportes...", true, false);
        new MiTareaGet("http://semgerd.com/semgerd/index.php?PATH_INFO=reporte/listadoimagenes/",id).execute();
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

    public void cargarDatos(String[] dato){
        numero.setText(dato[0]);
        coordenadas.setText(dato[1]);
        direccion.setText(dato[2]);
        descripcion.setText(dato[3]);
        estado.setText(dato[4]);
        titulo.setText(dato[5]);
        fecha.setText(dato[6]);
        nombre.setText(dato[10]);
        telefono.setText(dato[11]);
        nombreAsignado.setText(dato[12]);
        fechaAsignado.setText(dato[13]);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth()-24;

        /*if (!dato[7].equals("")){
            llFoto1.setVisibility(View.VISIBLE);
            byte[] decodedString1 = Base64.decode(dato[7], Base64.DEFAULT);
            Bitmap mImageBitmap1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
            foto1.setImageBitmap(mImageBitmap1);
            foto1.getLayoutParams().height=width;
            cantidad++;
            actual++;
            ant.setVisibility(View.INVISIBLE);
            sig.setVisibility(View.INVISIBLE);
        }

        if (!dato[8].equals("")){
            lFoto2.setVisibility(View.VISIBLE);
            byte[] decodedString2 = Base64.decode(dato[8], Base64.DEFAULT);
            Bitmap mImageBitmap2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
            foto2.setImageBitmap(mImageBitmap2);
            foto2.getLayoutParams().height=width;
            cantidad++;
            sig.setVisibility(View.VISIBLE);
        }else{

        }

        if (!dato[9].equals("")){
            byte[] decodedString3 = Base64.decode(dato[9], Base64.DEFAULT);
            Bitmap mImageBitmap3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
            foto3.setImageBitmap(mImageBitmap3);
            int w=llFoto3.getWidth();
            llFoto3.setMinimumHeight(w);
            llFoto3.setVisibility(View.VISIBLE);
            foto3.getLayoutParams().height=width;
            cantidad++;
        }*/

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
                        if (!(objO.get("REPOCONS").isJsonNull())) {

                            if (!objO.get("REPOCONS").getAsString().equals("0")){
                                reporte[0]=objO.get("REPOCONS").getAsString();
                                if (!(objO.get("REPOCOOR").isJsonNull())) {
                                    reporte[1]=objO.get("REPOCOOR").getAsString();
                                }if (!(objO.get("REPODIRE").isJsonNull())) {
                                    reporte[2]=objO.get("REPODIRE").getAsString();
                                }if (!(objO.get("REPODESCRI").isJsonNull())) {
                                    reporte[3]=objO.get("REPODESCRI").getAsString();
                                }if (!(objO.get("REPOESTAREPO").isJsonNull())) {
                                    reporte[4]=objO.get("REPOESTAREPO").getAsString();
                                }if (!(objO.get("REPOTITU").isJsonNull())) {
                                    reporte[5]=objO.get("REPOTITU").getAsString();
                                }if (!(objO.get("REPOFERE").isJsonNull())) {
                                    reporte[6]=objO.get("REPOFERE").getAsString();
                                }if (!(objO.get("IMAGEN1").isJsonNull())) {
                                    reporte[7]=objO.get("IMAGEN1").getAsString();
                                }else{
                                    reporte[7]="";
                                } if (!(objO.get("IMAGEN2").isJsonNull())) {
                                    reporte[8]=objO.get("IMAGEN2").getAsString();
                                }else{
                                    reporte[8]="";
                                }if (!(objO.get("IMAGEN3").isJsonNull())) {
                                    reporte[9]=objO.get("IMAGEN3").getAsString();
                                }else{
                                    reporte[9]="";
                                }if (!(objO.get("NOMBREREPORTA").isJsonNull())) {
                                    reporte[10]=objO.get("NOMBREREPORTA").getAsString();
                                }if (!(objO.get("TELEFONO").isJsonNull())) {
                                    reporte[11] = objO.get("TELEFONO").getAsString();
                                }if (!(objO.get("NOMBREASIGNADO").isJsonNull())) {
                                    reporte[12] = objO.get("NOMBREASIGNADO").getAsString();
                                }if (!(objO.get("FECHSASIGNADO").isJsonNull())) {
                                    reporte[13] = objO.get("FECHSASIGNADO").getAsString();
                                }
                                //fab3.setBackgroundColor(Color.parseColor(objO.get("COLORPRIORIDAD").getAsString()));
                                fab3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(objO.get("COLORPRIORIDAD").getAsString())));
                                prioridad=objO.get("PRIORIDAD").getAsString();
                                cargarDatos(reporte);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAnt:
                if (actual>1){
                    llFoto1.setVisibility(View.VISIBLE);
                    byte[] decodedString1 = Base64.decode(reporte[actual+6], Base64.DEFAULT);
                    Bitmap mImageBitmap1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                    foto1.setImageBitmap(mImageBitmap1);
                    actual--;
                    //sig.setVisibility(View.VISIBLE);
                    if (actual==1){
                        //ant.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case R.id.btnSig:
                if (actual<cantidad){
                    llFoto1.setVisibility(View.VISIBLE);
                    byte[] decodedString1 = Base64.decode(reporte[actual+6], Base64.DEFAULT);
                    Bitmap mImageBitmap1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                    foto1.setImageBitmap(mImageBitmap1);
                    actual++;
                    //ant.setVisibility(View.VISIBLE);
                    if (actual==cantidad){
                        //sig.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case R.id.btnCambiarEstado:
                Intent intent=new Intent(DetalleReporteActivity.this,CambiarEstadoActivity.class);
                Bundle bolsa=getIntent().getExtras();
                String docu=bolsa.getString("documento");
                intent.putExtra("documento",docu);
                intent.putExtra("reporte",reporte[0]);
                intent.putExtra("prioridad",reporte[4]);
                intent.putExtra("titulo",reporte[5]);

                startActivityForResult(intent,3);
                break;
            case R.id.btnVerFoto:
                Intent intent1=new Intent(DetalleReporteActivity.this,VerFotoActivity.class);
                intent1.putExtra("reporte",reporte[0]);
                startActivity(intent1);
                /*control=2;
                pd = ProgressDialog.show(this, "Foto", "Cargar foto...", true, false);
                new MiTareaGet("http://semgerd.com/semgerd/index.php?PATH_INFO=reporte/imagenesreporte/",reporte[0]).execute();
*/
                break;
        }
    }

    public void mandarFoto(String datos){
        //showAlertDialog(DetalleReporteActivity.this,"Ingresar",datos,false);
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("IMACONS").isJsonNull())) {
                            if (!objO.get("IMAGEN").getAsString().equals("0")){
                                String imag=objO.get("IMAGEN").getAsString();
                                Intent intent11=new Intent(DetalleReporteActivity.this,VerFotoActivity.class);
                                //intent11.putExtra("reporte",imag);
                                startActivity(intent11);
                                //showAlertDialog(DetalleReporteActivity.this,"Ingresar","jj",false);
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
                showAlertDialog(DetalleReporteActivity.this,"",ex.toString(),false);
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
                    verificarDatos(tiraJson);
                    break;
                case 2:
                    mandarFoto(tiraJson);
                    break;
            }
        }
    }
}
