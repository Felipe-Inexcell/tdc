package cl.tdc.felipe.tdc.objects.Seguimiento;

import android.graphics.Bitmap;
import android.util.Log;

public class Actividad {

    public Actividad() {
    }

    int idActivity;
    String nameActivity;
    float advance;
    boolean selected;
    boolean foto;

    Bitmap image;

    public float getAdvance() {

        Log.d("GETADVANCE", ""+advance);
        return advance;
    }

    public void setAdvance(String advance) {
        Log.d("SETADVANCE", advance);
        this.advance = Float.parseFloat(advance);
    }

    public boolean isSelected() {
        return selected;
    }

    public String isSelectedString() {
        if (selected)
            return "1";
        else
            return "0";
    }

    public void setSelected(Boolean b){
        this.selected = b;
    }

    public void setSelected(String selected) {
        if (selected.compareTo("0") == 0)
            this.selected = false;
        if (selected.compareTo("1") == 0)
            this.selected = true;

    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(String idActivity) {
        if (idActivity.compareTo("") == 0)
            this.idActivity = 0;
        else
            this.idActivity = Integer.valueOf(idActivity);
    }

    public String getNameActivity() {
        return nameActivity;
    }

    public void setNameActivity(String nameActivity) {
        this.nameActivity = nameActivity;
    }

    public boolean isFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        if (foto.toLowerCase().compareTo("si") == 0)
            this.foto = true;
        else
            this.foto = false;
    }
}
