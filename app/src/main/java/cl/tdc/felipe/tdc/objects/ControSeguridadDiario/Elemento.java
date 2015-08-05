package cl.tdc.felipe.tdc.objects.ControSeguridadDiario;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by felip on 04/08/2015.
 */
public class Elemento {

    int id;
    String name;
    String type;
    ArrayList<String> values;
    String valor;
    Bitmap firma;

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Bitmap getFirma() {
        return firma;
    }

    public void setFirma(Bitmap firma) {
        this.firma = firma;
    }

    public Elemento() {
    }

    public int getId() {
        return id;
    }

    public void setId(String id) {
        if (id.compareTo("") == 0)
            this.id = -1;
        else
            this.id = Integer.parseInt(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
}
