package cl.tdc.felipe.tdc.objects.Seguimiento;

import android.graphics.Bitmap;

/**
 * Created by felip on 02/08/2015.
 */
public class Actividad {

    public Actividad() {
    }

    int idActivity;
    String nameActivity;
    boolean foto;
    Bitmap image;

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
