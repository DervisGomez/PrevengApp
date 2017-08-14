package org.app.prevengapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SeguimientoActivity extends AppCompatActivity {

    private ProgressDialog pd = null;
    List<String[]> listaNota=new ArrayList<>();
    ListView listNotas;
    String repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguimiento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listNotas=(ListView)findViewById(R.id.lvListaNota);


        Bundle bolsa=getIntent().getExtras();
        final String docu=bolsa.getString("documento");
        repo=bolsa.getString("reporte");

        pd = ProgressDialog.show(this, "Reportes", "Buscando Reportes...", true, false);
        new MiTareaGet("http://semgerdcucuta.com/semgerd/index.php?PATH_INFO=notas/listado/",repo).execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SeguimientoActivity.this,ComunicacionActivity.class);
                intent.putExtra("documento",docu);
                intent.putExtra("reporte",repo);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        pd = ProgressDialog.show(this, "Reportes", "Buscando Reportes...", true, false);
        new MiTareaGet("http://semgerdcucuta.com/semgerd/index.php?PATH_INFO=notas/listado/",repo).execute();
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

    public void verificarDatos(String datos){
        //showAlertDialog(SeguimientoActivity.this,"Ingresar",datos,false);
        listaNota.clear();
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    for (int x = 0; x < array.size(); x++) {
                        String[] nota=new String[5];
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("NOTACONS").isJsonNull())) {
                                nota[0]=objO.get("NOTACONS").getAsString();
                                if (!(objO.get("NOTATEXTO").isJsonNull())) {
                                    nota[1]=objO.get("NOTATEXTO").getAsString();
                                }if (!(objO.get("NOTAUSUA").isJsonNull())) {
                                    nota[2]=objO.get("NOTAUSUA").getAsString();
                                }if (!(objO.get("NOTAFECHA").isJsonNull())) {
                                    nota[3]=objO.get("NOTAFECHA").getAsString();
                                }if (!(objO.get("COLORFONDO").isJsonNull())) {
                                    nota[4]=objO.get("COLORFONDO").getAsString();
                                }
                                listaNota.add(nota);
                        }
                    }
                }
            }catch (Exception e){
                showAlertDialog(this, "Error", e.toString(),false);
            }
        }else{
            //showAlertDialog(this, "Segimiento", "No hay mensajes", false);
        }
        if (listaNota.size()>0) {
            listNotas.setAdapter(new SeguimientoAdater(this, listaNota));
            listNotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    String[] item = (String[]) listNotas.getAdapter().getItem(pos);
                    //llamarActivity(item);

                }
            });
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
