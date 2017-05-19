package org.app.prevengapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

public class MiServicio extends Service implements LocationListener {

    private Context ctx;
    double latitud;
    double longitud;
    double altitud;
    Location location;
    boolean gpsActivo;
    TextView texto;
    LocationManager locationManager;
    boolean enviar = true;

    public MiServicio() {
        super();
        this.ctx = this;
        getLocation();
//		this.ctx=this;
    }

    public MiServicio(Context c) {
        super();
        this.ctx = c;
        getLocation();
    }


    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public double getAltitud() {
        return altitud;
    }

    @SuppressWarnings("MissingPermission")//Metodo que calcula la posión
    public void getLocation() {
        try {
            locationManager = (LocationManager) this.ctx.getSystemService(LOCATION_SERVICE);
            gpsActivo = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		} catch (Exception e) {
            latitud=0.0;
            longitud=0.0;
            altitud=0.0;
		}
        //valida que el gps este funcionando
        if (gpsActivo) {
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 10, 1, this);
            location=locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            if(location!=null){
                latitud = location.getLatitude();
                longitud = location.getLongitude();
                altitud = location.getAltitude();
            }else {
                latitud=0.0;
                longitud=0.0;
                altitud=0.0;
            }
        }else{
            latitud=0.0;
            longitud=0.0;
            altitud=0.0;
        }
	}
	
	@Override
    public void onCreate() {
		this.ctx=this;
//          Toast.makeText(this,"Servicio creado",Toast.LENGTH_SHORT).show();
          getLocation();
    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
//          Toast.makeText(this,"Servicio arrancado "+ idArranque,Toast.LENGTH_SHORT).show();
          enviar=true;
//          reproductor.start();
          return START_STICKY;
    }

    @Override
    public void onDestroy() {
//          Toast.makeText(this,"Servicio detenido",Toast.LENGTH_SHORT).show();
          enviar=false;
//          reproductor.stop();
    }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
//		Toast.makeText(this,"Cambio realizado",Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	public void mensaje(String a){
//		Toast.makeText(this,a,Toast.LENGTH_SHORT).show();
	}
     
}
