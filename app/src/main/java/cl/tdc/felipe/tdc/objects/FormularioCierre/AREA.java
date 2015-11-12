package cl.tdc.felipe.tdc.objects.FormularioCierre;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AREA {
    String idArea;
    String nameArea;
    ArrayList<ITEM> items;

    TextView view;

    public AREA() {

    }

    public TextView generateView(Context ctx){
        view = new TextView(ctx);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.leftMargin =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, ctx.getResources().getDisplayMetrics());
        view.setLayoutParams(p);
        view.setText(nameArea);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        view.setTypeface(null, Typeface.BOLD);
        return view;
    }

    public String getIdArea() {
        return idArea;
    }

    public void setIdArea(String idArea) {
        this.idArea = idArea;
    }

    public String getNameArea() {
        return nameArea;
    }

    public void setNameArea(String nameArea) {
        this.nameArea = nameArea;
    }

    public ArrayList<ITEM> getItems() {
        return items;
    }

    public void setItems(ArrayList<ITEM> items) {
        this.items = items;
    }
}
