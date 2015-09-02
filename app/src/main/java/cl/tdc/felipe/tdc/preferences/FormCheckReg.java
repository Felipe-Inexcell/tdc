package cl.tdc.felipe.tdc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class FormCheckReg {

    public static final String NAME = "FORMCHECK";

    public SharedPreferences sharedPreferences;

    Context ctx;
    String form;

    public FormCheckReg(Context c, String form) {
        ctx = c;
        this.form = form;
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

    public void addValue(String FLAG,int state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i(NAME, FLAG+" - "+state);
        editor.putInt(FLAG, state);
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

    public int getInt(String FLAG){
        int state =  sharedPreferences.getInt(FLAG, -100);
        Log.i(NAME, FLAG+" - "+state);
        return state;
    }

    public void clearPreferences() {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.clear();
        ed.apply();
    }








}
