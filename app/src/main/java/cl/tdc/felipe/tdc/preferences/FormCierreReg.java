package cl.tdc.felipe.tdc.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class FormCierreReg {

    public static final String NAME = "FORMCIERRE";

    public SharedPreferences sharedPreferences;

    Context ctx;

    public FormCierreReg(Context c, String TYPE) {
        ctx = c;
        sharedPreferences = ctx.getSharedPreferences(NAME+TYPE, Context.MODE_PRIVATE);

    }

    public void addValue(String FLAG, Boolean state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i(NAME, FLAG + " - " + state.toString());
        editor.putBoolean(FLAG, state);
        editor.apply();
    }

    public void addValue(String FLAG, String state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i(NAME, FLAG + " - " + state);
        editor.putString(FLAG, state);
        editor.apply();
    }

    public void addValue(String FLAG, int state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i(NAME, FLAG + " - " + state);
        editor.putInt(FLAG, state);
        editor.apply();
    }

    public Boolean getBoolean(String FLAG) {
        Boolean state = sharedPreferences.getBoolean(FLAG, false);
        Log.i(NAME, FLAG + " - " + state.toString());
        return state;
    }

    public String getString(String FLAG) {
        String state = sharedPreferences.getString(FLAG, "");
        Log.i(NAME, FLAG + " - " + state);
        return state;
    }

    public int getInt(String FLAG) {
        int state = sharedPreferences.getInt(FLAG, -100);
        Log.i(NAME, FLAG + " - " + state);
        return state;
    }

    public void clearPreferences() {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.clear();
        ed.apply();
    }


}
