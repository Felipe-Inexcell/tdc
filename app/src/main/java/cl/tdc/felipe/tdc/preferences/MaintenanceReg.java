package cl.tdc.felipe.tdc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import cl.tdc.felipe.tdc.objects.Maintenance.Activity;

public class MaintenanceReg {

    public static final String NAME_MAINTENANCE_PREF = "MAINENANCE_REG";
    public static final String MAINTENANCE_ID = "MAINTENANSE_ID";
    public static final String MAINTENANCE_FLAG = "MAINTENANSE_FLAG";
    public static final String MAINTENANCE_CHECK = "MAINTENANSE_CHECK";

    public SharedPreferences sharedPreferences;

    Context ctx;

    public MaintenanceReg(Context c) {
        ctx = c;
        sharedPreferences = ctx.getSharedPreferences(NAME_MAINTENANCE_PREF, Context.MODE_PRIVATE);

    }

    public void newMaintenance(String ID, String FLAG) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i("NEW SHARED", ID + "   " + FLAG);
        editor.putString(MAINTENANCE_ID, ID);
        editor.putString(MAINTENANCE_FLAG, FLAG);
        editor.apply();
    }

    public void setChecklistState(boolean state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MAINTENANCE_CHECK, state);
        editor.apply();
    }

    public boolean getChecklistState(){return sharedPreferences.getBoolean(MAINTENANCE_CHECK,false);}

    public String getMaintenance() {
        Log.d("GET SHARED", sharedPreferences.getString(MAINTENANCE_ID, "-1") + "   " + sharedPreferences.getString(MAINTENANCE_FLAG, "-1"));
        return sharedPreferences.getString(MAINTENANCE_ID, "-1") + ";" + sharedPreferences.getString(MAINTENANCE_FLAG, "-1");
    }

    public String getID(){
        return sharedPreferences.getString(MAINTENANCE_ID,"-1");
    }

    public void clearPreferences() {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.clear();
        ed.apply();
    }

    public void stateActivity(Activity a, boolean complete) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ACTIVIDAD" + a.getIdActivity(), complete);
        editor.apply();
    }

    public void deleteStateActivity(Activity a) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("ACTIVIDAD" + a.getIdActivity());
        editor.apply();
    }

    public boolean isCompleteActivity(Activity a) {
        return sharedPreferences.getBoolean("ACTIVIDAD" + a.getIdActivity(), false);
    }

}
