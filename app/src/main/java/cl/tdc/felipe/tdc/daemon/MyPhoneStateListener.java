package cl.tdc.felipe.tdc.daemon;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class MyPhoneStateListener extends PhoneStateListener {
    private static final String TAG = "MY_PHONE_STATE_LISTENER";
    private Context mContext;
    private int signal;
    TelephonyManager telephonyManager;
    List<NeighboringCellInfo> neighboringCellInfos;

    public MyPhoneStateListener(Context mContext) {
        this.mContext = mContext;

        telephonyManager = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(this,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        telephonyManager.listen(this, PhoneStateListener.LISTEN_CELL_INFO);
        telephonyManager.listen(this, PhoneStateListener.LISTEN_CELL_LOCATION);
        neighboringCellInfos = telephonyManager.getNeighboringCellInfo();
        Log.d(TAG, neighboringCellInfos.toString());
    }

    public void debug(){
        CellLocation cellLocation = telephonyManager.getCellLocation();
        Log.d(TAG,cellLocation.toString());
        Toast.makeText(mContext,telephonyManager.getNeighboringCellInfo().toString(),Toast.LENGTH_LONG).show();
        Log.d(TAG, telephonyManager.getNeighboringCellInfo().toString());

    }

    public String getOperatorName(){
        return telephonyManager.getNetworkOperatorName();

    }

    public String getNetworkType(){
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_1xRTT)return  "1xRTT";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_CDMA)return  "CDMA";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE)return  "EDGE";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_EHRPD)return  "eHRPD";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_EVDO_0)return  "EVDO revision 0";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_EVDO_A)return  "EVDO revision A";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_EVDO_B)return  "EVDO revision B";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_GPRS)return  "GPRS";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA)return  "HSDPA";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA)return  "HSPA";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPAP)return  "HSPA+";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA)return  "HSUPA";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_IDEN)return  "iDen";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE)return  "LTE";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS)return  "UMTS";
        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UNKNOWN)return  "Desconocido";
        return "Desconocido";
    }

    public int getSignal() {
        return signal;
    }



    @Override
    public void onCellLocationChanged(CellLocation location) {
        super.onCellLocationChanged(location);
        Log.d(TAG, "Location "+location.toString());
    }

    @Override
    public void onCellInfoChanged(List<CellInfo> cellInfo) {
        super.onCellInfoChanged(cellInfo);
        Log.d(TAG, "cellInfo "+cellInfo.toString());
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength)
    {
        super.onSignalStrengthsChanged(signalStrength);
        try{
            int asu = signalStrength.getGsmSignalStrength();
            signal = -113 + 2*asu;
        }catch(Exception e){
            e.printStackTrace();
        }


    }

}
