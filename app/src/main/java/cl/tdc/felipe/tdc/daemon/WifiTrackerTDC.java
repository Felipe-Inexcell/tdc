package cl.tdc.felipe.tdc.daemon;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cl.tdc.felipe.tdc.MainActivity;
import cl.tdc.felipe.tdc.webservice.SoapRequest;

public class WifiTrackerTDC extends Service {
    private static final String TAG = "WifiTrackerTDC";
    private static final long MIN_PERIOD = 1000 * 60 * 20;
    private static final long MIN_DELAY = 1000 * 10;
    private static final String DIRECTORYNAME = "/TDC@";
    private static final String FILENAME = "wifi_pendent.txt";
    private static final String FILENAME_AUX = "wifi_pendent_tmp.txt";

    Timer mTimer;
    MyLocationListener gps;
    MyWifiListener wifi;

    @Override
    public void onCreate() {
        super.onCreate();
        gps = new MyLocationListener(this);
        wifi = new MyWifiListener(this);

        /**
         * Timer: Cada MIN_PERIOD segundos revisa las redes wifi que capta.
         * Si el usuario esta conectado a internet wifi, la informacion de cada red identificada
         *  es enviada a la torre de control.
         * Si no, la informacion se guardara en /TDC@/wifi_pendent.txt en
         * la tarjeta SD.
         * Luego, si el usuario esta conectado a internet wifi, se revisa si el archivo
         * /TDC@/wifi_pendent.txt existe en la tarjeta SD.
         * Si existe, se envía la información almacenada a la torre de control.
         */
        this.mTimer = new Timer();
        this.mTimer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        ArrayList<String> list = wifi.getWifiListString();
                        Log.d(TAG, list.toString());

                        try {

                            ConnectivityManager conMan = (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);
                            NetworkInfo.State wifiState = conMan.getNetworkInfo(1).getState();
                            NetworkInfo.State ntwrkState = conMan.getNetworkInfo(0).getState();
                            /** REVISAMOS SI HAY DATOS PENDIENTES
                             * Y TRATAMOS DE ENVIAR SI HAY CONEXION                             *
                             **/

                            if (Environment.getExternalStorageState().equals("mounted")) {
                                File sdCard = Environment.getExternalStorageDirectory();
                                File directory = new File(sdCard.getAbsolutePath()
                                        + DIRECTORYNAME);
                                File file = new File(directory, FILENAME);
                                //BufferedWriter out = new BufferedWriter(fw);
                                if (file.exists() && (wifiState == NetworkInfo.State.CONNECTED || ntwrkState == NetworkInfo.State.CONNECTED)) {
                                    BufferedReader buffer = new BufferedReader(new FileReader(file));
                                    String line;
                                    buffer.readLine(); //leemos la linea en blanco
                                    int count = 0;
                                    while ((line = buffer.readLine()) != null) {
                                        try {
                                            Log.i(TAG, "PENDIENTE: " + line);
                                            String[] pendents = line.split(";");
                                            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                            String query = SoapRequest.sendWifi(pendents[0], pendents[1], pendents[2], pendents[3], pendents[4], pendents[5], telephonyManager.getDeviceId());
                                            Log.i(TAG, "ENVIADO\n" + query);
                                        } catch (Exception e) {
                                            Log.e("POSITION", e.getMessage() + ": " + e.getCause());
                                        }
                                    }
                                    buffer.close();
                                    if (file.delete())
                                        Log.i(TAG, FILENAME + " BORRADO");
                                }
                            }

                            /** PARA CADA RED ENCONTRADA SE INTENTA ENVIAR
                             * SI ENCUENTRA CONEXION ENVIA LOS DATOS A TDC
                             * SI NO LO GUARDA EN EL ARCHIVO
                             */
                            if(list.size()>0) {
                                for (String w : list) {
                                    String[] datos = w.split(";");
                                    String mac = datos[1];
                                    String signal = datos[3];
                                    String channel = datos[4];
                                    String info = datos[2];

                                    if (wifiState == NetworkInfo.State.CONNECTED || ntwrkState == NetworkInfo.State.CONNECTED) {
                                        /** ENVIAMOS LA INFO**/
                                        try {
                                            Log.i(TAG, "Enviando... MAC=" + mac + " SIGNAL= " + signal + " CHANNEL= " + channel + " INFORMATION: " + info + " LONGITUDE= " + gps.getLongitude() + " LATITUDE= " + gps.getLatitude());
                                            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                            String response = SoapRequest.sendWifi(mac, signal, channel, info, String.valueOf(gps.getLongitude()), String.valueOf(gps.getLatitude()), telephonyManager.getDeviceId());
                                            Log.i(TAG, "ENVIADO\n" + response);
                                        } catch (Exception e) {
                                            Log.e("POSITION", e.getMessage() + ": " + e.getCause());
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
                                            String line = "\n" + mac + ";" +
                                                    signal + ";" +
                                                    channel + ";" +
                                                    info + ";" +
                                                    gps.getLongitude() + ";" +
                                                    gps.getLatitude();

                                            File file = new File(directory, FILENAME);
                                            FileWriter fw = new FileWriter(file, true);
                                            BufferedWriter out = new BufferedWriter(fw);
                                            Log.i(TAG, "AGREGADO A PENDIENES: " + line);
                                            out.write(line);
                                            out.flush();
                                            out.close();
                                        }
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                , MIN_DELAY, MIN_PERIOD);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
