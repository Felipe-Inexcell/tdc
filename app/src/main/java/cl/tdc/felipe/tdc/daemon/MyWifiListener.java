package cl.tdc.felipe.tdc.daemon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyWifiListener extends BroadcastReceiver {
    private static final String TAG = "MY_WIFI_LISTENER";

    Context mContext;
    WifiManager wifiManager;

    List<ScanResult> wifiList;

    public MyWifiListener(Context context) {
        super();
        mContext = context;

        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mContext.registerReceiver(this, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    public List<ScanResult> getWifiList() {
        wifiManager.startScan();
        return wifiManager.getScanResults();
    }

    public ArrayList<String> getWifiListString(){
        try {
            ArrayList<String> arrayList = new ArrayList<String>();
            wifiManager.startScan();
            for (ScanResult scanResult : wifiManager.getScanResults()) {
                arrayList.add(scanResult.SSID + ";"
                        + scanResult.BSSID + ";"
                        + scanResult.capabilities + ";"
                        + scanResult.level + ";"
                        + getChannel(scanResult.frequency));
            }
            Log.d(TAG, arrayList.toString());
            return arrayList;
        }catch(Exception e){
            Log.e("WIFILISTENER", e.getMessage()+": "+e.getCause());
            ArrayList<String> vacio = new ArrayList<>();
            return vacio;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        wifiList = wifiManager.getScanResults();

    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }



    private int getChannel(int freq){
        if(freq == 2401 || freq == 2412 || freq == 2423 )
            return 1;
        if(freq == 2404 || freq == 2417 || freq == 2428 )
            return 2;
        if(freq == 2411 || freq == 2422 || freq == 2433 )
            return 3;
        if(freq == 2416 || freq == 2427 || freq == 2438 )
            return 4;
        if(freq == 2421 || freq == 2432 || freq == 2443 )
            return 5;
        if(freq == 2426 || freq == 2437 || freq == 2448 )
            return 6;
        if(freq == 2431 || freq == 2442 || freq == 2453 )
            return 7;
        if(freq == 2436 || freq == 2447 || freq == 2458 )
            return 8;
        if(freq == 2441 || freq == 2452 || freq == 2463 )
            return 9;
        if(freq == 2446 || freq == 2457 || freq == 2468 )
            return 10;
        if(freq == 2451 || freq == 2462 || freq == 2473 )
            return 11;
        if(freq == 2456 || freq == 2467 || freq == 2478 )
            return 12;
        if(freq == 2461 || freq == 2472 || freq == 2483 )
            return 13;
        if(freq == 2473 || freq == 2484 || freq == 2495 )
            return 14;

        return 0;
    }
}

/* TODO CHANEL FREQUENCY TABLE
CHA LOWER   CENTER  UPPER
NUM FREQ    FREQ    FREQ
    MHZ     MHZ     MHZ
  1 2 401   2 412   2 423
  2 2 404   2 417   2 428
  3 2 411   2 422   2 433
  4 2 416   2 427   2 438
  5 2 421   2 432   2 443
  6 2 426   2 437   2 448
  7 2 431   2 442   2 453
  8 2 436   2 447   2 458
  9 2 441   2 452   2 463
 10 2 451   2 457   2 468
 11 2 451   2 462   2 473
 12 2 456   2 467   2 478
 13 2 461   2 472   2 483
 14 2 473   2 484   2 495
 */






