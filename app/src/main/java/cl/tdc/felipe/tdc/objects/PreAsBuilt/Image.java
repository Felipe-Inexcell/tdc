package cl.tdc.felipe.tdc.objects.PreAsBuilt;

import android.graphics.Bitmap;

/**
 * Created by felip on 10/09/2015.
 */
public class Image {
    String name;
    Bitmap bitmap;
    String commentary;

    public Image() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
}
