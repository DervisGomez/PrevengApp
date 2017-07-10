package org.app.prevengapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class VerFotoActivity extends AppCompatActivity {

    ImageView foto;
    Button regresar;
    Button guardar;
    private ProgressDialog pd = null;
    PhotoViewAttacher photoViewAttacher;
    String repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_foto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        foto=(ImageView)findViewById(R.id.ivFotoVer);
        regresar=(Button)findViewById(R.id.btnRegresarVerFoto);
        guardar=(Button)findViewById(R.id.btnGuardarVerFoto);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Bundle bolsa=getIntent().getExtras();
        repo=bolsa.getString("reporte");

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap imagen = ((BitmapDrawable)foto.getDrawable()).getBitmap();
                String ruta = guardarImagen(getApplicationContext(), "imagen"+repo, imagen);
                //Toast.makeText(getApplicationContext(), ruta, Toast.LENGTH_LONG).show();
                showAlertDialog(VerFotoActivity.this,"Guardar Foto",ruta,true);

            }
        });
        pd = ProgressDialog.show(this, "Foto", "Cargando foto...", true, false);
        new MiTareaGet("http://semgerd.com/semgerd/index.php?PATH_INFO=reporte/imagenesreporte/",repo).execute();
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

    private String guardarImagen (Context context, String nombre, Bitmap imagen){
        ContextWrapper cw = new ContextWrapper(context);

        File imagesFolder = new File(
                Environment.getExternalStorageDirectory(), "PrevengAPP");
        imagesFolder.mkdirs();
        File myPath = new File(imagesFolder, nombre+".jpg");

        File dirImages = cw.getDir("Imagenes", Context.MODE_PRIVATE);
        File myPath2 = new File(dirImages, nombre + ".png");

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myPath);
            imagen.compress(Bitmap.CompressFormat.JPEG, 10, fos);
            fos.flush();
        }catch (FileNotFoundException ex){
            return "Ocurrio un Error ";
        }catch (IOException ex){
            return "Ocurrio un Error ";
        }
        return "Foto guardada exitosamente en "+myPath.getAbsolutePath();
    }

    public void verificarDatos(String datos){
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
                                byte[] decodedString1 = Base64.decode(imag, Base64.DEFAULT);
                                Bitmap mImageBitmap1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                                foto.setImageBitmap(mImageBitmap1);
                                photoViewAttacher=new PhotoViewAttacher(foto);

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

