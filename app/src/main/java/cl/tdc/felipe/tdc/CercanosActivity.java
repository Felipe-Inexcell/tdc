package cl.tdc.felipe.tdc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import cl.tdc.felipe.tdc.daemon.MyLocationListener;
import cl.tdc.felipe.tdc.daemon.PositionTrackerTDC;
import cl.tdc.felipe.tdc.extras.Funciones;
import cl.tdc.felipe.tdc.webservice.SoapRequest;
import cl.tdc.felipe.tdc.webservice.XMLParser;

public class CercanosActivity extends FragmentActivity implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMyLocationChangeListener {
    public static Activity actividad;
    public static final int ZOOM_LEVEL = 12;

    MyLocationListener gps;
    private GoogleMap mapa;

    boolean ubicacionOK = false;

    PositionTrackerTDC trackerTDC;
    ProgressDialog mapDialog;


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AVERIA", "onResume");
        Intent intent = new Intent(this, PositionTrackerTDC.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("AVERIA", "onPause");
        unbindService(mConnection);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PositionTrackerTDC.MyBinder b = (PositionTrackerTDC.MyBinder) iBinder;
            trackerTDC = b.getService();
            Log.d("AVERIA", "SERVICE CONNECTED");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            trackerTDC = null;
            Log.d("AVERIA", "SERVICE DISCONNECTED");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cercanos);
        actividad = this;

        mapDialog = new ProgressDialog(this);
        mapDialog.setMessage("Cargando Mapa...");
        mapDialog.show();
        gps = new MyLocationListener(this);

        mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if(mapa != null) {
            mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mapa.setMyLocationEnabled(true);
            mapa.setOnMapLoadedCallback(this);
            mapa.getUiSettings().setZoomControlsEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);
            mapa.getUiSettings().setMyLocationButtonEnabled(true);

            mapa.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
                    String[] datos = marker.getSnippet().split("&");
                    TextView t1 = (TextView) v.findViewById(R.id.t1);
                    t1.setText(datos[0].split(";")[1]);
                    TextView t2 = (TextView) v.findViewById(R.id.t2);
                    t2.setText(datos[1].split(";")[1]);
                    TextView t3 = (TextView) v.findViewById(R.id.t3);
                    t3.setText(datos[2].split(";")[1]);
                    TextView t4 = (TextView) v.findViewById(R.id.t4);
                    t4.setText(datos[3].split(";")[1]);
                    TextView t5 = (TextView) v.findViewById(R.id.t5);
                    t5.setText(datos[4].split(";")[1]);
                    return v;
                }
            });


        }
        else{
            new AlertDialog.Builder(this).setMessage("Compruebe que tiene la aplicacion de GoogleMap y PlayStore").setTitle("No se pudo cargar el mapa").setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            }).show();
            finish();
        }

    }

    // TODO: funcion onClick del botón apagar.

    public void onClick_apagar(View v) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("¿Seguro que desea cerrar Sitios Cercanos?");
        b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(MainActivity.actividad != null){
                    MainActivity.actividad.finish();
                }
                actividad.finish();
            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        b.show();
        MainActivity.actividad.finish();
        finish();
    }

    public void onClick_search(View v) {

        mapa.clear();
        search_marker searchMarker = new search_marker();
        searchMarker.execute();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        onClick_back(null);
    }


    public void onClick_back(View v) {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("¿Seguro que desea cerrar Sitios Cercanos?");
        b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                actividad.finish();
            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        b.show();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLoaded() {
        Log.i("MAP", "Map Loaded");
        /*mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(trackerTDC.gps.getLatitude(), trackerTDC.gps.getLongitude()), ZOOM_LEVEL));
        mapDialog.dismiss();*/

        if (mapa.getMyLocation() != null && !ubicacionOK) {
            LatLng punto = new LatLng(mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude());
            LatLng cero = new LatLng(0,0);

            if(!punto.equals(cero) && !ubicacionOK) {
                ubicacionOK = true;
                mapa.addMarker(new MarkerOptions().position(punto).snippet("AQUI"));
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(punto, ZOOM_LEVEL));
                mapDialog.dismiss();
            }
            //mapa.addPolyline(new PolylineOptions().add(new LatLng(mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()),punto));
        }
    }

    @Override
    public void onMyLocationChange(Location loc) {
        if (loc != null && !ubicacionOK) {
            LatLng punto = new LatLng(loc.getLatitude(), loc.getLongitude());
            LatLng cero = new LatLng(0,0);

            if(!punto.equals(cero) && !ubicacionOK) {
                ubicacionOK = true;
                mapa.addMarker(new MarkerOptions().position(punto).snippet("AQUI"));
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(punto, ZOOM_LEVEL));
                mapDialog.dismiss();
            }
            //mapa.addPolyline(new PolylineOptions().add(new LatLng(mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()),punto));
        }
    }

    /**
     * CLASE ASINCRÓNICA
     */
    private class search_marker extends AsyncTask<String, String, ArrayList<String>> {

        ProgressDialog dialog;
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String response = SoapRequest.getLocation(String.valueOf(trackerTDC.gps.getLatitude()), String.valueOf(trackerTDC.gps.getLongitude()), telephonyManager.getDeviceId());

                Log.i("CERCANOS", response);
                ArrayList<String> parse = XMLParser.getLocations(response);
                Log.d("MAP", parse.toString());
                return parse;
            } catch (Exception e) {
                Log.e("MAP", e.getMessage());
                return null;

            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(actividad);
            dialog.setTitle("Por favor espere...");
            dialog.setMessage("Buscando sitios cercanos a su ubicación...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            if(dialog.isShowing())
                dialog.dismiss();

            if(s == null){
                Toast.makeText(getApplicationContext(), "Error en la conexión.", Toast.LENGTH_LONG).show();
                return;
            }

            if(s.get(0).compareTo("0")==0){
                int count = 0;
                for(int i=2; i<s.size();i++){
                    Log.d("CERCANOS", s.toString());
                    String[] Datos = s.get(i).split("&");
                    String[] latitud = Datos[Datos.length - 2].split(";");
                    String[] longitud = Datos[Datos.length - 1].split(";");
                    if(longitud.length != 2 || latitud.length != 2){
                        continue;
                    }
                    double latitude = Double.valueOf(Datos[Datos.length - 2].split(";")[1]);
                    double longitude = Double.valueOf(Datos[Datos.length - 1].split(";")[1]);
                    LatLng position = new LatLng(latitude,longitude);
                    Log.i("MAP", "posicion:" + position.toString());
                    mapa.addMarker(new MarkerOptions()
                                    .position(position)
                                    .snippet(s.get(i))
                    );
                    count++;
                }
                if(count == 0){
                    Toast.makeText(getApplicationContext(), "No se encontraron sitios cercanos", Toast.LENGTH_LONG).show();
                }
                if(count >0 ){
                    Toast.makeText(getApplicationContext(), "Se encontraron "+count+" sitios cercanos", Toast.LENGTH_LONG).show();
                }
            }
            else
                Toast.makeText(actividad, s.get(1),Toast.LENGTH_LONG).show();


        }
    }
}
