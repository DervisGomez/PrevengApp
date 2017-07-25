package org.app.prevengapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.app.prenengapp.DAOApp;
import org.app.prenengapp.Reporte;
import org.app.prenengapp.ReporteDao;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import uk.co.senab.photoview.PhotoViewAttacher;

public class NuevoReporteActivity extends AppCompatActivity implements View.OnClickListener{

    private String APP_DIRECTORY="myPictureApp/";
    private String MEDIA_DIRECTORY=APP_DIRECTORY+"media";
    private String TEMPORAL_PICTURE_NAME="tempora.jpg";

    private final int PHOTO_CODE=100;
    private final int SELECT_PICTURE=200;
    public static final int ACTION_TAKE_PHOTO = 1;
    public static final int CHOOSE_FROM_GALLERY = 2;
    private ImageView foto;
    private Button btnFoto1;
    private Button btnFoto2;
    private Button btnFoto3;
    String[] imagenes={"","",""};
    LinearLayout llFoto;
    Button btnRegresar;
    EditText etTitulo;
    EditText etDescripcion;
    EditText etDireccion;
    EditText etCoordenada;
    Button btnEnviar;
    private ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_reporte);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        foto=(ImageView)findViewById(R.id.ivFoto);
        btnFoto1=(Button)findViewById(R.id.btnFoto1);
        btnFoto2=(Button)findViewById(R.id.btnFoto2);
        btnFoto3=(Button)findViewById(R.id.btnFoto3);
        llFoto=(LinearLayout)findViewById(R.id.llFotoRep);
        btnRegresar=(Button)findViewById(R.id.btnRegresarRepo);
        btnEnviar=(Button)findViewById(R.id.btnNuevoReporte);
        etTitulo=(EditText)findViewById(R.id.etTituloReporte);
        etDescripcion=(EditText)findViewById(R.id.etDescripcionReporte);
        etDireccion=(EditText)findViewById(R.id.etDireccionReporte);
        etCoordenada=(EditText)findViewById(R.id.etCoordenadasReporte);
        btnEnviar=(Button)findViewById(R.id.btnNuevoReporte);
        Bundle bolsa=getIntent().getExtras();
        String coor=bolsa.getString("coordenadas");
        String dire=bolsa.getString("direccion");
        String docu=bolsa.getString("documento");
        etDireccion.setText(dire);
        etDireccion.setFocusable(false);
        etCoordenada.setText(coor);
        etCoordenada.setVisibility(View.GONE);
        btnRegresar.setOnClickListener(this);
        btnFoto1.setOnClickListener(this);
        //btnFoto2.setOnClickListener(this);
        //btnFoto3.setOnClickListener(this);
        btnFoto2.setVisibility(View.GONE);
        btnFoto3.setVisibility(View.GONE);
        btnEnviar.setOnClickListener(this);
        llFoto.setVisibility(View.GONE);

        if(docu.equals("-1")){
            etDireccion.setVisibility(View.GONE);
        }

        tomarFoto();
    }

    public void iniciarCamara(){
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        //Creamos una carpeta en la memeria del terminal
        File imagesFolder = new File(
                Environment.getExternalStorageDirectory(), "Tutorialeshtml5");
        imagesFolder.mkdirs();
        //añadimos el nombre de la imagen
        File image = new File(imagesFolder, "foto.jpg");
        Uri uriSavedImage = Uri.fromFile(image);
        //Le decimos al Intent que queremos grabar la imagen
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        //Lanzamos la aplicacion de la camara con retorno (forResult)
        startActivityForResult(cameraIntent, PHOTO_CODE);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprovamos que la foto se a realizado
        if (requestCode == PHOTO_CODE && resultCode == RESULT_OK) {
            //Creamos un bitmap con la imagen recientemente
            //almacenada en la memoria
            Bitmap bMap = BitmapFactory.decodeFile(
                    Environment.getExternalStorageDirectory()+
                            "/Tutorialeshtml5/"+"foto.jpg");
            //Añadimos el bitmap al imageView para
            //mostrarlo por pantalla
            //foto.setImageBitmap(bMap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        /*bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        foto = Base64.encodeToString(byteArray, Base64.DEFAULT);*/
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            bMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            if (imagenes[0].equals("")){
                imagenes[0]  = Base64.encodeToString(byteArray, Base64.DEFAULT);
                btnFoto1.setText("Ver Foto");
                //btnFoto2.setText("Nueva Foto");
            }/*else if (imagenes[1].equals("")){
                imagenes[1]  = Base64.encodeToString(byteArray, Base64.DEFAULT);
                btnFoto2.setText("Ver Foto");
                btnFoto3.setText("Nueva Foto");
            }else if (imagenes[2].equals("")) {
                imagenes[2] = Base64.encodeToString(byteArray, Base64.DEFAULT);
                btnFoto3.setText("Ver Foto");
            }*/
        }else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bMap = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        /*bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        foto = Base64.encodeToString(byteArray, Base64.DEFAULT);*/
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            bMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            if (imagenes[0].equals("")){
                imagenes[0]  = Base64.encodeToString(byteArray, Base64.DEFAULT);
                btnFoto1.setText("Ver Foto");
                //btnFoto2.setText("Nueva Foto");
            }/*else if (imagenes[1].equals("")){
                imagenes[1]  = Base64.encodeToString(byteArray, Base64.DEFAULT);
                btnFoto2.setText("Ver Foto");
                btnFoto3.setText("Nueva Foto");
            }else if (imagenes[2].equals("")) {
                imagenes[2] = Base64.encodeToString(byteArray, Base64.DEFAULT);
                btnFoto3.setText("Ver Foto");
            }*/
            //myAsyncTask.execute();
        }
    }

    public void pickImage() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_PICTURE);
    }

    public void tomarFoto(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NuevoReporteActivity.this);
        builder.setTitle("Seleccione una opcion");
        builder.setItems(R.array.camera_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i+1 == ACTION_TAKE_PHOTO){
                    iniciarCamara();
                }
                if(i+1 == CHOOSE_FROM_GALLERY){
                    pickImage();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnFoto1:
                if (imagenes[0].equals("")){
                    tomarFoto();
                }else {
                    byte[] decodedString = Base64.decode(imagenes[0], Base64.DEFAULT);
                    Bitmap mImageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    foto.setImageBitmap(mImageBitmap);
                    llFoto.setVisibility(View.VISIBLE);
                    PhotoViewAttacher photoViewAttacher=new PhotoViewAttacher(foto);
                }
                break;
            case R.id.btnFoto2:
                /*if (!imagenes[0].equals("")){
                    if (imagenes[1].equals("")){
                        tomarFoto();
                    }else {
                        byte[] decodedString = Base64.decode(imagenes[1], Base64.DEFAULT);
                        Bitmap mImageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        foto.setImageBitmap(mImageBitmap);
                        llFoto.setVisibility(View.VISIBLE);
                    }
                }*/
                break;
            case R.id.btnFoto3:
                /*if (!imagenes[0].equals("")&&!imagenes[1].equals("")){
                    if (imagenes[2].equals("")){
                        tomarFoto();
                    }else {
                        byte[] decodedString = Base64.decode(imagenes[2], Base64.DEFAULT);
                        Bitmap mImageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        foto.setImageBitmap(mImageBitmap);
                        llFoto.setVisibility(View.VISIBLE);
                    }
                }*/
                break;
            case R.id.btnRegresarRepo:
                llFoto.setVisibility(View.GONE);
                break;
            case R.id.btnNuevoReporte:
                if (!imagenes[0].equals("")){
                    String titu=etTitulo.getText().toString();
                    String desc=etDescripcion.getText().toString();
                    String dire=etDireccion.getText().toString();
                    String coor=etCoordenada.getText().toString();
                    if(titu.length()>0&&desc.length()>0){
                        Bundle bolsa=getIntent().getExtras();
                        String docu=bolsa.getString("documento");
                        JSONObject persObject = new JSONObject();
                        if (docu.equals("-1")){
                            DAOApp daoApp=new DAOApp();
                            ReporteDao reporteDao=daoApp.getUsuarioDao();
                            Reporte reporte=new Reporte();
                            reporte.setCoordenadas(coor);
                            reporte.setDescripcion(elimianrAcento(desc));
                            reporte.setImagen(imagenes[0]);
                            reporte.setTitulo(elimianrAcento(titu));
                            reporteDao.insert(reporte);
                            showAlertDialog2(NuevoReporteActivity.this,"Reporte","Registro guardado localmente",false);

                        }else{
                            try {
                                persObject.put("imagen1",imagenes[0]);
                                persObject.put("imagen2",imagenes[1]);
                                persObject.put("imagen3",imagenes[2]);
                                persObject.put("coordenadas",coor);
                                persObject.put("direccion",elimianrAcento(dire));
                                persObject.put("titulo",elimianrAcento(titu));
                                persObject.put("usuario",docu);
                                persObject.put("descripcion",elimianrAcento(desc));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            pd = ProgressDialog.show(this, "Reporte", "Validando Datos...", true, false);
                            new MiTareaPost("http://semgerd.com/semgerd/index.php?PATH_INFO=reporte/registro",persObject.toString()).execute();
                        }

                    }else{
                        showAlertDialog(NuevoReporteActivity.this,"Reporte","Hay campos vacios",false);
                    }

                }else{
                    showAlertDialog(NuevoReporteActivity.this,"Reporte","De guardar al menos una foto",false);
                }
                break;
        }
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
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
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
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
                                showAlertDialog(NuevoReporteActivity.this,"Reporte","Reporte no guardado",false);
                            }else{
                                showAlertDialog2(NuevoReporteActivity.this,"Reporte","Registro guardado exitosamente",false);
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
