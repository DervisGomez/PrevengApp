package org.app.prevengapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {
    private GoogleMap mMap;
    Marker marcador;
    double lat = 0.0;
    double lng = 0.0;
    String direccionText;
    TextView posicionamiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        posicionamiento=(TextView)findViewById(R.id.tvPosicionamiento);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ubicar();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Bundle bolsa=getIntent().getExtras();
        String tipo=bolsa.getString("tipo");
        String nomb=bolsa.getString("nombre");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View p=navigationView.getHeaderView(0);
        TextView nombre = (TextView)p.findViewById(R.id.tvNombreUsuario);
        TextView rol = (TextView)p.findViewById(R.id.tvRolUsuario);
        nombre.setText(nomb);

        if (tipo.equals("0")){
            rol.setText("Administrador");
            navigationView.getMenu().setGroupVisible(R.id.grupo,true);
            navigationView.getMenu().setGroupVisible(R.id.grupo2,false);
        }else if (tipo.equals("1")){
            rol.setText("Externo");
            navigationView.getMenu().setGroupVisible(R.id.grupo,false);
            navigationView.getMenu().setGroupVisible(R.id.grupo2,false);
        }else{
            rol.setText("Funcionario");
            navigationView.getMenu().setGroupVisible(R.id.grupo,false);
            navigationView.getMenu().setGroupVisible(R.id.grupo2,true);

        }

        int status= GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if(status== ConnectionResult.SUCCESS) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            //Toast.makeText(MapaActivity.this, "Exito", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(MapaActivity.this, "Error", Toast.LENGTH_SHORT).show();
            Dialog dialog=GooglePlayServicesUtil.getErrorDialog(status,(Activity)getApplicationContext(),10);
            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(MapaActivity.this,NuevoReporteActivity.class);
            Bundle bolsa=getIntent().getExtras();
            String docu=bolsa.getString("documento");
            intent.putExtra("documento",docu);
            intent.putExtra("direccion",direccionText);
            intent.putExtra("coordenadas",String.valueOf(lat)+","+String.valueOf(lng));
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(MapaActivity.this,MisReportesActivity.class);
            intent.putExtra("usuario","1");
            Bundle bolsa=getIntent().getExtras();
            String docu=bolsa.getString("documento");
            intent.putExtra("documento",docu);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(MapaActivity.this,TipsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(MapaActivity.this,MisReportesAdminActivity.class);
            intent.putExtra("usuario","0");
            Bundle bolsa=getIntent().getExtras();
            String docu=bolsa.getString("documento");
            intent.putExtra("documento",docu);
            startActivity(intent);
        } else if (id == R.id.nav_contactato){
            Intent intent = new Intent(MapaActivity.this,ContactoActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_salir){
            finish();
        }else if (id == R.id.nav_Cambiar_usuario){
            Intent intent= new Intent(MapaActivity.this,CambiarUsuario.class);
            startActivity(intent);
        }else if (id == R.id.nav_asignar){
            Intent intent= new Intent(MapaActivity.this,AsignarActivity.class);
            Bundle bolsa=getIntent().getExtras();
            String docu=bolsa.getString("documento");
            intent.putExtra("documento",docu);
            startActivity(intent);
        }else if (id == R.id.nav_reporte_asignado) {
            Intent intent = new Intent(MapaActivity.this,MisReportesAdminActivity.class);
            intent.putExtra("usuario","2");
            Bundle bolsa=getIntent().getExtras();
            String docu=bolsa.getString("documento");
            intent.putExtra("documento",docu);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ubicar();
        //miUbicacion();
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    public void ubicar(){
        MiServicio miServicio=new MiServicio(MapaActivity.this);
        lat=miServicio.getLatitud();
        lng=miServicio.getLongitud();

        // Add a marker in Sydney and move the camera
        if (lat!=0.0&&lng!=0.0){
            LatLng sydney = new LatLng(lat, lng);

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

            CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(sydney, 12);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Mi ubicación: "+direccionText));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(miUbicacion);
            Toast.makeText(MapaActivity.this, "Posicionamiento Activado", Toast.LENGTH_SHORT).show();
            posicionamiento.setVisibility(View.GONE);
            //posicionamiento.setText("Posicionamiento Inactivado");
        }else{
            posicionamiento.setText("Posicionamiento Inactivado");
            //Toast.makeText(MapaActivity.this, "Posicionamiento Inactivado, Activalo!!!", Toast.LENGTH_SHORT).show();
        }

    }
}
