package cl.tdc.felipe.tdc.daemon;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DemonioTDC extends Service {
    private static final String TAG = "DEMONIO_TDC";
    private static final long MIN_PERIOD = 1000 * 60 * 3;
    private static final long MIN_DELAY = 1000 * 15;
    private static final String DIRECTORYNAME = "/TDC@";
    private static final String FILENAME = "3G.txt";

    Timer mTimer;
    MyLocationListener gps;
    MyPhoneStateListener tresg;
    MyWifiListener wifi;

    @Override
    public void onCreate() {
        super.onCreate();
        gps = new MyLocationListener(this);
        tresg = new MyPhoneStateListener(this);
        wifi = new MyWifiListener(this);

        ArrayList<String> l = wifi.getWifiListString();
        this.mTimer = new Timer();
        this.mTimer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        Date now = Calendar.getInstance().getTime();
                        DateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmss");
                        DateFormat formatHour = new SimpleDateFormat("HHmmss");

                        String line ="\n"+formatDate.format(now)
                                +";"+gps.getLongitude()
                                + ";"+gps.getLatitude()
                                + ";"+tresg.getOperatorName()
                                + ";"+tresg.getNetworkType()
                                + ";"+tresg.getSignal();

                        try {
                            Date floorH = formatHour.parse("234459");
                            Date topH = formatHour.parse("235959");
                            Date actual = formatHour.parse(formatHour.format(now));
                            if(actual.before(topH) && actual.after(floorH)) {

                                /*if(wifi.isEnable()){
                                    wasSend = Calendar.getInstance.getTime();
                                 */
                                    sendTodayEntries();
                                /*}else
                                    wasSend = false;
                                    lastSend = Calendar.getInstance().getTime();
                                 */

                            }else {

                                writeToArchive(line);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
                , MIN_DELAY, MIN_PERIOD);


    }

    public void sendTodayEntries(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                File sdCard = Environment.getExternalStorageDirectory();
                try {
                    File directory = new File(sdCard.getAbsolutePath()
                            + DIRECTORYNAME);
                    if (!directory.exists())
                        directory.mkdirs();

                    File file = new File(directory, FILENAME);
                    BufferedReader buffer = new BufferedReader(new FileReader(file));
                    String line;
                    buffer.readLine();
                    int count = 0;
                    while ((line = buffer.readLine()) != null) {
                        count++;
                    }

                    buffer.close();
                    file.delete();
                    Log.d(TAG,"Se enviaron "+count+" registros");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void writeToArchive(final String line){
        Thread t = new Thread(new Runnable() {
            public void run() {
                if (Environment.getExternalStorageState().equals("mounted")) {
                    File sdCard = Environment.getExternalStorageDirectory();
                    FileWriter fw;
                    try {
                        File directory = new File(sdCard.getAbsolutePath()
                                + DIRECTORYNAME);
                        if (!directory.exists())
                            directory.mkdirs();


                        File file = new File(directory, FILENAME);

                        fw = new FileWriter(file, true);
                        BufferedWriter out = new BufferedWriter(fw);

                        out.write(line);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
