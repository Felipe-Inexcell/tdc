package cl.tdc.felipe.tdc.objects.FormularioCierre;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by felip on 05/11/2015.
 */
public class SYSTEM {
    String idSystem;
    String nameSystem;
    ArrayList<AREA> areas;
    TextView view;

    public SYSTEM() {
    }

    public TextView generateView(Context ctx){
        view = new TextView(ctx);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(p);
        view.setText(nameSystem);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        view.setTypeface(null, Typeface.BOLD);
        view.setGravity(Gravity.CENTER_HORIZONTAL);
        return view;
    }

    public String getIdSystem() {
        return idSystem;
    }

    public void setIdSystem(String idSystem) {
        this.idSystem = idSystem;
    }

    public String getNameSystem() {
        return nameSystem;
    }

    public void setNameSystem(String nameSystem) {
        this.nameSystem = nameSystem;
    }

    public ArrayList<AREA> getAreas() {
        return areas;
    }

    public void setAreas(ArrayList<AREA> areas) {
        this.areas = areas;
    }
}
