package cl.tdc.felipe.tdc.daemon;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import cl.tdc.felipe.tdc.MainActivity;


public class MyLocationListener extends Service implements LocationListener {
    public final Context mContext;
    public Location location;
    double longitude;
    double latitude;
    boolean isGPSEnabled = false;// flag para el estado del GPS
    boolean isNetworkEnabled = false;// flag para el estado de Network
    boolean canGetLocation = false;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;

    protected LocationManager locationManager;

    public MyLocationListener(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    public double getLatitude() {

        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }


    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                location = null;
            } else {
                this.canGetLocation = true;

                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("tdc demonio", "GPS Enable");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                   /* if (isNetworkEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "NETWORK Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider.compareTo(LocationManager.GPS_PROVIDER) == 0) {
            isGPSEnabled = true;
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.compareTo(LocationManager.GPS_PROVIDER) == 0) {
            isGPSEnabled = false;
            handler.sendEmptyMessage(0);

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    Toast.makeText(mContext, "Active GPS para el correcto funcionamiento de TDC@", Toast.LENGTH_LONG).show();
                    break;
            default:
                    break;
            }


        }
    };


}
