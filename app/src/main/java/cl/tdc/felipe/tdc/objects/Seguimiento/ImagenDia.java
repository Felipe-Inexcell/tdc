package cl.tdc.felipe.tdc.objects.Seguimiento;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by felip on 07/08/2015.
 */
public class ImagenDia {
    int idproject;
    int idday;
    Bitmap bitmap;
    Date timestamp;
    String filename;

    public ImagenDia(int idproject, int idday, Bitmap bitmap, Date timestamp) {
        this.idproject = idproject;
        this.idday = idday;
        this.bitmap = bitmap;
        this.timestamp = timestamp;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getIdproject() {
        return idproject;
    }

    public void setIdproject(int idproject) {
        this.idproject = idproject;
    }

    public int getIdday() {
        return idday;
    }

    public void setIdday(int idday) {
        this.idday = idday;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
