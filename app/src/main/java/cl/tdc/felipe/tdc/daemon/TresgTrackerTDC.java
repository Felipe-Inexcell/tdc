package cl.tdc.felipe.tdc.daemon;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.CellInfo;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cl.tdc.felipe.tdc.MainActivity;
import cl.tdc.felipe.tdc.extras.Funciones;
import cl.tdc.felipe.tdc.preferences.MaintenanceReg;
import cl.tdc.felipe.tdc.webservice.SoapRequest;

public class TresgTrackerTDC extends Service {
    private static final String TAG = "TresgTrackerTDC";
    private static long MIN_PERIOD;
    private static long MINUTE = 1000 * 60;
    private static long MIN_DELAY = 1000 * 2;
    private static final String DIRECTORYNAME = "/TDC@";
    private static final String FILENAME = "tresg_pendent.txt";
    public String LATITUDE;
    public String LONGITUDE;
    public String strength;
    public Geocoder geocoder;
    MyPhoneStateListener MyListener;
    TelephonyManager tm;
    int netType;

    Timer mTimer;
    public MyLocationListener gps;
    private final IBinder mBinder = new MyBinder();
    private List<CellInfo> allcellinfo;
    List<NeighboringCellInfo> neighboringInfo;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int _time = intent.getIntExtra("TIME", -1);

        if (_time != 0 && _time != -1) {
            MIN_PERIOD = MINUTE * _time;
        }
        this.mTimer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        try {

                            ConnectivityManager conMan = (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);
                            NetworkInfo.State wifiState = conMan.getNetworkInfo(1).getState();
                            NetworkInfo.State ntwrkState = conMan.getNetworkInfo(0).getState();

                            String latitude = String.valueOf(gps.getLatitude());
                            String longitude = String.valueOf(gps.getLongitude());
                            Log.i(TAG, latitude + " " + longitude);

                                /*Process process = Runtime.getRuntime().exec("logcat -b radio");
                                BufferedReader bufferedReader = new BufferedReader(
                                        new InputStreamReader(process.getInputStream()));

                                StringBuilder log=new StringBuilder();
                                String line = "";
                                while ((line = bufferedReader.readLine()) != null) {
                                    log.append(line);
                                }*/



                            if (wifiState == NetworkInfo.State.CONNECTED || ntwrkState == NetworkInfo.State.CONNECTED) {
                                /** ENVIAMOS LA INFO**/
                                try {
                                    LONGITUDE = longitude;
                                    LATITUDE = latitude;
                                    String NT = Funciones.getNetworkType(netType);

                                    neighboringInfo = tm.getNeighboringCellInfo();
                                    if(neighboringInfo != null) {
                                        for (NeighboringCellInfo info : neighboringInfo) {
                                            SoapRequest.sendTresG(longitude, latitude, "", tm.getDeviceId(), info.getCid(), info.getPsc(), getRXLEV(info.getRssi()));
                                        }
                                    }

                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage() + ": " + e.getCause());
                                }

                                /** REVISAMOS SI HAY DATOS PENDIENTES
                                 * Y TRATAMOS DE ENVIAR SI HAY CONEXION                             *
                                 **/

                                if (Environment.getExternalStorageState().equals("mounted")) {
                                    File sdCard = Environment.getExternalStorageDirectory();
                                    File directory = new File(sdCard.getAbsolutePath()
                                            + DIRECTORYNAME);
                                    File file = new File(directory, FILENAME);
                                    if (file.exists() && (wifiState == NetworkInfo.State.CONNECTED || ntwrkState == NetworkInfo.State.CONNECTED)) {
                                        BufferedReader buffer = new BufferedReader(new FileReader(file));
                                        String line;
                                        buffer.readLine(); //leemos la linea en blanco

                                        while ((line = buffer.readLine()) != null) {
                                            try {
                                                Log.i(TAG, "PENDIENTE: " + line);
                                                String[] pendents = line.split(";");
                                                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                                String query = SoapRequest.sendTresG(pendents[0], pendents[1], pendents[2], telephonyManager.getDeviceId(), -1, -1, "");

                                                Log.i(TAG, "ENVIADO\n" + query);
                                            } catch (Exception e) {
                                                Log.e(TAG, e.getMessage() + ": " + e.getCause());
                                            }
                                        }
                                        buffer.close();
                                        if (file.delete()) {
                                            Log.i(TAG, FILENAME + " BORRADO");
                                        }
                                    }
                                }

                            } else {
                                if (Environment.getExternalStorageState().equals("mounted")) {
                                    File sdCard = Environment.getExternalStorageDirectory();
                                    File directory = new File(sdCard.getAbsolutePath()
                                            + DIRECTORYNAME);
                                    if (!directory.exists())
                                        if (directory.mkdir())
                                            Log.i(TAG, "Directory \"" + DIRECTORYNAME + "\" created");
                                    /** GUARDAMOS LA INFO **/
                                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date fecha = new Date();
                                    String line = "\n" + longitude + ";" +
                                            latitude + ";" +
                                            formatter.format(fecha);

                                    File file = new File(directory, FILENAME);
                                    FileWriter fw = new FileWriter(file, true);
                                    BufferedWriter out = new BufferedWriter(fw);
                                    Log.i(TAG, "AGREGADO A PENDIENES: " + line);
                                    out.write(line);
                                    out.flush();
                                    out.close();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                , MIN_DELAY, MIN_PERIOD);


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gps = new MyLocationListener(this);
        geocoder = new Geocoder(this);
        this.mTimer = new Timer();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        MyListener = new MyPhoneStateListener();
        tm.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);


        allcellinfo = tm.getAllCellInfo();
        neighboringInfo = tm.getNeighboringCellInfo();
        netType = tm.getNetworkType();



    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        public TresgTrackerTDC getService() {
            return TresgTrackerTDC.this;
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        /* Get the Signal strength from the provider, each time there is an update */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            try {
                int asu = signalStrength.getGsmSignalStrength();
                int signal = -113 + 2 * asu;
                strength = String.valueOf(signal);
            } catch (Exception e) {
                Log.e(TAG, "Error:" + e);
            }


        }
    }

    private String getRXLEV(int RSSI){
        int asu = RSSI;
        int signal = -113 + 2 * asu;
        return String.valueOf(signal);
    }

}
