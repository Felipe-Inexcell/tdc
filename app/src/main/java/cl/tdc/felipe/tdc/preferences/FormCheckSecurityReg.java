package cl.tdc.felipe.tdc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import cl.tdc.felipe.tdc.objects.Maintenance.Activity;

public class FormCheckSecurityReg {

    public static final String NAME = "FORMCHECKSECURITYREG";

    public SharedPreferences sharedPreferences;

    Context ctx;

    public FormCheckSecurityReg(Context c) {
        ctx = c;
        sharedPreferences = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);

    }

    public void addValue(String FLAG, Boolean state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i(NAME, FLAG+" - "+state.toString());
        editor.putBoolean(FLAG, state);
        editor.apply();
    }

    public void addValue(String FLAG,String state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i(NAME, FLAG+" - "+state);
        editor.putString(FLAG, state);
        editor.apply();
    }

    public Boolean getBoolean(String FLAG){
        Boolean state = sharedPreferences.getBoolean(FLAG, false);
        Log.i(NAME, FLAG+" - "+state.toString());
        return state;
    }

    public String getString(String FLAG){
        String state =  sharedPreferences.getString(FLAG, "");
        Log.i(NAME, FLAG+" - "+state);
        return state;
    }

    public void clearPreferences() {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.clear();
        ed.apply();
    }








}
