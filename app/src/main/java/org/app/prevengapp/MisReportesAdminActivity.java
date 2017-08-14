package org.app.prevengapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MisReportesAdminActivity extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog pd = null;
    List<String[]> listaReporte= new ArrayList<>();
    //List<String[]> listaReporteMostrar= new ArrayList<>();
    ListView lista;
    String usua;
    int i=0;
    RelativeLayout rlFiltrar;
    Button btnFiltrar;
    TextView btnCancelar;
    Button btnVerTodo;
    Spinner spFiltrar;
    int control=1;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mYear2;
    private int mMonth2;
    private int mDay2;
    static final int DATE_DIALOG_DESDE = 2;
    static final int DATE_DIALOG_HASTA = 1;
    Button btnVerTodo2;
    Button btnDesde;
    Button btnHasta;
    Button btnFiltrar2;
    EditText etDesde;
    EditText etHasta;
    TextView btnCancelar2;
    RelativeLayout rlFiltrar2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reportes_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lista=(ListView)findViewById(R.id.lvReportesAdmin);
        rlFiltrar=(RelativeLayout)findViewById(R.id.rlFiltrarEstado);
        btnFiltrar=(Button)findViewById(R.id.btnFiltrarEstado);
        btnCancelar=(TextView) findViewById(R.id.btnCancelarfiltrar);
        btnVerTodo=(Button)findViewById(R.id.btnVerTodo);
        spFiltrar=(Spinner)findViewById(R.id.spFiltrarEstado);
        btnVerTodo.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnFiltrar.setOnClickListener(this);
        rlFiltrar.setVisibility(View.GONE);

        rlFiltrar2=(RelativeLayout)findViewById(R.id.rlFiltrar);
        etHasta=(EditText)findViewById(R.id.etHasta);
        etDesde=(EditText)findViewById(R.id.etDesde);
        btnDesde=(Button)findViewById(R.id.btnDesde);
        btnHasta=(Button)findViewById(R.id.btnHasta);
        btnFiltrar2=(Button)findViewById(R.id.btnFiltrar);
        btnCancelar2=(TextView) findViewById(R.id.btnCancelarfilt);
        btnVerTodo2=(Button)findViewById(R.id.btnTodo);
        btnVerTodo2.setOnClickListener(this);
        btnCancelar2.setOnClickListener(this);
        btnFiltrar2.setOnClickListener(this);
        btnDesde.setOnClickListener(this);
        btnHasta.setOnClickListener(this);
        rlFiltrar2.setVisibility(View.GONE);

        Bundle bolsa=getIntent().getExtras();
        String docu=bolsa.getString("documento");
        usua=bolsa.getString("usuario");
        pd = ProgressDialog.show(this, "Reportes", "Buscando Reportes...", true, false);
        if (usua.equals("0")){
            new MiTareaGet("http://semgerdcucuta.com/semgerd/index.php?PATH_INFO=reporte/listadoadmin","").execute();
        }else{
            new MiTareaGet("http://semgerdcucuta.com/semgerd/index.php?PATH_INFO=reporte/reportesasignados/",docu).execute();
        }

        final Calendar c= Calendar.getInstance();
        mYear=c.get(Calendar.YEAR);
        mMonth=c.get(Calendar.MONTH);
        mDay=c.get(Calendar.DAY_OF_MONTH);
        mYear2=c.get(Calendar.YEAR)-10;
        mMonth2=c.get(Calendar.MONTH);
        mDay2=c.get(Calendar.DAY_OF_MONTH);
        actualizarFecha();

    }

    public void actualizarFecha(){
        //if(i==1){
        etHasta.setText(
                new StringBuilder()
                        .append(mDay).append("-")
                        .append(mMonth+1).append("-")
                        .append(mYear).append(""));
        //}else{
        etDesde.setText(
                new StringBuilder()
                        .append(mDay2).append("-")
                        .append(mMonth2+1).append("-")
                        .append(mYear2).append(""));
        //}
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i0, int i1, int i2) {
            if (i==1){
                mYear=i0;
                mMonth=i1;
                mDay=i2;
            }else{
                mYear2=i0;
                mMonth2=i1;
                mDay2=i2;
            }
            actualizarFecha();
        }
    };

    public void llamarActivity(String[] item){
        Intent i= new Intent(this, DetalleReporteActivity.class);
        i.putExtra("id",item[0]);
        Bundle bolsa=getIntent().getExtras();
        String docu=bolsa.getString("documento");
        i.putExtra("documento",docu);
        i.putExtra("usuario",usua);

        if (usua.equals("0")){
            startActivityForResult(i,1);
        }else {
            startActivity(i);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pd = ProgressDialog.show(this, "Reportes", "Buscando Reportes...", true, false);
        if (usua.equals("0")){
            new MiTareaGet("http://semgerdcucuta.com/semgerd/index.php?PATH_INFO=reporte/listadoadmin","").execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            case R.id.action_filtrar:
                if (control==1){
                    pd = ProgressDialog.show(this, "Cambiar", "Cargando...", true, false);
                    control=2;
                    new MiTareaGet("http://semgerdcucuta.com/semgerd/index.php?PATH_INFO=reporte/listadoestados","").execute();
                }else{
                    rlFiltrar.setVisibility(View.VISIBLE);
                }

                return true;
            case R.id.action_fecha:
                rlFiltrar2.setVisibility(View.VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle bolsa=getIntent().getExtras();
        String usua=bolsa.getString("usuario");
        if (usua.equals("0")){
            getMenuInflater().inflate(R.menu.filtrar, menu);
        }

        return true;
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
        //showAlertDialog(MisReportesAdminActivity.this,"Ingresar",datos,false);
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    listaReporte.clear();
                    for (int x = 0; x < array.size(); x++) {
                        String[] reporte=new String[10];
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("REPOCONS").isJsonNull())) {
                            //showAlertDialog(MisReportesAdminActivity.this,"Ingresar",objO.get("REPOCONS").getAsString(),false);
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
                                }if (!(objO.get("PRIORIDAD").isJsonNull())) {
                                    reporte[7]=objO.get("PRIORIDAD").getAsString();
                                }if (!(objO.get("COLORPRIORIDAD").isJsonNull())) {
                                    reporte[8]=objO.get("COLORPRIORIDAD").getAsString();
                                }
                                if (usua.equals("0")){
                                    if (!(objO.get("REPOFERE").isJsonNull())) {
                                        reporte[6]=objO.get("REPOFERE").getAsString();
                                    }if (!(objO.get("ASIGNADO").isJsonNull())) {
                                        reporte[9]=objO.get("ASIGNADO").getAsString();
                                    }
                                }else{
                                    if (!(objO.get("FECHAREPORTE").isJsonNull())) {
                                        reporte[6]=objO.get("FECHAREPORTE").getAsString();
                                    }if (!(objO.get("FECHAASIGNADA").isJsonNull())) {
                                        reporte[9]=objO.get("FECHAASIGNADA").getAsString();
                                    }
                                }
                                listaReporte.add(reporte);
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

        if (listaReporte.size()>0){
            List<String[]> listaReporteMostrar=new ArrayList<>();
            listaReporteMostrar=listaReporte;
            List<String> prueba=new ArrayList<>();
            lista.setAdapter(new MisReportesAdminAdater(this, listaReporte,usua));
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    String[] item = (String[]) lista.getAdapter().getItem(pos);
                    llamarActivity(item);

                }
            });
        }else{
            showAlertDialog(MisReportesAdminActivity.this,"Reportes", "No hay reportes asociados a este usuario",false);
        }

        if (pd != null) {
            pd.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnFiltrarEstado:
                filtrarEstado();
                //rlFiltrar.setVisibility(View.GONE);
                break;
            case R.id.btnCancelarfiltrar:
                rlFiltrar.setVisibility(View.GONE);
                break;
            case R.id.btnVerTodo:
                lista.setAdapter(new MisReportesAdminAdater(this, listaReporte,usua));
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        String[] item = (String[]) lista.getAdapter().getItem(pos);
                        llamarActivity(item);

                    }
                });
                rlFiltrar.setVisibility(View.GONE);
                break;
            case R.id.btnDesde:
                i=2;
                showDialog(DATE_DIALOG_DESDE);
                break;
            case R.id.btnHasta:
                i=1;
                showDialog(DATE_DIALOG_HASTA);
                break;
            case R.id.btnFiltrar:
                filtrarFecha();
                rlFiltrar2.setVisibility(View.GONE);
                break;
            case R.id.btnCancelarfilt:
                rlFiltrar2.setVisibility(View.GONE);
                break;
            case R.id.btnTodo:
                List<String[]> listaReporteMostrar=new ArrayList<>();
                listaReporteMostrar=listaReporte;
                lista.setAdapter(new MisReportesAdater(this, listaReporteMostrar));
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        String[] item = (String[]) lista.getAdapter().getItem(pos);
                        llamarActivity(item);

                    }
                });
                rlFiltrar2.setVisibility(View.GONE);
                break;
        }
    }

    public void filtrarFecha(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //Para declarar valores en nuevos objetos date, usa el mismo formato date que usaste al crear las fechas
        try {
            List<String[]> listaReporteMostrar=new ArrayList<>();
            final Calendar c= Calendar.getInstance();
            Calendar desde = Calendar.getInstance();
            Calendar hasta = Calendar.getInstance();
            desde.set(mYear2,mMonth2,mDay2);
            hasta.set(mYear,mMonth,mDay);
            for (int x=0;x<listaReporte.size();x++){
                //showAlertDialog(MisReportesActivity.this,listaReporte.get(x)[6].substring(0,4)+"-"+listaReporte.get(x)[6].substring(5,7)+"-"+listaReporte.get(x)[6].substring(8,10),String.valueOf(x),true);
                Calendar fecha = Calendar.getInstance();
                fecha.set(Integer.valueOf(listaReporte.get(x)[6].substring(0,4)),Integer.valueOf(listaReporte.get(x)[6].substring(5,7))-1,Integer.valueOf(listaReporte.get(x)[6].substring(8,10)));
                if (desde.before(fecha)&&fecha.before(hasta)){
                    //showAlertDialog(MisReportesActivity.this,"hola",String.valueOf(x)+"dd",true);
                    listaReporteMostrar.add(listaReporte.get(x));
                }

            }
            lista.setAdapter(new MisReportesAdater(this, listaReporteMostrar));
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    String[] item = (String[]) lista.getAdapter().getItem(pos);
                    llamarActivity(item);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showAlertDialog(MisReportesAdminActivity.this,"Reportes",e.toString(),false);
        }

    }

    @Override
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DATE_DIALOG_HASTA:
                return  new DatePickerDialog(this,mDateSetListener,mYear,mMonth,mDay);
            case DATE_DIALOG_DESDE:
                return  new DatePickerDialog(this,mDateSetListener,mYear2,mMonth2,mDay2);
        }
        return  null;

    }

    public void filtrarEstado(){
        List<String[]> listaReporteMostrar=new ArrayList<>();
        String estado=spFiltrar.getSelectedItem().toString();
        for (int x=0;x<listaReporte.size();x++){
            if ((estado.equals(listaReporte.get(x)[4]))){
                listaReporteMostrar.add(listaReporte.get(x));
            }
        }

        if (listaReporteMostrar.size()>0){
            rlFiltrar.setVisibility(View.GONE);
            lista.setAdapter(new MisReportesAdminAdater(this, listaReporteMostrar,usua));
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    String[] item = (String[]) lista.getAdapter().getItem(pos);
                    llamarActivity(item);

                }
            });
        }else{
            showAlertDialog(MisReportesAdminActivity.this,"Reportes", "No hay reportes en este estado",false);
        }
    }

    public void cargarSpinner(String datos){
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    String[] lista2=new String[array.size()+1];
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("ESTADO").isJsonNull())) {
                            lista2[x]=objO.get("ESTADO").getAsString();
                        }
                    }
                    lista2[array.size()]="Seleccione";
                    ArrayAdapter<String> spFormatoAdaptener=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lista2);
                    spFiltrar.setAdapter(spFormatoAdaptener);
                    spFiltrar.setSelection(array.size());
                    rlFiltrar.setVisibility(View.VISIBLE);
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
                    verificarDatos(tiraJson);
                    break;
                case 2:
                    cargarSpinner(tiraJson);
                    break;
            }

        }
    }
}
