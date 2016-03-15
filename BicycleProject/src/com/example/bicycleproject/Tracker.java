package com.example.bicycleproject;


import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;



public class Tracker extends Service implements LocationListener {
	
    private final Context mContext;
    
    boolean gpsEn = false;
 
    boolean netEn = false;
 
    boolean canGetLocation = false;
 
    Location location; 
    double latitude;
    double longitude;
    double altitude;
 

    private static final long MIN_METTERS = 5; // 5 metros
 
    private static final long MIN_SECS = 10000;  // 10 segundos
 
    protected LocationManager locationManager;
 
    public Tracker(Context context) {
        this.mContext = context;
        getLocation();
    }
    
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
 
            gpsEn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            netEn = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!gpsEn && !netEn) {
            } else {
                this.canGetLocation = true;
                if (netEn) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_SECS, MIN_METTERS, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            altitude = location.getAltitude();
                            
                        }
                    }
                }
                if (gpsEn) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_SECS,MIN_METTERS, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                altitude = location.getAltitude();
                            }
                        }
                    }
                }

            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return location;
    }
    
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
         
        return latitude;
    }
     

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
         
        return longitude;
    }
    
    public double getAltitude(){
        if(location != null){
            altitude = location.getAltitude();
        }
         
        return altitude;
    }
    
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
     

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
      
        alertDialog.setTitle("Configuración GPS");
  
        alertDialog.setMessage("GPS desactivado. ¿Quiere configurarlo en Ajustes?");
  
        alertDialog.setPositiveButton("Ajustes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
  
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        
        alertDialog.show();
    }
    
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(Tracker.this);
        }       
    }

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
