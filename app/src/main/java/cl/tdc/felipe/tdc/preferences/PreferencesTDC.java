package cl.tdc.felipe.tdc.preferences;


import android.content.Context;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cl.tdc.felipe.tdc.adapters.Actividad;
import cl.tdc.felipe.tdc.adapters.Actividades;
import cl.tdc.felipe.tdc.objects.Maintenance.Activity;

public class PreferencesTDC  {
    public static final String NAME = "TDC_PREFERENCES";
    public static final String NAME_MAINTENANCE_PREF = "MAINENANCE_REG";
    public static final String SETTING_IMEI = "IMEI";
    public static final String SETTING_IMSI = "IMSI";
    public static final String MAINTENANCE_ID = "MAINTENANSE ID";


    public Context mContext;
    public SharedPreferences sharedPreferences;

    public PreferencesTDC(Context context) {
        super();
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public void setIMEI(String data){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SETTING_IMEI,data);
        editor.apply();
    }




    public void setIMSI(String data){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SETTING_IMSI, data);
        editor.apply();
    }



}
