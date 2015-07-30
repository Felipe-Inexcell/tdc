package cl.tdc.felipe.tdc.objects;

import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Felipes on 02-07-2015.
 */
public class FormSystem {

    int idSystem;
    String nameSystem;
    ArrayList<FormSubSystem> subSystemList;
    LinearLayout contenido;

    public LinearLayout getContenido() {
        return contenido;
    }

    public void setContenido(LinearLayout contenido) {
        this.contenido = contenido;
    }

    public FormSystem() {
    }

    public int getIdSystem() {
        return idSystem;
    }

    public void setIdSystem(int idSystem) {
        this.idSystem = idSystem;
    }

    public String getNameSystem() {
        return nameSystem;
    }

    public void setNameSystem(String nameSystem) {
        this.nameSystem = nameSystem;
    }

    public ArrayList<FormSubSystem> getSubSystemList() {
        return subSystemList;
    }

    public void setSubSystemList(ArrayList<FormSubSystem> subSystemList) {
        this.subSystemList = subSystemList;
    }
}
